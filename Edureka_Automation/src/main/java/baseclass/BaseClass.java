
package baseclass;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseClass {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Properties prop;

    // Load CommonData.properties
    private void loadProperties() {
        prop = new Properties();

        try (InputStream input = getClass()
                .getClassLoader()
                .getResourceAsStream("CommonData.properties")) {

            if (input == null) {
                throw new IllegalStateException(
                    "CommonData.properties not found in src/test/resources.");
            }

            prop.load(input);

        } catch (IOException e) {
            throw new RuntimeException(
                "Unable to load CommonData.properties.", e);
        }
    }

    // Read a property value
    protected String getProperty(String key) {
        if (prop == null) {
            loadProperties();
        }

        String value = prop.getProperty(key);

        if (value == null) {
            throw new IllegalArgumentException(
                "Property not found in CommonData.properties: " + key);
        }

        return value.trim();
    }

    // Initialize browser before every test
    @Parameters("browser")
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("chrome") String browser) {

        loadProperties();

        if (browser == null || browser.trim().isEmpty()) {
            browser = prop.getProperty("browser", "chrome");
        }

        browser = browser.trim().toLowerCase();

        System.out.println("\n========== BROWSER SETUP ==========");
        System.out.println("Selected browser: " + browser);

        try {
            switch (browser) {

                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--start-maximized");
                    chromeOptions.addArguments("--disable-notifications");

                    driver = new ChromeDriver(chromeOptions);
                    break;

                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();

                    driver = new FirefoxDriver(firefoxOptions);
                    driver.manage().window().maximize();
                    break;

                default:
                    throw new IllegalArgumentException(
                        "Unsupported browser: " + browser
                        + ". Use chrome or firefox.");
            }

            int waitSeconds;

            try {
                waitSeconds = Integer.parseInt(
                    prop.getProperty("explicitWait", "20").trim());
            } catch (NumberFormatException e) {
                waitSeconds = 20;
            }

            driver.manage().timeouts()
                    .implicitlyWait(Duration.ZERO);

            driver.manage().timeouts()
                    .pageLoadTimeout(Duration.ofSeconds(60));

            wait = new WebDriverWait(
                    driver, Duration.ofSeconds(waitSeconds));

            String url = getProperty("url");

            System.out.println("Opening URL: " + url);
            driver.get(url);

            System.out.println("Browser started successfully.");
            System.out.println("Current URL: " + driver.getCurrentUrl());
            System.out.println("Page title: " + driver.getTitle());
            System.out.println("Explicit wait: " + waitSeconds + " seconds");
            System.out.println("===================================\n");

            Reporter.log(
                "Browser started: " + browser
                + " | URL: " + driver.getCurrentUrl(), true);

        } catch (RuntimeException e) {
            if (driver != null) {
                try {
                    driver.quit();
                } catch (Exception ignored) {
                    // Preserve the original setup error.
                }

                driver = null;
            }

            throw e;
        }
    }

    // Pause for demonstration purposes
    protected void pauseForDemo() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(
                "Demo pause was interrupted.", e);
        }
    }

    // Close browser after every test
    @AfterMethod(alwaysRun = true)
    public void tearDown() {

        if (driver != null) {
            System.out.println(
                "Closing browser after test: "
                + driver.getClass().getSimpleName());

            try {
                driver.quit();
            } finally {
                driver = null;
                wait = null;
            }
        }
    }
}
