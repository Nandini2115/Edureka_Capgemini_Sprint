
package com.edureka.AllCourses;

import java.util.List;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import baseclass.BaseClass;
import objRepo.AllCoursesPage;

public class AllCoursesTestScript1 extends BaseClass {

    @Test
    public void verifyAllCoursesPageLoads() {
        System.out.println("\n TC_AC_01 START ");
        AllCoursesPage page = new AllCoursesPage(driver, wait);
        page.openAllCourses();
        System.out.println("Current URL: " + page.getCurrentUrl());
        System.out.println("Page title: " + page.getPageTitle());

        Assert.assertTrue(page.getCurrentUrl().contains("/all-courses"),"All Courses page URL did not load");
        Assert.assertTrue(page.getPageTitle().toLowerCase().contains("course"),"All Courses page title is incorrect");
        Reporter.log("Passed: TC_AC_01", true);
        System.out.println(" TC_AC_01 PASSED \n");
    }

    @Test
    public void verifyAllCoursesCatalogue() {

        System.out.println("\n TC_AC_04 START ");

        AllCoursesPage page = new AllCoursesPage(driver, wait);

        System.out.println("STEP 1: Navigate through the homepage");
        page.navigateToAllCoursesByClick();
        System.out.println("Current URL: " + page.getCurrentUrl());
        System.out.println("Page title: " + page.getPageTitle());
        Assert.assertTrue(page.getCurrentUrl().contains("/all-courses"), "All Courses page did not open");
        System.out.println("STEP 2: Verify page title");

        Assert.assertTrue(page.getPageTitle().toLowerCase().contains("course"),"Page title does not contain 'course'");
        System.out.println("STEP 3: Find visible course headings");
        
        List<String> headings = page.getVisibleHeadings();
        for (String heading : headings) {
            System.out.println("VISIBLE HEADING: " + heading);
        }

        System.out.println("Total visible headings: " + headings.size());

        Assert.assertFalse(headings.isEmpty(), "No visible headings found on All Courses page");

        Reporter.log("Passed: TC_AC_04", true);
        System.out.println(" TC_AC_04 PASSED \n");
    }

    @Test
    public void verifyAiCategoryIsAvailable() {

        System.out.println("\n========== TC_AC_05 START ==========");

        AllCoursesPage page = new AllCoursesPage(driver, wait);

        System.out.println("STEP 1: Open All Courses page");
        page.openAllCourses();

        System.out.println("STEP 2: Check AI category");

        Assert.assertTrue(
            page.isAiCategoryVisible(),
            "Artificial Intelligence category is not visible"
        );

        System.out.println("STEP 3: Click AI category");
        page.selectAiCategory();

        String finalUrl = page.getCurrentUrl();
        System.out.println("Final URL: " + finalUrl);

        Assert.assertTrue(
            finalUrl.contains("/all-courses?cat="),
            "AI category selection did not update the catalogue URL"
        );

        Reporter.log("Passed: TC_AC_05", true);
        System.out.println("========== TC_AC_05 PASSED ==========\n");
    }
}
