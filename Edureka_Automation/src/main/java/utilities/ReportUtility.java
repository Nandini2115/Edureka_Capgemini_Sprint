package utilities;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ReportUtility {

    private static final Path LOG_FILE =
            Paths.get("reports", "report.log");

    private ReportUtility() {
    }

    public static synchronized void log(String message) {
        try {
            Files.createDirectories(LOG_FILE.getParent());

            String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            String line = timestamp + " | " + message
                    + System.lineSeparator();

            Files.write(
                LOG_FILE,
                line.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);

        } catch (IOException e) {
            System.err.println(
                "Unable to write report log: " + e.getMessage());
        }
    }
}