package minibank.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

/**
 * Append-only audit logger using modern Java NIO Files API (Practical 11 Part B).
 */
public class TransactionLog {

    /**
     * Appends a log line to the specified file using Files.write with CREATE and APPEND.
     */
    public static void append(Path file, String line) throws IOException {
        if (file.getParent() != null) {
            Files.createDirectories(file.getParent());
        }
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String entry = "[" + timestamp + "] " + line;
        Files.write(file, Collections.singletonList(entry), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}
