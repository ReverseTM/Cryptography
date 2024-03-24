package ru.mai.khasanov.cryptography.Padding;

import ru.mai.khasanov.cryptography.interfaces.IPadding;

import java.security.SecureRandom;
import java.util.Arrays;

public class ISO_10126 implements IPadding {
    @Override
    public byte[] applyPadding(byte[] data, int blockSize) {
        int paddingLength = (data.length % blockSize == 0)
                ? blockSize
                : blockSize - (data.length % blockSize);

        byte[] paddingBytes = new byte[paddingLength];
        new SecureRandom().nextBytes(paddingBytes);
        paddingBytes[paddingLength - 1] = (byte) paddingLength;

        byte[] paddedInput = new byte[data.length + paddingLength];
        System.arraycopy(data, 0, paddedInput, 0, data.length);
        System.arraycopy(paddingBytes, 0, paddedInput, data.length, paddingLength);

        return paddedInput;
    }

    @Override
    public byte[] removePadding(byte[] data) {
        return Arrays.copyOf(data, data.length - data[data.length - 1]);
    }
}
