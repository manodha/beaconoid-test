package com.company.testscripts;

import com.company.model.*;
import com.company.pageobjects.*;
import com.company.util.Constants;
import org.testng.annotations.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.testng.Assert.assertNotNull;

/**
 * Created by manodha on 21/5/17.
 */
public class StoreManagerRoleTest extends FunctionalTest {

    private NavigationMenu navigationMenu;
    private StaffPage staffPage;
    private DashboardPage dashboardPage;
    private StoresPage storesPage;
    private CategoryPage categoryPage;
    private BeaconsPage beaconsPage;
    private AdvertisementsPage advertisementsPage;

    /* Adding the user with the role store manager */

    @BeforeTest
    @Parameters({"email", "password", "userName3", "userEmail3", "nickname3", "userPassword3", "confirmPassword3", "store_manager_role"})
    public void setUpTestData(String loginEmail, String loginPassword, String name, String email, String nickname,
                              String password, String confirmPassword, String role) {
        navigationMenu = loginToBeaconoid(loginEmail, loginPassword);
        staffPage = accessStaffPage(navigationMenu);
        createStaff(staffPage, new Staff(name, email, nickname, password, confirmPassword, role));
        navigationMenu.clickLogoutLink();
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @BeforeGroups({"stores_categories", "delete_category_store"})
    @Parameters({"userEmail3", "userPassword3"})
    public void loginStoreManager(String email, String password) {
        navigationMenu = loginToBeaconoid(email, password);
    }

    @AfterGroups({"stores_categories", "delete_category_store"})
    public void logoutStoreManager() {
        navigationMenu.clickLogoutLink();
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 1, testName = "TC096", groups = "stores_categories")
    public void checkIfSMCanAccessDasboard() {
        assertNotNull(navigationMenu.getDashboardLink());
        dashboardPage = accessDashboardPage(navigationMenu);
        assertEquals(Constants.dashboardUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 2, testName = "TC097", groups = "stores_categories")
    public void checkIfSMCanAccessReports() {
        assertEquals(Constants.dashboardUrl, webDriver.getCurrentUrl());
        assertNull(navigationMenu.getReportLink());
    }

    @Test(priority = 3, testName = "TC098", groups = "stores_categories")
    public void checkIfSMCanAccessStaffPage() {
        assertEquals(Constants.dashboardUrl, webDriver.getCurrentUrl());
        assertNull(navigationMenu.getReportLink());
    }

    /* Store Manager - Stores Page Test Cases */

    @Test(priority = 4, testName = "TC099", groups = "stores_categories")
    public void checkIfSMCanAccessStorePage() {
        assertNotNull(navigationMenu.getStoresLink());
        storesPage = accessStoresPage(navigationMenu);
        assertEquals(Constants.storesUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 5, testName = "TC100", groups = "stores_categories")
    @Parameters({"storeName", "storeUniqueCode", "imgUrl"})
    public void checkIfSMCanCreaNewStore(String name, String uniqueCode, String imgUrl) {
        assertEquals(Constants.storesUrl, webDriver.getCurrentUrl());
        createStore(storesPage, new Stores(name, uniqueCode, imgUrl));

        assertThat(storesPage.getAllStores(), hasItem(allOf(
                hasProperty("name", equalTo(name)),
                hasProperty("storeCode", equalTo(uniqueCode))
        )));
    }

    @Test(priority = 6, testName = "TC101", groups = "stores_categories")
    public void checkIfSMCanSeeAListOfStores() {
        assertEquals(Constants.storesUrl, webDriver.getCurrentUrl());
        assertNotNull(storesPage.getAllStores());
    }

    @Test(priority = 7, testName = "TC102", groups = "stores_categories")
    @Parameters({"storeName", "storeUniqueCode"})
    public void checkIfSMCanUpdaStore(String name, String code) {
        Stores store = storesPage.getStore(storesPage.getAllStores(), name, code);
        updateStore(storesPage, store, Constants.defaultTestStore);

        assertThat(storesPage.getAllStores(), not(hasItem(allOf(
                hasProperty("name", equalTo(name)),
                hasProperty("storeCode", equalTo(code))
        ))));

        assertThat(storesPage.getAllStores(), hasItem(allOf(
                hasProperty("name", equalTo(Constants.defaultTestStore.getName())),
                hasProperty("storeCode", equalTo(Constants.defaultTestStore.getStoreCode()))
        )));
    }

    @Test(priority = 23, testName = "TC103", groups = "delete_category_store")
    public void checkIfSMCanDeleteStore() {
        if (!webDriver.getCurrentUrl().equals(Constants.storesUrl))
            storesPage = accessStoresPage(navigationMenu);
        Stores store = storesPage.getStore(storesPage.getAllStores(), Constants.defaultTestStore.getName(),
                Constants.defaultTestStore.getStoreCode());
        deleteStore(storesPage, store);

        assertThat(storesPage.getAllStores(), not(hasItem(allOf(
                hasProperty("name", equalTo(Constants.defaultTestStore.getName())),
                hasProperty("storeCode", equalTo(Constants.defaultTestStore.getStoreCode()))
        ))));
    }


    /* Store Manager - Categories Page Test Cases */

    @Test(priority = 8, testName = "TC104", groups = "stores_categories")
    public void checkIfSMCanAccessCategoPage() {
        assertNotNull(navigationMenu.getCategoriesLink());
        categoryPage = accessCategoriesPage(navigationMenu);
        assertEquals(Constants.categoriesUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 9, testName = "TC105", groups = "stores_categories")
    @Parameters({"categoryName", "categoryDescription"})
    public void checkIfSMCanCreaCategory(String name, String desc) {
        createCategory(categoryPage, new Category(name, desc));
        assertThat(categoryPage.getAllCategories(), hasItem(allOf(
                hasProperty("categoryName", equalTo(name)),
                hasProperty("categoryDescription", equalTo(desc))
        )));
    }

    @Test(priority = 10, testName = "TC106", groups = "stores_categories")
    public void checkIfSMCanSeeListOfCatego() {
        assertEquals(Constants.categoriesUrl, webDriver.getCurrentUrl());
        assertNotNull(categoryPage.getAllCategories());
    }

    @Test(priority = 11, testName = "TC107", groups = "stores_categories")
    @Parameters({"categoryName", "categoryDescription"})
    public void checkIfSMCanUpdaCatego(String name, String desc) {
        Category category = categoryPage.getCategory(categoryPage.getAllCategories(), name, desc);
        updateCategory(categoryPage, category, Constants.defaultTestCategory);

        assertThat(categoryPage.getAllCategories(), not(hasItem(allOf(
                hasProperty("categoryName", equalTo(name)),
                hasProperty("categoryDescription", equalTo(desc))
        ))));

        assertThat(categoryPage.getAllCategories(), hasItem(allOf(
                hasProperty("categoryName", equalTo(Constants.defaultTestCategory.getCategoryName())),
                hasProperty("categoryDescription", equalTo(Constants.defaultTestCategory.getCategoryDescription()))
        )));
    }

    @Test(priority = 22, testName = "TC108", groups = "delete_category_store")
    public void checkIfSMCanDeleteCatego() {
        if (!webDriver.getCurrentUrl().equals(Constants.categoriesUrl))
            accessCategoriesPage(navigationMenu);
        Category category = categoryPage.getCategory(categoryPage.getAllCategories(), Constants.defaultTestCategory.getCategoryName(),
                Constants.defaultTestCategory.getCategoryDescription());

        deleteCategory(categoryPage, category);

        assertThat(categoryPage.getAllCategories(), not(hasItem(allOf(
                hasProperty("categoryName", equalTo(Constants.defaultTestCategory.getCategoryName())),
                hasProperty("categoryDescription", equalTo(Constants.defaultTestCategory.getCategoryDescription()))
        ))));
    }

    @BeforeGroups("beacons_advertisements")
    @Parameters({"email", "password", "userEmail3", "userPassword3"})
    public void createDefBeaconAndAdver(String sAEmail, String sAPassword, String sMEmail, String sMPassword) {
        navigationMenu = loginToBeaconoid(sAEmail, sAPassword);
        beaconsPage = accessBeaconsPage(navigationMenu);
        createBeacon(beaconsPage, Constants.defaultTestBeacon);

        advertisementsPage = accessAdvertisementsPage(navigationMenu);
        createAdvertisement(advertisementsPage, Constants.defaultTestAdvertisement);

        navigationMenu.clickLogoutLink();
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        navigationMenu = loginToBeaconoid(sMEmail, sMPassword);
    }

    /* Store Manager - Beacons Page Test Cases */

    @Test(priority = 12, testName = "TC109", groups = "beacons_advertisements")
    public void checkIfSMCanAccessBeaconsPage() {
        assertNotNull(navigationMenu.getBeaconsLink());
        beaconsPage = accessBeaconsPage(navigationMenu);
        assertEquals(Constants.beaconsUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 13, testName = "TC110", groups = "beacons_advertisements")
    public void checkIfSMCanSeeListOfBeacons() {
        assertEquals(Constants.beaconsUrl, webDriver.getCurrentUrl());
        assertNotNull(beaconsPage.getAllBeacons());
    }

    @Test(priority = 14, testName = "TC111", groups = "beacons_advertisements")
    public void checkIfSMCanCreaBeacons() {
        beaconsPage.clickNewBeaconBtn();
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.beaconsUrl, webDriver.getCurrentUrl());
        assertEquals(Constants.notAuthorisedMsg, beaconsPage.getDangerAlert());
    }

    @Test(priority = 15, testName = "TC112", groups = "beacons_advertisements")
    public void checkIfSMCanUpdaBeacon() {
        Beacons beacon = beaconsPage.getBeacon(beaconsPage.getAllBeacons(), Constants.defaultTestBeacon.getUniqueRef(),
                Constants.defaultTestBeacon.getName());
        beaconsPage.clickEditBeaconBtn(beacon.getEditLink());
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.beaconsUrl, webDriver.getCurrentUrl());
        assertEquals(Constants.notAuthorisedMsg, beaconsPage.getDangerAlert());
    }

    @Test(priority = 16, testName = "TC113", groups = "beacons_advertisements")
    public void checkIfSMCanDelBeacon() {
        Beacons beacon = beaconsPage.getBeacon(beaconsPage.getAllBeacons(), Constants.defaultTestBeacon.getUniqueRef(),
                Constants.defaultTestBeacon.getName());
        beaconsPage.clickDeleteBeaconBtn(beacon.getDeleteLink());
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.beaconsUrl, webDriver.getCurrentUrl());
        assertEquals(Constants.notAuthorisedMsg, beaconsPage.getDangerAlert());
        assertThat(beaconsPage.getAllBeacons(), hasItem(allOf(
                hasProperty("uniqueRef", equalTo(Constants.defaultTestBeacon.getUniqueRef())),
                hasProperty("name", equalTo(Constants.defaultTestBeacon.getName()))
        )));

    }

    /* Store Manager - Advertisements Page Test Cases */

    @Test(priority = 17, testName = "TC114", groups = "beacons_advertisements")
    public void checkIfSMCanAccessAdverPage() {
        assertNotNull(navigationMenu.getAdvertisementsLink());
        advertisementsPage = accessAdvertisementsPage(navigationMenu);
        assertEquals(Constants.advertisementsUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 18, testName = "TC115", groups = "beacons_advertisements")
    public void checkIfSMCanSeeListOfAdver() {
        assertEquals(Constants.advertisementsUrl, webDriver.getCurrentUrl());
        assertNotNull(advertisementsPage.getAllAdvertisements());
    }

    @Test(priority = 19, testName = "TC116", groups = "beacons_advertisements")
    public void checkIfSMCanCreaAdver() {
        assertEquals(Constants.advertisementsUrl, webDriver.getCurrentUrl());
        advertisementsPage.clickNewAdvertisementBtn();
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.advertisementsUrl, webDriver.getCurrentUrl());
        assertEquals(Constants.notAuthorisedMsg, advertisementsPage.getDangerAlert());
    }

    @Test(priority = 20, testName = "TC117", groups = "beacons_advertisements")
    public void checkIfSMCanUpdaAdver() {
        assertEquals(Constants.advertisementsUrl, webDriver.getCurrentUrl());
        Advertisement advertisement = advertisementsPage.getAdvertisment(advertisementsPage.getAllAdvertisements(),
                Constants.defaultTestAdvertisement.getName(), Constants.defaultTestAdvertisement.getBeacon());
        advertisementsPage.clickEditAdverBtn(advertisement.getEditLink());
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.advertisementsUrl, webDriver.getCurrentUrl());
        assertEquals(Constants.notAuthorisedMsg, advertisementsPage.getDangerAlert());
    }

    @Test(priority = 21, testName = "TC118", groups = "beacons_advertisements")
    public void checkIfSMCanDelAdver() {
        assertEquals(Constants.advertisementsUrl, webDriver.getCurrentUrl());
        Advertisement advertisement = advertisementsPage.getAdvertisment(advertisementsPage.getAllAdvertisements(),
                Constants.defaultTestAdvertisement.getName(), Constants.defaultTestAdvertisement.getBeacon());
        advertisementsPage.clickDeleteAdvBtn(advertisement.getDeleteBtn());
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.advertisementsUrl, webDriver.getCurrentUrl());
        assertEquals(Constants.notAuthorisedMsg, advertisementsPage.getDangerAlert());
        assertThat(advertisementsPage.getAllAdvertisements(), hasItem(allOf(
                hasProperty("name", equalTo(Constants.defaultTestAdvertisement.getName())),
                hasProperty("beacon", equalTo(Constants.defaultTestAdvertisement.getBeacon()))
        )));
    }

    @AfterGroups("beacons_advertisements")
    @Parameters({"email", "password"})
    public void deleteDefBeaconAndAdver(String email, String password) {
        navigationMenu.clickLogoutLink();
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        navigationMenu = loginToBeaconoid(email, password);
        advertisementsPage = accessAdvertisementsPage(navigationMenu);
        deleteAdvertisement(advertisementsPage, Constants.defaultTestAdvertisement);

        beaconsPage = accessBeaconsPage(navigationMenu);
        deleteBeacon(beaconsPage, Constants.defaultTestBeacon);

        navigationMenu.clickLogoutLink();
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /* Removing the Store Manager User */
    @AfterTest
    @Parameters({"email", "password", "store_manager_role"})
    public void deleteStoreManager(String email, String password, String role) {
        navigationMenu = loginToBeaconoid(email, password);
        staffPage = accessStaffPage(navigationMenu);
        Staff beaconManager = staffPage.getStaffByRole(role);
        deleteStaff(staffPage, beaconManager);
    }

}
