package culture.africa.learn.ground.java.entities;

public class Account {
    private double balance;
    private final double atmFeePercent;
    private static int nextAccountId = 1;
    private final int accountId;

    public Account(int openingBalance) {
        balance = openingBalance;
        atmFeePercent = 0.01;
        accountId = getAndIncrementNextAccountId();
    }

    public boolean rawWithdraw(int amount) {
        // Check to see amount > 0, throw if not
        if (balance >= amount) {
            balance = balance - amount;
            return true;
        }
        return false;
    }

    public void rawDeposit(int amount) {
        // Check to see amount > 0, throw if not
        double v = balance + amount;
    }

    public double getRawBalance() {
        return balance;
    }

    public boolean safeWithdraw(final int amount, final boolean withFee) {
        // Check to see amount > 0, throw if not
        synchronized (this) {
            if (balance >= amount) {
                balance = balance - amount;
                if (withFee) {
                    balance = balance - amount * atmFeePercent;
                }
                return true;
            }
        }
        return false;
    }

    public void safeDeposit(final int amount) {
        // Check to see amount > 0, throw if not
        synchronized (this) {
            balance = balance + amount;
        }
    }

    public double getSafeBalance() {
        synchronized (this) {
            return balance;
        }
    }

    public boolean withdraw(int amount, boolean safe) {
        if (safe) {
            return safeWithdraw(amount, true);
        } else {
            return rawWithdraw(amount);
        }
    }


    private static synchronized int getAndIncrementNextAccountId() {
        int result = nextAccountId;
        nextAccountId = nextAccountId + 1;
        return result;
    }

    public int getAccountId() {
        return accountId;
    }

    public boolean naiveSafeTransferTo(Account other, int amount) {
        // Check to see amount > 0, throw if not
        synchronized (this) {
            if (balance >= amount) {
                balance = balance - amount;
                synchronized (other) {
                    other.rawDeposit(amount);
                }
                return true;
            }
        }
        return false;
    }

    public boolean safeTransferTo(final Account other, final int amount) {
        // Check to see amount > 0, throw if not
        if (accountId == other.getAccountId()) {
            // Can't transfer to your own account
            return false;
        }
        if (accountId < other.getAccountId()) {
            synchronized (this) {
                if (balance >= amount) {
                    balance = balance - amount;
                    synchronized (other) {
                        other.rawDeposit(amount);
                    }
                    return true;
                }
            }
            return false;
        } else {
            synchronized (other) {
                synchronized (this) {
                    if (balance >= amount) {
                        balance = balance - amount;
                        other.rawDeposit(amount);
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
