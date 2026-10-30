package minibank.util;

import minibank.model.Command;
import minibank.model.TransactionType;

public class CommandParser {

    public static Command parse(String line) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty command string");
        }

        String[] tokens = line.trim().split("\\s+");
        if (tokens.length != 3) {
            throw new IllegalArgumentException("Invalid command format. Expected: <TYPE> <ACCOUNT_NUMBER> <AMOUNT>. Received " + tokens.length + " tokens.");
        }

        TransactionType type;
        try {
            type = TransactionType.valueOf(tokens[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown transaction type: " + tokens[0] + ". Valid types: DEPOSIT, WITHDRAW, TRANSFER");
        }

        String accountNumber = tokens[1];
        long amount;
        try {
            amount = Long.parseLong(tokens[2]);
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be a positive integer: " + tokens[2]);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format for amount: " + tokens[2]);
        }

        return new Command(type, accountNumber, amount);
    }
}
