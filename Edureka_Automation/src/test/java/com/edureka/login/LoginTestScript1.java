
package com.edureka.login;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import baseclass.BaseClass;
import objRepo.LoginPage;

public class LoginTestScript1 extends BaseClass {

    @Test(groups = {"smoke"})
    public void verifyLoginPopupOpens() {
        Reporter.log("Starting TC_LOGIN_01", true);

        LoginPage login = new LoginPage(driver, wait);
        login.openLoginPopup();

        Assert.assertTrue(
            login.isLoginPopupVisible(),
            "Login popup should be visible");

        Reporter.log(
            "Passed: TC_LOGIN_01 - Login popup opened", true);
    }

    @Test(groups = {"functional"})
    public void verifyLoginWithValidCredentials() {
        Reporter.log("Starting TC_LOGIN_02", true);

        String email = getProperty("validEmail");
        String password = getProperty("validPassword");

        Assert.assertFalse(email.isBlank(),
            "Set validEmail in CommonData.properties first");
        Assert.assertFalse(password.isBlank(),
            "Set validPassword in CommonData.properties first");

        LoginPage login = new LoginPage(driver, wait);
        login.openLoginPopup();
        login.enterEmail(email);
        login.enterPassword(password);
        login.clickLogin();

        // Replace with a verified post-login assertion.
        Assert.assertFalse(
            login.getCurrentUrl().isBlank(),
            "Browser URL should be available after login");

        Reporter.log(
            "Login action completed; verify the authenticated state",
            true);
    }

    @Test(groups = {"functional"})
    public void verifyPasswordFieldIsAvailable() {
        Reporter.log("Starting TC_LOGIN_08", true);

        LoginPage login = new LoginPage(driver, wait);
        login.openLoginPopup();

        Assert.assertTrue(
            login.isPasswordFieldVisible(),
            "Password field should be visible");

        Reporter.log(
            "Passed: Password field is visible", true);
    }
}
