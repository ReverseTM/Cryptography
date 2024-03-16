package ru.mai.khasanov.cryptography;

import ru.mai.khasanov.cryptography.CipherMode.CipherMode;
import ru.mai.khasanov.cryptography.Padding.PaddingMode;
import ru.mai.khasanov.cryptography.interfaces.IEncryptor;
import ru.mai.khasanov.cryptography.CipherMode.ACipherMode;
import ru.mai.khasanov.cryptography.interfaces.IPadding;

import java.io.File;

public class CryptoContext {
    private final ACipherMode cipherMode;
    private final IPadding padding;

    public CryptoContext(
            byte[] key,
            IEncryptor encryptor,
            CipherMode.Mode cypherMode,
            PaddingMode.Mode paddingMode,
            byte[] IV,
            Object... args)
    {
        encryptor.setKeys(key);
        cipherMode = CipherMode.getInstance(cypherMode, encryptor, IV, args);
        padding = PaddingMode.getInstance(paddingMode);
    }

    public void encrypt(byte[] text, byte[][] cipherText) {
        cipherText[0] = cipherMode.encrypt(text);
    }

    public void decrypt(byte[] cipherText, byte[][] text) {
        text[0] = cipherMode.decrypt(cipherText);
    }

    public void encrypt(String inputFile, String outputFile) {

    }

    public void decrypt(String inputFile, String outputFile) {

    }
}
