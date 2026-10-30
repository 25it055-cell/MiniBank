package minibank.exception;

public class DailyLimitExceededException extends BankException {
    public DailyLimitExceededException(long limit, long attempted) {
        super("Daily withdrawal limit of " + limit + " exceeded. Attempted: " + attempted);
    }
}
