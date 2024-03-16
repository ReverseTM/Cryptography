package ru.mai.khasanov.cryptography.CipherMode;

import ru.mai.khasanov.cryptography.interfaces.IEncryptor;

public class CipherMode {
    public enum Mode {
        ECB,
        CBC,
        CFB,
        OFB,
        CTR,
        RD
    }

    public static ACipherMode getInstance(
            Mode mode,
            IEncryptor encryptor,
            byte[] IV,
            Object... args) {
        return switch (mode) {
            case ECB -> new ECB(encryptor, IV, 8);
            case CBC -> null;
            case CFB -> null;
            case OFB -> null;
            case CTR -> null;
            case RD -> null;
        };
    }
}
