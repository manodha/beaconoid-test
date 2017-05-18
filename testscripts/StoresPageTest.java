package com.company.testscripts;

import com.company.model.Beacons;
import com.company.model.Stores;
import com.company.pageobjects.BeaconsPage;
import com.company.pageobjects.NavigationMenu;
import com.company.pageobjects.StoresPage;
import com.company.util.Constants;
import org.testng.annotations.*;

import java.util.ArrayList;
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
    private BeaconsPage beaconsPage;
    private List<Stores> allStores;
    private List<Beacons> beacons;
    private List<Stores> testStores = new ArrayList();

    @BeforeTest(description = "Login in to the Beaconoid and accessing the stores page")
    @Parameters({"email", "password"})
    public void accessStoresPage(String email, String password) {
        navigationMenu = loginToBeaconoid(email, password);

        beaconsPage = navigationMenu.clickBeconsLink();
        assertEquals(Constants.beaconsUrl, webDriver.getCurrentUrl());
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        beacons = beaconsPage.getAllBeacons();

        storesPage = navigationMenu.clickStoresLink();
        assertEquals(Constants.storesUrl, webDriver.getCurrentUrl());
    }

    @Test(testName = "TC017", priority = 1)
    @Parameters({"storeName", "storeUniqueCode", "imgUrl"})
    public void createNewStoreTC017(String storeName, String storeUniqueCode, String imgUrl) {

        createStore(storesPage, new Stores(storeName, storeUniqueCode, imgUrl));
        allStores = storesPage.getAllStores();

        /*Checking if the Store has been successfully created by checking if the list of stores contains the newly
        created store.*/
        assertThat(allStores, hasItem(allOf(
                hasProperty("name", equalTo(storeName)),
                hasProperty("storeCode", equalTo(storeUniqueCode)
                ))));
    }

    @Test(testName = "TC022", priority = 2)
    @Parameters({"storeName", "storeUniqueCode", "storeNameNew", "storeUniqueCodeNew", "imgUrlNew"})
    public void updateStoreDetails(String storeName, String storeUniqueCode, String storeNameNew,
                                   String storeUniqueCodeNew, String imgUrlNew) {
        assertEquals(Constants.storesUrl, webDriver.getCurrentUrl());
        allStores = storesPage.getAllStores();
        Stores store = storesPage.getStore(allStores, storeName, storeUniqueCode);
        updateStore(store, new Stores(storeNameNew, storeUniqueCodeNew, imgUrlNew));

        allStores = storesPage.getAllStores();
        assertThat(allStores, hasItem(allOf(
                hasProperty("name", equalTo(storeNameNew)),
                hasProperty("storeCode", equalTo(storeUniqueCodeNew)
                ))));
    }

    @Test(testName = "TC019", priority = 3)
    @Parameters({"storeNameNew", "storeUniqueCodeNew"})
    public void deleteStoreTC019(String storeName, String storeUniqueCode) {
        //checking if the webdriver is in the Stores Page
        assertEquals(Constants.storesUrl, webDriver.getCurrentUrl());
        //checking if this store is not assigned to any beacons
        assertThat(beacons, not(hasItem(hasProperty("storeName", equalTo(storeName)))));
        //Deleting the Store
        allStores = storesPage.getAllStores();
        Stores store = storesPage.getStore(allStores, storeName, storeUniqueCode);
        deleteStore(storesPage, store);
        //checking if the store has been successfully deleted.
        allStores = storesPage.getAllStores();
        assertThat(allStores, not(hasItem(allOf(hasProperty("name", equalTo(storeName)),
                hasProperty("storeCode", equalTo(storeUniqueCode))))));

    }

    @Test(testName = "TC018", priority = 4)
    @Parameters({"storeName", "storeUniqueCode", "imgUrl"})
    public void createNewStoreTC018(String storeName, String storeUniqueCode, String imgUrl) {
        assertEquals(Constants.storesUrl, webDriver.getCurrentUrl());
        storesPage.clickNewStore();
        assertEquals(Constants.addStoreUrl, webDriver.getCurrentUrl());

        //Checking if a store can be created with only Store Unique Code and Image Url
        storesPage.createUpdateStore(new Stores("", storeUniqueCode, imgUrl));
        assertEquals(Constants.addStoreUrl, webDriver.getCurrentUrl());

        //Checking if a store can be created with only Image Url
        storesPage.createUpdateStore(new Stores("", "", imgUrl));
        assertEquals(Constants.addStoreUrl, webDriver.getCurrentUrl());

        //Checking if a store can be created with only Store Unique Code
        storesPage.createUpdateStore(new Stores("", storeUniqueCode, ""));
        assertEquals(Constants.addStoreUrl, webDriver.getCurrentUrl());

        //Checking if a store can be created with empty fields
        storesPage.createUpdateStore(new Stores("", storeUniqueCode, ""));
        assertEquals(Constants.addStoreUrl, webDriver.getCurrentUrl());
    }

    @BeforeGroups("AssignedStore")
    public void createDefaultStoreAndBeacon() {
        storesPage.createUpdateStore(Constants.defaultTestStore);
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.storesUrl, webDriver.getCurrentUrl());
        testStores.add(Constants.defaultTestStore);

        beaconsPage = accessBeaconsPage(navigationMenu);
        createBeacon(beaconsPage, Constants.defaultTestBeacon);

        storesPage = accessStoresPage(navigationMenu);
    }

    @Test(priority = 5, testName = "TC020", groups = "AssignedStore")
    public void deleteStoreTC020() {
        Stores store = storesPage.getStore(storesPage.getAllStores(), Constants.defaultTestStore.getName(),
                Constants.defaultTestStore.getStoreCode());

        deleteStore(storesPage, store);
        //checking if the store hasn't been deleted and still exists.

        assertEquals(store.getName() + Constants.cantDelStoreMsg, storesPage.getSucessAlert());
        assertThat(storesPage.getAllStores(), hasItem(allOf(
                hasProperty("name", equalTo(Constants.defaultTestStore.getName())),
                hasProperty("storeCode", equalTo(Constants.defaultTestStore.getStoreCode()))
        )));
    }

    @AfterGroups("AssignedStore")
    public void deleteDefaultBeacon() {
        beaconsPage = accessBeaconsPage(navigationMenu);
        deleteBeacon(beaconsPage, Constants.defaultTestBeacon);
        storesPage = accessStoresPage(navigationMenu);
    }


    @Test(testName = "TC021", priority = 6)
    @Parameters({"storeName", "storeUniqueCode", "imgUrl"})
    public void checkIfStoreCanBeCreWithRF(String storeName, String storeUniqueCode, String imgUrl) {
        Stores store = new Stores(storeName, storeUniqueCode, "");
        testStores.add(store);
        createStore(storesPage, store);

    }

    @AfterTest
    public void clearTestData() {
        if (!webDriver.getCurrentUrl().equals(Constants.storesUrl))
            webDriver.get(Constants.storesUrl);
        assertEquals(Constants.storesUrl, webDriver.getCurrentUrl());
        if (testStores != null) {
            for (Stores testStore : testStores) {
                allStores = storesPage.getAllStores();
                for (Stores store : allStores) {
                    if (testStore.getName().equals(store.getName()) && testStore.getStoreCode().equals(store.getStoreCode())) {
                        deleteStore(storesPage, store);
                    }
                }
            }
        }
    }

    private void updateStore(Stores oldStore, Stores newStore) {
        assertEquals(Constants.storesUrl, webDriver.getCurrentUrl());
        storesPage.clickEditButton(oldStore);
        assertEquals(oldStore.getEditLink().getAttribute("href"), webDriver.getCurrentUrl());
        storesPage.createUpdateStore(newStore);
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.storesUrl, webDriver.getCurrentUrl());
    }
}
