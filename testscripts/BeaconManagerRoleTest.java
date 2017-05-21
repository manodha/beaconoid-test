package com.company.testscripts;

import com.company.model.*;
import com.company.pageobjects.*;
import com.company.util.Constants;
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
        navigationMenu = loginToBeaconoid(loginEmail, loginPassword);

        // Creating the default test store that is required to check the CRUD permission of Beacon Manager on StoresPage
        storesPage = accessStoresPage(navigationMenu);
        createStore(storesPage, Constants.defaultTestStore);

        // Creating the default test category that is required to check the CRUD permission of Beacon Manager on StoresPage
        categoryPage = accessCategoriesPage(navigationMenu);
        createCategory(categoryPage, Constants.defaultTestCategory);

        //Creating the user with the role BeaconManager
        staffPage = accessStaffPage(navigationMenu);
        createStaff(staffPage, new Staff(name, email, nickname, password, confirmPassword, role));

        // Logging out the Super Admin
        loginPage = navigationMenu.clickLogoutLink();
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Logging in the Beacon Manager
        navigationMenu = loginToBeaconoid(email, password);
    }

    @Test(priority = 1, testName = "TC073", groups = "BeaconManager")
    public void checkIfBMCanAcessStaffPage() {
        assertEquals(Constants.baseUrl, webDriver.getCurrentUrl());
        assertNull(navigationMenu.getStaffLink());
    }

    @Test(priority = 2, testName = "TC074", groups = "BeaconManager")
    public void checkIfBMCanAcessReports() {
        assertEquals(Constants.baseUrl, webDriver.getCurrentUrl());
        assertNull(navigationMenu.getReportLink());
    }

    /* Beacon Manager - Stores Page */

    @Test(priority = 3, testName = "TC075", groups = "BeaconManager")
    public void checkIfBMCanAccessStoresPage() {
        storesPage = accessStoresPage(navigationMenu);
        assertEquals(Constants.storesUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 4, testName = "TC076", groups = "BeaconManager")
    public void checkIfBMCanSeeAListOfStores() {
        assertEquals(Constants.storesUrl, webDriver.getCurrentUrl());
        assertNotNull(storesPage.getAllStores());
    }

    @Test(priority = 5, testName = "TC077", groups = "BeaconManager")
    public void checkIfBMCanCreaNewStore() {
        storesPage.clickNewStore();
        try {
            Thread.sleep(Constants.waitMilliSeconds * 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.storesUrl, webDriver.getCurrentUrl());
        assertEquals(Constants.notAuthorisedMsg, storesPage.getDangerAlert());
    }

    @Test(priority = 6, testName = "TC078", groups = "BeaconManager")
    public void checkIfBMCanUpdateStore() {
        Stores store = storesPage.getStore(storesPage.getAllStores(), Constants.defaultTestStore.getName(),
                Constants.defaultTestStore.getStoreCode());
        storesPage.clickEditButton(store);
        try {
            Thread.sleep(Constants.waitMilliSeconds * 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.storesUrl, webDriver.getCurrentUrl());
        assertEquals(Constants.notAuthorisedMsg, storesPage.getDangerAlert());
    }

    @Test(priority = 7, testName = "TC079", groups = "BeaconManager")
    public void checkIfBMCanDeleteStore() {
        Stores store = storesPage.getStore(storesPage.getAllStores(), Constants.defaultTestStore.getName(),
                Constants.defaultTestStore.getStoreCode());
        deleteStore(storesPage, store);
        assertEquals(Constants.storesUrl, webDriver.getCurrentUrl());
        assertEquals(Constants.notAuthorisedMsg, storesPage.getDangerAlert());
        assertThat(storesPage.getAllStores(), hasItem(allOf(
                hasProperty("name", equalTo(Constants.defaultTestStore.getName())),
                hasProperty("storeCode", equalTo(Constants.defaultTestStore.getStoreCode())
                ))));
    }

    /* Beacon Manager - Category Page */

    @Test(priority = 8, testName = "TC080", groups = "BeaconManager")
    public void checkIfBMCanAccessCategoryPage() {
        categoryPage = accessCategoriesPage(navigationMenu);
        assertEquals(Constants.categoriesUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 9, testName = "TC081", groups = "BeaconManager")
    public void checkIfBMCanSeeAListOfCategories() {
        assertEquals(Constants.categoriesUrl, webDriver.getCurrentUrl());
        assertNotNull(categoryPage.getAllCategories());
    }

    @Test(priority = 10, testName = "TC082", groups = "BeaconManager")
    public void checkIfBMCanCreaNewCategory() {
        categoryPage.clickNewCategoryBtn();
        try {
            Thread.sleep(Constants.waitMilliSeconds * 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.categoriesUrl, webDriver.getCurrentUrl());
        assertEquals(Constants.notAuthorisedMsg, categoryPage.getDangerAlert());
    }

    @Test(priority = 11, testName = "TC083", groups = "BeaconManager")
    public void checkIfBMCanUpdateCategory() {
        Category category = categoryPage.getCategory(categoryPage.getAllCategories(), Constants.defaultTestCategory.getCategoryName(),
                Constants.defaultTestCategory.getCategoryDescription());
        categoryPage.clickEditCategoryBtn(category.getEditButton());
        try {
            Thread.sleep(Constants.waitMilliSeconds * 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.categoriesUrl, webDriver.getCurrentUrl());
        assertEquals(Constants.notAuthorisedMsg, categoryPage.getDangerAlert());
    }

    @Test(priority = 12, testName = "TC084", groups = "BeaconManager")
    public void checkIfBMCanDeleteCategory() {
        Category category = categoryPage.getCategory(categoryPage.getAllCategories(), Constants.defaultTestCategory.getCategoryName(),
                Constants.defaultTestCategory.getCategoryDescription());
        deleteCategory(categoryPage, category);

        assertEquals(Constants.categoriesUrl, webDriver.getCurrentUrl());
        assertEquals(Constants.notAuthorisedMsg, categoryPage.getDangerAlert());
        assertThat(categoryPage.getAllCategories(), hasItem(allOf(
                hasProperty("categoryName", equalTo(Constants.defaultTestCategory.getCategoryName())),
                hasProperty("categoryDescription", equalTo(Constants.defaultTestCategory.getCategoryDescription())
                ))));
    }

    /* Beacon Manager - Beacons Page */

    @Test(priority = 13, testName = "TC085", groups = "BeaconManager")
    public void checkIfBMCanAccessBeaconsPage() {
        beaconsPage = accessBeaconsPage(navigationMenu);
        assertEquals(Constants.beaconsUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 14, testName = "TC086", groups = "BeaconManager")
    @Parameters({"uniqueRef1", "beaconName1", "currentStatus1", "latitude1", "longitude1"})
    public void checkIfBMCanCreaNewBeacon(String uniqueRef, String beaconName, String status, String latitude,
                                          String longitude) {
        createBeacon(beaconsPage, new Beacons(uniqueRef, beaconName, Constants.defaultTestStore.getName(), status,
                latitude, longitude));
        assertThat(beaconsPage.getAllBeacons(), hasItem(allOf(
                hasProperty("uniqueRef", equalTo(uniqueRef)),
                hasProperty("name", equalTo(beaconName)
                ))));
    }

    @Test(priority = 15, testName = "TC087", groups = "BeaconManager")
    public void checkIfBMCanSeeAListOfBeacons() {
        assertEquals(Constants.beaconsUrl, webDriver.getCurrentUrl());
        assertNotNull(beaconsPage.getAllBeacons());
    }

    @Test(priority = 16, testName = "TC088", groups = "BeaconManager")
    @Parameters({"uniqueRef1", "beaconName1"})
    public void checkIfBMCanUpdateBeacon(String uniqueRef, String beaconName) {
        Beacons beacon = beaconsPage.getBeacon(beaconsPage.getAllBeacons(), uniqueRef, beaconName);
        updateBeacon(beaconsPage, beacon, Constants.defaultTestBeacon);

        assertThat(beaconsPage.getAllBeacons(), hasItem(allOf(
                hasProperty("uniqueRef", equalTo(Constants.defaultTestBeacon.getUniqueRef())),
                hasProperty("name", equalTo(Constants.defaultTestBeacon.getName()))
        )));

        assertThat(beaconsPage.getAllBeacons(), not(hasItem(allOf(
                hasProperty("uniqueRef", equalTo(uniqueRef)),
                hasProperty("name", equalTo(beaconName))
        ))));
    }

    @Test(priority = 22, testName = "TC089", groups = "BeaconManager")
    public void checkIfBMCanDeleteBeacon() {
        if (!webDriver.getCurrentUrl().equals(Constants.beaconsUrl))
            accessBeaconsPage(navigationMenu);
        Beacons beacon = beaconsPage.getBeacon(beaconsPage.getAllBeacons(), Constants.defaultTestBeacon.getUniqueRef(),
                Constants.defaultTestBeacon.getName());
        deleteBeacon(beaconsPage, beacon);

        assertThat(beaconsPage.getAllBeacons(), not(hasItem(allOf(
                hasProperty("uniqueRef", equalTo(Constants.defaultTestBeacon.getUniqueRef())),
                hasProperty("name", equalTo(Constants.defaultTestBeacon.getName())
                )))));
    }

    /* Beacon Manager - Advertisements Page */

    @Test(priority = 17, testName = "TC090", groups = "BeaconManager")
    public void checkIfBMCanAccessAdvertisementPage() {
        advertisementsPage = accessAdvertisementsPage(navigationMenu);
        assertEquals(Constants.advertisementsUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 18, testName = "TC091", groups = "BeaconManager")
    public void checkIfBMCanCreaNewAdvertisement() {
        createAdvertisement(advertisementsPage, Constants.defaultTestAdvertisement);
        assertThat(advertisementsPage.getAllAdvertisements(), hasItem(allOf(
                hasProperty("name", equalTo(Constants.defaultTestAdvertisement.getName())),
                hasProperty("beacon", equalTo(Constants.defaultTestBeacon.getName()))
        )));
    }

    @Test(priority = 19, testName = "TC092", groups = "BeaconManager")
    public void checkIfBMCanSeeAListOfAdvertisement() {
        assertEquals(Constants.advertisementsUrl, webDriver.getCurrentUrl());
        assertNotNull(advertisementsPage.getAllAdvertisements());
    }

    @Test(priority = 20, testName = "TC093", groups = "BeaconManager")
    @Parameters({"adverName", "adverDescription", "adverPrice"})
    public void checkIfBMCanUpdateAdvertisement(String name, String description, String price) {
        Advertisement advertisement = advertisementsPage.getAdvertisment(advertisementsPage.getAllAdvertisements(),
                Constants.defaultTestAdvertisement.getName(), Constants.defaultTestBeacon.getName());

        updateAdvertisement(advertisementsPage, advertisement, new Advertisement(name, Constants.defaultTestBeacon.getName(),
                Constants.defaultTestCategory.getCategoryName(), description, price));

        List<Advertisement> allAdvertisements = advertisementsPage.getAllAdvertisements();

        // Asserting that there are no advertisements with the old name details
        assertThat(allAdvertisements, not(hasItem(allOf(
                hasProperty("name", equalTo(Constants.defaultTestAdvertisement.getName())),
                hasProperty("beacon", equalTo(Constants.defaultTestBeacon.getName()))
        ))));

        // Asserting that the details are updated in the old Advertisement
        assertThat(allAdvertisements, hasItem(allOf(
                hasProperty("name", equalTo(name)),
                hasProperty("beacon", equalTo(Constants.defaultTestBeacon.getName())),
                hasProperty("category", equalTo(Constants.defaultTestCategory.getCategoryName()))
        )));
    }

    @Test(priority = 21, testName = "TC094", groups = "BeaconManager")
    @Parameters({"adverName"})
    public void checkIfBMCanDeleteAdvertisement(String name) {
        Advertisement advertisement = advertisementsPage.getAdvertisment(advertisementsPage.getAllAdvertisements(), name, Constants.defaultTestBeacon.getName());
        deleteAdvertisement(advertisementsPage, advertisement);

        assertThat(advertisementsPage.getAllAdvertisements(), not(hasItem(allOf(
                hasProperty("name", equalTo(name)),
                hasProperty("beacon", equalTo(Constants.defaultTestBeacon.getName()))
        ))));
    }

    @Test(priority = 23, testName = "TC095", groups = "BeaconManager")
    public void checkIfBMCanAdcessDashboard() {
        dashboardPage = accessDashboardPage(navigationMenu);
        assertEquals(Constants.dashboardUrl, webDriver.getCurrentUrl());
    }

    @AfterTest
    @Parameters({"email", "password", "beacon_manager_role"})
    public void clearTestData(String email, String password, String role) {
        loginPage = navigationMenu.clickLogoutLink();
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        navigationMenu = loginToBeaconoid(email, password);

        categoryPage = accessCategoriesPage(navigationMenu);
        deleteCategory(categoryPage, Constants.defaultTestCategory);

        storesPage = accessStoresPage(navigationMenu);
        deleteStore(storesPage, Constants.defaultTestStore);

        staffPage = accessStaffPage(navigationMenu);
        deleteStaff(staffPage, staffPage.getStaffByRole(role));
    }

}
