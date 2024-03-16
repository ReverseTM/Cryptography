package ru.mai.khasanov.cryptography.CipherMode;

import ru.mai.khasanov.cryptography.interfaces.IEncryptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ECB extends ACipherMode {
    public ECB(IEncryptor encryptor, byte[] IV, int blockLength) {
        super(encryptor, IV, blockLength);
    }

    @Override
    public byte[] encrypt(byte[] value) {
        List<byte[]> blocks = null;
        List<byte[]> result = new ArrayList<>();

        ExecutorService executorService = Executors.newFixedThreadPool(blockLength);

        List<Future<byte[]>> futures = new ArrayList<>();
        for (int i = 0; i < blockLength; i++) {
            int index = i;
            futures.add(executorService.submit(() -> encryptor.encode(blocks.get(index))));
        }

        for (Future<byte[]> future : futures) {
            try {
                result.add(future.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();

        return flattenByteArrayList(result);
    }

    @Override
    public byte[] decrypt(byte[] data) {
        List<byte[]> blocks = null;
        List<byte[]> result = new ArrayList<>();

        ExecutorService executorService = Executors.newFixedThreadPool(blockLength);

        List<Future<byte[]>> futures = new ArrayList<>();
        for (int i = 0; i < blockLength; i++) {
            int index = i;
            futures.add(executorService.submit(() -> encryptor.decode((blocks.get(index)))));
        }

        for (Future<byte[]> future : futures) {
            try {
                result.add(future.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();

        return flattenByteArrayList(result);
    }

    private byte[] flattenByteArrayList(List<byte[]> list) {
        int totalLength = list.stream().mapToInt(arr -> arr.length).sum();
        byte[] result = new byte[totalLength];

        int currentIndex = 0;
        for (byte[] arr : list) {
            System.arraycopy(arr, 0, result, currentIndex, arr.length);
            currentIndex += arr.length;
        }

        return result;
    }
}
