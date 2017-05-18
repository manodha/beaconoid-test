package com.company.testscripts;

import com.company.pageobjects.LoginPage;
import com.company.pageobjects.NavigationMenu;
import com.company.util.Constants;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * Created by manodha on 9/4/17.
 */
public class NavigationMenuTest extends FunctionalTest {

    private NavigationMenu navigationMenu;


    @Test(testName = "TC008", priority = 1)
    @Parameters({"email", "password"})
    public void isDashboard(String email, String password) {
        navigationMenu = loginToBeaconoid(email, password);
        assertEquals(Constants.baseUrl, webDriver.getCurrentUrl());
        assertNotNull(navigationMenu.getLogoutLink());

    }

    @Test(testName = "TC010", priority = 2)
    public void clickBeaconoid() {
        navigationMenu.clickBeconsWebConsole();
        assertEquals(Constants.dashboardUrl, webDriver.getCurrentUrl());

    }

    @Test(testName = "TC011", priority = 3)
    public void clickDashboard() {
        navigationMenu.clickDashboardLink();
        assertEquals(Constants.dashboardUrl, webDriver.getCurrentUrl());
    }

    @Test(testName = "TC012", priority = 4)
    public void clickStores() {
        navigationMenu.clickStoresLink();
        assertEquals(Constants.storesUrl, webDriver.getCurrentUrl());
    }

    @Test(testName = "TC013", priority = 5)
    public void clickCatogories() {
        navigationMenu.clickCatogoriesLink();
        assertEquals(Constants.categoriesUrl, webDriver.getCurrentUrl());
    }

    @Test(testName = "TC014", priority = 6)
    public void clickBeacons() {
        navigationMenu.clickBeconsLink();
        assertEquals(Constants.beaconsUrl, webDriver.getCurrentUrl());
    }

    @Test(testName = "TC015", priority = 7)
    public void clickAdvertisements() {
        navigationMenu.clickAdvertisementsLink();
        assertEquals(Constants.advertisementsUrl, webDriver.getCurrentUrl());
    }

    @Test(testName = "TC016", priority = 8)
    public void clickStaff() {
        navigationMenu.clickStaffLink();
        assertEquals(Constants.staffUrl, webDriver.getCurrentUrl());
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(testName = "TC009", priority = 9)
    public void clickLogout() {
        LoginPage loginPage = navigationMenu.clickLogoutLink();
        try {
            Thread.sleep(Constants.waitMilliSeconds * 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.baseUrl, webDriver.getCurrentUrl());
        assertEquals(Constants.loginTitle, loginPage.getTitleText());
    }
}
