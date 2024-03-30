package ru.mai.khasanov.cryptography.CipherMode;

import ru.mai.khasanov.cryptography.interfaces.IEncryptor;

public class CipherMode {
    public enum Mode {
        ECB,
        CBC,
        PCBC,
        CFB,
        OFB,
        CTR,
        RD,
        WithoutMode
    }

    public static ACipherMode getInstance(
            Mode mode,
            IEncryptor encryptor,
            byte[] IV)
    {
        return switch (mode) {
            case ECB -> new ECB(encryptor, IV);
            case CBC -> new CBC(encryptor, IV);
            case PCBC -> new PCBC(encryptor, IV);
            case CFB -> new CFB(encryptor, IV);
            case OFB -> new OFB(encryptor, IV);
            case CTR -> new CTR(encryptor, IV);
            case RD -> new RD(encryptor, IV);
            case WithoutMode -> new WithoutCipherMode(encryptor, null);
        };
    }
}
