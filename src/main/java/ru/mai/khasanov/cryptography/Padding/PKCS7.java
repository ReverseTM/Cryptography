package ru.mai.khasanov.cryptography.Padding;

import ru.mai.khasanov.cryptography.interfaces.IPadding;

import java.util.Arrays;

public class PKCS7 implements IPadding {
    @Override
    public byte[] applyPadding(byte[] data, int blockSize) {
        int paddingLength = (data.length % blockSize == 0)
                ? blockSize
                : blockSize - (data.length % blockSize);

        byte[] paddedInput = new byte[data.length + paddingLength];
        System.arraycopy(data, 0, paddedInput, 0, data.length);
        Arrays.fill(paddedInput, data.length, paddedInput.length, (byte) paddingLength);
        System.out.println("Padding length: " + paddedInput[paddedInput.length - 1]);
        return paddedInput;
    }

    @Override
    public byte[] removePadding(byte[] data) {
        System.out.println("Padding length: " + data[data.length - 1]);
        return Arrays.copyOf(data, data.length - data[data.length - 1]);
    }
}
