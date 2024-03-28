package ru.mai.khasanov.cryptography.DEAL;

import ru.mai.khasanov.cryptography.DES.DESAdapter;
import ru.mai.khasanov.cryptography.ExpandKey.DEALKeyExpand;
import ru.mai.khasanov.cryptography.FeistelNetwork.FeistelNetwork;
import ru.mai.khasanov.cryptography.interfaces.IEncryptor;

public class DEAL implements IEncryptor {
    public enum Version {
        DEAL_128,
        DEAL_192,
        DEAL_256
    }

    private final IEncryptor feistelNetwork;
    private int rounds;

    public DEAL(Version version) {
        switch (version) {
            case DEAL_128, DEAL_192 -> this.rounds = 6;
            case DEAL_256 -> this.rounds = 8;
        }

        feistelNetwork = new FeistelNetwork(new DEALKeyExpand(version), new DESAdapter(), rounds);
    }

    @Override
    public byte[] encode(byte[] data) {
        // TODO ADAPT FOR FEISTEL NETWORK
        return feistelNetwork.encode(data);
    }

    @Override
    public byte[] decode(byte[] data) {
        // TODO ADAPT FOR FEISTEL NETWORK
        return feistelNetwork.decode(data);
    }

    @Override
    public void setKeys(byte[] key) {
        feistelNetwork.setKeys(key);
    }

    @Override
    public int getBlockLength() {
        return 0;
    }
}
