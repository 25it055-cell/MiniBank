package minibank;

import minibank.exception.BankException;
import minibank.model.Account;
import minibank.model.BankInfo;
import minibank.model.Command;
import minibank.model.MenuOption;
import minibank.model.TransactionType;
import minibank.service.BankService;
import minibank.test.MiniBankTest;
import minibank.test.PracticalDemonstrator;
import minibank.util.CommandParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main application class for MiniBank (Practicals 1 through 14 Part B).
 * Combines the interactive console menu shell with complete backend integration.
 */
public class MiniBank {
    private static final BankInfo BANK_INFO = new BankInfo("MiniBank India", "Headquarters - Nariman Point");
    private static final BankService bankService = new BankService();

    public static void main(String[] args) {
        // Command-line flags for automated grading / testing
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("--demo")) {
                PracticalDemonstrator.runAllPracticals();
                return;
            } else if (args[0].equalsIgnoreCase("--test")) {
                MiniBankTest.runAllTests();
                return;
            }
        }

        // Ensure sample accounts exist on fresh start if none loaded from persistence
        if (bankService.listAccounts().isEmpty()) {
            seedInitialData();
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println(BANK_INFO.getFormattedHeader());
        System.out.println("Welcome to MiniBank Console Banking Portal!");

        boolean running = true;
        while (running) {
            displayMenu();
            System.out.print("\nEnter your choice (1-" + MenuOption.values().length + "): ");

            String input = scanner.nextLine().trim();
            int choiceCode;
            try {
                choiceCode = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("\n[!] Invalid input. Please enter a valid menu number.");
                continue;
            }

            MenuOption option = MenuOption.fromCode(choiceCode);
            if (option == null) {
                System.out.println("\n[!] Option number " + choiceCode + " is outside valid range. Please try again.");
                continue;
            }

            // Using switch expression for menu navigation (Practicals 1 & 14 Part B)
            running = switch (option) {
                case OPEN_ACCOUNT -> {
                    handleOpenAccount(scanner);
                    yield true;
                }
                case DEPOSIT -> {
                    handleDeposit(scanner);
                    yield true;
                }
                case WITHDRAW -> {
                    handleWithdraw(scanner);
                    yield true;
                }
                case TRANSFER -> {
                    handleTransfer(scanner);
                    yield true;
                }
                case STATEMENT -> {
                    handleStatement(scanner);
                    yield true;
                }
                case LIST_ACCOUNTS -> {
                    handleListAccounts();
                    yield true;
                }
                case SEARCH_ACCOUNTS -> {
                    handleSearchAccounts(scanner);
                    yield true;
                }
                case BATCH_TRANSACTIONS -> {
                    handleBatchTransactions();
                    yield true;
                }
                case DAILY_REPORT -> {
                    handleDailyReport();
                    yield true;
                }
                case WORKING_HOURS -> {
                    printWorkingHours();
                    yield true;
                }
                case DEMO_ALL_PRACTICALS -> {
                    PracticalDemonstrator.runAllPracticals();
                    MiniBankTest.runAllTests();
                    yield true;
                }
                case EXIT -> {
                    System.out.println("\nSaving application state to disk...");
                    bankService.saveAccounts();
                    System.out.println("Thank you for banking with MiniBank. Have a wonderful day!");
                    yield false;
                }
            };
        }
    }

    private static void displayMenu() {
        System.out.println("\n========================================================");
        System.out.println("                  MAIN BANKING MENU                     ");
        System.out.println("========================================================");
        for (MenuOption opt : MenuOption.values()) {
            System.out.printf("  [%2d] %s\n", opt.getCode(), opt.getDescription());
        }
        System.out.println("========================================================");
    }

    private static void handleOpenAccount(Scanner scanner) {
        System.out.println("\n--- [1] OPEN NEW ACCOUNT ---");
        System.out.print("Enter Account Type (SAVINGS / CURRENT / FIXED_DEPOSIT): ");
        String type = scanner.nextLine().trim().toUpperCase();

        System.out.print("Enter Customer Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter Initial Deposit Amount (INR): ");
        long amount;
        try {
            amount = Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid numeric amount.");
            return;
        }

        System.out.print("Enter Mobile Number (10 digits, starting 6-9): ");
        String mobile = scanner.nextLine().trim();

        System.out.print("Enter Email Address: ");
        String email = scanner.nextLine().trim();

        System.out.print("Enter Address (Street Line): ");
        String line = scanner.nextLine().trim();

        System.out.print("Enter City: ");
        String city = scanner.nextLine().trim();

        System.out.print("Enter Pincode: ");
        String pincode = scanner.nextLine().trim();

        try {
            Account account = bankService.openAccount(type, name, amount, email, mobile, line, city, pincode);
            System.out.println("\n[SUCCESS] Account successfully opened!");
            System.out.println("Generated Account Number: " + account.getAccountNumber());
            System.out.println("Account Type            : " + account.getAccountType());
            System.out.println("Opening Balance         : " + account.getBalance() + " INR");
        } catch (BankException e) {
            System.out.println("\n[FAILED] Could not open account: " + e.getMessage());
        }
    }

    private static void handleDeposit(Scanner scanner) {
        System.out.println("\n--- [2] DEPOSIT FUNDS ---");
        System.out.print("Choose mode: (1) Standard (2) Command String format (e.g. 'DEPOSIT AC0001 500'): ");
        String mode = scanner.nextLine().trim();

        if (mode.equals("2")) {
            System.out.print("Enter command line: ");
            String cmdLine = scanner.nextLine().trim();
            try {
                Command cmd = CommandParser.parse(cmdLine);
                if (cmd.type() != TransactionType.DEPOSIT) {
                    System.out.println("[!] Command is not a DEPOSIT command.");
                    return;
                }
                bankService.deposit(cmd.accountNumber(), cmd.amount());
                System.out.println("[SUCCESS] Successfully processed deposit of " + cmd.amount() + " INR into " + cmd.accountNumber());
            } catch (Exception e) {
                System.out.println("[ERROR] " + e.getMessage());
            }
        } else {
            System.out.print("Enter Account Number (e.g. AC0001): ");
            String accNum = scanner.nextLine().trim();

            System.out.print("Enter Deposit Amount (INR): ");
            try {
                long amount = Long.parseLong(scanner.nextLine().trim());
                bankService.deposit(accNum, amount);
                System.out.println("[SUCCESS] Deposited " + amount + " INR. New balance: " + bankService.getAccountOrThrow(accNum).getBalance() + " INR");
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid amount format.");
            } catch (BankException e) {
                System.out.println("[FAILED] " + e.getMessage());
            }
        }
    }

    private static void handleWithdraw(Scanner scanner) {
        System.out.println("\n--- [3] WITHDRAW FUNDS ---");
        System.out.print("Enter Account Number (e.g. AC0001): ");
        String accNum = scanner.nextLine().trim();

        System.out.print("Enter Withdrawal Amount (INR): ");
        try {
            long amount = Long.parseLong(scanner.nextLine().trim());
            bankService.withdraw(accNum, amount);
            System.out.println("[SUCCESS] Withdrawn " + amount + " INR. Remaining balance: " + bankService.getAccountOrThrow(accNum).getBalance() + " INR");
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid amount format.");
        } catch (BankException e) {
            System.out.println("[FAILED] " + e.getMessage());
        }
    }

    private static void handleTransfer(Scanner scanner) {
        System.out.println("\n--- [4] TRANSFER FUNDS ---");
        System.out.print("Enter Source Account Number: ");
        String fromAcc = scanner.nextLine().trim();

        System.out.print("Enter Destination Account Number: ");
        String toAcc = scanner.nextLine().trim();

        System.out.print("Enter Transfer Amount (INR): ");
        try {
            long amount = Long.parseLong(scanner.nextLine().trim());
            bankService.transfer(fromAcc, toAcc, amount);
            System.out.println("[SUCCESS] Successfully transferred " + amount + " INR from " + fromAcc + " to " + toAcc);
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid amount format.");
        } catch (BankException e) {
            System.out.println("[FAILED] " + e.getMessage());
        }
    }

    private static void handleStatement(Scanner scanner) {
        System.out.println("\n--- [5] VIEW ACCOUNT STATEMENT ---");
        System.out.print("Enter Account Number: ");
        String accNum = scanner.nextLine().trim();

        try {
            String statement = bankService.statement(accNum);
            System.out.println(statement);
        } catch (BankException e) {
            System.out.println("[FAILED] " + e.getMessage());
        }
    }

    private static void handleListAccounts() {
        System.out.println("\n--- [6] ALL REGISTERED ACCOUNTS (SORTED BY BALANCE DESCENDING) ---");
        List<Account> accounts = bankService.listAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No accounts currently registered.");
            return;
        }

        System.out.printf("%-10s | %-18s | %-15s | %-12s | %s\n", "Acc Number", "Owner Name", "Balance (INR)", "Type", "Status");
        System.out.println("---------------------------------------------------------------------------------");
        for (Account a : accounts) {
            System.out.printf("%-10s | %-18s | %,15d | %-12s | %s\n",
                    a.getAccountNumber(), a.getOwnerName(), a.getBalance(), a.getAccountType(),
                    a.isActive() ? "Active" : "Inactive");
        }
    }

    private static void handleSearchAccounts(Scanner scanner) {
        System.out.println("\n--- [7] SEARCH ACCOUNT BY OWNER NAME ---");
        System.out.print("Enter search keyword: ");
        String query = scanner.nextLine().trim();

        List<Account> results = bankService.searchAccountsByName(query);
        if (results.isEmpty()) {
            System.out.println("No matching accounts found for query: \"" + query + "\"");
        } else {
            System.out.println("Found " + results.size() + " matching account(s):");
            for (Account a : results) {
                System.out.println(" - " + a);
            }
        }
    }

    private static void handleBatchTransactions() {
        System.out.println("\n--- [8] EXECUTING CONCURRENT BATCH TRANSACTIONS ---");
        List<Account> accounts = bankService.listAccounts();
        if (accounts.size() < 2) {
            System.out.println("Need at least 2 accounts for batch demonstration. Please open accounts first.");
            return;
        }

        String acc1 = accounts.get(0).getAccountNumber();
        String acc2 = accounts.get(1).getAccountNumber();

        List<Command> batch = new ArrayList<>();
        batch.add(new Command(TransactionType.DEPOSIT, acc1, 1500));
        batch.add(new Command(TransactionType.WITHDRAW, acc1, 500));
        batch.add(new Command(TransactionType.DEPOSIT, acc2, 3000));
        batch.add(new Command(TransactionType.WITHDRAW, acc2, 1000));

        System.out.println("Dispatching batch of " + batch.size() + " commands to TransactionProcessor managed thread pool...");
        bankService.processBatch(batch);
        System.out.println("[SUCCESS] Batch processing completed safely across background threads.");
    }

    private static void handleDailyReport() {
        System.out.println("\n--- [9] GENERATING DAILY AUDIT REPORT (JAVA NIO) ---");
        try {
            String report = bankService.generateReport();
            System.out.println(report);
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to generate daily report: " + e.getMessage());
        }
    }

    private static void printWorkingHours() {
        System.out.println("\n========================================================");
        System.out.println("               MINIBANK OPERATIONAL HOURS                ");
        System.out.println("========================================================");
        System.out.println("  Monday to Friday : 09:30 AM – 04:30 PM IST");
        System.out.println("  Saturday         : 09:30 AM – 01:30 PM IST (1st & 3rd)");
        System.out.println("  Sunday & Holidays: CLOSED (Digital Services 24x7)");
        System.out.println("========================================================");
    }

    private static void seedInitialData() {
        try {
            bankService.openAccount("SAVINGS", "Riya Sharma", 15000, "riya.sharma@example.com", "9876543210", "MG Road 45", "Mumbai", "400001");
            bankService.openAccount("CURRENT", "Apex Solutions Ltd", 50000, "finance@apex.com", "9123456780", "BKC Complex 12", "Mumbai", "400051");
            bankService.openAccount("FIXED_DEPOSIT", "Vikram Rathore", 100000, "vikram.r@example.com", "9988776655", "Ring Road 8", "Pune", "411001");
        } catch (Exception ignored) {
        }
    }
}
