package minibank.test;

import minibank.exception.InsufficientFundsException;
import minibank.exception.InvalidAmountException;
import minibank.model.Account;
import minibank.model.SavingsAccount;
import minibank.service.AccountWorker;
import minibank.service.BankService;
import minibank.util.StatePersister;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Unit Test suite verifying core banking rules (Practical 14 Part B).
 * Asserts:
 *  1. Deposit increases balance
 *  2. Withdrawal exceeding balance/limit throws exception
 *  3. Concurrent deposits maintain balance accuracy
 *  4. State persistence save & reload keeps data intact
 */
public class MiniBankTest {

    public static void runAllTests() {
        System.out.println("\n------------------------------------------------------------");
        System.out.println("  RUNNING MINIBANK UNIT TESTS");
        System.out.println("------------------------------------------------------------");

        int passed = 0;
        int failed = 0;

        try {
            testDepositIncreasesBalance();
            System.out.println("  [PASS] testDepositIncreasesBalance");
            passed++;
        } catch (Throwable t) {
            System.err.println("  [FAIL] testDepositIncreasesBalance: " + t.getMessage());
            failed++;
        }

        try {
            testWithdrawalOverLimitThrowsException();
            System.out.println("  [PASS] testWithdrawalOverLimitThrowsException");
            passed++;
        } catch (Throwable t) {
            System.err.println("  [FAIL] testWithdrawalOverLimitThrowsException: " + t.getMessage());
            failed++;
        }

        try {
            testConcurrentDepositsKeepBalanceCorrect();
            System.out.println("  [PASS] testConcurrentDepositsKeepBalanceCorrect");
            passed++;
        } catch (Throwable t) {
            System.err.println("  [FAIL] testConcurrentDepositsKeepBalanceCorrect: " + t.getMessage());
            failed++;
        }

        try {
            testPersistenceStateSavesAndReloadsUnchanged();
            System.out.println("  [PASS] testPersistenceStateSavesAndReloadsUnchanged");
            passed++;
        } catch (Throwable t) {
            System.err.println("  [FAIL] testPersistenceStateSavesAndReloadsUnchanged: " + t.getMessage());
            failed++;
        }

        System.out.println("------------------------------------------------------------");
        System.out.println("  TEST RUN FINISHED: " + passed + " Passed, " + failed + " Failed");
        System.out.println("------------------------------------------------------------\n");
    }

    public static void testDepositIncreasesBalance() throws Exception {
        Account acc = new SavingsAccount("TestUser", 5000, 1000);
        acc.deposit(2500);
        assertEquals(7500, acc.getBalance(), "Deposit should increase balance to 7500");
    }

    public static void testWithdrawalOverLimitThrowsException() {
        Account acc = new SavingsAccount("TestUser", 2000, 1000);
        boolean threw = false;
        try {
            acc.withdraw(1500); // would leave 500 which is < minBalance 1000
        } catch (InsufficientFundsException e) {
            threw = true;
        } catch (InvalidAmountException e) {
            throw new AssertionError("Wrong exception type thrown: " + e.getMessage());
        }
        assertTrue(threw, "Withdrawal below minimum balance must throw InsufficientFundsException");
    }

    public static void testConcurrentDepositsKeepBalanceCorrect() throws Exception {
        Account acc = new SavingsAccount("ConcurrencyTest", 0, 0);
        int threadsCount = 10;
        int countPerThread = 1000;
        Thread[] threads = new Thread[threadsCount];

        for (int i = 0; i < threadsCount; i++) {
            threads[i] = new Thread(new AccountWorker(acc, countPerThread, 1));
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        assertEquals(10000, acc.getBalance(), "Concurrent synchronized deposits must yield exactly 10,000");
    }

    public static void testPersistenceStateSavesAndReloadsUnchanged() throws Exception {
        Path testPath = Paths.get("data", "test_save_load.dat");
        Account[] original = new Account[]{
                new SavingsAccount("AC9001", "PersistTest", 45000, true, 1000)
        };

        StatePersister.save(original, testPath);
        Account[] reloaded = StatePersister.load(testPath);

        assertEquals(1, reloaded.length, "Reloaded array should have length 1");
        assertEquals("AC9001", reloaded[0].getAccountNumber(), "Account number must match");
        assertEquals(45000, reloaded[0].getBalance(), "Balance must be intact");
        assertEquals("PersistTest", reloaded[0].getOwnerName(), "Owner name must match");
    }

    private static void assertEquals(long expected, long actual, String message) {
        if (expected != actual) {
            throw new AssertionError(message + " (Expected: " + expected + ", Actual: " + actual + ")");
        }
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + " (Expected: " + expected + ", Actual: " + actual + ")");
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("Assertion failed: " + message);
        }
    }

    public static void main(String[] args) {
        runAllTests();
    }
}

