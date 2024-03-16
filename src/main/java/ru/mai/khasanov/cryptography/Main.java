package ru.mai.khasanov.cryptography;

import ru.mai.khasanov.cryptography.CipherMode.CipherMode;
import ru.mai.khasanov.cryptography.DES.DES;
import ru.mai.khasanov.cryptography.Padding.PaddingMode;
import ru.mai.khasanov.cryptography.UtilFunctions.Util;
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

        byte[] text = { 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8 };
        byte[][] result = new byte[1][];



    }
}