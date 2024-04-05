package ru.mai.khasanov.cryptography.interfaces;


// По сути функция фейстеля
public interface IConvert {

    byte[] convert(byte[] block, byte[] roundKey);
}
