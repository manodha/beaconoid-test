package com.company;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


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

    @Test(testName = "TC017 - Check if a store can be created with valid input")
    @Parameters({"storeName", "storeUniqueCode"})
    public void addNewStoreTC017(String storeName, String storeUniqueCode) {
        storesPage.clickNewStore();
        assertEquals(addStoreUrl, webDriver.getCurrentUrl());
        storesPage.enterStoreName(storeName);
        storesPage.enterStoreUniqueId(storeUniqueCode);
        storesPage.clickCreateStore();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(storesUrl, webDriver.getCurrentUrl());
        List<Stores> stores = storesPage.getAllStores();

        //Checking if the Store has been successfully created by checking if the list of stores contains the newly created store.
        assertThat(stores, hasItem(allOf(hasProperty("name", equalTo(storeName)),
                hasProperty("storeCode", equalTo(storeUniqueCode)))));
    }
}
