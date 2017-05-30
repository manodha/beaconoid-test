package testscripts;

import model.*;
import pageobjects.*;
import util.WebConstants;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.testng.Assert.*;

/**
 * Created by manodha on 17/5/17.
 */
public class BeaconManagerRoleTest extends FunctionalTest {

    private LoginPage loginPage;
    private NavigationMenu navigationMenu;
    private DashboardPage dashboardPage;
    private StaffPage staffPage;
    private StoresPage storesPage;
    private CategoryPage categoryPage;
    private BeaconsPage beaconsPage;
    private AdvertisementsPage advertisementsPage;

    /* Beacon Manager */

    @BeforeTest
    @Parameters({"email", "password", "name4", "email4", "nickname4", "password4", "confirmPassword4", "beacon_manager_role"})
    public void setUpTestData(String loginEmail, String loginPassword, String name, String email, String nickname,
                              String password, String confirmPassword, String role) {
        navigationMenu = loginToBeaconoid(webDriver, loginEmail, loginPassword);

        // Creating the default test store that is required to check the CRUD permission of Beacon Manager on StoresPage
        storesPage = accessStoresPage(webDriver, navigationMenu);
        createStore(webDriver, storesPage, WebConstants.defaultTestStore);

        // Creating the default test category that is required to check the CRUD permission of Beacon Manager on StoresPage
        categoryPage = accessCategoriesPage(webDriver, navigationMenu);
        createCategory(webDriver, categoryPage, WebConstants.defaultTestCategory);

        //Creating the user with the role BeaconManager
        staffPage = accessStaffPage(navigationMenu);
        createStaff(staffPage, new Staff(name, email, nickname, password, confirmPassword, role));

        // Logging out the Super Admin
        loginPage = navigationMenu.clickLogoutLink();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Logging in the Beacon Manager
        navigationMenu = loginToBeaconoid(webDriver, email, password);
    }

    @Test(priority = 1, testName = "TC073", groups = "BeaconManager")
    public void checkIfBMCanAcessStaffPage() {
        assertEquals(WebConstants.baseUrl, webDriver.getCurrentUrl());
        assertNull(navigationMenu.getStaffLink());
    }

    @Test(priority = 2, testName = "TC074", groups = "BeaconManager")
    public void checkIfBMCanAcessReports() {
        assertEquals(WebConstants.baseUrl, webDriver.getCurrentUrl());
        assertNull(navigationMenu.getReportLink());
    }

    /* Beacon Manager - Stores Page */

    @Test(priority = 3, testName = "TC075", groups = "BeaconManager")
    public void checkIfBMCanAccessStoresPage() {
        storesPage = accessStoresPage(webDriver, navigationMenu);
        assertEquals(WebConstants.storesUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 4, testName = "TC076", groups = "BeaconManager")
    public void checkIfBMCanSeeAListOfStores() {
        assertEquals(WebConstants.storesUrl, webDriver.getCurrentUrl());
        assertNotNull(storesPage.getAllStores());
    }

    @Test(priority = 5, testName = "TC077", groups = "BeaconManager")
    public void checkIfBMCanCreaNewStore() {
        storesPage.clickNewStore();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds * 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.storesUrl, webDriver.getCurrentUrl());
        assertEquals(WebConstants.notAuthorisedMsg, storesPage.getDangerAlert());
    }

    @Test(priority = 6, testName = "TC078", groups = "BeaconManager")
    public void checkIfBMCanUpdateStore() {
        Stores store = storesPage.getStore(storesPage.getAllStores(), WebConstants.defaultTestStore.getName(),
                WebConstants.defaultTestStore.getStoreCode());
        storesPage.clickEditButton(store);
        try {
            Thread.sleep(WebConstants.waitMilliSeconds * 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.storesUrl, webDriver.getCurrentUrl());
        assertEquals(WebConstants.notAuthorisedMsg, storesPage.getDangerAlert());
    }

    @Test(priority = 7, testName = "TC079", groups = "BeaconManager")
    public void checkIfBMCanDeleteStore() {
        Stores store = storesPage.getStore(storesPage.getAllStores(), WebConstants.defaultTestStore.getName(),
                WebConstants.defaultTestStore.getStoreCode());
        deleteStore(webDriver, storesPage, store);
        assertEquals(WebConstants.storesUrl, webDriver.getCurrentUrl());
        assertEquals(WebConstants.notAuthorisedMsg, storesPage.getDangerAlert());
        assertThat(storesPage.getAllStores(), hasItem(allOf(
                hasProperty("name", equalTo(WebConstants.defaultTestStore.getName())),
                hasProperty("storeCode", equalTo(WebConstants.defaultTestStore.getStoreCode())
                ))));
    }

    /* Beacon Manager - Category Page */

    @Test(priority = 8, testName = "TC080", groups = "BeaconManager")
    public void checkIfBMCanAccessCategoryPage() {
        categoryPage = accessCategoriesPage(webDriver, navigationMenu);
        assertEquals(WebConstants.categoriesUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 9, testName = "TC081", groups = "BeaconManager")
    public void checkIfBMCanSeeAListOfCategories() {
        assertEquals(WebConstants.categoriesUrl, webDriver.getCurrentUrl());
        assertNotNull(categoryPage.getAllCategories());
    }

    @Test(priority = 10, testName = "TC082", groups = "BeaconManager")
    public void checkIfBMCanCreaNewCategory() {
        categoryPage.clickNewCategoryBtn();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds * 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.categoriesUrl, webDriver.getCurrentUrl());
        assertEquals(WebConstants.notAuthorisedMsg, categoryPage.getDangerAlert());
    }

    @Test(priority = 11, testName = "TC083", groups = "BeaconManager")
    public void checkIfBMCanUpdateCategory() {
        Category category = categoryPage.getCategory(categoryPage.getAllCategories(), WebConstants.defaultTestCategory.getCategoryName(),
                WebConstants.defaultTestCategory.getCategoryDescription());
        categoryPage.clickEditCategoryBtn(category.getEditButton());
        try {
            Thread.sleep(WebConstants.waitMilliSeconds * 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.categoriesUrl, webDriver.getCurrentUrl());
        assertEquals(WebConstants.notAuthorisedMsg, categoryPage.getDangerAlert());
    }

    @Test(priority = 12, testName = "TC084", groups = "BeaconManager")
    public void checkIfBMCanDeleteCategory() {
        Category category = categoryPage.getCategory(categoryPage.getAllCategories(), WebConstants.defaultTestCategory.getCategoryName(),
                WebConstants.defaultTestCategory.getCategoryDescription());
        deleteCategory(webDriver, categoryPage, category);

        assertEquals(WebConstants.categoriesUrl, webDriver.getCurrentUrl());
        assertEquals(WebConstants.notAuthorisedMsg, categoryPage.getDangerAlert());
        assertThat(categoryPage.getAllCategories(), hasItem(allOf(
                hasProperty("categoryName", equalTo(WebConstants.defaultTestCategory.getCategoryName())),
                hasProperty("categoryDescription", equalTo(WebConstants.defaultTestCategory.getCategoryDescription())
                ))));
    }

    /* Beacon Manager - Beacons Page */

    @Test(priority = 13, testName = "TC085", groups = "BeaconManager")
    public void checkIfBMCanAccessBeaconsPage() {
        beaconsPage = accessBeaconsPage(webDriver, navigationMenu);
        assertEquals(WebConstants.beaconsUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 14, testName = "TC086", groups = "BeaconManager")
    @Parameters({"uniqueRef1", "beaconName1", "currentStatus1", "latitude1", "longitude1"})
    public void checkIfBMCanCreaNewBeacon(String uniqueRef, String beaconName, String status, String latitude,
                                          String longitude) {
        createBeacon(webDriver, beaconsPage, new Beacons(uniqueRef, beaconName, WebConstants.defaultTestStore.getName(), status,
                latitude, longitude));
        assertThat(beaconsPage.getOtherBeaconsList(), hasItem(allOf(
                hasProperty("uniqueRef", equalTo(uniqueRef)),
                hasProperty("name", equalTo(beaconName)
                ))));
    }

    @Test(priority = 15, testName = "TC087", groups = "BeaconManager")
    public void checkIfBMCanSeeAListOfBeacons() {
        assertEquals(WebConstants.beaconsUrl, webDriver.getCurrentUrl());
        assertNotNull(beaconsPage.getOtherBeaconsList());
    }

    @Test(priority = 16, testName = "TC088", groups = "BeaconManager")
    @Parameters({"uniqueRef1", "beaconName1"})
    public void checkIfBMCanUpdateBeacon(String uniqueRef, String beaconName) {
        Beacons beacon = beaconsPage.getBeacon(beaconsPage.getOtherBeaconsList(), uniqueRef, beaconName);
        updateBeacon(webDriver, beaconsPage, beacon, WebConstants.defaultTestBeacon);

        assertThat(beaconsPage.getOtherBeaconsList(), hasItem(allOf(
                hasProperty("uniqueRef", equalTo(WebConstants.defaultTestBeacon.getUniqueRef())),
                hasProperty("name", equalTo(WebConstants.defaultTestBeacon.getName()))
        )));

        assertThat(beaconsPage.getOtherBeaconsList(), not(hasItem(allOf(
                hasProperty("uniqueRef", equalTo(uniqueRef)),
                hasProperty("name", equalTo(beaconName))
        ))));
    }

    @Test(priority = 22, testName = "TC089", groups = "BeaconManager")
    public void checkIfBMCanDeleteBeacon() {
        if (!webDriver.getCurrentUrl().equals(WebConstants.beaconsUrl))
            beaconsPage = accessBeaconsPage(webDriver, navigationMenu);
        Beacons beacon = beaconsPage.getBeacon(beaconsPage.getOtherBeaconsList(), WebConstants.defaultTestBeacon.getUniqueRef(),
                WebConstants.defaultTestBeacon.getName());
        deleteBeacon(webDriver, beaconsPage, beacon);

        assertThat(beaconsPage.getOtherBeaconsList(), not(hasItem(allOf(
                hasProperty("uniqueRef", equalTo(WebConstants.defaultTestBeacon.getUniqueRef())),
                hasProperty("name", equalTo(WebConstants.defaultTestBeacon.getName())
                )))));
    }

    /* Beacon Manager - Advertisements Page */

    @Test(priority = 17, testName = "TC090", groups = "BeaconManager")
    public void checkIfBMCanAccessAdvertisementPage() {
        advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);
        assertEquals(WebConstants.advertisementsUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 18, testName = "TC091", groups = "BeaconManager")
    public void checkIfBMCanCreaNewAdvertisement() {
        createAdvertisement(webDriver, advertisementsPage, WebConstants.defaultTestAdvertisement);

        if (!webDriver.getCurrentUrl().equals(WebConstants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);

        assertThat(advertisementsPage.getAllAdvertisements(), hasItem(allOf(
                hasProperty("name", equalTo(WebConstants.defaultTestAdvertisement.getName())),
                hasProperty("beacon", equalTo(WebConstants.defaultTestBeacon.getName()))
        )));
    }

    @Test(priority = 19, testName = "TC092", groups = "BeaconManager")
    public void checkIfBMCanSeeAListOfAdvertisement() {
        assertEquals(WebConstants.advertisementsUrl, webDriver.getCurrentUrl());
        assertNotNull(advertisementsPage.getAllAdvertisements());
    }

    @Test(priority = 20, testName = "TC093", groups = "BeaconManager")
    @Parameters({"adverName", "adverDescription", "adverImage", "adverPrice"})
    public void checkIfBMCanUpdateAdvertisement(String name, String description, String image, String price) {
        Advertisement advertisement = advertisementsPage.getAdvertisment(advertisementsPage.getAllAdvertisements(),
                WebConstants.defaultTestAdvertisement.getName(), WebConstants.defaultTestBeacon.getName());

        updateAdvertisement(webDriver, advertisementsPage, advertisement, new Advertisement(name, WebConstants.defaultTestBeacon.getName(),
                WebConstants.defaultTestCategory.getCategoryName(), description, image, price));

        if (!webDriver.getCurrentUrl().equals(WebConstants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);

        List<Advertisement> allAdvertisements = advertisementsPage.getAllAdvertisements();

        // Asserting that there are no advertisements with the old name details
        assertThat(allAdvertisements, not(hasItem(allOf(
                hasProperty("name", equalTo(WebConstants.defaultTestAdvertisement.getName())),
                hasProperty("beacon", equalTo(WebConstants.defaultTestBeacon.getName()))
        ))));

        // Asserting that the details are updated in the old Advertisement
        assertThat(allAdvertisements, hasItem(allOf(
                hasProperty("name", equalTo(name)),
                hasProperty("beacon", equalTo(WebConstants.defaultTestBeacon.getName())),
                hasProperty("category", equalTo(WebConstants.defaultTestCategory.getCategoryName()))
        )));
    }

    @Test(priority = 21, testName = "TC094", groups = "BeaconManager")
    @Parameters({"adverName"})
    public void checkIfBMCanDeleteAdvertisement(String name) {
        Advertisement advertisement = advertisementsPage.getAdvertisment(advertisementsPage.getAllAdvertisements(), name, WebConstants.defaultTestBeacon.getName());
        deleteAdvertisement(webDriver, advertisementsPage, advertisement);

        assertThat(advertisementsPage.getAllAdvertisements(), not(hasItem(allOf(
                hasProperty("name", equalTo(name)),
                hasProperty("beacon", equalTo(WebConstants.defaultTestBeacon.getName()))
        ))));
    }

    @Test(priority = 23, testName = "TC095", groups = "BeaconManager")
    public void checkIfBMCanAdcessDashboard() {
        dashboardPage = accessDashboardPage(navigationMenu);
        assertEquals(WebConstants.dashboardUrl, webDriver.getCurrentUrl());
    }

    @AfterTest
    @Parameters({"email", "password", "beacon_manager_role"})
    public void clearTestData(String email, String password, String role) {
        loginPage = navigationMenu.clickLogoutLink();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        navigationMenu = loginToBeaconoid(webDriver, email, password);

        categoryPage = accessCategoriesPage(webDriver, navigationMenu);
        deleteCategory(webDriver, categoryPage, WebConstants.defaultTestCategory);

        storesPage = accessStoresPage(webDriver, navigationMenu);
        deleteStore(webDriver, storesPage, WebConstants.defaultTestStore);

        staffPage = accessStaffPage(navigationMenu);
        deleteStaff(staffPage, staffPage.getStaffByRole(role));
    }

}
