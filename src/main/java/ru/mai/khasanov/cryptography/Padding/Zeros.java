package ru.mai.khasanov.cryptography.Padding;

import ru.mai.khasanov.cryptography.interfaces.IPadding;

import java.util.Arrays;

public class Zeros implements IPadding {
    @Override
    public byte[] applyPadding(byte[] data, int blockSize) {
        // Длину дополнения
        int paddingLength = blockSize - (data.length % blockSize);
        byte[] paddedInput = new byte[data.length + paddingLength];
        System.arraycopy(data, 0, paddedInput, 0, data.length);

        return paddedInput;
    }

    @Override
    public byte[] removePadding(byte[] data) {
        int lastNonZeroIndex = data.length - 1;
        // Находим индекс последнего ненулевого байта
        for (int i = data.length - 1; i >= 0; i--) {
            if (data[i] != 0) {
                lastNonZeroIndex = i;
                break;
            }
        }

        return Arrays.copyOf(data, lastNonZeroIndex + 1);
    }
}
