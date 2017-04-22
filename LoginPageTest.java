package com.company;


import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by manodha on 29/3/17.
 */
public class LoginPageTest extends FunctionalTest {

    // TC.OO1
    @Parameters({"emailTC001", "passwordTC001"})
    @Test(testName = "TC001- Test if registered admin user is able to login successfully.", priority = 6)
    public void loginTC001(String email, String password) {
        webDriver.get(loginUrl);
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        NavigationMenu navigationMenu = loginPage.login();

        assertEquals("Signed in successfully.", navigationMenu.getSuccessAlertText());
    }

    @Parameters({"emailTC002", "passwordTC002"})
    @Test(testName = "TC002 - Test if unregistered users is not able to login to the site", priority = 1)
    public void loginTC002(String email, String password) {
        webDriver.get(loginUrl);
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.login();
        assertEquals(loginUrl, webDriver.getCurrentUrl());
    }

    // TC.OO3
    @Parameters({"emailTC003", "passwordTC003"})
    @Test(testName = "TC003 - Test if registered admin user is able to login successfully.", priority = 2)
    public void loginTC003(String email, String password) {
        webDriver.get(loginUrl);
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.login();
        assertEquals(loginUrl, webDriver.getCurrentUrl());
    }

    // TC.OO4
    @Parameters({"emailTC004", "passwordTC004"})
    @Test(testName = "TC004 - Test with empty username  and valid password such that login must get failed", priority = 3)
    public void loginTC004(String email, String password) {
        webDriver.get(loginUrl);
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.login();
        assertEquals(loginUrl, webDriver.getCurrentUrl());
    }

    // TC.OO5
    @Parameters({"emailTC005", "passwordTC005"})
    @Test(testName = "TC005 - Test with empty username and empty password and check if login fails", priority = 4)
    public void loginTC005(String email, String password) {
        webDriver.get(loginUrl);
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.login();
        assertEquals(loginUrl, webDriver.getCurrentUrl());
    }

    // TC.OO6
    @Parameters("passwordTC006")
    @Test(testName = "TC006 - Check if the password is masked on the screen i.e., password must be in bullets or asterisks", priority = 5)
    public void loginTC006(String password) {
        webDriver.get(loginUrl);
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.enterPassword(password);
        assertTrue(loginPage.isPassword());
    }

    // TC.OO7
    @Test(testName = "TC007- Check on selecting back button (after logging out) if the user is not signed in", priority = 7)
    public void loginTC007() {
        assertEquals(baseUrl, webDriver.getCurrentUrl());
        NavigationMenu navigationMenu = new NavigationMenu(webDriver);
        navigationMenu.clickLogoutLink();
        assertEquals(loginUrl, webDriver.getCurrentUrl());

    }


}
