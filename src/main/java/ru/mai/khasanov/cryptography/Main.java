package ru.mai.khasanov.cryptography;

import ru.mai.khasanov.cryptography.CipherMode.CipherMode;
import ru.mai.khasanov.cryptography.DES.DES;
import ru.mai.khasanov.cryptography.Padding.PaddingMode;
import ru.mai.khasanov.cryptography.interfaces.IEncryptor;

public class Main {
    public static void main(String[] args) {
        byte[] key = { 0x25, 0x25, 0x25, 0x25, 0x25, 0x25, 0x25, 0x25 };
        IEncryptor encryptor = new DES();

        CryptoContext context = new CryptoContext(
                key,
                encryptor,
                CipherMode.Mode.ECB,
                PaddingMode.Mode.Zeros,
                null);

        String originalFile = "C:/Users/Reverse/Desktop/original.png";
        String encryptedFile = "C:/Users/Reverse/Desktop/encrypted.txt";
        String decryptedFile = "C:/Users/Reverse/Desktop/decrypted.png";

        var start = System.currentTimeMillis();

        context.encrypt(originalFile, encryptedFile);
        context.decrypt(encryptedFile, decryptedFile);

        var end = System.currentTimeMillis();

        System.out.println("Time: " + (end - start) + " ms");
    }
}