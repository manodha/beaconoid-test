package testscripts;


import pageobjects.LoginPage;
import pageobjects.NavigationMenu;
import util.WebConstants;
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

    @Test(priority = 1, testName = "TC_LP_01")
    @Parameters({"emailTC002", "passwordTC002"})
    public void chckIFCanLoginUnreUser(String email, String password) {
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


    @Test(priority = 2, testName = "TC_LP_02")
    @Parameters({"emailTC003", "passwordTC003"})
    public void chckIFCanLoginWithVUEP(String email, String password) {
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


    @Test(priority = 3, testName = "TC_LP_03")
    @Parameters({"emailTC004", "passwordTC004"})
    public void chckIFCanLoginWithEUVP(String email, String password) {
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

    @Test(priority = 4, testName = "TC_LP_04")
    @Parameters({"emailTC005", "passwordTC005"})
    public void chckIFCanLoginWithEUEP(String email, String password) {
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

    @Test(priority = 5, testName = "TC_LP_05")
    @Parameters("passwordTC006")
    public void chkIfPasswIsMasked(String password) {
        loginPage.enterPassword(password);
        assertTrue(loginPage.isPassword());
    }


    @Parameters({"emailTC001", "passwordTC001"})
    @Test(priority = 6, testName = "TC_LP_06")
    public void chkIfRegiUserCanLogin(String email, String password) {
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

    @Test(priority = 7, testName = "TC_LP_07")
    public void chkIfOnBackLogin() {
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
