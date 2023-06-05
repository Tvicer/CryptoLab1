package DES;

import java.util.Arrays;

public class DESExpandRoundKey implements ExpandRoundKey {
    @Override
    public byte[][] expand(byte[] key) {
        byte[] bits = AlgsForDES.bytesToBits(key);

//        for (byte i = 0; i < 7; i++)
//            for (byte j = 7; j > -1; j--)
//                bits[i * 8 + 7 - j] = (byte) (key[i] >> j & 1);

        byte[] newBits = new byte[64];
        Arrays.fill(newBits, (byte) 0);

        byte z = 0;
        byte y = 0;

        while (z < bits.length) {
            if (z % 8 != 0) {
                newBits[y] = bits[z];
                y++;
            }
            z++;

        }

        byte[] newNewBits = new byte[64];

        for (byte pos = 0; pos < Entities.CandD.length; pos++)
            newNewBits[pos] = newBits[Entities.CandD[pos]];

        byte[] c = Arrays.copyOfRange(newNewBits, 0, 27);
        byte[] d = Arrays.copyOfRange(newNewBits, 28, 55);

//        for (byte pos = 0; pos < Entities.C.length; pos++)
//            c[pos] = bits[Entities.C[pos]];
//
//        for (byte pos = 0; pos < Entities.D.length; pos++)
//            d[pos] = bits[Entities.D[pos]];


        byte[] round_key = new byte[56];

        byte[][] keys = new byte[16][7];

        Arrays.fill(newBits, (byte) 0);

        for (byte s = 0; s < Entities.SHIFTS.length; s++) {
            for (byte i = 0; i < 27; i++) {
                bits[(i + Entities.SHIFTS[s]) % 27] = c[i];
                bits[28 + (i + Entities.SHIFTS[s]) % 27] = d[i];
            }

            for (byte i = 0; i < Entities.CD.length; i++)
                round_key[i] = bits[Entities.CD[i] - 1];

            keys[s] = round_key;
        }
        return keys;
    }
}
