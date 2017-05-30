package testscripts;

import model.Beacons;
import model.Stores;
import pageobjects.BeaconsPage;
import pageobjects.NavigationMenu;
import pageobjects.StoresPage;
import util.WebConstants;
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
        navigationMenu = loginToBeaconoid(webDriver, email, password);

        beaconsPage = accessBeaconsPage(webDriver, navigationMenu);
        beacons = beaconsPage.getOtherBeaconsList();

        storesPage = navigationMenu.clickStoresLink();
        assertEquals(WebConstants.storesUrl, webDriver.getCurrentUrl());
    }

    @Test(testName = "TC017", priority = 1)
    @Parameters({"storeName", "storeUniqueCode", "sales"})
    public void checkIfStoreCanBeCreaWAF(String storeName, String storeUniqueCode, String sales) {

        createStore(webDriver, storesPage, new Stores(storeName, storeUniqueCode, sales));
        allStores = storesPage.getAllStores();

        /*Checking if the Store has been successfully created by checking if the list of stores contains the newly
        created store.*/
        assertThat(allStores, hasItem(allOf(
                hasProperty("name", equalTo(storeName)),
                hasProperty("storeCode", equalTo(storeUniqueCode)
                ))));
    }

    @Test(testName = "TC022", priority = 2)
    @Parameters({"storeName", "storeUniqueCode", "storeNameNew", "storeUniqueCodeNew", "salesNew"})
    public void checkIfStoreCanBeUpda(String storeName, String storeUniqueCode, String storeNameNew,
                                      String storeUniqueCodeNew, String salesNew) {
        assertEquals(WebConstants.storesUrl, webDriver.getCurrentUrl());
        allStores = storesPage.getAllStores();
        Stores store = storesPage.getStore(allStores, storeName, storeUniqueCode);
        updateStore(webDriver, storesPage, store, new Stores(storeNameNew, storeUniqueCodeNew, salesNew));

        allStores = storesPage.getAllStores();
        assertThat(allStores, hasItem(allOf(
                hasProperty("name", equalTo(storeNameNew)),
                hasProperty("storeCode", equalTo(storeUniqueCodeNew)
                ))));
    }

    @Test(testName = "TC019", priority = 3)
    @Parameters({"storeNameNew", "storeUniqueCodeNew"})
    public void checkIfStoreWithNoBeaCanBeDel(String storeName, String storeUniqueCode) {
        //checking if the webdriver is in the Stores Page
        assertEquals(WebConstants.storesUrl, webDriver.getCurrentUrl());
        //checking if this store is not assigned to any beacons
        assertThat(beacons, not(hasItem(hasProperty("storeName", equalTo(storeName)))));
        //Deleting the Store
        allStores = storesPage.getAllStores();
        Stores store = storesPage.getStore(allStores, storeName, storeUniqueCode);
        deleteStore(webDriver, storesPage, store);
        //checking if the store has been successfully deleted.
        allStores = storesPage.getAllStores();
        assertThat(allStores, not(hasItem(allOf(hasProperty("name", equalTo(storeName)),
                hasProperty("storeCode", equalTo(storeUniqueCode))))));

    }

    @Test(testName = "TC018", priority = 4)
    @Parameters({"storeName", "storeUniqueCode"})
    public void checkIfStoreCanBeCreaWORF(String storeName, String storeUniqueCode) {
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

    @BeforeGroups("AssignedStore")
    public void createDefStoreAndBeacon() {
        storesPage.createUpdateStore(WebConstants.defaultTestStore);
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.storesUrl, webDriver.getCurrentUrl());
        testStores.add(WebConstants.defaultTestStore);

        beaconsPage = accessBeaconsPage(webDriver, navigationMenu);
        createBeacon(webDriver, beaconsPage, WebConstants.defaultTestBeacon);

        storesPage = accessStoresPage(webDriver, navigationMenu);
    }

    @Test(priority = 5, testName = "TC020", groups = "AssignedStore")
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
    public void deleteDefBeacon() {
        beaconsPage = accessBeaconsPage(webDriver, navigationMenu);
        deleteBeacon(webDriver, beaconsPage, WebConstants.defaultTestBeacon);
        storesPage = accessStoresPage(webDriver, navigationMenu);
    }


    @Test(testName = "TC021", priority = 6)
    @Parameters({"storeName", "storeUniqueCode"})
    public void checkIfStoreCanBeCreWRF(String storeName, String storeUniqueCode) {
        Stores store = new Stores(storeName, storeUniqueCode, "");
        testStores.add(store);
        createStore(webDriver, storesPage, store);

    }

    @AfterTest
    public void clearTestData() {
        if (!webDriver.getCurrentUrl().equals(WebConstants.storesUrl))
            storesPage = accessStoresPage(webDriver, navigationMenu);
        if (testStores != null) {
            for (Stores testStore : testStores) {
                allStores = storesPage.getAllStores();
                for (Stores store : allStores) {
                    if (testStore.getName().equals(store.getName()) && testStore.getStoreCode().equals(store.getStoreCode())) {
                        deleteStore(webDriver, storesPage, store);
                    }
                }
            }
        }
    }


}
