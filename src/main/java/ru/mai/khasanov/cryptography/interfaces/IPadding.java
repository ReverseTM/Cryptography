package ru.mai.khasanov.cryptography.interfaces;

public interface IPadding {
    byte[] applyPadding(byte[] data, int blockSize);

    byte[] removePadding(byte[] data);
}
