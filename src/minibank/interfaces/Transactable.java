package minibank.interfaces;

import minibank.exception.InsufficientFundsException;
import minibank.exception.InvalidAmountException;

/**
 * Interface representing transactable capability (Practical 6 Part B).
 */
public interface Transactable {
    void deposit(long amount) throws InvalidAmountException;
    boolean withdraw(long amount) throws InsufficientFundsException, InvalidAmountException;
}
