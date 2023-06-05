package DES;

import java.util.Arrays;

public class ModeCBC implements Mode {
    private Encryption algorithm;
    private byte[] initializationVec;
    private byte[] prevBlock;
    public ModeCBC(Encryption algo, byte[] init){
        this.algorithm = algo;
        this.initializationVec = init;
        this.prevBlock = initializationVec;
    }
    public void reset() {
        prevBlock = initializationVec;
    }
    public byte[] encrypt(byte[] buffer, int len) {
        byte[] resBuf = new byte[80000];
        int index = 0;
        try {
            for (int i = 0; i < len; i += 8) {
                byte[] newBuf = Arrays.copyOfRange(buffer, i, i + 8);
                for(int j = 0; j < 8; j++) {
                    newBuf[j] = (byte) (newBuf[j] ^ prevBlock[j]);
                }

                prevBlock = algorithm.encryption(newBuf);
                for (int k = 0; k < 8; k++) {
                    resBuf[index++] = prevBlock[k];
                }
            }
        }
        catch (KeyNotSetException e) {
            //asmfa;slmlm
        }
        return resBuf;
    }
    public byte[] decrypt(byte[] buffer, int len){
        byte[] resBuf = new byte[80000];
        int index = 0;
        try {
            for (int i = 0; i < len; i += 8) {
                byte[] newBuf = Arrays.copyOfRange(buffer, i, i + 8);

                byte[] decrypted = algorithm.decryption(newBuf);
                for(int j = 0; j < 8; j++) {
                    decrypted[j] = (byte) (decrypted[j] ^ prevBlock[j]);
                }
                prevBlock = Arrays.copyOfRange(newBuf, 0, 8);
                for (int k = 0; k < 8; k++) {
                    resBuf[index++] = decrypted[k];
                }
            }
        }
        catch (KeyNotSetException e) {
            //asmfa;slmlm
        }
        return resBuf;
    }
}
