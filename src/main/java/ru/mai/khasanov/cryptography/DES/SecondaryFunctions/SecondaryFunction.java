package ru.mai.khasanov.cryptography.DES.SecondaryFunctions;

public class SecondaryFunction {

    public static byte[] permutation(
            byte[] block,
            int[] PBlock,
            boolean reverseOrder,
            int indexing) {
        if (block == null || PBlock == null) {
            throw new RuntimeException("Null pointer encountered");
        }

        byte[] result = new byte[8];
        int currentIndex = 0;

        for (var i : PBlock) {
            int pos = i - indexing; // учитываем с какого номера начинается индексация
            int bitOffset = reverseOrder ? pos % 8 : 7 - pos % 8; // находим смещение, учитывая как нумеруются биты

            boolean value = (block[pos / 8] & (1 << bitOffset)) != 0;

            int resultOffset = reverseOrder ? currentIndex % 8 : 7 - currentIndex % 8;

            result[currentIndex / 8] = (byte) (value
                    ? result[currentIndex / 8] | (1 << resultOffset)
                    : result[currentIndex / 8] & ~(1 << resultOffset));

            currentIndex++;
        }

        return result;
    }

}
