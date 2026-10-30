package minibank.interfaces;

/**
 * Interface representing interest-bearing accounts (Practical 6 Part B).
 * Features a default method yearlyInterest and a supplementary default method monthlyInterest.
 */
public interface InterestBearing {
    double interestRate();
    long getBalance();

    /**
     * Calculates projected yearly interest based on current balance and interest rate (Practical 6 Part B).
     */
    default double yearlyInterest() {
        return getBalance() * (interestRate() / 100.0);
    }

    /**
     * Supplementary problem Practical 6: Second default method.
     */
    default double monthlyInterest() {
        return yearlyInterest() / 12.0;
    }
}

