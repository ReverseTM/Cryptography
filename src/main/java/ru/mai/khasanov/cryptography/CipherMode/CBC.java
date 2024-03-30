package ru.mai.khasanov.cryptography.CipherMode;

import ru.mai.khasanov.cryptography.UtilFunctions.Util;
import ru.mai.khasanov.cryptography.interfaces.IEncryptor;
import java.util.Arrays;
import java.util.stream.IntStream;

public class CBC extends ACipherMode {
    public CBC(IEncryptor encryptor, byte[] IV) {
        super(encryptor, IV, encryptor.getBlockLength());
    }

    @Override
    public byte[] encrypt(byte[] data) {
        byte[] result = new byte[data.length];
        byte[] previousBlock = IV;

        int length = data.length / blockLength;

        for (int i = 0; i < length; ++i) {
            int startIndex = i * blockLength;
            byte[] block = new byte[blockLength];
            System.arraycopy(data, startIndex, block, 0, blockLength);

            // Шифруем результат XOR текущего блока и результата шифрования предыдущего блока
            byte[] processedBlock = encryptor.encode(Util.xor(block, previousBlock));

            System.arraycopy(processedBlock, 0, result, startIndex, processedBlock.length);
            previousBlock = processedBlock;
        }

        return result;
    }

    @Override
    public byte[] decrypt(byte[] data) {
        byte[] result = new byte[data.length];

        IntStream.range(0, data.length / blockLength)
                .parallel()
                .forEach(i -> {
                    // Получаем предыдущий зашифрованный блок
                    byte[] previousBlock = (i == 0) ? IV : new byte[blockLength];
                    if (i != 0) {
                        System.arraycopy(data, (i - 1) * blockLength, previousBlock, 0, blockLength);
                    }

                    int startIndex = i * blockLength;
                    byte[] currentBlock = new byte[blockLength];
                    System.arraycopy(data, startIndex, currentBlock, 0, blockLength);

                    // XOR с предыдущим зашифрованным блоком
                    byte[] processedBlock = Util.xor(previousBlock, encryptor.decode(currentBlock));
                    System.arraycopy(processedBlock, 0, result, startIndex, processedBlock.length);
                });

        return result;
    }
}
