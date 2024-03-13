package ru.mai.khasanov.cryptography.interfaces;

public interface IEncryptor {

    byte[] encode(byte[] data);

    byte[] decode(byte[] data);

    void setKeys(byte[] key);
}
