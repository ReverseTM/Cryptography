package ru.mai.khasanov.cryptography.FeistelNetwork;

import lombok.Setter;
import ru.mai.khasanov.cryptography.UtilFunctions.Util;
import ru.mai.khasanov.cryptography.interfaces.IEncrypt;
import ru.mai.khasanov.cryptography.interfaces.IEncryptor;
import ru.mai.khasanov.cryptography.interfaces.IKeyExtend;

public class FeistelNetwork implements IEncryptor {

    private final IKeyExtend keyExtend;

    private final IEncrypt feistelFunction;

    @Setter
    private int rounds;

    private byte[][] roundKeys;

    public FeistelNetwork(IKeyExtend keyExtend, IEncrypt feistelFunction) {
        this.keyExtend = keyExtend;
        this.feistelFunction = feistelFunction;
    }

    @Override
    public byte[] encode(byte[] data) {
        var half = data.length / 2;
        byte[] L = new byte[half], R = new byte[half];

        System.arraycopy(data, 0, L, 0, half);
        System.arraycopy(data, half, R, 0, half);

        for (int i = 0; i < rounds - 1; ++i) {
            byte[] tmp = Util.xor(L, feistelFunction.encrypt(R, roundKeys[i]));
            L = R;
            R = tmp;
        }
        R = Util.xor(L, feistelFunction.encrypt(R, roundKeys[rounds - 1]));

        byte[] result = new byte[data.length];

        System.arraycopy(L, 0, result, 0, half);
        System.arraycopy(R, 0, result, half, half);

        return result;
    }

    @Override
    public byte[] decode(byte[] data) {
        var half = data.length / 2;
        byte[] L = new byte[half], R = new byte[half];

        System.arraycopy(data, 0, L, 0, half);
        System.arraycopy(data, half, R, 0, half);

        R = Util.xor(L, feistelFunction.encrypt(R, roundKeys[rounds - 1]));
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
}
