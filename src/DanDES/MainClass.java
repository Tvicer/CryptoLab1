package DanDES;

public class MainClass {


    public static void main(String[] args){
        byte[] key = DESHelper.generateRandomArray(8);

        byte[] initVector = DESHelper.generateRandomArray(8);

        var crypto = new SymmetricalCrypto(key, CipherModes.CTR, initVector, new FeistelNetwork(new DESKeyExpansion(), new DESEncryptor()));
        crypto.encryptFile("D:\\IntelliJ IDEA\\Projects\\CryptLab1\\src\\DanDES\\diagram.png", "D:\\IntelliJ IDEA\\Projects\\CryptLab1\\src\\DanDES\\out3");
        crypto.decryptFile("D:\\IntelliJ IDEA\\Projects\\CryptLab1\\src\\DanDES\\out3", "D:\\IntelliJ IDEA\\Projects\\CryptLab1\\src\\DanDES\\out4.png");

    }
}
