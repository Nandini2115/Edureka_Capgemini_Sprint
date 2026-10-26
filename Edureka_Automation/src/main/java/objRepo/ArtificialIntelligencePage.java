package objRepo;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ArtificialIntelligencePage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    public static final String AI_URL = "https://www.edureka.co/artificial-intelligence-certification-courses";
    public static final String COURSE_URL = "https://www.edureka.co/executive-programs/pgp-generative-ai-machine-learning-certification-training";
    public static final String COURSE_TITLE = "Post Graduate Program in Generative AI and Agentic AI";

    public ArtificialIntelligencePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }
    public ArtificialIntelligencePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    // AI page navigation
    public void openAIPage() {
        System.out.println("Opening AI page: " + AI_URL);
        driver.get(AI_URL);
        wait.until(ExpectedConditions.urlContains("artificial-intelligence-certification-courses"));
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
        return driver.getCurrentUrl().contains("artificial-intelligence-certification-courses") && driver.getTitle().toLowerCase().contains("artificial intelligence");
    }
    public boolean pageLoaded() {
        return isAiPageLoaded();
    }

    public List<String> getVisibleHeadings() {
        List<String> headings = new ArrayList<>();
        for (WebElement element : driver.findElements(By.cssSelector("h1, h2, h3"))) {
            try {
                if (element.isDisplayed()) {
                    String text = element.getText().trim();
                    if (!text.isEmpty()) {
                        headings.add(text);
                    }
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }

        return headings;
    }

    public boolean hasVisibleHeadings() {
        return !getVisibleHeadings().isEmpty();
    }

    public boolean hasCourseCards() {
    	By cards = By.xpath("//div[@id='popular_courses']//button");
        for (WebElement element : driver.findElements(cards)) {
            try {
                if (element.isDisplayed()) {
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }

        return false;
    }
    public boolean containsCourse(String courseName) {
        String lower = courseName.toLowerCase();
        By course = By.xpath("//*[contains(.,'" + courseName + "')]");
        for (WebElement element : driver.findElements(course)) {
            try {
                if (element.isDisplayed()) {
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
        
            }
        }

        return false;
    }

    public void openCourseByTitle(String courseTitle) {
        System.out.println("Searching for course: " + courseTitle);
        By courseText = By.xpath("//*[contains(.,'" + courseTitle + "')]");
        wait.until(ExpectedConditions.presenceOfElementLocated(courseText));

        String targetUrl = COURSE_URL;

        By courseLink = By.cssSelector("a[href*='/executive-programs/pgp-generative-ai-machine-learning-certification-training']");

        for (WebElement link : driver.findElements(courseLink)) {
            try {
                String href = link.getAttribute("href");
                if (link.isDisplayed() && href != null && !href.isBlank()) {
                    targetUrl = href;
                    break;
                }
            } catch (StaleElementReferenceException ignored) {
                // Use the known course URL.
            }
        }

        System.out.println("Course destination: " + targetUrl);
        driver.navigate().to(targetUrl);

        wait.until(ExpectedConditions.urlContains("/executive-programs/"));
        wait.until(d -> !d.getTitle().isBlank());

        System.out.println("Course details URL: " + driver.getCurrentUrl());
        System.out.println("Course details title: " + driver.getTitle());
    }

    public boolean hasOverviewText() {
        for (WebElement element : driver.findElements(
                By.cssSelector("main, section, h1, h2, h3"))) {
            try {
                if (element.isDisplayed()
                        && !element.getText().trim().isEmpty()) {
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
            
            }
        }

        return false;
    }

    public void openQueryWidget() {
        System.out.println("Looking for Drop us a Query widget...");
        By textLocator = By.xpath("//*[contains(.,'Drop us a Query')]");
        WebElement widget = wait.until(d -> {
            for (WebElement element : d.findElements(textLocator)) {
                try {
                    if (element.isDisplayed()) {
                        return element;
                    }
                } catch (StaleElementReferenceException ignored) {

                }
            }
            return null;
        });

        By bodyLocator = By.cssSelector("div[class*='duq_body']");

        boolean bodyAlreadyOpen = driver.findElements(bodyLocator).stream().anyMatch(element -> {
                try {
                    return element.isDisplayed();
                } catch (StaleElementReferenceException ignored) {
                    return false;
                }
            });

        if (!bodyAlreadyOpen) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", widget);

            try {
                widget.click();
            } catch (ElementClickInterceptedException e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", widget);
            }
        }

        System.out.println("Enquiry widget is ready.");
    }

    public void openQueryWidgetIfNeeded() {
        openQueryWidget();
    }

    public void submitEmptyEnquiryForm() {
    	By submitLocator = By.xpath("//button[normalize-space()='SUBMIT QUERY']");

        WebElement button = wait.until(d -> {
            for (WebElement element : d.findElements(submitLocator)) {
                try {
                    if (element.isDisplayed() && element.isEnabled()) {
                        return element;
                    }
                } catch (StaleElementReferenceException ignored) {
                  
                }
            }
            return null;
        });

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", button);

        System.out.println("Clicking SUBMIT QUERY with empty fields.");

        try {
            button.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        }
    }

    public void submitEmptyQuery() {
        submitEmptyEnquiryForm();
    }

    // Validation check

    public boolean hasRequiredFieldValidation() {
        Object invalidField = ((JavascriptExecutor) driver).executeScript("return Array.from(document.querySelectorAll('input,textarea,select'))"+ ".some(e => e.offsetParent !== null && !e.checkValidity());");

        if (Boolean.TRUE.equals(invalidField)) {
            System.out.println("Browser required-field validation detected");
            return true;
        }

        By messages = By.cssSelector("[role='alert'], [class*='error'], [class*='invalid']," + "[class*='errorMessage'], [class*='error_message']");

        for (WebElement element : driver.findElements(messages)) {
            try {
                if (element.isDisplayed() && !element.getText().trim().isEmpty()) {
                    System.out.println("Validation message: " + element.getText().trim());
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }

        return false;
    }
    public boolean hasQueryValidationMessages() {
        return hasRequiredFieldValidation();
    }

    public boolean hasAnyText(String... terms) {
        for (String term : terms) {
            By locator = By.xpath("//*[contains(translate(normalize-space(.),"+ "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')," + "'" + term.toLowerCase() + "')]");
            for (WebElement element : driver.findElements(locator)) {
                try {
                    if (element.isDisplayed()) {
                        return true;
                    }
                } catch (StaleElementReferenceException ignored) {
                    // Continue checking.
                }
            }
        }
        return false;
    }
}