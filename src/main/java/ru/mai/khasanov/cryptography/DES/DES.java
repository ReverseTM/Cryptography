package ru.mai.khasanov.cryptography.DES;

import ru.mai.khasanov.cryptography.ExpandKey.DESKeyExpand;
import ru.mai.khasanov.cryptography.FeistelNetwork.FeistelFunction;
import ru.mai.khasanov.cryptography.FeistelNetwork.FeistelNetwork;
import ru.mai.khasanov.cryptography.UtilFunctions.Util;
import ru.mai.khasanov.cryptography.constants.Constants;
import ru.mai.khasanov.cryptography.interfaces.IEncryptor;

import java.util.Arrays;

public class DES implements IEncryptor {
    private final IEncryptor feistelNetwork;

    public DES() {
        this.feistelNetwork = new FeistelNetwork(new DESKeyExpand(16), new FeistelFunction(), 16);
    }
    @Override
    public byte[] encode(byte[] data) {
        var cipherText = feistelNetwork.encode(Util.permutation(data, Constants.IP, false, 1));
        return Util.permutation(cipherText, Constants.P, false, 1);
    }

    @Override
    public byte[] decode(byte[] data) {
        var cipherText = feistelNetwork.decode(Util.permutation(data, Constants.IP, false, 1));
        return Util.permutation(cipherText, Constants.P, false, 1);
    }

    @Override
    public void setKeys(byte[] key) {
        feistelNetwork.setKeys(key);
    }

    @Override
    public int getBlockLength() {
        return feistelNetwork.getBlockLength();
    }
}
