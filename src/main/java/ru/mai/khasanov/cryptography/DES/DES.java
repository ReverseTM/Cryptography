package ru.mai.khasanov.cryptography.DES;

import ru.mai.khasanov.cryptography.ExpandKey.KeyExpand;
import ru.mai.khasanov.cryptography.FeistelNetwork.FeistelFunction;
import ru.mai.khasanov.cryptography.FeistelNetwork.FeistelNetwork;
import ru.mai.khasanov.cryptography.UtilFunctions.Util;
import ru.mai.khasanov.cryptography.constants.Constants;
import ru.mai.khasanov.cryptography.interfaces.IEncryptor;

public class DES implements IEncryptor {
    private final IEncryptor feistelNetwork;

    public DES() {
        this.feistelNetwork = new FeistelNetwork(new KeyExpand(16), new FeistelFunction(), 16);
    }
    @Override
    public byte[] encode(byte[] data) {
        var cipherText = feistelNetwork.encode(Util.permutation(data, Constants.IP, true, 1));
        return Util.permutation(cipherText, Constants.P, true, 1);
    }

    @Override
    public byte[] decode(byte[] data) {
        var cipherText = feistelNetwork.decode(Util.permutation(data, Constants.IP, true, 1));
        return Util.permutation(cipherText, Constants.P, true, 1);
    }

    @Override
    public void setKeys(byte[] key) {
        feistelNetwork.setKeys(key);
    }
}
