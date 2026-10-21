package objRepo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By loginDialog =
            By.xpath("//div[@role='dialog']");

    private final By loginEntry =
            By.xpath("//a[contains(.,'Login') or contains(.,'Log In')]"
                    + " | //button[contains(.,'Login') or contains(.,'Log In')]");

    private final By emailField =
            By.cssSelector("input[type='email'], input[name='email']");

    private final By passwordField =
            By.cssSelector("input[type='password']");

    private final By submitButton =
            By.xpath("//div[@role='dialog']//button[contains(.,'Login')"
                    + " or contains(.,'Log In')]");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    // Open login popup
    public void openLoginPopup() {
        if (isLoginPopupVisible()) {
            System.out.println("Login popup is already open");
            return;
        }

        wait.until(
                ExpectedConditions.elementToBeClickable(loginEntry))
                .click();

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(emailField));

        System.out.println("Login popup opened");
    }

    // Check login popup
    public boolean isLoginPopupVisible() {
        for (WebElement field : driver.findElements(emailField)) {
            if (field.isDisplayed()) {
                return true;
            }
        }

        return false;
    }

    // Enter email
    public void enterEmail(String email) {
        WebElement field = wait.until(
                ExpectedConditions.visibilityOfElementLocated(emailField));

        field.clear();
        field.sendKeys(email);
    }

    // Enter password
    public void enterPassword(String password) {
        WebElement field = wait.until(
                ExpectedConditions.visibilityOfElementLocated(passwordField));

        field.clear();
        field.sendKeys(password);
    }

    // Click login
    public void clickLogin() {
        WebElement button = wait.until(
                ExpectedConditions.elementToBeClickable(submitButton));

        button.click();
    }

    // Check password field
    public boolean isPasswordFieldVisible() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(passwordField))
                .isDisplayed();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}
