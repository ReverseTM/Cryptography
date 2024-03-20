package ru.mai.khasanov.cryptography.FeistelNetwork;

import ru.mai.khasanov.cryptography.UtilFunctions.Util;
import ru.mai.khasanov.cryptography.interfaces.IEncrypt;
import ru.mai.khasanov.cryptography.interfaces.IEncryptor;
import ru.mai.khasanov.cryptography.interfaces.IKeyExpand;

public class FeistelNetwork implements IEncryptor {
    private final IKeyExpand keyExtend;
    private final IEncrypt feistelFunction;
    protected int rounds;
    private byte[][] roundKeys;

    public FeistelNetwork(IKeyExpand keyExtend, IEncrypt feistelFunction, int rounds) {
        this.keyExtend = keyExtend;
        this.feistelFunction = feistelFunction;
        this.rounds = rounds;
    }

    @Override
    public byte[] encode(byte[] data) {
        // Делим входной блок(64 бита) на 2 блока по 32 бита
        var half = data.length / 2;
        byte[] L = new byte[half], R = new byte[half];

        System.arraycopy(data, 0, L, 0, half);
        System.arraycopy(data, half, R, 0, half);

        // Выполняем 16 раундов шифрования, на 16 раунде не меняем местами блоки
        for (int i = 0; i < rounds - 1; ++i) {
            byte[] tmp = Util.xor(L, feistelFunction.encrypt(R, roundKeys[i]));
            L = R;
            R = tmp;
        }
        L = Util.xor(L, feistelFunction.encrypt(R, roundKeys[rounds - 1]));

        byte[] result = new byte[data.length];

        System.arraycopy(L, 0, result, 0, half);
        System.arraycopy(R, 0, result, half, half);

        return result;
    }

    @Override
    public byte[] decode(byte[] data) {
        // Делим входной блок(64 бита) на 2 блока по 32 бита
        var half = data.length / 2;
        byte[] L = new byte[half], R = new byte[half];

        System.arraycopy(data, 0, L, 0, half);
        System.arraycopy(data, half, R, 0, half);

        // Выполняем 16 раундов дешифрования, на 1 раунде не меняем местами блоки
        L = Util.xor(L, feistelFunction.encrypt(R, roundKeys[rounds - 1]));
        for (int i = rounds - 2; i >= 0; --i) {
            byte[] tmp = Util.xor(R, feistelFunction.encrypt(L, roundKeys[i]));
            R = L;
            L = tmp;
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
