
package objRepo;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OnlineDoctoratePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final String LANDING_URL =
        "https://www.edureka.co/online-doctorate-programs";

    private final By pageHeading = By.xpath(
        "//h1[contains(normalize-space(.),"
        + "'Online Doctorate Programs')]");

    private final By programLinks = By.cssSelector(
        "a[href*='/online-doctorate-programs/']");

    public OnlineDoctoratePage(
            WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    public void openLandingPage() {
        driver.get(LANDING_URL);
        wait.until(
            ExpectedConditions.visibilityOfElementLocated(pageHeading));
    }

    public void openVerifiedPage(String url) {
        if (url == null
                || !url.startsWith(
                    "https://www.edureka.co/online-doctorate-programs")) {
            throw new IllegalArgumentException(
                "Provide a valid Edureka doctorate URL.");
        }

        driver.get(url);
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("h1")));
    }

    public boolean isHeadingVisible() {
        return wait.until(
            ExpectedConditions.visibilityOfElementLocated(pageHeading))
            .isDisplayed();
    }

    public String getHeadingText() {
        return wait.until(
            ExpectedConditions.visibilityOfElementLocated(pageHeading))
            .getText();
    }

    public List<WebElement> getProgramLinks() {
        return wait.until(
            ExpectedConditions.visibilityOfAllElementsLocatedBy(
                programLinks));
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public void openProgramByUrl(String programUrl) {
        if (programUrl == null
                || !programUrl.startsWith(
                    "https://www.edureka.co/online-doctorate-programs/")) {
            throw new IllegalArgumentException(
                "Provide a verified doctorate program URL.");
        }

        driver.get(programUrl);
        wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("h1")));
    }
}
