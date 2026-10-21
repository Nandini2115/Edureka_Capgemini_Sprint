package objRepo;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ArtificialIntelligencePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public static final String AI_URL =
            "https://www.edureka.co/artificial-intelligence-certification-courses";

    public static final String COURSE_URL =
            "https://www.edureka.co/executive-programs/pgp-generative-ai-machine-learning-certification-training";

    public static final String COURSE_TITLE =
            "Post Graduate Program in Generative AI and Agentic AI";

    private final By headings =
            By.cssSelector("h1, h2, h3");

    private final By courseLink =
            By.cssSelector("a[href*='/executive-programs/pgp-generative-ai-machine-learning-certification-training']");

    private final By queryWidget =
            By.xpath("//*[contains(.,'Drop us a Query')]");

    private final By submitButton =
            By.xpath("//button[contains(.,'SUBMIT QUERY')] | //input[@value='SUBMIT QUERY']");

    public ArtificialIntelligencePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    public ArtificialIntelligencePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    // Open AI page
    public void openAIPage() {
        System.out.println("Opening AI page");

        driver.get(AI_URL);

        wait.until(ExpectedConditions.urlContains(
                "artificial-intelligence-certification-courses"));

        System.out.println("AI page title: " + driver.getTitle());
        System.out.println("AI page URL: " + driver.getCurrentUrl());
    }

    public void open() {
        openAIPage();
    }

    public void openAiCategory() {
        openAIPage();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }

    public boolean isAiPageLoaded() {
        return driver.getCurrentUrl().contains(
                "artificial-intelligence-certification-courses")
                && driver.getTitle().toLowerCase().contains(
                        "artificial intelligence");
    }

    public boolean pageLoaded() {
        return isAiPageLoaded();
    }

    // Get visible headings
    public List<String> getVisibleHeadings() {
        List<String> list = new ArrayList<>();

        for (WebElement element : driver.findElements(headings)) {
            if (element.isDisplayed()) {
                String text = element.getText().trim();

                if (!text.isEmpty()) {
                    list.add(text);
                }
            }
        }

        return list;
    }

    public boolean hasVisibleHeadings() {
        return !getVisibleHeadings().isEmpty();
    }

    // Check course cards
    public boolean hasCourseCards() {
        By cards = By.xpath(
                "//a[contains(.,'View Program') or contains(.,'View Course Details')]"
                + " | //button[contains(.,'View Program') or contains(.,'View Course Details')]");

        for (WebElement element : driver.findElements(cards)) {
            if (element.isDisplayed()) {
                return true;
            }
        }

        return false;
    }

    // Check whether a course is present
    public boolean containsCourse(String courseName) {
        By course = By.xpath(
                "//*[contains(.,'" + courseName + "')]");

        for (WebElement element : driver.findElements(course)) {
            if (element.isDisplayed()) {
                return true;
            }
        }

        return false;
    }

    // Open course details
    public void openCourseByTitle(String courseTitle) {
        System.out.println("Opening course: " + courseTitle);

        WebElement link = wait.until(
                ExpectedConditions.elementToBeClickable(courseLink));

        link.click();

        wait.until(ExpectedConditions.urlContains("/executive-programs/"));

        System.out.println("Course URL: " + driver.getCurrentUrl());
        System.out.println("Course title: " + driver.getTitle());
    }

    // Check overview content
    public boolean hasOverviewText() {
        for (WebElement element : driver.findElements(
                By.cssSelector("main, section, h1, h2, h3"))) {

            if (element.isDisplayed()
                    && !element.getText().trim().isEmpty()) {
                return true;
            }
        }

        return false;
    }

    // Open enquiry widget
    public void openQueryWidget() {
        System.out.println("Looking for enquiry widget");

        WebElement widget = wait.until(
                ExpectedConditions.visibilityOfElementLocated(queryWidget));

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", widget);

        widget.click();

        System.out.println("Enquiry widget opened");
    }

    public void openQueryWidgetIfNeeded() {
        openQueryWidget();
    }

    // Submit empty enquiry form
    public void submitEmptyEnquiryForm() {
        WebElement button = wait.until(
                ExpectedConditions.elementToBeClickable(submitButton));

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", button);

        System.out.println("Submitting enquiry form with empty fields");

        button.click();
    }

    public void submitEmptyQuery() {
        submitEmptyEnquiryForm();
    }

    // Check required field validation
    public boolean hasRequiredFieldValidation() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        boolean invalidField = (Boolean) js.executeScript(
                "return Array.from(document.querySelectorAll('input,textarea,select'))"
                + ".some(e => e.offsetParent !== null && !e.checkValidity());");

        if (invalidField) {
            System.out.println("Required field validation detected");
            return true;
        }

        By messages = By.cssSelector(
                "[role='alert'], [class*='error'], [class*='invalid']");

        for (WebElement element : driver.findElements(messages)) {
            if (element.isDisplayed()
                    && !element.getText().trim().isEmpty()) {

                System.out.println("Validation message: "
                        + element.getText().trim());

                return true;
            }
        }

        return false;
    }

    public boolean hasQueryValidationMessages() {
        return hasRequiredFieldValidation();
    }

    // Search for visible text
    public boolean hasAnyText(String... terms) {
        for (String term : terms) {
            By locator = By.xpath(
                    "//*[contains(translate(.,"
                    + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
                    + "'abcdefghijklmnopqrstuvwxyz'),'"
                    + term.toLowerCase() + "')]");

            for (WebElement element : driver.findElements(locator)) {
                if (element.isDisplayed()) {
                    return true;
                }
            }
        }

        return false;
    }
}
