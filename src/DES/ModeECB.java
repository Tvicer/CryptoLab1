package DES;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ModeECB implements Mode{
    private Encryption algorithm;
    public ModeECB(Encryption algo){
        this.algorithm = algo;
    }
    public void reset(){}
    public byte[] encrypt(byte[] buffer, int len){
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(processors);
        List<Future<byte[]>> encryptedBlocksFutures = new LinkedList<>();
        for (int i = 0; i < len; i += 8){
            byte[] newBuf = Arrays.copyOfRange(buffer, i, i + 8);
            encryptedBlocksFutures.add(service.submit(() -> algorithm.encryption(newBuf)));
        }
        service.shutdown();

        return AlgsForDES.getBytes(encryptedBlocksFutures);
    }

    public byte[] decrypt(byte[] buffer, int len){
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(processors);
        List<Future<byte[]>> encryptedBlocksFutures = new LinkedList<>();
        for (int i = 0; i < len; i += 8){
            byte[] newBuf = Arrays.copyOfRange(buffer, i, i + 8);
            encryptedBlocksFutures.add(service.submit(() -> algorithm.decryption(newBuf)));
        }
        service.shutdown();
        return AlgsForDES.getBytes(encryptedBlocksFutures);
    }
}
