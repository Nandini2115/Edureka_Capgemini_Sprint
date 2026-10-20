package utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public final class ScreenShot {

    private ScreenShot() {
    }

    public static String captureScreenshot(
            WebDriver driver, String testName) throws IOException {

        if (driver == null) {
            throw new IllegalArgumentException("WebDriver is null.");
        }

        Path directory = Paths.get("Screenshots");
        Files.createDirectories(directory);

        String safeName = (testName == null ? "test" : testName)
                .replaceAll("[^a-zA-Z0-9_-]", "_");

        String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));

        String fileName = safeName + "_" + timestamp + "_"
                + UUID.randomUUID().toString().substring(0, 8) + ".png";

        File source = ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.FILE);

        Path destination = directory.resolve(fileName);
        Files.copy(source.toPath(), destination);

        return destination.toAbsolutePath().toString();
    }
}