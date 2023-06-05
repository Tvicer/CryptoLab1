package DES;

public class DESEncript implements Encrypt {
    @Override
    public byte[] encrypt(byte[] data, byte[] round_key) {
        byte[] bits = new byte[48];
        for (byte pos = 0; pos < Entities.E.length; pos++)
            bits[pos] = data[Entities.E[pos] - 1];

        for (byte i = 0; i < 48; i++)
            bits[i] = (byte) (bits[i] ^ round_key[i]);

        byte[] buf = new byte[8];

        for (byte i = 0; i < 8; i++)
            for (byte j = 0; j < 6; j++)
                buf[i] += (byte) (Math.pow(2, j) * bits[i * 6 + j]);

        byte byte_buf;
        byte[] result = new byte[32];

        for (byte i = 0; i < 8; i++) {
            byte_buf = AlgsForDES.change_S_block(buf[i], Entities.S[i]);
            for (byte j = 0; j < 4; j++)
                result[i * 4 + j] = (byte) (byte_buf >> (3 - j) & 1);
        }

        return result;
    }
}
