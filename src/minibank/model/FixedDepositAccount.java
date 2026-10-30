package minibank.model;

import minibank.interfaces.Premium;

/**
 * FixedDepositAccount subclass (Practical 5 Part B).
 * Offers 7.0% interest rate; funds are locked (canWithdraw returns false).
 * Implements the Premium marker interface (Practical 6 Part B).
 */
public class FixedDepositAccount extends Account implements Premium {
    private static final long serialVersionUID = 1L;
    private final int tenureMonths;

    public FixedDepositAccount(String ownerName, long openingBalance, int tenureMonths) {
        super(ownerName, openingBalance);
        this.tenureMonths = tenureMonths;
    }

    public FixedDepositAccount(String ownerName, long openingBalance) {
        this(ownerName, openingBalance, 12);
    }

    public FixedDepositAccount(String accountNumber, String ownerName, long balance, boolean active, int tenureMonths) {
        super(accountNumber, ownerName, balance, active);
        this.tenureMonths = tenureMonths;
    }

    @Override
    public double interestRate() {
        return 7.0;
    }

    @Override
    public synchronized boolean canWithdraw(long amount) {
        // Funds are locked in Fixed Deposit
        return false;
    }

    public int getTenureMonths() {
        return tenureMonths;
    }

    @Override
    public String getAccountType() {
        return "FIXED_DEPOSIT";
    }
}

