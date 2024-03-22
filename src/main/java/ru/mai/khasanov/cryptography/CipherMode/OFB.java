package ru.mai.khasanov.cryptography.CipherMode;

import ru.mai.khasanov.cryptography.UtilFunctions.Util;
import ru.mai.khasanov.cryptography.interfaces.IEncryptor;

public class OFB extends ACipherMode {
    public OFB(IEncryptor encryptor, byte[] IV) {
        super(encryptor, IV, encryptor.getBlockLength());
    }

    @Override
    public byte[] encrypt(byte[] data) {
        return processData(data);
    }

    @Override
    public byte[] decrypt(byte[] data) {
        return processData(data);
    }

    private byte[] processData(byte[] data) {
        byte[] result = new byte[data.length];
        byte[] previousBlock = IV;

        int length = data.length / blockLength;

        for (int i = 0; i < length; ++i) {
            int startIndex = i * blockLength;
            byte[] block = new byte[blockLength];
            System.arraycopy(data, startIndex, block, 0, blockLength);

            byte[] encryptedPart = encryptor.encode(previousBlock);
            byte[] processedBlock = Util.xor(block, encryptedPart);

            System.arraycopy(processedBlock, 0, result, startIndex, processedBlock.length);
            previousBlock = encryptedPart;
        }

        return result;
    }
}
