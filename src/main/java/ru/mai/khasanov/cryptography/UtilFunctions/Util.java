package ru.mai.khasanov.cryptography.UtilFunctions;

import ru.mai.khasanov.cryptography.constants.Constants;

public class Util {

    public static byte[] permutation(
            byte[] block,
            int[] PBlock,
            boolean reverseOrder,
            int indexing) {
        if (block == null || PBlock == null) {
            throw new RuntimeException("Null pointer encountered");
        }
        if (indexing < 0 || indexing > 1) {
            throw new RuntimeException("Indexing out of bounds");
        }

        byte[] result = new byte[(PBlock.length + 7) / 8];
        int currentIndex = 0;

        for (var i : PBlock) {
            // учитываем с какого номера начинается индексация
            int pos = i - indexing;

            // находим смещение, учитывая как нумеруются биты
            int bitOffset = reverseOrder ? pos % 8 : 7 - pos % 8;
            int resultOffset = 7 - currentIndex % 8;

            // находим нужный индекс, учитывая нумерацию битов
            int blockIndex = reverseOrder ? block.length - 1 - pos / 8 : pos / 8;
            int resultIndex = currentIndex / 8;

            boolean value = (block[blockIndex] & (1 << bitOffset)) != 0;

            result[resultIndex] = (byte) (value
                    ? result[resultIndex] | (1 << resultOffset)
                    : result[resultIndex] & ~(1 << resultOffset));

            currentIndex++;
        }

        return result;
    }

    public static byte[] Substitution(byte[] block)
    {
        byte[] result = {0, 0, 0, 0};

        // Преобразовываем массив из 6 байтов в лонг
        long longBlock = 0;
        for (int i = 0; i < 6; ++i) {
            longBlock = (longBlock << 8) | (block[i] & 0xFF);
        }

        // Итерируемся по каждому блоку по 6 бит
        for (int i = 0; i < 8; i++)
        {
            // Достаём 6 бит
            int bits = (int) ((longBlock >> ((7 - i) * 6)) & 0b111111);
            int[] bitsArray = new int[6];

            // Достаём значение каждого бита
            for (int j = 5; j >= 0; --j) {
                bitsArray[j] = (bits >> j) & 1;
            }

            // Находим номер строки и столбца
            int row = (bitsArray[0] << 1) | bitsArray[5];
            int col = (bitsArray[1] << 3)
                    | (bitsArray[2] << 2)
                    | (bitsArray[3] << 1)
                    | (bitsArray[4]);

            int value = Constants.F_S[i][row * 16 + col];

            result[i / 2] |= (byte) ((i & 1) == 0
                    ? value << 4
                    : value);
        }
        return result;
    }

    public static byte[] xor(byte[] x, byte[] y) {
        var size = Math.min(x.length, y.length);

        var result = new byte[size];
        for (int i = 0; i < size; i++) {
            result[i] = (byte) (x[i] ^ y[i]);
        }
        return result;
    }

    public static byte[] intToBytes(int value) {
        byte[] result = new byte[4];

        result[0] = (byte) (value >> 24);
        result[1] = (byte) (value >> 16);
        result[2] = (byte) (value >> 8);
        result[3] = (byte) value;

        return result;
    }

    public static int bytesToInt(byte[] bytes) {
        return (bytes[0] & 0xFF) << 24 |
                (bytes[1] & 0xFF) << 16 |
                (bytes[2] & 0xFF) << 8 |
                (bytes[3] & 0xFF);
    }
}
