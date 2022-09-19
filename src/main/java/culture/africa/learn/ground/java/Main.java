package culture.africa.learn.ground.java;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class Main {
    private static Logger logger = Logger.getGlobal();

    public static void main(String[] args) throws InterruptedException {

        var latch = new CountDownLatch(2);
        var map = new ConcurrentHashMap<String, String>();
        int size = 10_000_000;

        var t1 = new Thread(() -> {
            for (int i = 0; i < size; i++) {
                map.put("t1" + i, "0");
            }
            latch.countDown();
        });
        var t2 = new Thread(() -> {
            for (int i = 0; i < size; i++) {
                map.put("t2" + i, "0");
            }
            latch.countDown();
        });

        t1.start();
        t2.start();

        try {
            latch.await();
        } catch (InterruptedException __) {
            System.err.println("Threads interrupted");
        }
        logger.info("Count: "+map.size());

    }


}
