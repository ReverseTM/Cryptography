package ru.mai.khasanov.cryptography.CipherMode;

import ru.mai.khasanov.cryptography.interfaces.IEncryptor;

import java.util.stream.IntStream;

public class ECB extends ACipherMode {
    public ECB(IEncryptor encryptor, byte[] IV) {
        super(encryptor, IV, encryptor.getBlockLength());
    }

    @Override
    public byte[] encrypt(byte[] data) {
        return processData(data, true);
    }

    @Override
    public byte[] decrypt(byte[] data) {
        return processData(data, false);
    }

    private byte[] processData(byte[] data, boolean encrypt) {
        byte[] result = new byte[data.length];
        IntStream.range(0, data.length / blockLength)
                //.parallel()
                .forEach(i -> {
                    int startIndex = i * blockLength;
                    byte[] block = new byte[blockLength];
                    System.arraycopy(data, startIndex, block, 0, blockLength);

                    byte[] processedBlock = encrypt ? encryptor.encode(block) : encryptor.decode(block);
                    System.arraycopy(processedBlock, 0, result, startIndex, processedBlock.length);
                });

        return result;
    }
}
