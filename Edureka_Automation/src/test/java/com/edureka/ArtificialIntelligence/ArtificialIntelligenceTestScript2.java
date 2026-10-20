package com.edureka.ArtificialIntelligence;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import baseclass.BaseClass;
import objRepo.ArtificialIntelligencePage;

public class ArtificialIntelligenceTestScript2 extends BaseClass {

    @Test(groups = {"functional"})
    public void verifyCourseDetailsNavigation() {

        System.out.println("\n========== TC_AI_04 START ==========");

        ArtificialIntelligencePage aiPage =
                new ArtificialIntelligencePage(driver, wait);

        System.out.println("STEP 1: Open AI category");
        aiPage.openAiCategory();

        System.out.println("STEP 2: Open the selected AI course");
        aiPage.openCourseByTitle(
                "Post Graduate Program in Generative AI and Agentic AI");

        System.out.println("STEP 3: Verify course details page");

        Assert.assertTrue(
                driver.getCurrentUrl().contains("executive-programs"),
                "TC_AI_04 FAILED: Course details page did not open.");

        Assert.assertTrue(
                driver.getTitle().toLowerCase().contains("agentic ai"),
                "TC_AI_04 FAILED: Unexpected course details page title.");

        System.out.println("TC_AI_04 PASSED: Course details navigation verified.");

        Reporter.log(
                "TC_AI_04 PASSED: Course details navigation verified.",
                true);
    }

    @Test(groups = {"negative"})
    public void verifyRequiredFieldValidation() {

        System.out.println("\n========== TC_AI_09 START ==========");

        ArtificialIntelligencePage aiPage =
                new ArtificialIntelligencePage(driver, wait);

        System.out.println("STEP 1: Open AI category");
        aiPage.openAiCategory();

        System.out.println("STEP 2: Open the selected AI course");
        aiPage.openCourseByTitle(
                "Post Graduate Program in Generative AI and Agentic AI");

        System.out.println("STEP 3: Open the enquiry widget");
        aiPage.openQueryWidget();

        System.out.println("STEP 4: Submit the enquiry form with empty fields");
        aiPage.submitEmptyQuery();

        System.out.println("STEP 5: Check required-field validation");

        boolean validationDisplayed =
                aiPage.hasQueryValidationMessages();

        Assert.assertTrue(
                validationDisplayed,
                "TC_AI_09 FAILED: No required-field validation messages "
                + "were detected after submitting the empty enquiry form.");

        System.out.println(
                "TC_AI_09 PASSED: Required-field validation detected.");

        Reporter.log(
                "TC_AI_09 PASSED: Required-field validation detected.",
                true);
    }
}
