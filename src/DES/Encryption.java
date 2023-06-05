package DES;

public interface Encryption {
    public void expandKeys(byte[] key);
    public byte[] encryption(byte[] data) throws KeyNotSetException;
    public byte[] decryption(byte[] data) throws KeyNotSetException;
}
