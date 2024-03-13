package ru.mai.khasanov.cryptography.FeistelNetwork;

import ru.mai.khasanov.cryptography.UtilFunctions.Util;
import ru.mai.khasanov.cryptography.constants.Constants;
import ru.mai.khasanov.cryptography.interfaces.IEncrypt;

public class FeistelFunction implements IEncrypt {
    @Override
    public byte[] encrypt(byte[] block, byte[] roundKey) {
        byte[] extendedBlock = Util.permutation(block, Constants.F_P, false, 1);
        byte[] xoredBlock = Util.xor(extendedBlock, roundKey);
        byte[] transformedBlock = Util.substitution(xoredBlock);
        return Util.permutation(transformedBlock, Constants.F_P, false, 1);
    }
}
