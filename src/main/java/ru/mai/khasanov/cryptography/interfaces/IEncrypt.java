package ru.mai.khasanov.cryptography.interfaces;


// По сути функция фейстеля
public interface IEncrypt {

    byte[] encrypt(byte[] block, byte[] roundKey);
}
