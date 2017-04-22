package com.company;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;

import java.util.concurrent.TimeUnit;

/**
 * Created by manodha on 27/3/17.
 */
public class FunctionalTest {

    protected static WebDriver webDriver;
    protected static String baseUrl = "http://52.36.35.170:3000/";
    protected static String loginUrl = baseUrl + "users/sign_in";
    protected static String dashboardUrl = baseUrl + "dashboard";
    protected static String storesUrl = baseUrl + "stores";
    protected static String catogoriesUrl = baseUrl + "categories";
    protected static String beaconsUrl = baseUrl + "beacons";
    protected static String advertisementsUrl = baseUrl + "advertisements";
  /*  protected static String settingsUrl = baseUrl + ""*/

    @BeforeSuite
    public static void setUp() {
        System.setProperty("webdriver.gecko.driver", "/Users/manodha/Selenium Web Driver/geckodriver");
        webDriver = new FirefoxDriver();
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

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

    /*@AfterSuite
    public static void tearDown(){
        webDriver.manage().deleteAllCookies();
        webDriver.close();
        webDriver.quit();
    }*/

}
