package ru.mai.khasanov.cryptography.RoundFunction;

import ru.mai.khasanov.cryptography.DES.DES;
import ru.mai.khasanov.cryptography.interfaces.IConvert;

public class DESAdapter implements IConvert {
    private final DES des;

    public DESAdapter() {
        des = new DES();
    }

    @Override
    public byte[] convert(byte[] block, byte[] roundKey) {
        des.setKeys(roundKey);
        return des.encode(block);
    }
}
