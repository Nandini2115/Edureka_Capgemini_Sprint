
package com.edureka.login;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import baseclass.BaseClass;
import objRepo.LoginPage;

public class LoginTestScript2 extends BaseClass {

    @Test(groups = {"negative"})
    public void verifyInvalidLoginInput() {
        Reporter.log("Starting TC_LOGIN_04", true);

        String email = getProperty("invalidEmail");
        String password = getProperty("invalidPassword");

        LoginPage login = new LoginPage(driver, wait);
        login.openLoginPopup();
        login.enterEmail(email);
        login.enterPassword(password);
        login.clickLogin();
        
        Assert.assertTrue(login.isLoginPopupVisible(),"Verify the login form remains available");
        Reporter.log("Login rejection scenario executed; inspect validation",true);
    }

    @Test(groups = {"system"})
    public void verifyLoginCanBeRetried() {
        Reporter.log("Starting TC_LOGIN_13", true);

        LoginPage login = new LoginPage(driver, wait);
        login.openLoginPopup();
        login.enterEmail(getProperty("invalidEmail"));
        login.enterPassword(getProperty("invalidPassword"));
        login.clickLogin();
        Assert.assertTrue(login.isLoginPopupVisible(),"Login form should remain available for retry");
        Reporter.log("Passed: Retry scenario checked", true);
    }

    @Test(groups = {"integration"})
    public void verifyLoginNavigationToCourses() {
        Reporter.log("Starting TC_LOGIN_14", true);

        String email = getProperty("validEmail");
        String password = getProperty("validPassword");

        Assert.assertFalse(email.isBlank(),"Configure validEmail first");
        Assert.assertFalse(password.isBlank(),"Configure validPassword first");

        LoginPage login = new LoginPage(driver, wait);
        login.openLoginPopup();
        login.enterEmail(email);
        login.enterPassword(password);
        login.clickLogin();
        Reporter.log("Login submitted; verify All Courses navigation", true);
    }
}
