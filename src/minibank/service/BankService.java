package minibank.service;

import minibank.exception.AccountNotFoundException;
import minibank.exception.BankException;
import minibank.exception.InsufficientFundsException;
import minibank.exception.InvalidAmountException;
import minibank.model.Account;
import minibank.model.Command;
import minibank.model.CurrentAccount;
import minibank.model.Customer;
import minibank.model.FixedDepositAccount;
import minibank.model.SavingsAccount;
import minibank.model.annotation.Audit;
import minibank.repository.AccountStore;
import minibank.repository.Repository;
import minibank.util.ReportGenerator;
import minibank.util.StatePersister;
import minibank.util.StatementFormatter;
import minibank.util.TransactionLog;

import static minibank.util.Validator.*; // Static import (Practical 6 Part B)

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * BankService providing business logic and wiring repositories, validation,
 * persistence, concurrency, and exceptions together (Practical 14 Part B).
 */
public class BankService {
    private final Repository<Account> accountRepo;
    private final Repository<Customer> customerRepo;
    private final AccountStore accountStore;
    private final Path dataPath = Paths.get("data", "accounts.dat");
    private final Path logsDir = Paths.get("logs");
    private final Path auditLogFile = Paths.get("logs", "transactions.log");
    private final Path reportFile = Paths.get("logs", "daily_report.txt");

    public BankService() {
        this.accountRepo = new Repository<>();
        this.customerRepo = new Repository<>();
        this.accountStore = new AccountStore();
        loadSavedAccounts();
    }

    /**
     * Opens a new account after validating customer input (Practicals 2, 4, 5, 14 Part B).
     */
    @Audit(action = "OPEN_ACCOUNT")
    public Account openAccount(String accountType, String ownerName, long initialBalance,
                               String email, String mobile, String line, String city, String pincode)
            throws BankException {

        AuditLogger.logIfAudited(BankService.class, "openAccount", new Object[]{accountType, ownerName, initialBalance});

        if (ownerName == null || ownerName.trim().isEmpty()) {
            throw new InvalidAmountException("Owner name cannot be empty");
        }
        if (email != null && !email.isBlank() && !isValidEmail(email)) {
            throw new BankException("Invalid email format: " + email);
        }
        if (mobile != null && !mobile.isBlank() && !isValidMobile(mobile)) {
            throw new BankException("Invalid 10-digit mobile number: " + mobile);
        }
        if (initialBalance < 0) {
            throw new InvalidAmountException("Initial balance cannot be negative: " + initialBalance);
        }

        Customer.Address address = new Customer.Address(line != null ? line : "Main St",
                                                        city != null ? city : "Mumbai",
                                                        pincode != null ? pincode : "400001");
        Customer customer = new Customer(ownerName, email, mobile, address);
        customerRepo.save(customer);

        Account account;
        switch (accountType.toUpperCase()) {
            case "SAVINGS" -> {
                if (initialBalance < 1000) {
                    throw new InvalidAmountException("Savings account requires at least 1,000 INR opening balance");
                }
                account = new SavingsAccount(ownerName, initialBalance, 1000L);
            }
            case "CURRENT" -> account = new CurrentAccount(ownerName, initialBalance, 5000L);
            case "FIXED_DEPOSIT", "FD" -> {
                if (initialBalance < 5000) {
                    throw new InvalidAmountException("Fixed Deposit requires at least 5,000 INR initial deposit");
                }
                account = new FixedDepositAccount(ownerName, initialBalance, 12);
            }
            default -> throw new BankException("Unknown account type: " + accountType + ". Allowed: SAVINGS, CURRENT, FIXED_DEPOSIT");
        }

        accountRepo.save(account);
        accountStore.add(account);

        try {
            TransactionLog.append(auditLogFile, "OPEN_ACCOUNT " + account.getAccountNumber() + " " + initialBalance + " " + accountType + " " + ownerName);
        } catch (IOException e) {
            System.err.println("Warning: Could not write to transaction log: " + e.getMessage());
        }

        return account;
    }

    /**
     * Deposits funds into an account (Practicals 2, 8, 14 Part B).
     */
    @Audit(action = "DEPOSIT")
    public void deposit(String accountNumber, long amount) throws BankException {
        AuditLogger.logIfAudited(BankService.class, "deposit", new Object[]{accountNumber, amount});

        Account account = getAccountOrThrow(accountNumber);
        account.deposit(amount);
        accountStore.logTransaction("DEPOSIT " + accountNumber + " " + amount);

        try {
            TransactionLog.append(auditLogFile, "DEPOSIT " + accountNumber + " " + amount);
        } catch (IOException e) {
            System.err.println("Warning: Failed to log transaction: " + e.getMessage());
        }
    }

    /**
     * Withdraws funds from an account (Practicals 2, 5, 8, 14 Part B).
     */
    @Audit(action = "WITHDRAW")
    public void withdraw(String accountNumber, long amount) throws BankException {
        AuditLogger.logIfAudited(BankService.class, "withdraw", new Object[]{accountNumber, amount});

        Account account = getAccountOrThrow(accountNumber);
        account.withdraw(amount);
        accountStore.logTransaction("WITHDRAW " + accountNumber + " " + amount);

        try {
            TransactionLog.append(auditLogFile, "WITHDRAW " + accountNumber + " " + amount);
        } catch (IOException e) {
            System.err.println("Warning: Failed to log transaction: " + e.getMessage());
        }
    }

    /**
     * Inter-account funds transfer (Practicals 8, 10, 14 Part B).
     */
    @Audit(action = "TRANSFER")
    public void transfer(String fromAccountNum, String toAccountNum, long amount) throws BankException {
        AuditLogger.logIfAudited(BankService.class, "transfer", new Object[]{fromAccountNum, toAccountNum, amount});

        Account from = getAccountOrThrow(fromAccountNum);
        Account to = getAccountOrThrow(toAccountNum);

        from.transfer(to, amount);

        String entry = "TRANSFER " + fromAccountNum + " -> " + toAccountNum + " " + amount;
        accountStore.logTransaction(entry);

        try {
            TransactionLog.append(auditLogFile, entry);
        } catch (IOException e) {
            System.err.println("Warning: Failed to log transfer: " + e.getMessage());
        }
    }

    /**
     * Generates account statement (Practical 4 Part B).
     */
    public String statement(String accountNumber) throws BankException {
        Account account = getAccountOrThrow(accountNumber);
        return StatementFormatter.buildStatement(account);
    }

    /**
     * Retrieves all accounts sorted by balance descending (Practical 12 Part B).
     */
    public List<Account> listAccounts() {
        return accountStore.sortedByBalance();
    }

    /**
     * Supplementary problem: search accounts by owner name (case-insensitive).
     */
    public List<Account> searchAccountsByName(String name) {
        if (name == null || name.isBlank()) return List.of();
        String query = name.toLowerCase();
        List<Account> matches = new ArrayList<>();
        for (Account acc : accountRepo.findAll()) {
            if (acc.getOwnerName().toLowerCase().contains(query)) {
                matches.add(acc);
            }
        }
        return matches;
    }

    /**
     * Batch transaction executor utilizing managed thread pool (Practicals 10, 14 Part B).
     */
    public void processBatch(List<Command> commands) {
        if (commands == null || commands.isEmpty()) return;

        TransactionProcessor processor = new TransactionProcessor();
        for (Command cmd : commands) {
            processor.submit(() -> {
                try {
                    switch (cmd.type()) {
                        case DEPOSIT -> deposit(cmd.accountNumber(), cmd.amount());
                        case WITHDRAW -> withdraw(cmd.accountNumber(), cmd.amount());
                        default -> System.err.println("Batch unsupported type: " + cmd.type());
                    }
                } catch (Exception e) {
                    System.err.println("Batch transaction error on " + cmd.accountNumber() + ": " + e.getMessage());
                }
            });
        }
        processor.stop();
    }

    /**
     * Generates daily NIO summary report (Practical 11 & 14 Part B).
     */
    public String generateReport() throws IOException {
        return ReportGenerator.generateDailyReport(logsDir, reportFile);
    }

    /**
     * Loads saved accounts from disk (Practical 11 Part B).
     */
    public void loadSavedAccounts() {
        try {
            Account[] accounts = StatePersister.load(dataPath);
            if (accounts != null && accounts.length > 0) {
                for (Account acc : accounts) {
                    accountRepo.save(acc);
                    accountStore.add(acc);
                }
                System.out.println("[PERSISTENCE] Successfully loaded " + accounts.length + " accounts from " + dataPath);
            }
        } catch (Exception e) {
            System.out.println("[PERSISTENCE] Initial startup or no existing data found: " + e.getMessage());
        }
    }

    /**
     * Saves all accounts to disk (Practical 11 & 14 Part B).
     */
    public void saveAccounts() {
        try {
            List<Account> accounts = accountRepo.findAll();
            Account[] arr = accounts.toArray(new Account[0]);
            StatePersister.save(arr, dataPath);
            System.out.println("[PERSISTENCE] Successfully saved " + arr.length + " accounts to " + dataPath);
        } catch (Exception e) {
            System.err.println("[PERSISTENCE ERROR] Failed to save accounts: " + e.getMessage());
        }
    }

    public Account getAccountOrThrow(String accountNumber) throws AccountNotFoundException {
        Account account = accountRepo.findById(accountNumber);
        if (account == null) {
            account = accountStore.get(accountNumber);
        }
        if (account == null) {
            throw new AccountNotFoundException(accountNumber);
        }
        return account;
    }

    public Repository<Account> getAccountRepo() {
        return accountRepo;
    }

    public Repository<Customer> getCustomerRepo() {
        return customerRepo;
    }

    public AccountStore getAccountStore() {
        return accountStore;
    }
}

