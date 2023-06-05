package DES;

import java.util.Arrays;

public class ModeCFB implements Mode {
    private Encryption algorithm;
    private byte[] initializationVec;
    private byte[] prevBlock;
    public ModeCFB(Encryption algo, byte[] init){
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
                prevBlock = algorithm.encryption(prevBlock);

                for(int j = 0; j < 8; j++) {
                    prevBlock[j] = (byte) (newBuf[j] ^ prevBlock[j]);
                }
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
                byte[] encryptedPrev = algorithm.encryption(prevBlock);
                prevBlock = Arrays.copyOfRange(buffer, i, i + 8);

                for(int j = 0; j < 8; j++) {
                    encryptedPrev[j] = (byte) (prevBlock[j] ^ encryptedPrev[j]);
                }
                for (int k = 0; k < 8; k++) {
                    resBuf[index++] = encryptedPrev[k];
                }
            }
        }
        catch (KeyNotSetException e) {
            //asmfa;slmlm
        }
        return resBuf;
    }
}
