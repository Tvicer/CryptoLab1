package DES;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AlgsForDES {
    public static byte[] swap_bits(byte[] data, byte[] rule) {
        byte[] result = new byte[64];
        for (int i = 0; i < result.length; i++){
            result[rule[i] - 1] = data[i];
        }
        return result;
    }

    public static byte change_S_block(byte cur_byte, byte[][] block) {
        return block[((cur_byte & 1) | (cur_byte >> 5))][cur_byte & 30 >> 1];
    }

    public static byte[] generateRandomArray(int length){
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++) {
            result[i] = (byte) (Math.random() * 256);
        }
        return result;
    }

    public static byte[] bytesToBits(byte[] data){
        byte[] result = new byte[data.length * 8];
        for (byte i = 0; i < data.length; i++)
            for (byte j = 0; j < 8; j++)
                result[i * 8 + j] = (byte) (data[i] >> j & 1);
        return result;
    }

    public static byte[] bitsToBytes(byte[] data){
        byte[] result = new byte[data.length / 8];
        for (byte i = 0; i < data.length / 8; i++)
            for (byte j = 0; j < 8; j++)
                result[i] += (byte) (data[i * 8 + j] << j);
        return result;
    }

    public static byte[] getBytes(List<Future<byte[]>> encryptedBlocksFutures) {
        byte[] resBuf = new byte[80000];
        int index = 0;
        try {
            for (var futureBufToWrite : encryptedBlocksFutures) {
                byte[] encryptedBuf = futureBufToWrite.get();
                for (int i = 0; i < 8; i++) {
                    resBuf[index++] = encryptedBuf[i];
                }
            }
        }
        catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return resBuf;
    }

    /*public static byte[][] expand(byte[] key) {
        byte[] bits = new byte[56];

        for (byte i = 0; i < 7; i++)
            for (byte j = 7; j > -1; j--)
                bits[i * 8 + 7 - j] = (byte) (key[i] >> j & 1);

        byte[] c = new byte[32];
        for (byte pos = 0; pos < Entities.C.length; pos++)
            c[pos] = bits[Entities.C[pos]];

        byte[] d = new byte[32];

        for (byte pos = 0; pos < Entities.D.length; pos++)
            d[pos] = bits[Entities.D[pos]];

        byte[] round_key = new byte[56];

        byte[][] keys = new byte[16][7];

        for (byte s = 0; s < Entities.SHIFTS.length; s++) {
            for(byte i = 0; i < 27; i++) {
                bits[(i + Entities.SHIFTS[i]) % 28] = c[i];
                bits[28 + (i + Entities.SHIFTS[i]) % 28] = d[i];
            }

            for(byte i = 0; i < Entities.CD.length; i++)
                round_key[i] = bits[Entities.CD[i]];

            keys[s] = round_key;
        }
        return keys;
    }

    public static byte[] encrypt(byte[] data, byte[] round_key) {
        byte[] bits = new byte[48];
        for (byte pos = 0; pos < Entities.E.length; pos++)
            bits[pos] = bits[Entities.E[pos]];

        for (byte i = 0; i < 48; i++)
            bits[i] = (byte) (bits[i] ^ round_key[i]);

        byte[] buf = new byte[8];

        for (byte i = 0; i < 8; i++)
            for (byte j = 0; j < 6; j++)
                buf[i] += (byte) (Math.pow(2, j) * bits[i * 6 + j]);

        byte byte_buf;
        byte[] result = new byte[32];

        for (byte i = 0; i < 8; i++) {
            byte_buf = change_S_block(buf[i], Entities.S[i]);
            for (byte j = 0; j < 4; j++)
                result[i * 4 + j] = (byte) (byte_buf >> (3 - j) & 1);
        }

        return result;
    }*/

}
