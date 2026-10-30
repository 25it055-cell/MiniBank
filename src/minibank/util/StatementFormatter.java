package minibank.util;

import minibank.model.Account;
import minibank.model.FixedDepositAccount;
import minibank.model.SavingsAccount;
import minibank.model.CurrentAccount;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility using StringBuilder to assemble multi-line account statements (Practical 4 Part B).
 */
public class StatementFormatter {

    public static String buildStatement(Account account) {
        if (account == null) {
            return "No account specified.";
        }

        StringBuilder sb = new StringBuilder(256);
        sb.append("------------------------------------------------------------\n");
        sb.append("                     ACCOUNT STATEMENT                      \n");
        sb.append("------------------------------------------------------------\n");
        sb.append("Generated On   : ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        sb.append("Account Number : ").append(account.getAccountNumber()).append("\n");
        sb.append("Account Holder : ").append(account.getOwnerName()).append("\n");
        sb.append("Account Type   : ").append(account.getAccountType()).append("\n");
        sb.append("Account Status : ").append(account.isActive() ? "Active" : "Suspended").append("\n");
        sb.append("Interest Rate  : ").append(account.interestRate()).append("% p.a.\n");
        sb.append("Current Balance: ").append(account.getBalance()).append(" INR\n");

        // Specific details based on account subtype using pattern matching instanceof
        if (account instanceof SavingsAccount sa) {
            sb.append("Minimum Balance: ").append(sa.getMinBalance()).append(" INR\n");
        } else if (account instanceof CurrentAccount ca) {
            sb.append("Overdraft Limit: ").append(ca.getOverdraftLimit()).append(" INR\n");
        } else if (account instanceof FixedDepositAccount fda) {
            sb.append("Tenure (Months): ").append(fda.getTenureMonths()).append("\n");
        }

        sb.append("Est. Yr Interest: ").append(String.format("%.2f", account.yearlyInterest())).append(" INR\n");
        sb.append("------------------------------------------------------------");
        return sb.toString();
    }
}
