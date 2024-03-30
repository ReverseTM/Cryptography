package ru.mai.khasanov.cryptography;

import org.javatuples.Pair;
import ru.mai.khasanov.cryptography.CipherMode.ACipherMode;
import ru.mai.khasanov.cryptography.CipherMode.CipherMode;
import ru.mai.khasanov.cryptography.Padding.PaddingMode;
import ru.mai.khasanov.cryptography.interfaces.IEncryptor;
import ru.mai.khasanov.cryptography.interfaces.IPadding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class CryptoContext {

    private static final int BLOCK_SIZE = 2_097_152; // 2Mb

    private final int blockLength;
    private final ACipherMode cipherMode;
    private final IPadding padding;

    public CryptoContext(
            byte[] key,
            IEncryptor encryptor,
            CipherMode.Mode cypherMode,
            PaddingMode.Mode paddingMode,
            byte[] IV)
    {
        blockLength = encryptor.getBlockLength();
        encryptor.setKeys(key);
        cipherMode = CipherMode.getInstance(cypherMode, encryptor, IV);
        padding = PaddingMode.getInstance(paddingMode);
    }

    public void encrypt(byte[] text, byte[][] cipherText) {
        cipherText[0] = cipherMode.encrypt(padding.applyPadding(text, blockLength));
    }

    public void decrypt(byte[] cipherText, byte[][] text) {
        text[0] = padding.removePadding(cipherMode.decrypt(cipherText));
    }

    public void encrypt(String inputFile, String outputFile) {
        syncProcess(inputFile, outputFile, true);
        //asyncProcess(inputFile, outputFile, true);
    }

    public void decrypt(String inputFile, String outputFile) {
        syncProcess(inputFile, outputFile, false);
        //asyncProcess(inputFile, outputFile, false);
    }

    private void asyncProcess(String inputFile, String outputFile, boolean encrypt){
        if (inputFile == null || outputFile == null) {
            throw new RuntimeException("Input and output files cannot be null");
        }

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        Map<Long, byte[]> cipherTextMap = new ConcurrentSkipListMap<>();

        try {
            File file = new File(inputFile);
            if (!file.exists()) {
                throw new FileNotFoundException(inputFile);
            }
            long fileLength = file.length();
            int blockSize = encrypt ? BLOCK_SIZE - blockLength : BLOCK_SIZE;

            for (long readBytes = 0L; readBytes < fileLength; readBytes += blockSize) {
                long currentReadBytes = readBytes;
                CompletableFuture<Void> future = CompletableFuture
                        .supplyAsync(() -> processFile(inputFile, currentReadBytes, fileLength, encrypt), executorService)
                        .thenAcceptAsync(result -> cipherTextMap.put(result.getValue0(), result.getValue1()), executorService);
                futures.add(future);
            }

            CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
            allOf.join();
        } catch (IOException e) {
            System.out.println("reading file failed");
            throw new RuntimeException(e);
        }

        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(2, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }

        try (RandomAccessFile output = new RandomAccessFile(outputFile, "rw")) {
            for (Map.Entry<Long, byte[]> entry : cipherTextMap.entrySet()) {
                writeFile(output, entry.getValue());
            }
        } catch (IOException e) {
            System.out.println("writing file failed");
            throw new RuntimeException(e);
        }
    }

    private Pair<Long, byte[]> processFile(String inputFile, long offset, long fileLength, boolean encrypt) {
        try (RandomAccessFile file = new RandomAccessFile(inputFile, "r")) {
            byte[] block = readBlock(file, offset, fileLength, encrypt);
            byte[][] processedBlock = new byte[1][];

            if (encrypt) {
                encrypt(block, processedBlock);
            } else {
                decrypt(block, processedBlock);
            }

            return new Pair<>(offset, processedBlock[0]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void syncProcess(String inputFile, String outputFile, boolean encrypt) {
        if (inputFile == null || outputFile == null) {
            throw new RuntimeException("Input and output files cannot be null");
        }


        int blockSize = encrypt ? BLOCK_SIZE - blockLength : BLOCK_SIZE;
        byte[][] processedBlock = new byte[1][];

        try (RandomAccessFile file = new RandomAccessFile(inputFile, "r")) {
            long fileLength = file.length();
            for (long readBytes = 0L; readBytes < fileLength; readBytes += blockSize) {
                var block = readBlock(file, readBytes, fileLength, encrypt);

                if (encrypt) {
                    encrypt(block, processedBlock);
                } else {
                    decrypt(block, processedBlock);
                }

                writeFile(outputFile, processedBlock[0], readBytes);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] readBlock(RandomAccessFile inputFile, long offset, long fileLength, boolean encrypt) throws IOException {
        inputFile.seek(offset);
        int bytesRead = 0;

        int blockSize = encrypt ? BLOCK_SIZE - blockLength : BLOCK_SIZE;

        long unreadBytes = fileLength - offset;
        int arrayLength = (int) (unreadBytes < blockSize ? unreadBytes : blockSize);

        byte[] bytes = new byte[arrayLength];

        while (bytesRead < blockSize && inputFile.getFilePointer() < fileLength) {
            bytes[bytesRead++] = inputFile.readByte();
        }

        return bytes;
    }

    private void writeFile(RandomAccessFile outputFile, byte[] bytes) throws IOException {
        for (var value : bytes) {
            outputFile.writeByte(value);
        }
    }

    private void writeFile(String outputFile, byte[] bytes, long offset) throws IOException {
        try (RandomAccessFile output = new RandomAccessFile(outputFile, "rw")) {
            output.seek(offset);
            for (var value : bytes) {
                output.write(value);
            }
        }
    }
}
