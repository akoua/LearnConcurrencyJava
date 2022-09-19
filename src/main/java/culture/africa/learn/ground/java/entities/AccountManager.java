package culture.africa.learn.ground.java.entities;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class AccountManager {
    private ConcurrentHashMap<Integer, Account> accounts =
            new ConcurrentHashMap<>();
    private volatile boolean shutdown = false;
    private BlockingQueue<TransferTask> pending =
            new LinkedBlockingQueue<>();
    private BlockingQueue<TransferTask> forDeposit =
            new LinkedBlockingQueue<>();
    private BlockingQueue<TransferTask> failed =
            new LinkedBlockingQueue<>();
    private Thread withdrawals;
    private Thread deposits;

    public Account createAccount(int balance) {
        var out = new Account(balance);
        accounts.put(out.getAccountId(), out);
        return out;
    }

    public void submit(TransferTask transfer) {
        try {
            pending.put(transfer);
        } catch (InterruptedException e) {
            try {
                failed.put(transfer);
            } catch (InterruptedException x) {
                // Log at high criticality
            }
        }
    }
    public void await() throws InterruptedException {
        withdrawals.join();
        deposits.join();
    }
    public void shutdown() {
        shutdown = true;
    }

    public void init(){
        Runnable withdraw = () -> {
            while (!shutdown){
                try {
                    var task = pending.take();
                    var sender = task.sender();
                    if (sender.withdraw(task.amount(), false)){
                        forDeposit.put(task);
                    }
                }catch (InterruptedException e){
                }
            }
        };

        Runnable deposit = () -> {
            LOOP:
            while (!shutdown) {
                TransferTask task;
                try {
                    task = forDeposit.take();
                } catch (InterruptedException e) {
                    // Log at critical and proceed to next item
                    continue LOOP;
                }
                var receiver = task.receiver();
                receiver.rawDeposit(task.amount());
            }
            // Drain forDeposit queue to failed or log
        };
        init(withdraw, deposit);
    }
    void init(Runnable withdraw, Runnable deposit) {
        withdrawals = new Thread(withdraw);
        deposits = new Thread(deposit);
        withdrawals.start();
        deposits.start();
    }
}
