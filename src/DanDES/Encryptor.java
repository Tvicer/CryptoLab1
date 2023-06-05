package DanDES;

public interface Encryptor {
    public byte[] encryptBlock(byte[] array, byte[] key);
}
