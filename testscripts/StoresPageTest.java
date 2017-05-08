package com.company.testscripts;

import com.company.model.Beacons;
import com.company.model.Stores;
import com.company.pageobjects.BeaconsPage;
import com.company.pageobjects.NavigationMenu;
import com.company.pageobjects.StoresPage;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;


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
        assertEquals(beaconsUrl, webDriver.getCurrentUrl());
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        beacons = beaconsPage.getAllBeacons();

        storesPage = navigationMenu.clickStoresLink();
        assertEquals(storesUrl, webDriver.getCurrentUrl());
    }

    @Test(testName = "TC017 - Check if a store can be created with all the fields filled", priority = 1)
    @Parameters({"storeName", "storeUniqueCode", "imgUrl"})
    public void createNewStoreTC017(String storeName, String storeUniqueCode, String imgUrl) {

        createStore(storesPage, new Stores(storeName, storeUniqueCode, imgUrl));
        allStores = storesPage.getAllStores();

        //Checking if the Store has been successfully created by checking if the list of stores contains the newly created store.
        assertThat(allStores, hasItem(allOf(hasProperty("name", equalTo(storeName)),
                hasProperty("storeCode", equalTo(storeUniqueCode)))));
    }

    @Test(testName = "TC018 - Verify that a store will not be created with required fields empty", priority = 3)
    @Parameters({"storeName", "storeUniqueCode", "imgUrl"})
    public void createNewStoreTC018(String storeName, String storeUniqueCode, String imgUrl) {
        assertEquals(storesUrl, webDriver.getCurrentUrl());
        storesPage.clickNewStore();
        assertEquals(addStoreUrl, webDriver.getCurrentUrl());

        //Checking if a store can be created with only Store Unique Code and Image Url
        storesPage.createUpdateStore(new Stores("", storeUniqueCode, imgUrl));
        assertEquals(addStoreUrl, webDriver.getCurrentUrl());

        //Checking if a store can be created with only Image Url
        storesPage.createUpdateStore(new Stores("", "", imgUrl));
        assertEquals(addStoreUrl, webDriver.getCurrentUrl());

        //Checking if a store can be created with only Store Unique Code
        storesPage.createUpdateStore(new Stores("", storeUniqueCode, ""));
        assertEquals(addStoreUrl, webDriver.getCurrentUrl());

        //Checking if a store can be created with empty fields
        storesPage.createUpdateStore(new Stores("", storeUniqueCode, ""));
        assertEquals(addStoreUrl, webDriver.getCurrentUrl());
    }


    @Test(testName = "TC019 - Check if user can successfully delete a store if there are no beacons assigned to that store",
            priority = 3)
    @Parameters({"storeNameNew", "storeUniqueCodeNew"})
    public void deleteStoreTC019(String storeName, String storeUniqueCode) {
        //checking if the webdriver is in the Stores Page
        assertEquals(storesUrl, webDriver.getCurrentUrl());
        //checking if this store is not assigned to any beacons
        assertThat(beacons, not(hasItem(hasProperty("storeName", equalTo(storeName)))));
        //Deleting the Store
        allStores = storesPage.getAllStores();
        Stores store = storesPage.getStore(allStores, storeName, storeUniqueCode);
        storesPage.clickDeleteButton(store.getDeleteBtn());
        ;

        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //checking if the store has been successfully deleted.
        allStores = storesPage.getAllStores();
        assertThat(allStores, not(hasItem(allOf(hasProperty("name", equalTo(storeName)),
                hasProperty("storeCode", equalTo(storeUniqueCode))))));

    }

    //@Test(testName = "TC020 - Check whether user is prompted with proper error message when user try to delete a store that has becaons assigned to it.")
    public void deleteStoreTC020() {
        String assginedStore = beacons.get(0).getStoreName();
        System.out.println(assginedStore);
        allStores = storesPage.getAllStores();
        /*deleteStore(allStores, assginedStore, "");*/

        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //checking if the store hasn't been deleted and still exists.
        allStores = storesPage.getAllStores();
        assertThat(allStores, hasItem(allOf(hasProperty("name", equalTo(assginedStore)))));
    }


    @Test(testName = "TC021 - Check if a store can be created with only required fields filled", priority = 4)
    @Parameters({"storeName", "storeUniqueCode", "imgUrl"})
    public void createNewStoreTC021(String storeName, String storeUniqueCode, String imgUrl) {
        webDriver.get(storesUrl);
        assertNotEquals("", storeName);
        assertNotEquals("", storeUniqueCode);
        assertNotEquals("", imgUrl);
        Stores store = new Stores(storeName, storeUniqueCode, "");
        testStores.add(store);
        createStore(storesPage, store);

    }

    @Test(testName = "TC022 - Check if a user can edit the details of an existing Store", priority = 2)
    @Parameters({"storeName", "storeUniqueCode", "storeNameNew", "storeUniqueCodeNew", "imgUrlNew"})
    public void updateStoreDetails(String storeName, String storeUniqueCode, String storeNameNew,
                                   String storeUniqueCodeNew, String imgUrlNew) {
        assertEquals(storesUrl, webDriver.getCurrentUrl());
        allStores = storesPage.getAllStores();
        Stores store = storesPage.getStore(allStores, storeName, storeUniqueCode);
        updateStore(store, new Stores(storeNameNew, storeUniqueCodeNew, imgUrlNew));

        allStores = storesPage.getAllStores();
        assertThat(allStores, hasItem(allOf(hasProperty("name", equalTo(storeNameNew)),
                hasProperty("storeCode", equalTo(storeUniqueCodeNew)))));
    }

    @AfterTest
    public void clearTestData() {
        if (!webDriver.getCurrentUrl().equals(storesUrl))
            webDriver.get(storesUrl);
        assertEquals(storesUrl, webDriver.getCurrentUrl());
        if (testStores != null) {
            for (Stores testStore : testStores) {
                allStores = storesPage.getAllStores();
                for (Stores store : allStores) {
                    if (testStore.getName().equals(store.getName()) && testStore.getStoreCode().equals(store.getStoreCode())) {
                        storesPage.clickDeleteButton(store.getDeleteBtn());
                    }
                }
            }
        }
    }

    private void updateStore(Stores oldStore, Stores newStore) {
        assertEquals(storesUrl, webDriver.getCurrentUrl());
        storesPage.clickEditButton(oldStore);
        assertEquals(oldStore.getEditLink().getAttribute("href"), webDriver.getCurrentUrl());
        storesPage.createUpdateStore(newStore);
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(storesUrl, webDriver.getCurrentUrl());
    }
}
