package ru.mai.khasanov.cryptography;

import ru.mai.khasanov.cryptography.interfaces.IEncryptor;
import ru.mai.khasanov.cryptography.CipherMode.CipherMode;
import ru.mai.khasanov.cryptography.interfaces.IPadding;

import java.io.File;

public class CryptoContext {
    public enum CypherModeEnum {
        ECB,
        CBC,
        CFB,
        OFB,
        CTR,
        RD
    }

    public enum PaddingMode {
        Zeros,
        ANSI_X_923,
        PKCS7,
        ISO_10126
    }

    private byte[] key;
    private IEncryptor encryptor;
    private CipherMode cipherMode;
    private IPadding padding;

    public CryptoContext(
            byte[] key,
            IEncryptor encryptor,
            CypherModeEnum cypherMode,
            PaddingMode paddingMode,
            //byte[] IV,
            Object... args) {
        this.key = key;
        this.encryptor = encryptor;
        encryptor.setKeys(key);
    }

    public void encrypt(byte[] text, byte[][] cipherText) {
        cipherText[0] = encryptor.encode(text);
    }

    public void decrypt(byte[] cipherText, byte[][] text) {
        text[0] = encryptor.decode(cipherText);
    }

    public void encrypt(File text, File cipherText) {

    }

    public void decrypt(File cipherText, File text) {

    }
}
