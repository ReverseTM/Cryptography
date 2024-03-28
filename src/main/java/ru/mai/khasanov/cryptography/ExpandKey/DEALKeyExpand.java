package ru.mai.khasanov.cryptography.ExpandKey;

import ru.mai.khasanov.cryptography.CipherMode.ACipherMode;
import ru.mai.khasanov.cryptography.CipherMode.CBC;
import ru.mai.khasanov.cryptography.DEAL.DEAL;
import ru.mai.khasanov.cryptography.DES.DES;
import ru.mai.khasanov.cryptography.UtilFunctions.IVGenerator;
import ru.mai.khasanov.cryptography.UtilFunctions.Util;
import ru.mai.khasanov.cryptography.interfaces.IEncryptor;
import ru.mai.khasanov.cryptography.interfaces.IKeyExpand;

import java.util.Arrays;

public class DEALKeyExpand implements IKeyExpand {
    private int rounds;
    private int s;

    private static final byte[] DESKey = {0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF};
    public DEALKeyExpand(DEAL.Version version) {
        switch (version) {
            case DEAL_128 -> {
                this.s = 2;
                this.rounds = 6;
            }
            case DEAL_192 -> {
                this.s = 3;
                this.rounds = 6;
            }
            case DEAL_256 -> {
                this.s = 4;
                this.rounds = 8;
            }
        }
    }
    @Override
    public byte[][] genKeys(byte[] key) {
        byte[][] keys = new byte[rounds][];

        byte[][] DESKeys = new byte[s][];
        for (int i = 0; i < s; ++i) {
            byte[] DESKey = new byte[8];
            System.arraycopy(key, i * 8, DESKey, 0, 8);
            DESKeys[i] = DESKey;
        }

        IEncryptor DES = new DES();
        DES.setKeys(DESKey);
        ACipherMode CBC = new CBC(DES, IVGenerator.generateIV(8));

        keys[0] = CBC.encrypt(DESKeys[0]);
        for (int i = 1; i < s; ++i) {
            keys[i] = CBC.encrypt(Util.xor(DESKeys[i] , keys[i - 1]));
        }

        for (int i = s; i < rounds; ++i) {
            int sIndex = i % s;

            long constant = 1L << (63 - (1 << sIndex));
            byte[] constantInBytes = new byte[8];
            for (int j = 0; j < 8; ++j) {
                constantInBytes[j] = (byte) ((constant >>> ((7 - j) * 8)) & 0xFF);
            }

            keys[i] = CBC.encrypt(Util.xor(Util.xor(DESKeys[sIndex], constantInBytes), keys[i - 1]));
        }

        return keys;
    }
}
