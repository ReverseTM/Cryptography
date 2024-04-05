package ru.mai.khasanov.cryptography;

import ru.mai.khasanov.cryptography.CipherMode.CipherMode;
import ru.mai.khasanov.cryptography.DEAL.DEAL;
import ru.mai.khasanov.cryptography.DES.DES;
import ru.mai.khasanov.cryptography.ExpandKey.DEALKeyExpand;
import ru.mai.khasanov.cryptography.Padding.PaddingMode;
import ru.mai.khasanov.cryptography.UtilFunctions.IVGenerator;
import ru.mai.khasanov.cryptography.interfaces.IEncryptor;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        byte[] key = {
                0x25, 0x25, 0x25, 0x25, 0x25, 0x25, 0x25, 0x25,
                0x25, 0x25, 0x25, 0x25, 0x25, 0x25, 0x25, 0x25
            };

        //byte[] key = { 0x25, 0x25, 0x25, 0x25, 0x25, 0x25, 0x25, 0x25 };
        //byte[] IV = { 0x1, 0x22, 0x3F, 0x0F, 0x22, 0x44, 0x44, 0x44 };
        byte[] IV = { 0x1, 0x22, 0x3F, 0x0F, 0x22, 0x44, 0x44, 0x44, 0x1, 0x22, 0x3F, 0x0F, 0x22, 0x44, 0x44, 0x44 };

        IEncryptor encryptor = new DEAL(DEAL.Version.DEAL_128);
        //IEncryptor encryptor = new DES();

        CryptoContext context = new CryptoContext(
                key,
                encryptor,
                CipherMode.Mode.ECB,
                PaddingMode.Mode.PKCS7,
                IV);

        String text = "Hello World!aaabbbаыыпаыыыыыыыыыыыып53523532534!";
        System.out.println(Arrays.toString(text.getBytes()));

        byte[][] enc = new byte[1][];
        context.encrypt(text.getBytes(), enc);
        System.out.println(Arrays.toString(enc[0]));

        byte[][] dec = new byte[1][];
        context.decrypt(enc[0], dec);

        System.out.println(Arrays.toString(dec[0]));
        //System.out.println(new String(dec[0]));


        var start = System.currentTimeMillis();

//        context.encrypt(x, y);
//        var result = new byte[1][];
//        context.decrypt(y[0], result);
//        System.out.println(Arrays.toString(result[0]));

//        context.encrypt(originalFile, encryptedFile);
//        context.decrypt(encryptedFile, decryptedFile);

        var end = System.currentTimeMillis();

        System.out.println("Time: " + (end - start) + " ms");
    }
}