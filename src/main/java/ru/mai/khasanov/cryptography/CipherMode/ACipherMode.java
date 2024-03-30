package ru.mai.khasanov.cryptography.CipherMode;

import ru.mai.khasanov.cryptography.interfaces.IEncryptor;

public abstract class ACipherMode {
    protected final IEncryptor encryptor;
    protected final byte[] IV;
    protected final int blockLength;

    protected ACipherMode(IEncryptor encryptor, byte[] IV, int blockLength) {
        this.encryptor = encryptor;
        this.IV = IV;
        this.blockLength = blockLength;
    }

    public abstract byte[] encrypt(byte[] data);
    public abstract byte[] decrypt(byte[] data);
}
