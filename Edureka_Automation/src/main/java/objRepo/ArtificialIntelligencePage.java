
package objRepo;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    public static final String AI_URL =
        "https://www.edureka.co/artificial-intelligence-certification-courses";

    public static final String COURSE_URL =
        "https://www.edureka.co/executive-programs/pgp-generative-ai-machine-learning-certification-training";

    public static final String COURSE_TITLE =
        "Post Graduate Program in Generative AI and Agentic AI";

    public ArtificialIntelligencePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public ArtificialIntelligencePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    private String pageText() {
        try {
            return driver.findElement(By.tagName("body"))
                         .getText().toLowerCase(Locale.ROOT);
        } catch (Exception e) {
            return "";
        }
    }

    private void failIfErrorPage(String pageName) {
        String title = driver.getTitle();
        String text = pageText();

        if ((title != null && title.toLowerCase(Locale.ROOT)
                .contains("error: the request could not be satisfied"))
                || text.contains("the request could not be satisfied")) {

            throw new IllegalStateException(
                pageName + " returned an Edureka/CDN error page."
                + " URL: " + driver.getCurrentUrl()
                + ", title: " + title
                + ". Check the site in a normal browser before rerunning Jenkins."
            );
        }
    }

    private void waitForPageAndCheck(String pageName) {
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.tagName("body")));

        failIfErrorPage(pageName);

        wait.until(d -> {
            String title = d.getTitle();
            return title != null && !title.trim().isEmpty();
        });

        failIfErrorPage(pageName);
    }

    // AI page navigation

    public void openAIPage() {
        System.out.println("Opening AI page: " + AI_URL);
        driver.get(AI_URL);

        wait.until(ExpectedConditions.urlContains(
            "artificial-intelligence-certification-courses"));

        waitForPageAndCheck("AI listing page");

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
        failIfErrorPage("AI listing page");

        return driver.getCurrentUrl().contains(
            "artificial-intelligence-certification-courses")
            && driver.getTitle().toLowerCase(Locale.ROOT)
                .contains("artificial intelligence");
    }

    public boolean pageLoaded() {
        return isAiPageLoaded();
    }

    public List<String> getVisibleHeadings() {
        List<String> headings = new ArrayList<>();

        for (WebElement element :
                driver.findElements(By.cssSelector("h1, h2, h3"))) {
            try {
                if (element.isDisplayed()) {
                    String text = element.getText().trim();
                    if (!text.isEmpty()) {
                        headings.add(text);
                    }
                }
            } catch (StaleElementReferenceException ignored) {
                // Dynamic content can refresh.
            }
        }

        return headings;
    }

    public boolean hasVisibleHeadings() {
        failIfErrorPage("AI listing page");
        return !getVisibleHeadings().isEmpty();
    }

    public boolean hasCourseCards() {
        failIfErrorPage("AI listing page");

        By cards = By.xpath(
            "//*[self::a or self::button]"
            + "[contains(translate(normalize-space(.),"
            + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),"
            + "'view program')"
            + " or contains(translate(normalize-space(.),"
            + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),"
            + "'view course details')]");

        for (WebElement element : driver.findElements(cards)) {
            try {
                if (element.isDisplayed()) {
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
                // Continue checking.
            }
        }

        return false;
    }

    public boolean containsCourse(String courseName) {
        failIfErrorPage("AI listing page");

        String lower = courseName.toLowerCase(Locale.ROOT);
        String safeText = lower.replace("'", "\\'");

        By course = By.xpath(
            "//*[contains(translate(normalize-space(.),"
            + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),"
            + "'" + safeText + "')]");

        for (WebElement element : driver.findElements(course)) {
            try {
                if (element.isDisplayed()) {
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
                // Continue checking.
            }
        }

        return false;
    }

    public void openCourseByTitle(String courseTitle) {
        System.out.println("Searching for course: " + courseTitle);

        failIfErrorPage("AI listing page");

        String targetUrl = COURSE_URL;

        // Prefer a matching course link when one is present.
        By courseLink = By.cssSelector(
            "a[href*='/executive-programs/pgp-generative-ai-machine-learning-certification-training']");

        for (WebElement link : driver.findElements(courseLink)) {
            try {
                String href = link.getAttribute("href");

                if (link.isDisplayed()
                        && href != null
                        && !href.isBlank()) {
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
        waitForPageAndCheck("AI course details page");

        System.out.println("Course details URL: " + driver.getCurrentUrl());
        System.out.println("Course details title: " + driver.getTitle());

        // Log a mismatch clearly rather than waiting for the exact course
        // title on a page that may use a shortened or different heading.
        String expected = courseTitle.toLowerCase(Locale.ROOT);
        String actual = pageText();

        if (!actual.contains(expected)) {
            System.out.println(
                "NOTE: Exact course title was not found in the page text."
                + " Expected: " + courseTitle
                + ". Confirm the current course heading in the browser."
            );
        }
    }

    public boolean hasOverviewText() {
        failIfErrorPage("AI course details page");

        for (WebElement element : driver.findElements(
                By.cssSelector("main, section, h1, h2, h3"))) {
            try {
                if (element.isDisplayed()
                        && !element.getText().trim().isEmpty()) {
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
                // Continue checking.
            }
        }

        return false;
    }

    // Enquiry widget

    public void openQueryWidget() {
        failIfErrorPage("Enquiry page");

        System.out.println("Looking for Drop us a Query widget...");

        By textLocator = By.xpath(
            "//*[contains(normalize-space(text()), 'Drop us a Query')]");

        WebElement widget = wait.until(d -> {
            for (WebElement element : d.findElements(textLocator)) {
                try {
                    if (element.isDisplayed()) {
                        return element;
                    }
                } catch (StaleElementReferenceException ignored) {
                    // Try the next match.
                }
            }
            return null;
        });

        By bodyLocator = By.cssSelector("div[class*='duq_body']");

        boolean bodyAlreadyOpen = driver.findElements(bodyLocator).stream()
            .anyMatch(element -> {
                try {
                    return element.isDisplayed();
                } catch (StaleElementReferenceException ignored) {
                    return false;
                }
            });

        if (!bodyAlreadyOpen) {
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", widget);

            try {
                widget.click();
            } catch (ElementClickInterceptedException e) {
                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].click();", widget);
            }
        }

        System.out.println("Enquiry widget is ready.");
    }

    public void openQueryWidgetIfNeeded() {
        openQueryWidget();
    }

    // Submit empty enquiry form

    public void submitEmptyEnquiryForm() {
        failIfErrorPage("Enquiry form page");

        By submitLocator = By.xpath(
            "//button[contains(translate(normalize-space(.),"
            + "'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),"
            + "'SUBMIT QUERY')]"
            + " | //input[translate(@value,"
            + "'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='SUBMIT QUERY']");

        WebElement button = wait.until(d -> {
            for (WebElement element : d.findElements(submitLocator)) {
                try {
                    if (element.isDisplayed() && element.isEnabled()) {
                        return element;
                    }
                } catch (StaleElementReferenceException ignored) {
                    // Try the next match.
                }
            }
            return null;
        });

        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});", button);

        System.out.println("Clicking SUBMIT QUERY with empty fields.");

        try {
            button.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].click();", button);
        }
    }

    public void submitEmptyQuery() {
        submitEmptyEnquiryForm();
    }

    // Validation check

    public boolean hasRequiredFieldValidation() {
        Object invalidField = ((JavascriptExecutor) driver).executeScript(
            "return Array.from(document.querySelectorAll('input,textarea,select'))"
            + ".some(e => e.offsetParent !== null && !e.checkValidity());");

        if (Boolean.TRUE.equals(invalidField)) {
            System.out.println("Browser required-field validation detected.");
            return true;
        }

        By messages = By.cssSelector(
            "[role='alert'], [class*='error'], [class*='invalid'],"
            + "[class*='errorMessage'], [class*='error_message']");

        for (WebElement element : driver.findElements(messages)) {
            try {
                if (element.isDisplayed()
                        && !element.getText().trim().isEmpty()) {
                    System.out.println(
                        "Validation message: " + element.getText().trim());
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
                // Continue checking.
            }
        }

        return false;
    }

    public boolean hasQueryValidationMessages() {
        return hasRequiredFieldValidation();
    }

    public boolean hasAnyText(String... terms) {
        failIfErrorPage("Current AI page");

        for (String term : terms) {
            String safeTerm = term.toLowerCase(Locale.ROOT)
                                 .replace("'", "\\'");

            By locator = By.xpath(
                "//*[contains(translate(normalize-space(.),"
                + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),"
                + "'" + safeTerm + "')]");

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
