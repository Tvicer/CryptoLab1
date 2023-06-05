package DES;

public class Main {
    public static void main(String[] args) {
        
        byte[] key = AlgsForDES.generateRandomArray(7);
        //byte[] key = {53, 78, 21, -89, -102, -86, 13};
        byte[] initArray = AlgsForDES.generateRandomArray(8);

        DES MyDES = new DES(key, Entities.Modes.RDH, initArray, new FeistelNetwork(new DESExpandRoundKey(), new DESEncript()));

        MyDES.encryptFile("D:\\IntelliJ IDEA\\Projects\\CryptLab1\\src\\DES\\text.txt", "D:\\IntelliJ IDEA\\Projects\\CryptLab1\\src\\DES\\out3");

        MyDES.decryptFile("D:\\IntelliJ IDEA\\Projects\\CryptLab1\\src\\DES\\out3", "D:\\IntelliJ IDEA\\Projects\\CryptLab1\\src\\DES\\out4.txt");

        System.out.println("Hello world!");
    }
}