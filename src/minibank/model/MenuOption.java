package minibank.model;

/**
 * Enum defining navigation options for MiniBank console menu (Practicals 1 & 14 Part B).
 */
public enum MenuOption {
    OPEN_ACCOUNT(1, "Open New Account"),
    DEPOSIT(2, "Deposit Funds"),
    WITHDRAW(3, "Withdraw Funds"),
    TRANSFER(4, "Transfer Funds"),
    STATEMENT(5, "View Account Statement"),
    LIST_ACCOUNTS(6, "List All Accounts (Collections & Sorting)"),
    SEARCH_ACCOUNTS(7, "Search Account by Owner Name"),
    BATCH_TRANSACTIONS(8, "Execute Concurrent Batch Transactions (ThreadPool)"),
    DAILY_REPORT(9, "Generate Daily Audit Log & Report (NIO)"),
    WORKING_HOURS(10, "View Bank Working Hours"),
    DEMO_ALL_PRACTICALS(11, "Run Complete Practical Verification Suite (Labs 1-14)"),
    EXIT(12, "Save State and Exit");

    private final int code;
    private final String description;

    MenuOption(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static MenuOption fromCode(int code) {
        for (MenuOption option : values()) {
            if (option.code == code) {
                return option;
            }
        }
        return null;
    }
}

