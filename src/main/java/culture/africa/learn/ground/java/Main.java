package culture.africa.learn.ground.java;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class Main {
    private static final int MAX_TRANSFERS = 1_000;
    private static Main inst = null;
    private static Logger logger = Logger.getGlobal();

    private Main() {
    }

    private void run() {
    }

    public static void main(String[] args) throws InterruptedException {

        var latch = new CountDownLatch(5);
        var count = new AtomicInteger();
        for (int i = 0; i < 5; i = i + 1) {
            var r = new Counter(latch, i, count);
            new Thread(r).start();
        }
        try {
            latch.await();
            logger.info("Total: " + count.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        FSOAccount a = new FSOAccount(10_000);
//        FSOAccount b = new FSOAccount(10_000);
//        Thread tA = new Thread(() -> {
//            for (int i = 0; i < MAX_TRANSFERS; i = i + 1) {
//                boolean ok = a.transferTo(b, 1);
//                if (!ok) {
//                    System.out.println("Thread A failed at "+ i);
//                }
//            }
//        });
//        Thread tB = new Thread(() -> {
//            for (int i = 0; i < MAX_TRANSFERS; i = i + 1) {
//                boolean ok = b.transferTo(a, 1);
//                if (!ok) {
//                    System.out.println("Thread B failed at "+ i);
//                }
//            }
//        });
//        System.out.println("Start: "+ Instant.now()+" -> " + a.getBalance() + " : "+ b.getBalance());
//        tA.start();
//        tB.start();
//        tA.join();
//        tB.join();
//        System.out.println("End: "+Instant.now()+" -> "+ a.getBalance() + " : "+ b.getBalance());

//        Runnable r = () -> {
//            var start = System.currentTimeMillis();
//            var wasInterrupted = false;
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                wasInterrupted = true;
//                e.printStackTrace();
//            }
//            var thisThread = Thread.currentThread();
//            System.out.println(thisThread.getName() +
//                    " slept for "+ (System.currentTimeMillis() - start));
//            if (wasInterrupted) {
//                System.out.println("Thread "+ thisThread.getName() +" interrupted");
//            }
//        };
//        var t = new Thread(r);
//        t.setName("Worker");
//        t.start();
//        Thread.sleep(100);
//        t.interrupt();
//        t.join();
//        System.out.println("Exiting");
//
//        final var numbers = new LinkedList<Integer>();
//        numbers.add(1);

//        long time = System.currentTimeMillis();
//// This explicit typing is deliberate... read on
//        HashMap<String, String> hm = new HashMap<>();
//        hm.put("now", "bar");
//        Map<String, String> m = hm;
//        m.put("foo", "baz");
    }

    public static class Counter implements Runnable {
        private final CountDownLatch latch;
        private final int value;
        private final AtomicInteger count;

        public Counter(CountDownLatch l, int v, AtomicInteger c) {
            this.latch = l;
            this.value = v;
            this.count = c;
        }

        @Override
        public void run() {
            try {
                logger.info(Thread.currentThread().getName() + " sleep");
                Thread.sleep(200);
                logger.info(Thread.currentThread().getName() + " awake");
            } catch (InterruptedException __) {
            }
            logger.info(count + " before " + value + " :" + Thread.currentThread().getName());
            count.addAndGet(value);
            latch.countDown();
            logger.info(count + " after " + latch.getCount() + " :" + Thread.currentThread().getName());

        }
    }
}
