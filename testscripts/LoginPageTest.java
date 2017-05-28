package com.company.testscripts;


import com.company.pageobjects.LoginPage;
import com.company.pageobjects.NavigationMenu;
import com.company.util.WebConstants;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by manodha on 29/3/17.
 */
public class LoginPageTest extends FunctionalTest {

    private NavigationMenu navigationMenu;
    private LoginPage loginPage;

    @BeforeTest
    public void accessLoginPage() {
        webDriver.get(WebConstants.loginUrl);
        loginPage = new LoginPage(webDriver);
    }

    // TC.OO1
    @Parameters({"emailTC001", "passwordTC001"})
    @Test(testName = "TC001- Test if registered admin user is able to login successfully.", priority = 6)
    public void loginTC001(String email, String password) {
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        navigationMenu = loginPage.login();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals("Signed in successfully.", navigationMenu.getSucessAlert());
    }

    @Parameters({"emailTC002", "passwordTC002"})
    @Test(testName = "TC002 - Test if unregistered users is not able to login to the site", priority = 1)
    public void loginTC002(String email, String password) {
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.login();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.loginUrl, webDriver.getCurrentUrl());
    }

    // TC.OO3
    @Parameters({"emailTC003", "passwordTC003"})
    @Test(testName = "TC003 - Test if registered admin user is able to login successfully.", priority = 2)
    public void loginTC003(String email, String password) {
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.login();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.loginUrl, webDriver.getCurrentUrl());
    }

    // TC.OO4
    @Parameters({"emailTC004", "passwordTC004"})
    @Test(testName = "TC004 - Test with empty username  and valid password such that login must get failed", priority = 3)
    public void loginTC004(String email, String password) {
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.login();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.loginUrl, webDriver.getCurrentUrl());
    }

    // TC.OO5
    @Parameters({"emailTC005", "passwordTC005"})
    @Test(testName = "TC005 - Test with empty username and empty password and check if login fails", priority = 4)
    public void loginTC005(String email, String password) {
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.login();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.loginUrl, webDriver.getCurrentUrl());
    }

    // TC.OO6
    @Parameters("passwordTC006")
    @Test(testName = "TC006 - Check if the password is masked on the screen i.e., password must be in bullets or asterisks", priority = 5)
    public void loginTC006(String password) {
        loginPage.enterPassword(password);
        assertTrue(loginPage.isPassword());
    }

    // TC.OO7
    @Test(testName = "TC007- Check on selecting back button (after logging out) if the user is not signed in", priority = 7)
    public void loginTC007() {
        assertEquals(WebConstants.baseUrl, webDriver.getCurrentUrl());
        navigationMenu.clickLogoutLink();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds * 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.baseUrl, webDriver.getCurrentUrl());
        assertEquals(WebConstants.loginTitle, loginPage.getTitleText());
        /*webDriver.navigate().back();
        assertEquals(loginUrl, webDriver.getCurrentUrl());*/
    }


}
