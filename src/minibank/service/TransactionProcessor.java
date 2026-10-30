package minibank.service;

import minibank.exception.BankException;
import minibank.model.Account;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Managed thread pool transaction processor (Practical 10 Part B).
 * Demonstrates thread pool execution, batch transaction handling,
 * and deadlock simulation vs deadlock prevention via consistent lock ordering.
 */
public class TransactionProcessor {
    private final ExecutorService executor;
    private final AtomicInteger completedTasks = new AtomicInteger(0);

    public TransactionProcessor() {
        // Managed thread pool of 4 worker threads (Practical 10 Part B)
        this.executor = Executors.newFixedThreadPool(4);
    }

    public void submit(Runnable task) {
        executor.execute(() -> {
            try {
                task.run();
                completedTasks.incrementAndGet();
            } catch (Exception e) {
                System.err.println("Transaction processing error: " + e.getMessage());
            }
        });
    }

    public void stop() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public int getCompletedTasksCount() {
        return completedTasks.get();
    }

    /**
     * Demonstrates deadlock resolution using consistent lock ordering (Practical 10 Part B).
     * Locks smaller accountNumber first.
     */
    public static void safeTransfer(Account from, Account to, long amount) throws BankException {
        from.transfer(to, amount);
    }

    /**
     * Helper to demonstrate how arbitrary locking order causes deadlock.
     * When simulateDeadlock is called with reversed lock orders, threads will block each other.
     */
    public static void unsafeTransferWithOppositeLocks(Account from, Account to, long amount) {
        synchronized (from) {
            try {
                Thread.sleep(50); // Pause to let other thread acquire 'to' lock
            } catch (InterruptedException ignored) {}
            synchronized (to) {
                try {
                    from.withdraw(amount);
                    to.deposit(amount);
                } catch (Exception ignored) {}
            }
        }
    }
}
