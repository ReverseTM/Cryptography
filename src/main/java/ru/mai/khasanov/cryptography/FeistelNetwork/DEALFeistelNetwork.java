package ru.mai.khasanov.cryptography.FeistelNetwork;

import ru.mai.khasanov.cryptography.UtilFunctions.Util;
import ru.mai.khasanov.cryptography.interfaces.IConvert;
import ru.mai.khasanov.cryptography.interfaces.IEncryptor;
import ru.mai.khasanov.cryptography.interfaces.IKeyExpand;

public class DEALFeistelNetwork implements IEncryptor {
    private final IKeyExpand keyExtend;
    private final IConvert function;
    protected int rounds;
    private byte[][] roundKeys;

    public DEALFeistelNetwork(IKeyExpand keyExtend, IConvert function, int rounds) {
        this.keyExtend = keyExtend;
        this.function = function;
        this.rounds = rounds;
    }

    @Override
    public byte[] encode(byte[] data) {
        // Делим входной блок на 2 блока
        var half = data.length / 2;
        byte[] L = new byte[half], R = new byte[half];

        System.arraycopy(data, 0, L, 0, half);
        System.arraycopy(data, half, R, 0, half);

        // Выполняем r раундов шифрования
        for (int i = 0; i < rounds; ++i) {
            byte[] tmp = Util.xor(R, function.convert(L, roundKeys[i]));
            R = L;
            L = tmp;
        }

        byte[] result = new byte[data.length];

        System.arraycopy(L, 0, result, 0, half);
        System.arraycopy(R, 0, result, half, half);

        return result;
    }

    @Override
    public byte[] decode(byte[] data) {
        // Делим входной блок на 2 блока
        var half = data.length / 2;
        byte[] L = new byte[half], R = new byte[half];

        System.arraycopy(data, 0, L, 0, half);
        System.arraycopy(data, half, R, 0, half);

        // Выполняем r раундов дешифрования
        for (int i = rounds - 1; i >= 0; --i) {
            byte[] tmp = Util.xor(L, function.convert(R, roundKeys[i]));
            L = R;
            R = tmp;
        }

        byte[] result = new byte[data.length];

        System.arraycopy(L, 0, result, 0, half);
        System.arraycopy(R, 0, result, half, half);

        return result;
    }

    @Override
    public void setKeys(byte[] key) {
        roundKeys = keyExtend.genKeys(key);
    }

    @Override
    public int getBlockLength() {
        return 8;
    }
}
