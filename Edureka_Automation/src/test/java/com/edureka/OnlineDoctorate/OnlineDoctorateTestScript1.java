
package com.edureka.OnlineDoctorate;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import baseclass.BaseClass;

public class OnlineDoctorateTestScript1 extends BaseClass {

    private final String doctorateUrl =
            "https://www.edureka.co/online-doctorate-programs";

    private final By doctorateHeading = By.xpath(
            "//h1[contains(translate(normalize-space(.),"
            + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),"
            + "'online doctorate programs')]"
    );

    private final By programLinks = By.cssSelector(
            "a[href*='/online-doctorate-programs/']"
    );

    private final By applyNowButton = By.xpath(
            "//*[self::a or self::button]"
            + "[contains(translate(normalize-space(.),"
            + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),"
            + "'apply now')]"
    );

    // Detect an Edureka/CDN error page before checking page content.
    private void failIfErrorPage(String pageName) {
        String title = driver.getTitle();
        String bodyText = "";

        try {
            bodyText = driver.findElement(By.tagName("body")).getText();
        } catch (Exception ignored) {
            // The body may not be available yet.
        }

        String titleLower =
                title == null ? "" : title.toLowerCase(Locale.ROOT);

        String bodyLower = bodyText.toLowerCase(Locale.ROOT);

        if (titleLower.contains("error: the request could not be satisfied")
                || bodyLower.contains("the request could not be satisfied")) {

            throw new IllegalStateException(
                    pageName + " returned an Edureka/CDN error page."
                    + " URL: " + driver.getCurrentUrl()
                    + ", title: " + title
                    + ". Check whether the page opens manually in Chrome."
            );
        }
    }

    // TC_ODP common navigation
    private void openDoctorateLandingPage() {

        System.out.println("Opening Online Doctorate Programs:");
        System.out.println(doctorateUrl);

        driver.get(doctorateUrl);

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.tagName("body")));

        failIfErrorPage("Online Doctorate landing page");

        wait.until(d -> {
            String title = d.getTitle();
            return title != null && !title.trim().isEmpty();
        });

        failIfErrorPage("Online Doctorate landing page");

        System.out.println("Page title: " + driver.getTitle());
        System.out.println("Current URL: " + driver.getCurrentUrl());

        wait.until(ExpectedConditions.urlContains(
                "/online-doctorate-programs"));

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                doctorateHeading));

        System.out.println("Doctorate landing page loaded successfully.");

        pauseForDemo();
    }

    // Collect unique program detail URLs.
    private List<String> getProgramUrls() {

        failIfErrorPage("Online Doctorate landing page");

        List<String> urls = new ArrayList<>();

        List<WebElement> links = driver.findElements(programLinks);

        for (WebElement link : links) {
            try {
                String href = link.getAttribute("href");

                if (link.isDisplayed()
                        && href != null
                        && href.contains("/online-doctorate-programs/")
                        && !href.equalsIgnoreCase(doctorateUrl)
                        && !urls.contains(href)) {

                    urls.add(href);
                }
            } catch (Exception ignored) {
                // Skip an element that changed during page rendering.
            }
        }

        return urls;
    }

    // Open and validate a program details page.
    private void openProgramDetails(String programUrl) {

        System.out.println("Opening doctorate program: " + programUrl);

        driver.get(programUrl);

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.tagName("body")));

        failIfErrorPage("Doctorate program details page");

        wait.until(d -> {
            String title = d.getTitle();
            return title != null && !title.trim().isEmpty();
        });

        failIfErrorPage("Doctorate program details page");

        wait.until(d -> d.getCurrentUrl().contains(
                "/online-doctorate-programs/"));

        System.out.println("Program details URL: " + driver.getCurrentUrl());
        System.out.println("Program details title: " + driver.getTitle());

        pauseForDemo();
    }

    // Extract a meaningful description from the current program page.
    private String getVisibleDescription() {

        failIfErrorPage("Doctorate program details page");

        List<WebElement> paragraphs =
                driver.findElements(By.cssSelector("main p, article p, p"));

        for (WebElement paragraph : paragraphs) {
            try {
                if (paragraph.isDisplayed()) {
                    String text = paragraph.getText().trim();

                    if (text.length() > 40) {
                        return text;
                    }
                }
            } catch (Exception ignored) {
                // Continue checking remaining paragraphs.
            }
        }

        return "";
    }

    // Count possible university/provider logos.
    private int countPossibleLogos() {

        List<WebElement> images = driver.findElements(By.cssSelector("img"));

        int count = 0;

        for (WebElement image : images) {
            try {
                if (!image.isDisplayed()) {
                    continue;
                }

                String alt = image.getAttribute("alt");
                String src = image.getAttribute("src");

                String details =
                        ((alt == null ? "" : alt) + " "
                        + (src == null ? "" : src))
                        .toLowerCase(Locale.ROOT);

                if (details.contains("university")
                        || details.contains("provider")
                        || details.contains("logo")) {

                    System.out.println("Possible logo: " + details);
                    count++;
                }
            } catch (Exception ignored) {
                // Ignore stale image elements.
            }
        }

        return count;
    }

    // TC_ODP_001: Verify landing page and program listing.
    @Test(groups = {"smoke", "functional"})
    public void verifyDoctorateLandingPageLoads() {

        System.out.println("\n========== TC_ODP_001 ==========");

        openDoctorateLandingPage();

        WebElement heading = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        doctorateHeading));

        Assert.assertTrue(
                heading.isDisplayed(),
                "Online Doctorate heading is not visible");

        System.out.println("Heading: " + heading.getText());

        List<String> urls = getProgramUrls();

        for (String url : urls) {
            System.out.println("Program URL: " + url);
        }

        Assert.assertFalse(
                urls.isEmpty(),
                "No doctorate program links were found");

        System.out.println("Unique program links: " + urls.size());

        Reporter.log(
                "TC_ODP_001 PASSED: Landing heading and program links verified",
                true);
    }

    // TC_ODP_004: Compare two doctorate program details.
    @Test(groups = {"functional", "integration"})
    public void compareDoctorateProgramDetails() {

        System.out.println("\n========== TC_ODP_004 ==========");

        openDoctorateLandingPage();

        List<String> urls = getProgramUrls();

        Assert.assertTrue(
                urls.size() >= 2,
                "At least two doctorate program links are required");

        String firstUrl = urls.get(0);
        String secondUrl = urls.get(1);

        Assert.assertNotEquals(
                firstUrl,
                secondUrl,
                "Both program URLs are identical");

        System.out.println("First program: " + firstUrl);
        System.out.println("Second program: " + secondUrl);

        // First program.
        openProgramDetails(firstUrl);

        String firstTitle = driver.getTitle();
        String firstDescription = getVisibleDescription();
        int firstLogoCount = countPossibleLogos();

        Assert.assertNotNull(firstTitle);
        Assert.assertFalse(
                firstTitle.trim().isEmpty(),
                "First program title is empty");

        Assert.assertFalse(
                firstDescription.isEmpty(),
                "First program description was not found");

        System.out.println("First title: " + firstTitle);
        System.out.println("First description: " + firstDescription);
        System.out.println("First possible logo count: " + firstLogoCount);

        // Second program.
        openProgramDetails(secondUrl);

        String secondTitle = driver.getTitle();
        String secondDescription = getVisibleDescription();
        int secondLogoCount = countPossibleLogos();

        Assert.assertNotNull(secondTitle);
        Assert.assertFalse(
                secondTitle.trim().isEmpty(),
                "Second program title is empty");

        Assert.assertFalse(
                secondDescription.isEmpty(),
                "Second program description was not found");

        System.out.println("Second title: " + secondTitle);
        System.out.println("Second description: " + secondDescription);
        System.out.println("Second possible logo count: " + secondLogoCount);

        System.out.println(
                "Titles different: "
                + !firstTitle.equalsIgnoreCase(secondTitle));

        System.out.println(
                "Descriptions different: "
                + !firstDescription.equalsIgnoreCase(secondDescription));

        Reporter.log(
                "TC_ODP_004 PASSED: Two program details pages inspected",
                true);
    }

    // TC_ODP_006: Verify Apply Now navigation.
    @Test(groups = {"functional", "integration"})
    public void verifyApplyNowNavigation() {

        System.out.println("\n========== TC_ODP_006 ==========");

        openDoctorateLandingPage();

        List<String> urls = getProgramUrls();

        Assert.assertFalse(
                urls.isEmpty(),
                "No doctorate program URLs found");

        openProgramDetails(urls.get(0));

        WebElement applyButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        applyNowButton));

        Assert.assertTrue(
                applyButton.isDisplayed(),
                "Apply Now button is not visible");

        String oldUrl = driver.getCurrentUrl();

        System.out.println("Selected program: " + oldUrl);
        System.out.println("Clicking Apply Now");

        applyButton.click();

        pauseForDemo();

        boolean destinationChanged = false;
        boolean formAppeared = false;

        try {
            wait.until(d -> !d.getCurrentUrl().equals(oldUrl));
            destinationChanged = true;
        } catch (Exception e) {
            System.out.println(
                    "URL did not change; checking for a visible form.");
        }

        for (WebElement form : driver.findElements(By.cssSelector("form"))) {
            try {
                if (form.isDisplayed()) {
                    formAppeared = true;
                    System.out.println("Visible form found.");
                    break;
                }
            } catch (Exception ignored) {
                // Continue checking forms.
            }
        }

        System.out.println("Destination URL: " + driver.getCurrentUrl());

        Assert.assertTrue(
                destinationChanged || formAppeared,
                "Apply Now did not open a new page or visible form");

        Reporter.log(
                "TC_ODP_006 PASSED: Apply Now destination or form detected",
                true);
    }
}
