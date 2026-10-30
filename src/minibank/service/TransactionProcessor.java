package minibank.service;

import minibank.exception.BankException;
import minibank.model.Account;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TransactionProcessor {
    private final ExecutorService executor;
    private final AtomicInteger completedTasks = new AtomicInteger(0);

    public TransactionProcessor() {

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

    public static void safeTransfer(Account from, Account to, long amount) throws BankException {
        from.transfer(to, amount);
    }

    public static void unsafeTransferWithOppositeLocks(Account from, Account to, long amount) {
        synchronized (from) {
            try {
                Thread.sleep(50);
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
