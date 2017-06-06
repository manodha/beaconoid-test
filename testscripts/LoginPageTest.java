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

    @Test(priority = 1, testName = "TC_LP_01", description = "Verify that unregistered users can not to login to the Beaconoid")
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


    @Test(priority = 2, testName = "TC_LP_02", description = "Test with valid username  and empty password such that login must get failed")
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


    @Test(priority = 3, testName = "TC_LP_03", description = "Test with empty username  and valid password such that login must get failed")
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

    @Test(priority = 4, testName = "TC_LP_04", description = "Test with empty username and empty password and check if login fails")
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

    @Test(priority = 5, testName = "TC_LP_05", description = "Check if the password is masked on the screen i.e., password must be in bullets or asterisks")
    @Parameters("passwordTC006")
    public void chkIfPasswIsMasked(String password) {
        loginPage.enterPassword(password);
        assertTrue(loginPage.isPassword());
    }


    @Parameters({"emailTC001", "passwordTC001"})
    @Test(priority = 6, testName = "TC_LP_06", description = "Verify that registered user can successfully login to the Beaconoid with correct email and password.")
    public void chkIfRegUserCanLogin(String email, String password) {
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

    @Test(priority = 7, testName = "TC_LP_07", description = "Check if on selecting back button (after logging out) if the user is not signed in")
    public void chkIfOnBackLogin() {
        assertEquals(WebConstants.baseUrl, webDriver.getCurrentUrl());
        navigationMenu.clickLogoutLink();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds * 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.baseUrl, webDriver.getCurrentUrl());
        assertTrue(loginPage.isLoginPage());

        /*webDriver.navigate().back();
        assertEquals(loginUrl, webDriver.getCurrentUrl());*/
    }


}
