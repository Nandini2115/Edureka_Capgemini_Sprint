
package com.edureka.AllCourses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import baseclass.BaseClass;
import objRepo.AllCoursesPage;

public class AllCoursesTestScript2 extends BaseClass {

    // TC_AC_14: Integration - Verify AI category navigation
    @Test(groups = {"integration"})
    public void verifyAiCategoryNavigation() {

        System.out.println("TC_AC_14: AI category navigation started");

        AllCoursesPage page = new AllCoursesPage(driver, wait);

        page.openAllCourses();

        Assert.assertTrue(
                page.getCurrentUrl().contains("/all-courses"),
                "All Courses page did not open"
        );

        System.out.println("Selecting AI category");

        page.selectAiCategory();

        String currentUrl = page.getCurrentUrl();

        System.out.println("Current URL: " + currentUrl);

        Assert.assertTrue(
                currentUrl.contains("cat=14"),
                "AI category navigation failed"
        );

        Reporter.log(
                "TC_AC_14 PASSED: AI category navigation verified",
                true
        );
    }


    // TC_AC_30: System - Verify page title and URL
    @Test(groups = {"system"})
    public void verifyPageTitleAcrossBrowserRun() {

        System.out.println("TC_AC_30: Page title verification started");

        AllCoursesPage page = new AllCoursesPage(driver, wait);

        page.openAllCourses();

        String title = page.getPageTitle();
        String currentUrl = page.getCurrentUrl();

        System.out.println("Page title: " + title);
        System.out.println("Current URL: " + currentUrl);

        Assert.assertNotNull(
                title,
                "Page title is null"
        );

        Assert.assertFalse(
                title.trim().isEmpty(),
                "Page title is empty"
        );

        Assert.assertTrue(
                currentUrl.contains("/all-courses"),
                "Incorrect All Courses URL"
        );

        Reporter.log(
                "TC_AC_30 PASSED: Page title and URL verified",
                true
        );
    }


    // TC_AC_25: Negative - Verify invalid category navigation
    @Test(groups = {"negative"})
    public void verifyInvalidCategoryNavigation() {

        System.out.println(
                "TC_AC_25: Invalid category navigation started"
        );

        String invalidUrl =
                "https://www.edureka.co/all-courses?cat=999999";

        driver.get(invalidUrl);

        System.out.println(
                "Opened URL: " + driver.getCurrentUrl()
        );

        System.out.println(
                "Page title: " + driver.getTitle()
        );

        // Wait for the document body to be available.
        wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions
                        .presenceOfElementLocated(By.tagName("body"))
        );

        WebElement body = driver.findElement(By.tagName("body"));

        Assert.assertTrue(
                body.isDisplayed(),
                "Page body is not displayed"
        );

        String currentUrl = driver.getCurrentUrl();
        String pageTitle = driver.getTitle();

        System.out.println("Final URL: " + currentUrl);
        System.out.println("Final title: " + pageTitle);

        // Basic response check only. Confirm the application's
        // actual invalid-category behavior before declaring this
        // negative test fully passed.
        Assert.assertFalse(
                pageTitle == null || pageTitle.trim().isEmpty(),
                "Page title is empty after invalid category navigation"
        );

        Reporter.log(
                "TC_AC_25: Invalid category URL loaded; " +
                "verify expected invalid-category behavior",
                true
        );
    }
}
