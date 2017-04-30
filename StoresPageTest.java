package com.company;

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
            Thread.sleep(2000);
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
        createStore(storeName, storeUniqueCode, imgUrl);
        assertEquals(storesUrl, webDriver.getCurrentUrl());
        allStores = storesPage.getAllStores();

        //Checking if the Store has been successfully created by checking if the list of stores contains the newly created store.
        assertThat(allStores, hasItem(allOf(hasProperty("name", equalTo(storeName)),
                hasProperty("storeCode", equalTo(storeUniqueCode)))));
    }

    //@Test(testName = "TC018 - Verify that a store will not be created with required fields empty", priority = 3)
    @Parameters({"storeName", "storeUniqueCode", "imgUrl"})
    public void createNewStoreTC018(String storeName, String storeUniqueCode, String imgUrl) {
        assertEquals(storesUrl, webDriver.getCurrentUrl());
        storesPage.clickNewStore();
        assertEquals(addStoreUrl, webDriver.getCurrentUrl());

        //Checking if a store can be created with only Store Unique Code and Image Url
        storesPage.createStore("", storeUniqueCode, imgUrl);
        assertEquals(addStoreUrl, webDriver.getCurrentUrl());

        //Checking if a store can be created with only Image Url
        storesPage.createStore("", "", imgUrl);
        assertEquals(addStoreUrl, webDriver.getCurrentUrl());

        //Checking if a store can be created with only Store Unique Code
        storesPage.createStore("", storeUniqueCode, "");
        assertEquals(addStoreUrl, webDriver.getCurrentUrl());

        //Checking if a store can be created with empty fields
        storesPage.createStore("", storeUniqueCode, "");
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
        deleteStore(allStores, storeName, storeUniqueCode);

        try {
            Thread.sleep(2000);
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
        deleteStore(allStores, assginedStore, "");

        try {
            Thread.sleep(2000);
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
        String store_name = storeName + "_TC021_1";
        testStores.add(new Stores(store_name, "", ""));
        createStore(store_name, "", "");

        store_name = storeName + "_TC021_2";
        testStores.add(new Stores(store_name, storeUniqueCode + "_TC021_2", ""));
        createStore(store_name, storeUniqueCode + "_TC021_2", "");

        store_name = storeName + "_TC021_3";
        testStores.add(new Stores(store_name, "", imgUrl + "_TC021_3"));
        createStore(store_name, "", imgUrl + "_TC021_3");

        store_name = storeName + "_TC021_4";
        testStores.add(new Stores(store_name, storeUniqueCode + "_TC021_4", imgUrl + "_TC021_4"));
        createStore(store_name, storeUniqueCode + "_TC021_4", imgUrl + "_TC021_4");

    }

    @Test(testName = "TC022 - Check if a user can edit the details of an existing Store", priority = 2)
    @Parameters({"storeName", "storeUniqueCode", "storeNameNew", "storeUniqueCodeNew", "imgUrlNew"})
    public void updateStoreDetails(String storeName, String storeUniqueCode, String storeNameNew,
                                   String storeUniqueCodeNew, String imgUrlNew) {
        assertEquals(storesUrl, webDriver.getCurrentUrl());
        allStores = storesPage.getAllStores();
        Stores store = getStore(allStores, storeName, storeUniqueCode);
        updateStore(store, storeNameNew, storeUniqueCodeNew, imgUrlNew);

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
                //storesPage.deleteStore(allStores, testStore.getName(), testStore.getStoreCode());
                deleteStore(allStores, testStore.getName(), testStore.getStoreCode());
            }
        }
    }

    private void createStore(String storeName, String storeUniqueCode, String imgUrl) {
        assertEquals(storesUrl, webDriver.getCurrentUrl());
        storesPage.clickNewStore();
        assertEquals(addStoreUrl, webDriver.getCurrentUrl());
        storesPage.createStore(storeName, storeUniqueCode, imgUrl);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(storesUrl, webDriver.getCurrentUrl());
    }

    private void updateStore(Stores store, String storeName, String storeUniqueCode, String imgUrl) {
        storesPage.clickEditButton(store);
        assertEquals(store.getEditLink().getAttribute("href"), webDriver.getCurrentUrl());
        storesPage.enterStoreName(storeName);
        storesPage.enterStoreUniqueId(storeUniqueCode);
        storesPage.enterImgUrl(imgUrl);
        storesPage.clickCreateUpdateStore();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(storesUrl, webDriver.getCurrentUrl());
    }

    private void deleteStore(List<Stores> stores, String storeName, String storeUniqueCode) {
        Stores store = getStore(stores, storeName, storeUniqueCode);
        storesPage.clickDeleteButton(store.getDeleteBtn());
    }

    private Stores getStore(List<Stores> stores, String storeName, String storeUniqueCode) {
        if (stores.size() == 0)
            return null;
        for (Stores store : stores) {
            if (store.getName().equals(storeName) && store.getStoreCode().equals(storeUniqueCode)) {
                return store;
            }
        }
        return null;
    }

}
