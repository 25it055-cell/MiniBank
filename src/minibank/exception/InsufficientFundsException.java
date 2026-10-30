package minibank.exception;

/**
 * Exception thrown when withdrawal/transfer exceeds available balance (Practical 8 Part B).
 * Holds the shortfall amount.
 */
public class InsufficientFundsException extends BankException {
    private final long shortfall;

    public InsufficientFundsException(long shortfall) {
        super("Insufficient funds: short by " + shortfall);
        this.shortfall = shortfall;
    }

    public InsufficientFundsException(String message, long shortfall) {
        super(message);
        this.shortfall = shortfall;
    }

    public long getShortfall() {
        return shortfall;
    }
}

