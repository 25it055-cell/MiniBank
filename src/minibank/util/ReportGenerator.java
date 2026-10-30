package minibank.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

/**
 * End-of-day audit and summary report generator using Java NIO (Practical 11 Part B).
 */
public class ReportGenerator {

    /**
     * Walks logs directory, computes deposit/withdrawal totals, and writes summary report.
     * Skips non-.log or empty files (Practical 11 Supplementary Problem).
     */
    public static String generateDailyReport(Path logsDir, Path reportFile) throws IOException {
        if (!Files.exists(logsDir)) {
            Files.createDirectories(logsDir);
        }

        long totalDeposits = 0;
        long totalWithdrawals = 0;
        int fileCount = 0;
        int totalLineCount = 0;

        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("============================================================\n");
        reportBuilder.append("               MINIBANK END-OF-DAY AUDIT REPORT              \n");
        reportBuilder.append("============================================================\n");
        reportBuilder.append("Generated Time: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        reportBuilder.append("Logs Directory: ").append(logsDir.toAbsolutePath()).append("\n\n");
        reportBuilder.append("Processed Audit Logs:\n");

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(logsDir)) {
            for (Path entry : stream) {
                // Supplementary rule: skip files not ending in .log or empty
                String fileName = entry.getFileName().toString();
                if (!fileName.endsWith(".log") || Files.size(entry) == 0L) {
                    continue;
                }

                BasicFileAttributes attrs = Files.readAttributes(entry, BasicFileAttributes.class);
                fileCount++;
                reportBuilder.append(String.format(" - %-25s | Size: %6d bytes | Last Modified: %s\n",
                        fileName, attrs.size(), attrs.lastModifiedTime()));

                try (BufferedReader reader = Files.newBufferedReader(entry, StandardCharsets.UTF_8)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        totalLineCount++;
                        // Line format: [timestamp] OPERATION ACxxxx AMOUNT ...
                        // Extract content after timestamp if present
                        String payload = line;
                        int closeBracket = line.indexOf(']');
                        if (closeBracket != -1) {
                            payload = line.substring(closeBracket + 1).trim();
                        }

                        String[] tokens = payload.split("\\s+");
                        if (tokens.length >= 3) {
                            String action = tokens[0].toUpperCase();
                            try {
                                long amt = Long.parseLong(tokens[2]);
                                if (action.contains("DEPOSIT")) {
                                    totalDeposits += amt;
                                } else if (action.contains("WITHDRAW")) {
                                    totalWithdrawals += amt;
                                }
                            } catch (NumberFormatException ignored) {
                            }
                        }
                    }
                }
            }
        }

        long netChange = totalDeposits - totalWithdrawals;

        reportBuilder.append("\n------------------------------------------------------------\n");
        reportBuilder.append("FINANCIAL RECONCILIATION SUMMARY:\n");
        reportBuilder.append("------------------------------------------------------------\n");
        reportBuilder.append(String.format("Log Files Processed  : %d\n", fileCount));
        reportBuilder.append(String.format("Total Audit Entries  : %d\n", totalLineCount));
        reportBuilder.append(String.format("Total Deposits       : %,d INR\n", totalDeposits));
        reportBuilder.append(String.format("Total Withdrawals    : %,d INR\n", totalWithdrawals));
        reportBuilder.append(String.format("Net Vault Change     : %+d INR\n", netChange));
        reportBuilder.append("============================================================\n");

        String reportText = reportBuilder.toString();

        if (reportFile.getParent() != null) {
            Files.createDirectories(reportFile.getParent());
        }
        Files.write(reportFile, Collections.singletonList(reportText), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        return reportText;
    }
}

