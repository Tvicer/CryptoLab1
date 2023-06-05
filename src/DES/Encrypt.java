package DES;

public interface Encrypt {
    public byte[] encrypt(byte[] data, byte[] round_key);
}
