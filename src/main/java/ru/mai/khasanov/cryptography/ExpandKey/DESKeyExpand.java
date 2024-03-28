package ru.mai.khasanov.cryptography.ExpandKey;

import ru.mai.khasanov.cryptography.UtilFunctions.Util;
import ru.mai.khasanov.cryptography.constants.Constants;
import ru.mai.khasanov.cryptography.interfaces.IKeyExpand;

public class DESKeyExpand implements IKeyExpand {
    private int rounds = 16;

    public DESKeyExpand(int rounds) {
        this.rounds = rounds;
    }
    @Override
    public byte[][] genKeys(byte[] key) {
        int countUnit = 0;
        for (int i = 0; i < key.length * 8; ++i) {
            if (((key[i / 8] >>> (i % 8)) & 1) == 1) {
                countUnit++;
            }
            if ((i + 1) % 8 == 0) {
                if ((countUnit & 1) == 0) {
                    throw new RuntimeException("Invalid key");
                }
                countUnit = 0;
            }
        }

        byte[][] keys = new byte[rounds][];

        // Сжимающая перестановка
        byte[] pKey = Util.permutation(key, Constants.PC_1, false, 1);

        // Делим ключ на 2 блока по 28 бит
        int C = ((pKey[0] & 0xFF) << 20)
                | ((pKey[1] & 0xFF) << 12)
                | ((pKey[2] & 0xFF) << 4)
                | ((pKey[3] & 0xFF) >>> 4);

        int D = ((pKey[3] & 0x0F) << 24)
                | ((pKey[4] & 0xFF) << 16)
                | ((pKey[5] & 0xFF) << 8)
                | ((pKey[6] & 0xFF));

        // Генерируем раундовые ключи
        for (int i = 0; i < rounds; ++i) {
            // Делаем циклические сдвиги
            C = Util.leftCycleShift(C, 28, Constants.SHIFTS[i]);
            D = Util.leftCycleShift(D, 28, Constants.SHIFTS[i]);

            // Склеиваем два блока в 1 блок
            long CD = ((long) C) << 28 | D;

            byte[] byteCD = new byte[7];
            for (int j = 0; j < 7; ++j) {
                byteCD[j] = (byte) ((CD >>> ((6 - j) * 8)) & 0xFF);
            }

            // Перестановка
            keys[i] = Util.permutation(byteCD, Constants.PC_2, false, 1);
        }

        return keys;
    }
}
