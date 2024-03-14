package ru.mai.khasanov.cryptography;

import ru.mai.khasanov.cryptography.DES.DES;
import ru.mai.khasanov.cryptography.ExpandKey.KeyExpand;
import ru.mai.khasanov.cryptography.FeistelNetwork.FeistelFunction;
import ru.mai.khasanov.cryptography.FeistelNetwork.FeistelNetwork;
import ru.mai.khasanov.cryptography.UtilFunctions.Util;
import ru.mai.khasanov.cryptography.interfaces.IEncryptor;
import ru.mai.khasanov.cryptography.interfaces.IKeyExpand;

import java.nio.ByteBuffer;

public class Main {
    public static void main(String[] args) {
        byte[] key = { 0x24, 0x26, 0x27, 0x28, 0x29, 0x2A, 0x2B, 0x2C };
        IEncryptor encryptor = new DES(new KeyExpand(), new FeistelFunction());

        CryptoContext context = new CryptoContext(
                key,
                encryptor,
                CryptoContext.CypherModeEnum.ECB,
                CryptoContext.PaddingMode.Zeros);

        byte[] text = { 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8 };
        byte[][] result = new byte[1][];

        for (byte b : text) {
            System.out.printf("%02X ", b);
        }
        System.out.println();

        context.encrypt(text, result);
        text = result[0];

        for (byte b : text) {
            System.out.printf("%02X ", b);
        }
        System.out.println();

        context.decrypt(text, result);
        text = result[0];

        for (byte b : text) {
            System.out.printf("%02X ", b);
        }
        System.out.println();

    }
}