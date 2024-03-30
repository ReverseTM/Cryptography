package ru.mai.khasanov.cryptography.UtilFunctions;

import java.security.SecureRandom;

public class IVGenerator {
    public static byte[] generateIV(int size) {
        byte[] IV = new byte[size];
        SecureRandom random = new SecureRandom();
        random.nextBytes(IV);

        return IV;
    }
}
