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

    protected static synchronized String generateAccountNumber() {
        accountCounter++;
        return String.format("AC%04d", accountCounter);
    }

    public Account(String ownerName, long openingBalance) {
        this.accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = openingBalance;
        this.active = true;
    }

    public Account(String ownerName) {
        this(ownerName, 0L);
    }

    public Account(String accountNumber, String ownerName, long openingBalance, boolean active) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = openingBalance;
        this.active = active;
    }

    @Override
    public abstract double interestRate();
    public abstract boolean canWithdraw(long amount);

    @Override
    public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive: " + amount);
        }
        this.balance += amount;
    }

    @Override
    public synchronized boolean withdraw(long amount) throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive: " + amount);
        }
        if (!canWithdraw(amount)) {
            long shortfall = amount - this.balance;
            if (shortfall < 0) shortfall = 0;
            throw new InsufficientFundsException("Withdrawal refused by account rules. Shortfall: " + shortfall, shortfall);
        }
        this.balance -= amount;
        return true;
    }

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

                        try {
                            this.deposit(amount);
                        } catch (InvalidAmountException ignored) {}
                    }
                    throw e;
                } finally {

                }
            }
        }
    }

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
