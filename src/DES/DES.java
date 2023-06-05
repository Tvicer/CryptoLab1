package DES;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class DES {

    private final Encryption algo;
    private final byte[] key;
    Entities.Modes mode;
    Mode modeRealization;
    private final byte[] initVector;

    protected DES(byte[] key, Entities.Modes mode, byte[] initArray, Encryption alg){
        this.key = Arrays.copyOfRange(key, 0, key.length);
        this.mode = mode;
        this.initVector = Arrays.copyOfRange(initArray, 0, initArray.length);
        this.algo = alg;
        //this.algo.expandKeys(AlgsForDES.bytesToBits(this.key));
        this.algo.expandKeys(this.key);
        this.modeRealization = getModeRealization();
    }
    private Mode getModeRealization(){
        return switch (mode) {
            case ECB -> new ModeECB(algo);
            case CBC -> new ModeCBC(algo, initVector);
            case CFB -> new ModeCFB(algo, initVector);
            case OFB -> new ModeOFB(algo, initVector);
            case CTR -> new ModeCTR(algo, initVector);
            case RD -> new ModeRD(algo, initVector);
            case RDH -> new ModeRDH(algo, initVector);
            default -> null;
        };
    }

    public void encryptFile(String file, String outFile) {
        modeRealization.reset();
        byte[] buffer = new byte[80000];
        int len;
        try (FileInputStream fileInputStream = new FileInputStream(file);
             FileOutputStream fileOutputStream = new FileOutputStream(outFile)) {
            while ((len = fileInputStream.read(buffer, 0, 79992)) > 0) {
                int last = len % 8;
                Arrays.fill(buffer, len, len + 8 - last, (byte) (8 - last));
                fileOutputStream.write(modeRealization.encrypt(buffer, len + 8 - last), 0 , len + 8 - last);
            }
        }
        catch  (IOException e) {
            e.printStackTrace();
        }
    }
    public void decryptFile(String file, String outFile) {
        modeRealization.reset();
        byte[] buffer = new byte[80000];
        int len;
        try (FileInputStream fileInputStream = new FileInputStream(file);
             FileOutputStream fileOutputStream = new FileOutputStream(outFile)) {
            while ((len = fileInputStream.read(buffer)) > 0) {
                System.out.println("dec");
                byte[] newBuf = modeRealization.decrypt(buffer, len);
                int last = newBuf[len - 1];
                fileOutputStream.write(newBuf, 0 , len - last);
            }
        }
        catch  (IOException e){
            e.printStackTrace();
        }
    }
}
