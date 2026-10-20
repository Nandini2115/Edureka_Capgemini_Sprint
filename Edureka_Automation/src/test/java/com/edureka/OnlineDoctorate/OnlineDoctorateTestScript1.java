
package com.edureka.OnlineDoctorate;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import baseclass.BaseClass;

public class OnlineDoctorateTestScript1 extends BaseClass {

    private final String homeUrl = "https://www.edureka.co/";

    private final String doctorateUrl =
            "https://www.edureka.co/online-doctorate-programs";

    private final By doctorateHeading = By.xpath(
            "//h1[contains(" +
            "translate(normalize-space(.)," +
            "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')," +
            "'online doctorate programs')]"
    );

    private final By programLinks = By.cssSelector(
            "a[href*='/online-doctorate-programs/']"
    );

    private final By applyNowButton = By.xpath(
            "//*[self::a or self::button]" +
            "[contains(translate(normalize-space(.)," +
            "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')," +
            "'apply now')]"
    );


    // Common navigation method
    private void openDoctorateLandingPage() {

        System.out.println("STEP 1: Open Edureka application");

        driver.get(homeUrl);

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.tagName("body")
                )
        );

        System.out.println(
                "Home page opened: " + driver.getCurrentUrl()
        );

        pauseForDemo();

        System.out.println(
                "STEP 2: Navigate to Online Doctorate Programs"
        );

        By doctorateNavigation = By.xpath(
                "//a[contains(translate(normalize-space(.)," +
                "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')," +
                "'online doctorate')]"
        );

        List<WebElement> navigationLinks =
                driver.findElements(doctorateNavigation);

        boolean clicked = false;

        for (WebElement link : navigationLinks) {

            if (link.isDisplayed() && link.isEnabled()) {

                try {
                    wait.until(
                            ExpectedConditions.elementToBeClickable(
                                    doctorateNavigation
                            )
                    );

                    link.click();
                    clicked = true;
                    break;

                } catch (Exception e) {
                    System.out.println(
                            "Navigation link could not be clicked."
                    );
                }
            }
        }

        if (!clicked) {
            System.out.println(
                    "Navigation link not found. Opening landing URL directly."
            );

            driver.get(doctorateUrl);
        }

        wait.until(
                ExpectedConditions.urlContains(
                        "/online-doctorate-programs"
                )
        );

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        doctorateHeading
                )
        );

        System.out.println(
                "Online Doctorate page opened: " + driver.getCurrentUrl()
        );

        pauseForDemo();
    }


    private List<String> getProgramUrls() {

        List<String> urls = new ArrayList<>();

        List<WebElement> links =
                driver.findElements(programLinks);

        for (WebElement link : links) {

            String href = link.getAttribute("href");

            if (href != null
                    && href.contains("/online-doctorate-programs/")
                    && !href.equalsIgnoreCase(doctorateUrl)
                    && !urls.contains(href)) {

                urls.add(href);
            }
        }

        return urls;
    }


    private void openProgramDetails(String programUrl) {

        System.out.println(
                "Opening doctorate program: " + programUrl
        );

        driver.get(programUrl);

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.tagName("body")
                )
        );

        wait.until(
                d -> d.getCurrentUrl().contains(
                        "/online-doctorate-programs/"
                )
        );

        System.out.println(
                "Program details page opened: " + driver.getCurrentUrl()
        );

        pauseForDemo();
    }


    private String getVisibleDescription() {

        List<WebElement> paragraphs =
                driver.findElements(By.cssSelector("main p, article p, p"));

        for (WebElement paragraph : paragraphs) {

            if (paragraph.isDisplayed()
                    && !paragraph.getText().trim().isEmpty()) {

                String text = paragraph.getText().trim();

                if (text.length() > 40) {
                    return text;
                }
            }
        }

        return "";
    }


    private int countPossibleLogos() {

        List<WebElement> images =
                driver.findElements(By.cssSelector("img"));

        int count = 0;

        for (WebElement image : images) {

            if (!image.isDisplayed()) {
                continue;
            }

            String alt = image.getAttribute("alt");
            String src = image.getAttribute("src");

            String details =
                    ((alt == null ? "" : alt) + " " +
                     (src == null ? "" : src)).toLowerCase();

            if (details.contains("university")
                    || details.contains("provider")
                    || details.contains("logo")) {

                System.out.println(
                        "Possible logo: " + details
                );

                count++;
            }
        }

        return count;
    }


    // TC_ODP_001: Verify landing page and program listing
    @Test(groups = {"smoke", "functional"})
    public void verifyDoctorateLandingPageLoads() {

        System.out.println("\n========== TC_ODP_001 ==========");

        openDoctorateLandingPage();

        System.out.println("STEP 3: Verify page heading");

        WebElement heading = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        doctorateHeading
                )
        );

        System.out.println(
                "Displayed heading: " + heading.getText()
        );

        Assert.assertTrue(
                heading.isDisplayed(),
                "Online Doctorate heading is not visible"
        );

        pauseForDemo();

        System.out.println("STEP 4: Verify program listing");

        List<String> urls = getProgramUrls();

        for (String programUrl : urls) {
            System.out.println("Program URL: " + programUrl);
        }

        Assert.assertTrue(
                !urls.isEmpty(),
                "No doctorate program links were found"
        );

        System.out.println(
                "Total unique program links: " + urls.size()
        );

        Reporter.log(
                "TC_ODP_001 PASSED: Landing heading and program links verified",
                true
        );
    }


    // TC_ODP_004: Compare two doctorate program details
    @Test(groups = {"functional", "integration"})
    public void compareDoctorateProgramDetails() {

        System.out.println("\n========== TC_ODP_004 ==========");

        openDoctorateLandingPage();

        System.out.println("STEP 3: Collect program links");

        List<String> urls = getProgramUrls();

        Assert.assertTrue(
                urls.size() >= 2,
                "At least two doctorate program links are required"
        );

        String firstUrl = urls.get(0);
        String secondUrl = urls.get(1);

        System.out.println("First program: " + firstUrl);
        System.out.println("Second program: " + secondUrl);

        System.out.println("\nSTEP 4: Open first program");

        openProgramDetails(firstUrl);

        String firstTitle = driver.getTitle();
        String firstDescription = getVisibleDescription();
        int firstLogoCount = countPossibleLogos();

        System.out.println("First title: " + firstTitle);
        System.out.println("First description: " + firstDescription);
        System.out.println("Possible logos: " + firstLogoCount);

        Assert.assertNotNull(firstTitle);

        Assert.assertFalse(
                firstTitle.trim().isEmpty(),
                "First program title is empty"
        );

        Assert.assertFalse(
                firstDescription.isEmpty(),
                "First program description was not found"
        );

        pauseForDemo();

        System.out.println("\nSTEP 5: Open second program");

        openProgramDetails(secondUrl);

        String secondTitle = driver.getTitle();
        String secondDescription = getVisibleDescription();
        int secondLogoCount = countPossibleLogos();

        System.out.println("Second title: " + secondTitle);
        System.out.println("Second description: " + secondDescription);
        System.out.println("Possible logos: " + secondLogoCount);

        Assert.assertNotNull(secondTitle);

        Assert.assertFalse(
                secondTitle.trim().isEmpty(),
                "Second program title is empty"
        );

        Assert.assertFalse(
                secondDescription.isEmpty(),
                "Second program description was not found"
        );

        System.out.println("\nSTEP 6: Compare program information");

        System.out.println(
                "Titles different: " +
                !firstTitle.equalsIgnoreCase(secondTitle)
        );

        System.out.println(
                "Descriptions different: " +
                !firstDescription.equalsIgnoreCase(secondDescription)
        );

        System.out.println("First program possible logos: " + firstLogoCount);
        System.out.println("Second program possible logos: " + secondLogoCount);

        Assert.assertNotEquals(
                firstUrl,
                secondUrl,
                "Both program URLs are identical"
        );

        Reporter.log(
                "TC_ODP_004: Two program details pages inspected and compared",
                true
        );
    }


    // TC_ODP_006: Verify Apply Now navigation
    @Test(groups = {"functional", "integration"})
    public void verifyApplyNowNavigation() {

        System.out.println("\n========== TC_ODP_006 ==========");

        openDoctorateLandingPage();

        System.out.println("STEP 3: Select a doctorate program");

        List<String> urls = getProgramUrls();

        Assert.assertFalse(
                urls.isEmpty(),
                "No doctorate program URLs found"
        );

        openProgramDetails(urls.get(0));

        System.out.println("STEP 4: Locate Apply Now button");

        WebElement applyButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        applyNowButton
                )
        );

        Assert.assertTrue(
                applyButton.isDisplayed(),
                "Apply Now button is not visible"
        );

        String oldUrl = driver.getCurrentUrl();

        System.out.println("Selected program: " + oldUrl);
        System.out.println("STEP 5: Click Apply Now");

        applyButton.click();

        pauseForDemo();

        System.out.println("STEP 6: Wait for destination or form");

        boolean destinationChanged = false;
        boolean formAppeared = false;

        try {
            wait.until(d -> !d.getCurrentUrl().equals(oldUrl));
            destinationChanged = true;

        } catch (Exception e) {
            System.out.println(
                    "URL did not change. Checking for a visible form."
            );
        }

        List<WebElement> forms =
                driver.findElements(By.cssSelector("form"));

        for (WebElement form : forms) {

            if (form.isDisplayed()) {
                formAppeared = true;
                System.out.println("Visible form found.");
                break;
            }
        }

        System.out.println(
                "Destination URL: " + driver.getCurrentUrl()
        );

        Assert.assertTrue(
                destinationChanged || formAppeared,
                "Apply Now did not open a new page or visible form"
        );

        System.out.println("STEP 7: Application destination verified");

        Reporter.log(
                "TC_ODP_006 PASSED: Apply Now destination or form detected",
                true
        );
    }
}
