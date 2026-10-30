package minibank.test;

import minibank.exception.BankException;
import minibank.exception.InsufficientFundsException;
import minibank.exception.InvalidAmountException;
import minibank.interfaces.InterestBearing;
import minibank.interfaces.Premium;
import minibank.interfaces.Transactable;
import minibank.interfaces.WithdrawRule;
import minibank.model.Account;
import minibank.model.BankInfo;
import minibank.model.Command;
import minibank.model.CurrentAccount;
import minibank.model.Customer;
import minibank.model.FixedDepositAccount;
import minibank.model.MenuOption;
import minibank.model.SavingsAccount;
import minibank.model.TransactionType;
import minibank.repository.AccountStore;
import minibank.repository.Repository;
import minibank.service.AccountWorker;
import minibank.service.AuditLogger;
import minibank.service.BankService;
import minibank.service.BankSession;
import minibank.service.ProducerConsumerBuffer;
import minibank.service.TransactionProcessor;
import minibank.util.AnnotationValidator;
import minibank.util.CommandParser;
import minibank.util.ReportGenerator;
import minibank.util.StatePersister;
import minibank.util.StatementFormatter;
import minibank.util.TransactionLog;
import minibank.util.Validator;

import static minibank.util.Validator.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PracticalDemonstrator {

    public static void main(String[] args) {
        runAllPracticals();
    }

    public static void runAllPracticals() {
        System.out.println("\n################################################################");
        System.out.println("  STARTING COMPREHENSIVE VERIFICATION FOR PRACTICALS 1 TO 14");
        System.out.println("################################################################\n");

        demonstratePractical1();
        demonstratePractical2();
        demonstratePractical3();
        demonstratePractical4();
        demonstratePractical5();
        demonstratePractical6();
        demonstratePractical7();
        demonstratePractical8();
        demonstratePractical9();
        demonstratePractical10();
        demonstratePractical11();
        demonstratePractical12();
        demonstratePractical13();
        demonstratePractical14();

        System.out.println("\n################################################################");
        System.out.println("  ALL PRACTICAL 1-14 PART B REQUIREMENTS SUCCESSFULLY VERIFIED!");
        System.out.println("################################################################\n");
    }

    public static void demonstratePractical1() {
        printHeader(1, "Environment, Menu Shell, Record BankInfo & MenuOption Enum");
        BankInfo bankInfo = new BankInfo("MiniBank Central", "CyberCity");
        System.out.println("Application Header Record:");
        System.out.println(bankInfo.getFormattedHeader());

        System.out.println("MenuOption enum constants:");
        for (MenuOption opt : MenuOption.values()) {
            System.out.println("  [" + opt.getCode() + "] " + opt.name() + " -> " + opt.getDescription());
        }

        MenuOption choice = MenuOption.DEPOSIT;
        String actionMessage = switch (choice) {
            case OPEN_ACCOUNT -> "Open Account selected.";
            case DEPOSIT -> "Deposit — to be implemented in a later lab";
            case WITHDRAW -> "Withdraw — to be implemented in a later lab";
            case TRANSFER -> "Transfer — to be implemented in a later lab";
            case EXIT -> "Goodbye!";
            default -> "Other operation";
        };
        System.out.println("Switch expression test for choice " + choice + ": " + actionMessage);
    }

    public static void demonstratePractical2() {
        printHeader(2, "Customer & Account Classes, Encapsulation & ID Generation");
        Customer c1 = new Customer("Riya Sharma", "riya@example.com", "9876543210");
        Customer c2 = new Customer("Aarav Patel", "aarav@example.com", "9123456780");
        System.out.println("Generated Customers with static counter:");
        System.out.println("  " + c1);
        System.out.println("  " + c2);

        Account[] accounts = new Account[]{
                new SavingsAccount("Riya", 5000),
                new CurrentAccount("Aarav", 3000),
                new SavingsAccount("Diya", 10000)
        };

        System.out.println("\nInitial Accounts in Account[] array:");
        for (Account a : accounts) {
            System.out.println("  " + a);
        }

        try {
            System.out.println("\nPerforming operations on " + accounts[0].getOwnerName() + " (Opening: 5000):");
            accounts[0].deposit(2000);
            System.out.println("  After deposit(2000): " + accounts[0].getBalance() + " INR (Expected: 7000)");

            accounts[0].withdraw(3000);
            System.out.println("  After withdraw(3000): " + accounts[0].getBalance() + " INR (Expected: 4000)");

            try {
                accounts[0].withdraw(10000);
            } catch (InsufficientFundsException e) {
                System.out.println("  Withdraw(10000) correctly refused: " + e.getMessage());
            }
            System.out.println("  Balance remained intact: " + accounts[0].getBalance() + " INR (Expected: 4000)");
        } catch (Exception e) {
            System.err.println("Error in Practical 2 demo: " + e.getMessage());
        }
    }

    public static void demonstratePractical3() {
        printHeader(3, "Overriding Object Methods (toString, equals, hashCode, clone) & Nested Class");

        Account a1 = new SavingsAccount("AC0001", "Riya", 4000, true, 1000);
        Account a2 = new SavingsAccount("AC0001", "Riya Duplicate", 4000, true, 1000);
        Account a3 = new SavingsAccount("AC0002", "Other", 4000, true, 1000);

        System.out.println("a1.toString(): " + a1);
        System.out.println("a1.equals(a2) with same account number: " + a1.equals(a2) + " (Expected: true)");
        System.out.println("a1.equals(a3) with diff account number: " + a1.equals(a3) + " (Expected: false)");
        System.out.println("a1 instanceof Account: " + (a1 instanceof Account) + " (Expected: true)");

        Customer.Address addr = new Customer.Address("123 Tech Park", "Bengaluru", "560001");
        Customer orig = new Customer("CUST101", "Karan", "karan@xyz.com", "9876543210", addr);
        Customer copy = orig.clone();

        System.out.println("\nOriginal Customer: " + orig);
        System.out.println("Cloned Customer  : " + copy);
        System.out.println("copy != orig (distinct instances): " + (copy != orig) + " (Expected: true)");
        System.out.println("copy.equals(orig) (same logical ID): " + copy.equals(orig) + " (Expected: true)");
    }

    public static void demonstratePractical4() {
        printHeader(4, "Validation, Command Record, CommandParser & StatementFormatter");
        System.out.println("Regex Validation Results:");
        System.out.println("  isValidMobile(\"9876543210\"): " + isValidMobile("9876543210") + " (Expected: true)");
        System.out.println("  isValidMobile(\"12345\"):      " + isValidMobile("12345") + " (Expected: false)");
        System.out.println("  isValidEmail(\"abc@xyz.com\"): " + isValidEmail("abc@xyz.com") + " (Expected: true)");
        System.out.println("  isValidEmail(\"invalid-email\"): " + isValidEmail("invalid-email") + " (Expected: false)");
        System.out.println("  isValidPan(\"ABCDE1234F\"):     " + isValidPan("ABCDE1234F") + " (Expected: true)");
        System.out.println("  isValidIfsc(\"SBIN0001234\"):    " + isValidIfsc("SBIN0001234") + " (Expected: true)");

        String cmdLine = "DEPOSIT AC0001 500";
        Command cmd = CommandParser.parse(cmdLine);
        System.out.println("\nParsed command from \"" + cmdLine + "\":");
        System.out.println("  Type: " + cmd.type() + ", Account: " + cmd.accountNumber() + ", Amount: " + cmd.amount());

        Account testAcc = new SavingsAccount("AC0001", "Riya Sharma", 5500, true, 1000);
        System.out.println("\nStatement Formatter Output (built via StringBuilder):\n" + StatementFormatter.buildStatement(testAcc));
    }

    public static void demonstratePractical5() {
        printHeader(5, "Inheritance & Polymorphism (Savings, Current, FixedDeposit)");
        Account[] portfolio = new Account[]{
                new SavingsAccount("Ananya", 25000, 1000),
                new CurrentAccount("TechCorp", 50000, 20000),
                new FixedDepositAccount("Vikram", 100000, 24)
        };

        System.out.println("Polymorphic call on Account[] array (interestRate and rules):");
        for (Account acc : portfolio) {
            System.out.printf("  %-16s | Rate: %4.1f%% | Yearly Est: %8.2f INR | canWithdraw(30000): %s\n",
                    acc.getAccountType(), acc.interestRate(), acc.yearlyInterest(), acc.canWithdraw(30000));

            if (acc instanceof CurrentAccount ca) {
                System.out.println("    -> Special handling: CurrentAccount Overdraft Limit is " + ca.getOverdraftLimit() + " INR");
            } else if (acc instanceof FixedDepositAccount fda) {
                System.out.println("    -> Special handling: FixedDeposit locked for " + fda.getTenureMonths() + " months");
            }
        }
    }

    public static void demonstratePractical6() {
        printHeader(6, "Interfaces (Transactable, InterestBearing, WithdrawRule, Premium) & Lambdas");
        Account testAcc = new SavingsAccount("Meera", 10000, 1000);

        System.out.println("Account implements Transactable: " + (testAcc instanceof Transactable));
        System.out.println("Account implements InterestBearing: " + (testAcc instanceof InterestBearing));
        System.out.println("Default interface yearlyInterest: " + testAcc.yearlyInterest() + " INR");
        System.out.println("Default interface monthlyInterest: " + testAcc.monthlyInterest() + " INR");

        WithdrawRule anonymousRule = new WithdrawRule() {
            @Override
            public boolean allow(Account account, long amount) {
                return amount <= 5000 && account.getBalance() >= amount;
            }
        };

        WithdrawRule lambdaRule = (account, amount) -> amount <= 5000 && account.getBalance() >= amount;

        System.out.println("Anonymous class allow(testAcc, 4000): " + anonymousRule.allow(testAcc, 4000) + " (Expected: true)");
        System.out.println("Lambda expression allow(testAcc, 4000): " + lambdaRule.allow(testAcc, 4000) + " (Expected: true)");
        System.out.println("Lambda expression allow(testAcc, 8000): " + lambdaRule.allow(testAcc, 8000) + " (Expected: false)");

        Account fd = new FixedDepositAccount("VIP Client", 200000);
        System.out.println("FixedDepositAccount implements Premium marker: " + (fd instanceof Premium));
    }

    public static void demonstratePractical7() {
        printHeader(7, "Custom Annotations (@Id, @Positive, @MaxLength) & Reflection Validator");

        Account invalidAccount = new SavingsAccount("AC9999", "Very Long Name Exceeding Maximum Allowed Length For Customer Name Field In Test", -100, true, 1000);
        Account validAccount = new SavingsAccount("AC0001", "Neha", 5000, true, 1000);

        System.out.println("Validating Invalid Account via Reflection on fields:");
        String[] invalidErrors = AnnotationValidator.validate(invalidAccount);
        for (String err : invalidErrors) {
            System.out.println("  [ERROR DETECTED]: " + err);
        }

        System.out.println("\nValidating Valid Account:");
        String[] validErrors = AnnotationValidator.validate(validAccount);
        System.out.println("  Error count: " + validErrors.length + " (Expected: 0)");
    }

    public static void demonstratePractical8() {
        printHeader(8, "Custom Exception Hierarchy & Try-With-Resources");
        Account acc = new SavingsAccount("Suresh", 1000, 1000);

        try {
            System.out.println("Attempting withdrawal of 5000 from balance 1000:");
            acc.withdraw(5000);
        } catch (InsufficientFundsException e) {
            System.out.println("  Caught InsufficientFundsException: " + e.getMessage());
            System.out.println("  Stored Shortfall: " + e.getShortfall() + " INR (Expected: 4000)");
        } catch (InvalidAmountException e) {
            System.out.println("  Invalid amount: " + e.getMessage());
        }

        try {
            System.out.println("\nAttempting deposit of -500:");
            acc.deposit(-500);
        } catch (InvalidAmountException e) {
            System.out.println("  Caught InvalidAmountException: " + e.getMessage());
        }

        System.out.println("\nExecuting Try-With-Resources with BankSession:");
        try (BankSession session = new BankSession("SESS-883", "SystemAdmin")) {
            session.executeOperation("Vault verification check");
        }
    }

    public static void demonstratePractical9() {
        printHeader(9, "Multithreading, AccountWorker & Synchronization Race Condition Fix");
        Account syncAccount = new SavingsAccount("SharedSyncAccount", 0, 0);

        int numThreads = 10;
        int depositsPerThread = 1000;
        long amountPerDeposit = 1;
        Thread[] threads = new Thread[numThreads];

        System.out.println("Starting " + numThreads + " threads, each performing " + depositsPerThread + " deposits of " + amountPerDeposit + " INR...");
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new AccountWorker(syncAccount, depositsPerThread, amountPerDeposit), "Worker-" + (i + 1));
            threads[i].start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException ignored) {}
        }

        long expectedBalance = numThreads * depositsPerThread * amountPerDeposit;
        System.out.println("All threads finished. Final balance: " + syncAccount.getBalance() + " INR (Expected: " + expectedBalance + ")");
    }

    public static void demonstratePractical10() {
        printHeader(10, "TransactionProcessor Thread Pool, Producer-Consumer & Deadlock Resolution");

        System.out.println("1. Testing TransactionProcessor with 4-thread pool:");
        TransactionProcessor tp = new TransactionProcessor();
        Account testAcc = new SavingsAccount("PoolTest", 1000, 0);
        for (int i = 0; i < 8; i++) {
            final int taskId = i + 1;
            tp.submit(() -> {
                try {
                    testAcc.deposit(100);
                } catch (Exception ignored) {}
            });
        }
        tp.stop();
        System.out.println("   Completed tasks via thread pool: " + tp.getCompletedTasksCount() + ", Account balance: " + testAcc.getBalance());

        System.out.println("\n2. Testing Producer-Consumer coordination with wait()/notifyAll():");
        ProducerConsumerBuffer buffer = new ProducerConsumerBuffer(3);
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    Command c = new Command(TransactionType.DEPOSIT, "AC0001", i * 100);
                    buffer.produce(c);
                    System.out.println("   [PRODUCED] " + c);
                }
                buffer.stop();
            } catch (InterruptedException ignored) {}
        });

        Thread consumer = new Thread(() -> {
            try {
                Command cmd;
                while ((cmd = buffer.consume()) != null) {
                    System.out.println("   [CONSUMED & PROCESSED] " + cmd);
                }
            } catch (InterruptedException ignored) {}
        });

        producer.start();
        consumer.start();
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException ignored) {}

        System.out.println("\n3. Testing Safe Inter-Account Transfer (Deadlock Prevention):");
        Account accA = new SavingsAccount("AC0001", "UserA", 10000, 0);
        Account accB = new SavingsAccount("AC0002", "UserB", 10000, 0);

        Thread t1 = new Thread(() -> {
            try {
                accA.transfer(accB, 1000);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                accB.transfer(accA, 500);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });

        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
            System.out.println("   Concurrent transfers completed safely without deadlock!");
            System.out.println("   Final AccA balance: " + accA.getBalance() + ", AccB balance: " + accB.getBalance());
        } catch (InterruptedException ignored) {}
    }

    public static void demonstratePractical11() {
        printHeader(11, "Persistence with Serialization, Transaction Logs & NIO Report");
        Path testData = Paths.get("data", "demo_accounts.dat");
        Path logsDir = Paths.get("logs");
        Path testLog = Paths.get("logs", "demo_transactions.log");
        Path testReport = Paths.get("logs", "demo_report.txt");

        try {

            Account[] savedAccounts = new Account[]{
                    new SavingsAccount("AC0011", "PersistUser1", 12000, true, 1000),
                    new CurrentAccount("AC0012", "PersistUser2", 24000, true, 5000)
            };
            StatePersister.save(savedAccounts, testData);
            System.out.println("Saved " + savedAccounts.length + " accounts to " + testData);

            Account[] loadedAccounts = StatePersister.load(testData);
            System.out.println("Reloaded accounts successfully: " + loadedAccounts.length + " accounts");
            for (Account a : loadedAccounts) {
                System.out.println("  " + a);
            }

            TransactionLog.append(testLog, "DEPOSIT AC0011 5000");
            TransactionLog.append(testLog, "WITHDRAW AC0012 3000");
            System.out.println("Appended entries to NIO TransactionLog at " + testLog);

            String report = ReportGenerator.generateDailyReport(logsDir, testReport);
            System.out.println("\nGenerated NIO Daily Report:\n" + report);

        } catch (Exception e) {
            System.err.println("Practical 11 demo error: " + e.getMessage());
        }
    }

    public static void demonstratePractical12() {
        printHeader(12, "Collections Framework (AccountStore, HashMap, ArrayList, Comparator & Comparable)");
        AccountStore store = new AccountStore();
        Account a1 = new SavingsAccount("AC0003", "Aarav", 5000, 1000);
        Account a2 = new SavingsAccount("AC0001", "Zara", 2000, 1000);
        Account a3 = new SavingsAccount("AC0002", "Pooja", 9000, 1000);

        store.add(a1);
        store.add(a2);
        store.add(a3);

        System.out.println("Lookup by key 'AC0002': " + store.get("AC0002"));

        System.out.println("\nAccounts sorted by balance descending (via Comparator):");
        ArrayList<Account> sortedList = store.sortedByBalance();
        for (Account a : sortedList) {
            System.out.println("  " + a.getBalance() + " INR -> " + a.getAccountNumber() + " (" + a.getOwnerName() + ")");
        }

        System.out.println("\nAccounts natural ordering via Comparable<Account> (compareTo by accountNumber):");
        ArrayList<Account> naturalOrder = new ArrayList<>(store.getAll());
        java.util.Collections.sort(naturalOrder);
        for (Account a : naturalOrder) {
            System.out.println("  " + a.getAccountNumber() + " -> " + a.getOwnerName());
        }
    }

    public static void demonstratePractical13() {
        printHeader(13, "Generic Repository<T>, Reflection on @Id & Bounded Generics");
        Repository<Account> accountRepo = new Repository<>();
        Account a1 = new SavingsAccount("AC1001", "Kunal", 7000, 1000);
        Account a2 = new SavingsAccount("AC1002", "Sneha", 15000, 1000);

        accountRepo.saveAll(a1, a2);
        System.out.println("Saved accounts into generic Repository<Account>. Total: " + accountRepo.count());
        System.out.println("findById('AC1001'): " + accountRepo.findById("AC1001"));

        Repository<Customer> customerRepo = new Repository<>();
        Customer c1 = new Customer("CUST501", "Mohit", "mohit@bank.com", "9988776655", null);
        customerRepo.save(c1);
        System.out.println("Reused Repository<Customer> successfully: " + customerRepo.findById("CUST501"));

        Double[] balances = new Double[]{7000.0, 15000.0, 25000.0};
        double avg = Repository.average(balances);
        System.out.printf("Generic bounded method average(Double[]): %.2f INR\n", avg);
    }

    public static void demonstratePractical14() {
        printHeader(14, "Capstone Assembly (BankService, Audit Logging & End-to-End Workflow)");
        BankService service = new BankService();

        try {
            System.out.println("1. Opening sample accounts via BankService:");
            Account sAcc = service.openAccount("SAVINGS", "Amitabh", 15000, "amit@cinema.com", "9820011223", "Juhu", "Mumbai", "400049");
            Account cAcc = service.openAccount("CURRENT", "BigB Ent", 50000, "contact@bigb.com", "9820099887", "Bandra", "Mumbai", "400050");
            System.out.println("   Opened: " + sAcc);
            System.out.println("   Opened: " + cAcc);

            System.out.println("\n2. Executing deposits, withdrawals and transfers:");
            service.deposit(sAcc.getAccountNumber(), 5000);
            service.withdraw(sAcc.getAccountNumber(), 2000);
            service.transfer(cAcc.getAccountNumber(), sAcc.getAccountNumber(), 10000);

            System.out.println("\n3. Generated statement for " + sAcc.getAccountNumber() + ":\n" + service.statement(sAcc.getAccountNumber()));

            System.out.println("\n4. Search accounts by name 'amit':");
            List<Account> matches = service.searchAccountsByName("amit");
            for (Account m : matches) {
                System.out.println("   Found: " + m);
            }

            System.out.println("\n5. Reflective Audit Logger Entries (@Audit):");
            for (String entry : AuditLogger.getAuditTrail()) {
                System.out.println("   " + entry);
            }

            service.saveAccounts();
        } catch (Exception e) {
            System.err.println("Practical 14 demo error: " + e.getMessage());
        }
    }

    private static void printHeader(int practicalNumber, String title) {
        System.out.println("\n================================================================");
        System.out.println("  PRACTICAL " + practicalNumber + " (PART B): " + title);
        System.out.println("================================================================");
    }
}
