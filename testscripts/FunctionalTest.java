package com.company.testscripts;

import com.company.view.LoginPage;
import com.company.view.NavigationMenu;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * Created by manodha on 27/3/17.
 */
public class FunctionalTest {

    protected static WebDriver webDriver;
    protected static String baseUrl = "https://www.beaconoid.me/";
    protected static String loginUrl = baseUrl + "users/sign_in";
    protected static String dashboardUrl = baseUrl + "dashboard";
    protected static String storesUrl = baseUrl + "stores";
    protected static String addStoreUrl = baseUrl + "stores/new";
    protected static String categoriesUrl = baseUrl + "categories";
    protected static String addCategoryUrl = baseUrl + "categories/new";
    protected static String beaconsUrl = baseUrl + "beacons";
    protected static String addBeaconUrl = baseUrl + "beacons/new";
    protected static String advertisementsUrl = baseUrl + "advertisements";
    protected int waitMilliSeconds = 1500;

  /*  protected static String settingsUrl = baseUrl + ""*/

    @BeforeSuite
    public static void setUp() {
        System.setProperty("webdriver.gecko.driver", "/Users/manodha/Selenium Web Driver/geckodriver");
        webDriver = new FirefoxDriver();
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    /*@AfterSuite
    public static void tearDown() {
        webDriver.manage().deleteAllCookies();
        webDriver.close();
        webDriver.quit();
    }*/

    @AfterMethod
    public void printResult(ITestResult result) {
        System.out.print(result.getTestName());
        try {
            if (result.getStatus() == ITestResult.SUCCESS) {

                //Do something here
                System.out.println(" - Passed");
            } else if (result.getStatus() == ITestResult.FAILURE) {
                //Do something here
                System.out.println(" - Failed");

            } else if (result.getStatus() == ITestResult.SKIP) {

                System.out.println(" - Skiped");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NavigationMenu loginToBeaconoid(String email, String password) {
        webDriver.get(loginUrl);
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        NavigationMenu navigationMenu = loginPage.login();
        assertEquals("Signed in successfully.", navigationMenu.getSuccessAlertText());
        return navigationMenu;
    }
}
