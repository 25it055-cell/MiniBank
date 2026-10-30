package minibank.exception;

/**
 * Base checked exception class for all bank errors (Practical 8 Part B).
 */
public class BankException extends Exception {
    public BankException(String message) {
        super(message);
    }

    public BankException(String message, Throwable cause) {
        super(message, cause);
    }
}

