
package objRepo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By loginDialog = By.xpath("//div[@role='dialog']");
    private final By loginEntry = By.xpath("//button[normalize-space()='LOG IN']");

    // Prefer fields inside the login popup.
    private final By emailField = By.cssSelector("input[type='email']");
    private final By passwordField = By.cssSelector("input[type='password']");

    // Find the submit button inside the popup, not the header.
    private final By submitButton = By.xpath("//button[normalize-space()='LOG IN']");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    public void openLoginPopup() {

        // Do not click the header button if the popup is already open.
        if (isLoginPopupVisible()) {
            return;
        }

        wait.until(ExpectedConditions.elementToBeClickable(loginEntry)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginDialog));
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
    }

    public boolean isLoginPopupVisible() {
        for (WebElement dialog : driver.findElements(loginDialog)) {
            if (dialog.isDisplayed()) {
                return true;
            }
        }

        // Fallback in case the site does not expose the dialog
        // with the expected role.
        for (WebElement field : driver.findElements(emailField)) {
            if (field.isDisplayed()) {
                return true;
            }
        }
        return false;
    }

    public void enterEmail(String email) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
        field.clear();
        field.sendKeys(email);
    }

    public void enterPassword(String password) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        field.clear();
        field.sendKeys(password);
    }

    public void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
    }
    public boolean isPasswordFieldVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).isDisplayed();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}
