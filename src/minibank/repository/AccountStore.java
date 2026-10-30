package minibank.repository;

import minibank.model.Account;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AccountStore managing accounts and transaction history with Collections Framework (Practical 12 Part B).
 */
public class AccountStore {
    private final Map<String, Account> accounts = new HashMap<>();
    private final Map<String, Account> concurrentAccounts = new ConcurrentHashMap<>();
    private final List<String> transactionHistory = new ArrayList<>();

    public synchronized void add(Account account) {
        if (account != null) {
            accounts.put(account.getAccountNumber(), account);
            concurrentAccounts.put(account.getAccountNumber(), account);
        }
    }

    public synchronized Account get(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public synchronized List<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }

    public Map<String, Account> getConcurrentAccounts() {
        return concurrentAccounts;
    }

    public synchronized void logTransaction(String entry) {
        transactionHistory.add(entry);
    }

    public synchronized List<String> getTransactionHistory() {
        return Collections.unmodifiableList(transactionHistory);
    }

    /**
     * Returns an ArrayList<Account> sorted by balance descending using a Comparator (Practical 12 Part B).
     */
    public synchronized ArrayList<Account> sortedByBalance() {
        ArrayList<Account> sortedList = new ArrayList<>(accounts.values());
        sortedList.sort(Comparator.comparingLong(Account::getBalance).reversed());
        return sortedList;
    }

    /**
     * Supplementary problem Practical 12: filters accounts with balance >= threshold.
     */
    public synchronized List<Account> findAccountsWithBalanceAbove(long threshold) {
        List<Account> filtered = new ArrayList<>();
        for (Account acc : accounts.values()) {
            if (acc.getBalance() >= threshold) {
                filtered.add(acc);
            }
        }
        return filtered;
    }
}
