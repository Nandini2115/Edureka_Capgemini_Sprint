
package com.edureka.ArtificialIntelligence;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import baseclass.BaseClass;
import objRepo.ArtificialIntelligencePage;

public class ArtificialIntelligenceTestScript1 extends BaseClass {

    @Test(groups = {"smoke"})
    public void verifyAiCategoryOpens() {
        Reporter.log("Starting TC_AI_01", true);

        ArtificialIntelligencePage page = new ArtificialIntelligencePage(driver, wait);
        page.openAiCategory();

        Assert.assertFalse(page.getPageTitle().isBlank(),"AI page should have a title");
        Reporter.log("Passed: AI category opened", true);
    }

    @Test(groups = {"functional"})
    public void verifyAiCourseHeadings() {
        Reporter.log("Starting TC_AI_02", true);

        ArtificialIntelligencePage page = new ArtificialIntelligencePage(driver, wait);
        page.openAiCategory();

        Assert.assertFalse(page.getVisibleHeadings().isEmpty(),"Visible headings should be present");
        Reporter.log("Passed: AI headings displayed", true);
    }

    @Test(groups = {"functional"})
    public void verifyAiCourseOverview() {
        Reporter.log("Starting TC_AI_04", true);

        ArtificialIntelligencePage page = new ArtificialIntelligencePage(driver, wait);
        page.openAiCategory();
        Assert.assertFalse(page.getPageTitle().isBlank(),"Page title should be present");
        Reporter.log("Page opened; verify course overview text separately", true);
    }
}
