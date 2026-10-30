package minibank.model;

/**
 * CurrentAccount subclass (Practical 5 Part B).
 * Allows overdraft limit and provides 0.0% interest.
 */
public class CurrentAccount extends Account {
    private static final long serialVersionUID = 1L;
    private final long overdraftLimit;

    public CurrentAccount(String ownerName, long openingBalance, long overdraftLimit) {
        super(ownerName, openingBalance);
        this.overdraftLimit = overdraftLimit;
    }

    public CurrentAccount(String ownerName, long openingBalance) {
        this(ownerName, openingBalance, 5000L); // Default 5000 overdraft limit
    }

    public CurrentAccount(String accountNumber, String ownerName, long balance, long overdraftLimit) {
        this(accountNumber, ownerName, balance, true, overdraftLimit);
    }

    public CurrentAccount(String accountNumber, String ownerName, long balance, boolean active, long overdraftLimit) {
        super(accountNumber, ownerName, balance, active);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public double interestRate() {
        return 0.0;
    }

    @Override
    public synchronized boolean canWithdraw(long amount) {
        return (this.balance - amount) >= -overdraftLimit;
    }

    public long getOverdraftLimit() {
        return overdraftLimit;
    }

    @Override
    public String getAccountType() {
        return "CURRENT";
    }
}
