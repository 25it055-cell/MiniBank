package minibank.model;

import minibank.exception.BankException;
import minibank.exception.InsufficientFundsException;
import minibank.exception.InvalidAmountException;
import minibank.interfaces.InterestBearing;
import minibank.interfaces.Transactable;
import minibank.model.annotation.Id;
import minibank.model.annotation.MaxLength;
import minibank.model.annotation.Positive;

import java.io.Serializable;
import java.util.Objects;

/**
 * Abstract Account base class (Practicals 2, 3, 5, 6, 7, 8, 9, 11, 12 Part B).
 * Combines encapsulation, polymorphism, annotations, synchronization, and interfaces.
 */
public abstract class Account implements Transactable, InterestBearing, Comparable<Account>, Serializable {
    private static final long serialVersionUID = 1L;
    private static long accountCounter = 0;

    @Id
    private final String accountNumber;

    @MaxLength(50)
    private String ownerName;

    @Positive
    protected long balance;

    private boolean active;

    /**
     * Thread-safe generator for account numbers (e.g. AC0001).
     */
    protected static synchronized String generateAccountNumber() {
        accountCounter++;
        return String.format("AC%04d", accountCounter);
    }

    // Constructor taking ownerName and opening balance
    public Account(String ownerName, long openingBalance) {
        this.accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = openingBalance;
        this.active = true;
    }

    // Constructor chaining using this(ownerName, 0)
    public Account(String ownerName) {
        this(ownerName, 0L);
    }

    // Constructor with explicit account number (for persistence / loading)
    public Account(String accountNumber, String ownerName, long openingBalance, boolean active) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = openingBalance;
        this.active = active;
    }

    // Abstract methods defined for polymorphism (Practical 5 Part B)
    @Override
    public abstract double interestRate();
    public abstract boolean canWithdraw(long amount);

    /**
     * Synchronized deposit method (Practicals 2, 8, 9 Part B).
     * Rejects non-positive amounts with InvalidAmountException.
     */
    @Override
    public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive: " + amount);
        }
        this.balance += amount;
    }

    /**
     * Synchronized withdraw method (Practicals 2, 5, 8, 9 Part B).
     * Rejects invalid amounts and verifies withdrawal eligibility.
     */
    @Override
    public synchronized boolean withdraw(long amount) throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive: " + amount);
        }
        if (!canWithdraw(amount)) {
            long shortfall = amount - this.balance;
            if (shortfall < 0) shortfall = 0; // If min balance was breached
            throw new InsufficientFundsException("Withdrawal refused by account rules. Shortfall: " + shortfall, shortfall);
        }
        this.balance -= amount;
        return true;
    }

    /**
     * Inter-account money transfer using try, catch, finally (Practical 8 Part B).
     * Acquires locks consistently to prevent deadlocks (Practical 10 Part B).
     */
    public boolean transfer(Account target, long amount) throws BankException {
        if (target == null) {
            throw new BankException("Target account cannot be null");
        }
        if (this.equals(target)) {
            throw new InvalidAmountException("Cannot transfer to the same account");
        }
        if (amount <= 0) {
            throw new InvalidAmountException("Transfer amount must be positive: " + amount);
        }

        // Lock ordering by accountNumber to eliminate deadlock (Practical 10 Part B)
        Account firstLock = this.compareTo(target) < 0 ? this : target;
        Account secondLock = this.compareTo(target) < 0 ? target : this;

        synchronized (firstLock) {
            synchronized (secondLock) {
                boolean debitSuccess = false;
                try {
                    this.withdraw(amount);
                    debitSuccess = true;
                    target.deposit(amount);
                    return true;
                } catch (BankException e) {
                    if (debitSuccess) {
                        // Rollback debit if deposit failed
                        try {
                            this.deposit(amount);
                        } catch (InvalidAmountException ignored) {}
                    }
                    throw e;
                } finally {
                    // Cleanup / logging hook if needed
                }
            }
        }
    }

    // Getters - NO public setter for balance (enforcing encapsulation)
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    @Override
    public synchronized long getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public abstract String getAccountType();

    @Override
    public int compareTo(Account other) {
        return this.accountNumber.compareTo(other.accountNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account account)) return false;
        return Objects.equals(accountNumber, account.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }

    @Override
    public String toString() {
        return accountNumber + " | " + ownerName + " | " + balance + " INR | " +
               getAccountType() + " | " + (active ? "ACTIVE" : "INACTIVE");
    }
}
