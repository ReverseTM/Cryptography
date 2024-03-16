package ru.mai.khasanov.cryptography.Padding;

import ru.mai.khasanov.cryptography.interfaces.IPadding;

public class PaddingMode {
    public enum Mode {
        Zeros,
        ANSI_X_923,
        PKCS7,
        ISO_10126
    }

    public static IPadding getInstance(PaddingMode.Mode mode) {
        return switch (mode) {
            case Zeros -> new Zeros();
            case ANSI_X_923 -> null;
            case PKCS7 -> null;
            case ISO_10126 -> null;
        };
    }

}
