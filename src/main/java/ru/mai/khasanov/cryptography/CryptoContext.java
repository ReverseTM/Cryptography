package ru.mai.khasanov.cryptography;

import org.javatuples.Pair;
import ru.mai.khasanov.cryptography.CipherMode.ACipherMode;
import ru.mai.khasanov.cryptography.CipherMode.CipherMode;
import ru.mai.khasanov.cryptography.Padding.PaddingMode;
import ru.mai.khasanov.cryptography.interfaces.IEncryptor;
import ru.mai.khasanov.cryptography.interfaces.IPadding;

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
            byte[] IV,
            Object... args) {
        blockLength = encryptor.getBlockLength();
        encryptor.setKeys(key);
        cipherMode = CipherMode.getInstance(cypherMode, encryptor, IV, args);
        padding = PaddingMode.getInstance(paddingMode);
    }

    public void encrypt(byte[] text, byte[][] cipherText) {
        if (text.length % blockLength != 0) {
            text = padding.applyPadding(text, blockLength);
        }
        cipherText[0] = cipherMode.encrypt(text);
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

    private void asyncProcess(String inputFile, String outputFile, boolean encrypt) throws IOException {
        if (inputFile == null || outputFile == null) {
            throw new RuntimeException("Input and output files cannot be null");
        }

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        Map<Long, byte[]> cipherTextMap = new ConcurrentSkipListMap<>();

        try (RandomAccessFile file = new RandomAccessFile(inputFile, "r")) {
            long fileLength = file.length();

            for (long readBytes = 0L; readBytes < fileLength; readBytes += BLOCK_SIZE) {
                long currentReadBytes = readBytes;
                CompletableFuture<Void> future = CompletableFuture
                        .supplyAsync(() -> processFile(file, currentReadBytes, fileLength, encrypt), executorService)
                        .thenAcceptAsync(result -> {
                            cipherTextMap.put(result.getValue0(), result.getValue1());
                        }, executorService);
                futures.add(future);
            }

            CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
            allOf.join();
        } catch (IOException e) {
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
        }
    }

    private Pair<Long, byte[]> processFile(RandomAccessFile inputFile, long offset, long fileLength, boolean encrypt) {
        try {
            byte[] block = readBlock(inputFile, offset, fileLength);
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

        List<byte[]> text = new ArrayList<>();
        byte[][] processedBlock = new byte[1][];

        try (RandomAccessFile file = new RandomAccessFile(inputFile, "r")) {
            long fileLength = file.length();

            for (long readBytes = 0L; readBytes < fileLength; readBytes += BLOCK_SIZE) {
                var block = readBlock(file, readBytes, fileLength);

                if (encrypt) {
                    encrypt(block, processedBlock);
                } else {
                    decrypt(block, processedBlock);
                }

                text.add(processedBlock[0]);
            }

            writeFile(outputFile, text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] readBlock(RandomAccessFile inputFile, long offset, long fileLength) throws IOException {
        inputFile.seek(offset);
        int bytesRead = 0;

        long unreadBytes = fileLength - offset;
        int arrayLength = (int) (unreadBytes < BLOCK_SIZE ? unreadBytes : BLOCK_SIZE);

        byte[] bytes = new byte[arrayLength];

        while (bytesRead < BLOCK_SIZE && inputFile.getFilePointer() < fileLength) {
            bytes[bytesRead++] = inputFile.readByte();
        }

        return bytes;
    }

    private void writeFile(RandomAccessFile outputFile, byte[] bytes) throws IOException {
        for (var value : bytes) {
            outputFile.writeByte(value);
        }
    }

    private void writeFile(String outputFile, List<byte[]> bytes) throws IOException {
        try (RandomAccessFile output = new RandomAccessFile(outputFile, "rw")) {
            for (var value : bytes) {
                output.write(value);
            }
        }
    }
}
