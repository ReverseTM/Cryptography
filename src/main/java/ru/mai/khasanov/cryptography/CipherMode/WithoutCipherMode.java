package ru.mai.khasanov.cryptography.CipherMode;

import ru.mai.khasanov.cryptography.interfaces.IEncryptor;

public class WithoutCipherMode extends ACipherMode {

    protected WithoutCipherMode(IEncryptor encryptor, byte[] IV) {
        super(encryptor, IV, encryptor.getBlockLength());
    }

    @Override
    public byte[] encrypt(byte[] data) {
        return encryptor.encode(data);
    }

    @Override
    public byte[] decrypt(byte[] data) {
        return encryptor.decode(data);
    }
}
