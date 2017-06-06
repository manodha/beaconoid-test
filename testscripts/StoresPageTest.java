package testscripts;

import model.Beacons;
import model.Stores;
import org.testng.annotations.*;
import pageobjects.BeaconsPage;
import pageobjects.NavigationMenu;
import pageobjects.StoresPage;
import util.WebConstants;

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
    public void setupTestData(String email, String password) throws InterruptedException {
        navigationMenu = loginToBeaconoid(webDriver, email, password);

        beaconsPage = accessBeaconsPage(webDriver, navigationMenu);
        beacons = beaconsPage.getRegOtherBeacons(WebConstants.otherBeaconTitle);
    }

    @Test(priority = 1, testName = "TC_SP_01", description = "Verify that user can see the title Stores when the Store Page is accessed from the Navigation Menu")
    public void chkIfUserCanAccessSP(){
        // Accessing the stores Page
        storesPage = accessStoresPage(webDriver, navigationMenu);
        assertEquals(WebConstants.storeIndexTitle, storesPage.getIndexTitle());
    }

    @Test(priority = 2, testName = "TC_SP_02", description = "Verify that the message 'No store found.'  is visible in the Store Table if there are no stores created.")
    public void chkIfNoStoreMsgIsShown(){
        assertEquals(WebConstants.storesUrl, webDriver.getCurrentUrl());
        // Verifying that there are no stores
        assertNull(storesPage.getAllStores());
        // Verifying if the message 'No store found.' is visible
        assertEquals(storesPage.getNoStoreTxt(), WebConstants.noStoreTxt);
    }

    @Test(priority = 3, testName = "TC_SP_03", description = "Verify that user can not create a store if the required fields are not filled")
    @Parameters({"storeName", "storeUniqueCode"})
    public void chkIfStoreCanBeCreaWORF(String storeName, String storeUniqueCode) {
        assertEquals(WebConstants.storesUrl, webDriver.getCurrentUrl());
        storesPage.clickNewStore();
        assertEquals(WebConstants.addStoreUrl, webDriver.getCurrentUrl());

        storesPage.createUpdateStore(new Stores("", "", ""));
        assertEquals(WebConstants.addStoreUrl, webDriver.getCurrentUrl());

        //Checking if a store can be created with only Image Url
        storesPage.createUpdateStore(new Stores(storeName, "", ""));
        assertEquals(WebConstants.addStoreUrl, webDriver.getCurrentUrl());

        storesPage.createUpdateStore(new Stores("", storeUniqueCode, ""));
        assertEquals(WebConstants.addStoreUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 4, testName = "TC_SP_04", description = "Verify that user can create a store if all the required fields are filled")
    @Parameters({"storeName", "storeUniqueCode"})
    public void checkIfStoreCanBeCreaWRF(String storeName, String storeUniqueCode) {
        assertEquals(WebConstants.addStoreUrl, webDriver.getCurrentUrl());
        storesPage.createUpdateStore(new Stores(storeName, storeUniqueCode, ""));
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.storesUrl, webDriver.getCurrentUrl());

        // Checking if the proper message is shown to the user
        assertEquals(WebConstants.creaStoreSucess, storesPage.getSucessAlert());

        // Checking if the Store has been successfully created.
        assertThat(storesPage.getAllStores(), hasItem(allOf(
                hasProperty("name", equalTo(storeName)),
                hasProperty("storeCode", equalTo(storeUniqueCode)
                ))));
    }

    @Test(priority = 5, testName = "TC_SP_05", description = "Verify that user can not create a store with the same name")
    @Parameters({"storeName", "storeUniqueCode"})
    public void chkIfStoreCanBeCreaWSN(String name, String code){
        assertEquals(WebConstants.storesUrl, webDriver.getCurrentUrl());
        storesPage.clickNewStore();
        assertEquals(WebConstants.addStoreUrl, webDriver.getCurrentUrl());

        storesPage.createUpdateStore(new Stores(name, code, ""));

        // checking if unsuccessfull message is shown to user
        assertEquals(WebConstants.sameStoreName, storesPage.getDangerAlert());
        assertEquals(WebConstants.addStoreUrl, webDriver.getCurrentUrl());

        // Accesing the stores page
        if(!webDriver.getCurrentUrl().equals(WebConstants.storesUrl))
            storesPage = accessStoresPage(webDriver, navigationMenu);

        allStores = storesPage.getAllStores();
        int numOfStoreWSN = 0;
        for(Stores store : allStores){
            if(store.getName().equals(name))
                numOfStoreWSN++;
        }
        // Verifying that there is only one store with the same name
        assertEquals(1, numOfStoreWSN);
    }

    @Test(priority = 6, testName = "TC_SP_06", description = "Verify that the the created store in the Test Case TC_SP_04 will be in the Stores Table")
    @Parameters({"storeName", "storeUniqueCode"})
    public void chkIfStoreIsInStoreTable(String name, String code){
        // Verifying that there is at least one Store in the Store Table
        assertNotNull(storesPage.getAllStores());
        // Checking if the Store is in the Store table.
        assertThat(storesPage.getAllStores(), hasItem(allOf(
                hasProperty("name", equalTo(name)),
                hasProperty("storeCode", equalTo(code)
                ))));

    }

    @Test(priority = 7, testName = "TC_SP_07", description = "Verify that user can not update a store if the required fields are not filled")
    @Parameters({"storeName", "storeUniqueCode", "storeNameNew", "storeUniqueCodeNew"})
    public void chkIfStoreCanBeUpdaWORF(String oldName, String oldCode, String name, String code){
        assertEquals(WebConstants.storesUrl, webDriver.getCurrentUrl());
        Stores store = storesPage.getStore(storesPage.getAllStores(), oldName, oldCode);
        String editUrl = store.getEditLink().getAttribute("href");

        storesPage.clickEditButton(store);
        assertEquals(editUrl, webDriver.getCurrentUrl());

        // Verifying tha a store can not be created without the required fields
        storesPage.createUpdateStore(new Stores("", "", ""));
        assertEquals(editUrl, webDriver.getCurrentUrl());

        storesPage.createUpdateStore(new Stores(name, "", ""));
        assertEquals(editUrl, webDriver.getCurrentUrl());

        storesPage.createUpdateStore(new Stores("", code, ""));
        assertEquals(editUrl, webDriver.getCurrentUrl());
    }


    @Test(priority = 8, testName = "TC_SP_08", description = "Verify that user can update a store successfully if the required fields are filled")
    @Parameters({"storeName", "storeUniqueCode", "storeNameNew", "storeUniqueCodeNew", "salesNew"})
    public void chkIfStoreCanBeUpda(String storeName, String storeUniqueCode, String storeNameNew,
                                      String storeUniqueCodeNew, String salesNew) {
        if(!webDriver.getCurrentUrl().equals(WebConstants.storesUrl))
            storesPage = accessStoresPage(webDriver, navigationMenu);

        Stores store = storesPage.getStore(storesPage.getAllStores(), storeName, storeUniqueCode);

        updateStore(webDriver, storesPage, store, new Stores(storeNameNew, storeUniqueCodeNew, salesNew));

        assertEquals(storeNameNew+WebConstants.updaStoreSucess, storesPage.getSucessAlert());
        assertThat(storesPage.getAllStores(), hasItem(allOf(
                hasProperty("name", equalTo(storeNameNew)),
                hasProperty("storeCode", equalTo(storeUniqueCodeNew)
                ))));
    }

    @BeforeGroups("UpdateStoreWSN")
    public void creaDefStore(){
        createStore(webDriver, storesPage, WebConstants.defaultTestStore);
    }

    @Test(priority = 9, testName = "TC_SP_09", groups = "UpdateStoreWSN", description = "Verify that user can not update a store with an existing store name")
    @Parameters({"storeNameNew", "storeUniqueCodeNew"})
    public void checkIfStoreCanBeUpdaWSN(String name, String code) throws InterruptedException {
        Stores store = storesPage.getStore(storesPage.getAllStores(), name, code);
        String editLink = store.getEditLink().getAttribute("href");
        storesPage.clickEditButton(store);
        assertEquals(editLink, webDriver.getCurrentUrl());

        storesPage.createUpdateStore(WebConstants.defaultTestStore);
        Thread.sleep(WebConstants.waitMilliSeconds);
        assertEquals(editLink, webDriver.getCurrentUrl());
        assertEquals(WebConstants.sameStoreName, storesPage.getDangerAlert());

        if(!webDriver.getCurrentUrl().equals(WebConstants.storesUrl))
            storesPage = accessStoresPage(webDriver, navigationMenu);

        allStores = storesPage.getAllStores();
        int numOfStoreWSN = 0;
        for(Stores stre : allStores){
            if(stre.getName().equals(name))
                numOfStoreWSN++;
        }
        // Verifying that there is only one store with the same name
        assertEquals(1, numOfStoreWSN);

    }

    @Test(priority = 10, testName = "TC_SP_10", description = "Verify that store can be deleted successfully if the store is not assigned to an beacon")
    @Parameters({"storeNameNew", "storeUniqueCodeNew"})
    public void chkIfStoreWithNoBeaCanBeDel(String storeName, String storeUniqueCode) {
        if(!webDriver.getCurrentUrl().equals(WebConstants.storesUrl))
            storesPage = accessStoresPage(webDriver, navigationMenu);
        //checking if the webdriver is in the Stores Page
        assertEquals(WebConstants.storesUrl, webDriver.getCurrentUrl());

        //checking if this store is not assigned to any beacons
        assertThat(beacons, not(hasItem(hasProperty("storeName", equalTo(storeName)))));

        //Deleting the Store
        Stores store = storesPage.getStore(storesPage.getAllStores(), storeName, storeUniqueCode);
        deleteStore(webDriver, storesPage, store);

        assertEquals(storeName+WebConstants.delStoreSucess, storesPage.getSucessAlert());
        //checking if the store has been successfully deleted.
        assertThat(storesPage.getAllStores(), not(hasItem(allOf(hasProperty("name", equalTo(storeName)),
                hasProperty("storeCode", equalTo(storeUniqueCode))))));

    }

    @BeforeGroups("AssignedStore")
    public void createDefBeacon() throws InterruptedException {
        beaconsPage = accessBeaconsPage(webDriver, navigationMenu);
        createBeacon(webDriver, beaconsPage, WebConstants.defaultTestBeacon);

        storesPage = accessStoresPage(webDriver, navigationMenu);
    }

    @Test(priority = 11, testName = "TC_SP_11", groups = "AssignedStore", description = "Verify that store can not be deleted if the store is assigned to an beacon and the user is prompted with an error message")
    public void checkIfStoreWithBeaconCanBeDel() {
        Stores store = storesPage.getStore(storesPage.getAllStores(), WebConstants.defaultTestStore.getName(),
                WebConstants.defaultTestStore.getStoreCode());
        deleteStore(webDriver, storesPage, store);

        // Checking the error message
        assertEquals(store.getName() + WebConstants.delStoreError, storesPage.getDangerAlert());

        //Checking if the store still exists in the Stores Page
        assertThat(storesPage.getAllStores(), hasItem(allOf(
                hasProperty("name", equalTo(WebConstants.defaultTestStore.getName())),
                hasProperty("storeCode", equalTo(WebConstants.defaultTestStore.getStoreCode()))
        )));
    }

    @AfterGroups("AssignedStore")
    public void deleteDefBeacon() throws InterruptedException {
        beaconsPage = accessBeaconsPage(webDriver, navigationMenu);
        deleteBeacon(webDriver, beaconsPage, WebConstants.defaultTestBeacon);
    }

    /* TODO Implement the test cases TC_SP_11, TC_SP_12, TC_SP_13 */

    @AfterTest
    public void clearTestData() {
        if(!webDriver.getCurrentUrl().equals(WebConstants.storesUrl))
            storesPage = accessStoresPage(webDriver, navigationMenu);
        allStores = storesPage.getAllStores();
        for(Stores store : allStores){
            deleteStore(webDriver, storesPage, store);
        }
    }


}
