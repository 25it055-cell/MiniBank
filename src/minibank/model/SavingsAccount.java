package minibank.model;

public class SavingsAccount extends Account {
    private static final long serialVersionUID = 1L;
    private final long minBalance;

    public SavingsAccount(String ownerName, long openingBalance, long minBalance) {
        super(ownerName, openingBalance);
        this.minBalance = minBalance;
    }

    public SavingsAccount(String ownerName, long openingBalance) {
        this(ownerName, openingBalance, 1000L);
    }

    public SavingsAccount(String accountNumber, String ownerName, long balance, long minBalance) {
        this(accountNumber, ownerName, balance, true, minBalance);
    }

    public SavingsAccount(String accountNumber, String ownerName, long balance, boolean active, long minBalance) {
        super(accountNumber, ownerName, balance, active);
        this.minBalance = minBalance;
    }

    @Override
    public double interestRate() {
        return 4.0;
    }

    @Override
    public synchronized boolean canWithdraw(long amount) {
        return (this.balance - amount) >= minBalance;
    }

    public long getMinBalance() {
        return minBalance;
    }

    @Override
    public String getAccountType() {
        return "SAVINGS";
    }
}
