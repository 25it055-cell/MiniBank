package minibank.interfaces;

import minibank.model.Account;

/**
 * Functional interface for custom withdrawal validation rules (Practical 6 & 7 Part B).
 * Used with both anonymous classes and lambda expressions.
 */
@FunctionalInterface
public interface WithdrawRule {
    boolean allow(Account account, long amount);
}
