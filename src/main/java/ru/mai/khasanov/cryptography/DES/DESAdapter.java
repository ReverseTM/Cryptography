package ru.mai.khasanov.cryptography.DES;

import ru.mai.khasanov.cryptography.interfaces.IEncrypt;

public class DESAdapter implements IEncrypt {
    private final DES des;

    public DESAdapter() {
        des = new DES();
    }

    @Override
    public byte[] encrypt(byte[] block, byte[] roundKey) {
        des.setKeys(roundKey);
        return des.encode(block);
    }
}
