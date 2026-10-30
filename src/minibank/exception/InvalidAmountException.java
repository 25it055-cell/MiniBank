package minibank.exception;

/**
 * Thrown when an invalid (negative or zero) transaction amount is provided (Practical 8 Part B).
 */
public class InvalidAmountException extends BankException {
    public InvalidAmountException(String message) {
        super(message);
    }
}

