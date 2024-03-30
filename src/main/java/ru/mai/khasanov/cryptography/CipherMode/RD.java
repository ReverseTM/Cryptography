package ru.mai.khasanov.cryptography.CipherMode;

import ru.mai.khasanov.cryptography.UtilFunctions.Util;
import ru.mai.khasanov.cryptography.interfaces.IEncryptor;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.stream.IntStream;

public class RD extends ACipherMode {
    private final BigInteger delta;
    public RD(IEncryptor encryptor, byte[] IV) {
        super(encryptor, IV, encryptor.getBlockLength());
        delta = new BigInteger(Arrays.copyOf(IV, blockLength / 2));
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

        BigInteger initialStart = new BigInteger(IV);

        IntStream.range(0, data.length / blockLength)
                .parallel()
                .forEach(i -> {
                    BigInteger initial = initialStart.add(delta.multiply(BigInteger.valueOf(i)));
                    int startIndex = i * blockLength;
                    byte[] block = new byte[blockLength];
                    System.arraycopy(data, startIndex, block, 0, blockLength);
                    byte[] processedBlock = encrypt
                            ? encryptor.encode(Util.xor(initial.toByteArray(), block))
                            : Util.xor(encryptor.decode(block), initial.toByteArray());
                    System.arraycopy(processedBlock, 0, result, startIndex, processedBlock.length);
                });

        return result;
    }
}
