package ru.mai.khasanov.cryptography.ExpandKey;

import lombok.Setter;
import ru.mai.khasanov.cryptography.UtilFunctions.Util;
import ru.mai.khasanov.cryptography.constants.Constants;
import ru.mai.khasanov.cryptography.interfaces.IKeyExpand;

@Setter
public class KeyExpand implements IKeyExpand {
    private int rounds = 16;
    @Override
    public byte[][] genKeys(byte[] key) {
        byte[][] keys = new byte[rounds][];

        byte[] pKey = Util.permutation(key, Constants.PC_1, true, 1);

        int C = ((pKey[0] & 0xFF) << 20)
                | ((pKey[1] & 0xFF) << 12)
                | ((pKey[2] & 0xFF) << 4)
                | ((pKey[3] & 0xFF) >>> 4);

        int D = (((pKey[3] & 0xFF) & 0x0F) << 24)
                | ((pKey[4] & 0xFF) << 16)
                | ((pKey[5] & 0xFF) << 8)
                | ((pKey[6] & 0xFF));

        for (int i = 0; i < rounds; ++i) {
            C = Util.leftCycleShift(C, 28, Constants.SHIFTS[i]);
            D = Util.leftCycleShift(D, 28, Constants.SHIFTS[i]);
            long CD = ((long) C) << 28 | D;

            byte[] byteCD = new byte[7];
            for (int j = 0; j < 7; ++j) {
                byteCD[j] = (byte) ((CD >>> ((6 - j) * 8)) & 0xFF);
            }

            keys[i] = Util.permutation(byteCD, Constants.PC_2, true, 1);
        }

        return keys;
    }
}
