package DES;

import DanDES.DESHelper;
import DanDES.KeyNotSetException;

public class FeistelNetwork implements Encryption {
    DESExpandRoundKey expander;
    DESEncript encryptor;
    byte[][] roundKeys;

    public FeistelNetwork(DESExpandRoundKey expander, DESEncript encryptor) {
        this.expander = expander;
        this.encryptor = encryptor;
    }

    @Override
    public void expandKeys(byte[] key) {
        roundKeys = expander.expand(key);
    }

    @Override
    public byte[] encryption(byte[] data) {
        byte[] newData = AlgsForDES.bytesToBits(data);
        byte[] swappedData = AlgsForDES.swap_bits(newData, Entities.IP);
        byte[] dataLeft = new byte[32];
        byte[] dataRight = new byte[32];
        for(int i = 0; i < 32; i++) {
            dataLeft[i] = swappedData[i];
            dataRight[i] = swappedData[i + 32];
        }

        byte[] left = new byte[32];
        byte[] right = new byte[32];

        for (int i = 0; i < 16; i++){
            right = encryptor.encrypt(dataRight, roundKeys[i]);
            for (int j = 0; j < 32; j++) {
                right[j] = (byte) (right[j] ^ dataLeft[j]);
            }
            dataLeft = dataRight;
            left = dataRight;
            dataRight = right;
        }

        byte[] result = new byte[64];
        for(int i = 0; i < 32; i++) {
            result[i] = left[i];
            result[i + 32] = right[i];
        }

        result = AlgsForDES.bitsToBytes(result);

        return result;
    }

    @Override
    public byte[] decryption(byte[] data) {
        byte[] newData = AlgsForDES.bytesToBits(data);
        byte[] swappedData = AlgsForDES.swap_bits(newData, Entities.IP);
        byte[] dataLeft = new byte[32];
        byte[] dataRight = new byte[32];

        for(int i = 0; i < 32; i++) {
            dataLeft[i] = swappedData[i];
            dataRight[i] = swappedData[i + 32];
        }

        byte[] left = new byte[32];
        byte[] right = new byte[32];

        for (int i = 0; i < 16; i++){
            left = encryptor.encrypt(dataRight, roundKeys[15 - i]);
            for (int j = 0; j < 32; j++){
                left[j] = (byte) (left[j] ^ dataRight[j]);
            }
            dataRight = dataLeft;
            right = dataLeft;
            dataLeft = left;
        }

        byte[] result = new byte[64];
        for(int i = 0; i < 32; i++) {
            result[i] = left[i];
            result[i + 32] = right[i];
        }

        result = AlgsForDES.bitsToBytes(result);

        return result;
    }

}
