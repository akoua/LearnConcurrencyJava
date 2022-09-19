package culture.africa.learn.ground.java;

import culture.africa.learn.ground.java.entities.AccountManager;
import culture.africa.learn.ground.java.entities.TransferTask;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class Main {
    private static Logger logger = Logger.getGlobal();

    public static void main(String[] args) throws InterruptedException {

        var manager = new AccountManager();
        manager.init();
        var acc1 = manager.createAccount(1000);
        var acc2 = manager.createAccount(20_000);
        var transfer = new TransferTask(acc1, acc2, 100);
        manager.submit(transfer);
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("account 1: "+acc1);
        logger.info("account 2: "+acc2);
        manager.shutdown();
        manager.await();
//        var transfer2 = new TransferTask(acc1, acc2, 200);
//        manager.submit(transfer2);
//        logger.info("-> account 1: "+acc1);
//        logger.info("-> account 2: "+acc2);
//        manager.await();

    }


}
