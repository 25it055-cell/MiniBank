package minibank.interfaces;

import minibank.model.Account;

@FunctionalInterface
public interface WithdrawRule {
    boolean allow(Account account, long amount);
}
