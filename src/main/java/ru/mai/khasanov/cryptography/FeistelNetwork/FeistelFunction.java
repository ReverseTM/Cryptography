package ru.mai.khasanov.cryptography.FeistelNetwork;

import ru.mai.khasanov.cryptography.UtilFunctions.Util;
import ru.mai.khasanov.cryptography.constants.Constants;
import ru.mai.khasanov.cryptography.interfaces.IEncrypt;

public class FeistelFunction implements IEncrypt {
    @Override
    public byte[] encrypt(byte[] block, byte[] roundKey) {
        // Расширяющая перестановка
        byte[] extendedBlock = Util.permutation(block, Constants.F_E, false, 1);
        // XOR с раундовым ключом
        byte[] xoredBlock = Util.xor(extendedBlock, roundKey);
        // Преобразования с помощью S-box
        byte[] transformedBlock = Util.substitution(xoredBlock);
        // Конечная перестановка
        return Util.permutation(transformedBlock, Constants.F_P, false, 1);
    }
}
