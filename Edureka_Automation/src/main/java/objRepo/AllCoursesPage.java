package objRepo;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AllCoursesPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private static final String ALL_COURSES_URL =
            "https://www.edureka.co/all-courses";

    // AI category XPath
    private final By aiCategory = By.xpath(
            "//a[contains(.,'Artificial Intelligence')] | " +
            "//button[contains(.,'Artificial Intelligence')]");

    // Course headings
    private final By courseHeadings =
            By.cssSelector("h1, h2, h3, [class*='course-title']");

    public AllCoursesPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    public AllCoursesPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    // Open All Courses page
    public void openAllCourses() {
        System.out.println("Opening All Courses page");

        driver.manage().timeouts()
                .pageLoadTimeout(Duration.ofSeconds(120));

        driver.get(ALL_COURSES_URL);

        wait.until(ExpectedConditions.urlContains("/all-courses"));

        System.out.println("All Courses page opened");
        System.out.println("Page title: " + driver.getTitle());
        System.out.println("Current URL: " + driver.getCurrentUrl());
    }

    public void navigateToAllCoursesByClick() {
        openAllCourses();
    }

    // Check whether the page is loaded
    public boolean isPageLoaded() {
        return driver.getCurrentUrl().contains("/all-courses")
                && !driver.getTitle().trim().isEmpty();
    }

    // Check whether AI category is visible
    public boolean isAiCategoryVisible() {
        for (WebElement element : driver.findElements(aiCategory)) {
            if (element.isDisplayed()) {
                return true;
            }
        }
        return false;
    }

    // Click AI category
    public void selectAiCategory() {
        WebElement element = wait.until(
                ExpectedConditions.elementToBeClickable(aiCategory));

        element.click();

        System.out.println("AI category clicked");
        System.out.println("Current URL: " + driver.getCurrentUrl());
    }

    // Get visible course headings
    public List<String> getVisibleCourseHeadings() {
        List<String> headings = new ArrayList<>();

        for (WebElement element : driver.findElements(courseHeadings)) {
            if (element.isDisplayed()) {
                String text = element.getText().trim();

                if (!text.isEmpty()) {
                    headings.add(text);
                }
            }
        }

        return headings;
    }

    public List<String> getVisibleHeadings() {
        return getVisibleCourseHeadings();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
