package com.company;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by manodha on 23/4/17.
 */
public class StoresPageTest extends FunctionalTest {

    private NavigationMenu navigationMenu;
    private StoresPage storesPage;

    @BeforeTest(description = "Login in to the Beaconoid and acessing the stores page")
    @Parameters({"email", "password"})
    public void accessStoresPage(String email, String password) {
        navigationMenu = loginToBeaconoid(email, password);
        storesPage = navigationMenu.clickStoresLink();
        assertEquals(storesUrl, webDriver.getCurrentUrl());
    }

    @Test
    @Parameters
    public void addNewStoreTC017() {
        storesPage.getAllStores();
        storesPage.clickNewStore();

    }
}
