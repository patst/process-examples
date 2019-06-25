package de.patst.process;

import java.util.concurrent.atomic.AtomicInteger;

public class TransactionWatcher {

    private static AtomicInteger successfulTransaction = new AtomicInteger();
    private static AtomicInteger rollbackTransaction = new AtomicInteger();

    public static void reset() {
        successfulTransaction.set(0);
        rollbackTransaction.set(0);
    }

    public static void incrementSuccessfulTransaction() {
        successfulTransaction.incrementAndGet();
    }

    public static void incrementRollbackTransactions() {
        rollbackTransaction.incrementAndGet();
    }

    public static int getSuccessfulTransactions() {
        return successfulTransaction.get();
    }

    public static int getRollbackTransactions() {
        return rollbackTransaction.get();
    }
}
