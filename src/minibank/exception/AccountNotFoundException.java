package minibank.exception;

/**
 * Thrown when an account cannot be located (Practical 8 Part B).
 */
public class AccountNotFoundException extends BankException {
    public AccountNotFoundException(String accountNumber) {
        super("Account not found: " + accountNumber);
    }
}

