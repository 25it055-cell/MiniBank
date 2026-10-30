package minibank.exception;

/**
 * Thrown when daily withdrawal limit is exceeded (Practical 8 Supplementary Problem).
 */
public class DailyLimitExceededException extends BankException {
    public DailyLimitExceededException(long limit, long attempted) {
        super("Daily withdrawal limit of " + limit + " exceeded. Attempted: " + attempted);
    }
}
