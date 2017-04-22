package com.company;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.junit.Assert.assertEquals;


/**
 * Created by manodha on 9/4/17.
 */
public class NavigationMenuTest extends FunctionalTest {

    private NavigationMenu navigationMenu;
    private LoginPage loginPage;

    @Test(testName = "TC008", priority = 1)
    @Parameters({"email", "password"})
    public void isLoggedIn(String email, String password) {
        webDriver.get(loginUrl);
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        navigationMenu = loginPage.login();
        assertEquals("Signed in successfully.", navigationMenu.getSuccessAlertText());
    }

    @Test(testName = "TC009", priority = 2)
    public void clickBeaconsWebConcsole() {
        assertEquals(baseUrl, webDriver.getCurrentUrl());
        navigationMenu.clickBeconsWebConsole();
        assertEquals(dashboardUrl, webDriver.getCurrentUrl());

    }

    @Test(testName = "TC010", priority = 3)
    public void clickDashboard() {
        navigationMenu.clickDashboardLink();
        assertEquals(dashboardUrl, webDriver.getCurrentUrl());
    }

    @Test(testName = "TC011", priority = 4)
    public void clickStores() {
        navigationMenu.clickStoresLink();
        assertEquals(storesUrl, webDriver.getCurrentUrl());
    }

    @Test(testName = "TC012", priority = 5)
    public void clickCatogories() {
        navigationMenu.clickCatogoriesLink();
        assertEquals(catogoriesUrl, webDriver.getCurrentUrl());
    }

    @Test(testName = "TC013", priority = 6)
    public void clickBecons() {
        navigationMenu.clickBeconsLink();
        assertEquals(beaconsUrl, webDriver.getCurrentUrl());
    }

    @Test(testName = "TC014", priority = 7)
    public void clickAdvertisements() {
        navigationMenu.clickAdvertisementsLink();
        assertEquals(advertisementsUrl, webDriver.getCurrentUrl());
    }

    @Test(testName = "TC015", priority = 8)
    public void clickLogout() {
        loginPage = navigationMenu.clickLogoutLink();
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(loginUrl, webDriver.getCurrentUrl());
    }


    /*@Test(testName = "TC016")
    public void clickSettings(){
        navigationMenu.clickSettingsLink();
        assertEquals(storesUrl,webDriver.getCurrentUrl());
    }*/


}
