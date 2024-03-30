package ru.mai.khasanov.cryptography.CipherMode;

import ru.mai.khasanov.cryptography.UtilFunctions.Util;
import ru.mai.khasanov.cryptography.interfaces.IEncryptor;

import java.util.stream.IntStream;

public class CTR extends ACipherMode {
    public CTR(IEncryptor encryptor, byte[] IV) {
        super(encryptor, IV, encryptor.getBlockLength());
    }

    @Override
    public byte[] encrypt(byte[] data) {
        return processData(data);
    }

    @Override
    public byte[] decrypt(byte[] data) {
        return processData(data);
    }

    private byte[] processData(byte[] data) {
        byte[] result = new byte[data.length];

        IntStream.range(0, data.length / blockLength)
                .parallel()
                .forEach(i -> {
                    int startIndex = i * blockLength;
                    byte[] block = new byte[blockLength];
                    System.arraycopy(data, startIndex, block, 0, blockLength);

                    byte[] blockForProcess = new byte[blockLength];
                    int length = blockLength / 2;
                    System.arraycopy(IV, 0, blockForProcess, 0, length);

                    byte[] counterInBytes = new byte[Integer.BYTES];
                    for (int j = 0; j < counterInBytes.length; ++j) {
                        counterInBytes[j] = (byte) (i >> (3 - j) * 8);
                    }
                    System.arraycopy(counterInBytes, 0, blockForProcess, length, length);

                    byte[] processedBlock = Util.xor(block, encryptor.encode(blockForProcess));
                    System.arraycopy(processedBlock, 0, result, startIndex, processedBlock.length);
                });

        return result;
    }
}
