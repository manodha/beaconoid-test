package com.company.controller;

import com.company.view.NavigationMenu;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.junit.Assert.assertEquals;


/**
 * Created by manodha on 9/4/17.
 */
public class NavigationMenuTest extends FunctionalTest {

    private NavigationMenu navigationMenu;

    @BeforeTest
    @Parameters({"email", "password"})
    public void accessNavigationMenu(String email, String password) {
        navigationMenu = loginToBeaconoid(email, password);
    }

    @Test(testName = "TC008", priority = 1)
    public void isDashboard() {
        assertEquals(baseUrl, webDriver.getCurrentUrl());
    }

    @Test(testName = "TC009", priority = 2)
    public void clickBeaconoid() {
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
        assertEquals(categoriesUrl, webDriver.getCurrentUrl());
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
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(testName = "TC015", priority = 8)
    public void clickLogout() {
        navigationMenu.clickLogoutLink();
        try {
            Thread.sleep(waitMilliSeconds * 2);
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
