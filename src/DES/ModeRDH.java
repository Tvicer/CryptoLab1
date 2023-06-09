package DES;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ModeRDH implements Mode {
    private Encryption algorithm;
    private byte[] initializationVec;
    private BigInteger delta;
    private BigInteger initial;

    public ModeRDH(Encryption algo, byte[] init){
        this.algorithm = algo;
        this.initializationVec = init;
        this.initial = new BigInteger(init);
        this.delta = new BigInteger(init, init.length/2, init.length/2);
    }
    @Override
    public byte[] encrypt(byte[] buffer, int len) {
        int index = 0;
        long shift = 1<<8;
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(processors);
        List<Future<byte[]>> encryptedBlocksFutures = new LinkedList<>();
        encryptedBlocksFutures.add(service.submit(() -> algorithm.encryption(ByteBuffer.allocate(8).put(initial.toByteArray()).array())));
        int hashCodeVec = initial.hashCode();
        ByteBuffer byteBufferHash = ByteBuffer.allocate(8);
        byteBufferHash.putInt(hashCodeVec);
        long XORedValue = initial.xor(BigInteger.valueOf(hashCodeVec)).longValue();
        encryptedBlocksFutures.add(service.submit(() -> algorithm.encryption(ByteBuffer.allocate(8).putLong(XORedValue).array())));
        for (int i = 0; i < len; i += 8) {
            byte[] initArray = initial.toByteArray();
            ByteBuffer byteBuffer = ByteBuffer.allocate(8);
            byteBuffer.put(initArray);
            initArray = byteBuffer.array();
            for (int j = 0; j < 8; j++) {
                buffer[index++] ^= initArray[j];
            }
            initial = initial.add(delta).mod(BigInteger.valueOf(shift));
        }
        for (int i = 0; i < len; i += 8) {
            byte[] newBuf = Arrays.copyOfRange(buffer, i, i + 8);
            encryptedBlocksFutures.add(service.submit(() -> algorithm.encryption(newBuf)));
        }
        service.shutdown();

        return AlgsForDES.getBytes(encryptedBlocksFutures);
    }

    @Override
    public byte[] decrypt(byte[] buffer, int len) {
        int index = 0;
        long shift = 1<<8;
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(processors);
        List<Future<byte[]>> decryptedBlocksFutures = new LinkedList<>();
        for (int i = 16; i < len; i += 8) {
            byte[] newBuf = Arrays.copyOfRange(buffer, i, i + 8);
            decryptedBlocksFutures.add(service.submit(() -> algorithm.decryption(newBuf)));
        }
        service.shutdown();

        byte[] resBytes = AlgsForDES.getBytes(decryptedBlocksFutures);

        for (int i = 0; i < len; i += 8) {
            byte[] initArray = initial.toByteArray();
            ByteBuffer byteBuffer = ByteBuffer.allocate(8);
            byteBuffer.put(initArray);
            initArray = byteBuffer.array();
            for (int j = 0; j < 8; j++) {
                resBytes[index++] ^= initArray[j];
            }
            initial = initial.add(delta).mod(BigInteger.valueOf(shift));
        }

        return resBytes;
    }

    @Override
    public void reset() {
        this.initial = new BigInteger(this.initializationVec);
    }
}
