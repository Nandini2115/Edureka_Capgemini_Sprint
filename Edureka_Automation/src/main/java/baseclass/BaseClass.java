package baseclass;

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
    protected Properties prop = new Properties();

    // Load properties
    private void loadProperties() throws Exception {
        InputStream input = getClass()
                .getClassLoader()
                .getResourceAsStream("CommonData.properties");

        if (input == null) {
            throw new RuntimeException(
                    "CommonData.properties not found");
        }

        prop.load(input);
        input.close();
    }

    // Get property value
    protected String getProperty(String key) {
        return prop.getProperty(key).trim();
    }

    // Browser setup
    @Parameters("browser")
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("chrome") String browser)
            throws Exception {

        loadProperties();

        System.out.println("===== BROWSER SETUP =====");
        System.out.println("Browser: " + browser);

        if (browser.equalsIgnoreCase("chrome")) {

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            options.addArguments("--disable-notifications");

            driver = new ChromeDriver(options);

        } else if (browser.equalsIgnoreCase("firefox")) {

            FirefoxOptions options = new FirefoxOptions();
            driver = new FirefoxDriver(options);
            driver.manage().window().maximize();

        } else {
            throw new RuntimeException("Unsupported browser: " + browser);
        }

        driver.manage().timeouts()
                .implicitlyWait(Duration.ZERO);

        driver.manage().timeouts()
                .pageLoadTimeout(Duration.ofSeconds(60));

        wait = new WebDriverWait(driver, Duration.ofSeconds(25));

        driver.get(getProperty("url"));

        System.out.println("URL: " + driver.getCurrentUrl());
        System.out.println("Title: " + driver.getTitle());
        System.out.println("Explicit wait: 25 seconds");

        Reporter.log("Browser started: " + browser, true);
    }

    // Pause for demonstration
    protected void pauseForDemo() throws InterruptedException {
        Thread.sleep(1000);
    }

    // Close browser
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
            wait = null;
            System.out.println("Browser closed.");
        }
    }
}
