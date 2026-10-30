package minibank.model;

/**
 * Record representing a parsed banking command (Practical 4 Part B).
 */
public record Command(TransactionType type, String accountNumber, long amount) {
    public Command {
        if (type == null) {
            throw new IllegalArgumentException("Transaction type cannot be null");
        }
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Account number cannot be empty");
        }
    }
}

