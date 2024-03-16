package ru.mai.khasanov.cryptography.Padding;

import ru.mai.khasanov.cryptography.interfaces.IPadding;

public class Zeros implements IPadding {
    @Override
    public byte[] applyPadding(byte[] data, int blockSize) {
        return new byte[0];
    }

    @Override
    public byte[] removePadding(byte[] data) {
        return new byte[0];
    }
}
