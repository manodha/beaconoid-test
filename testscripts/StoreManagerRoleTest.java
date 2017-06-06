package testscripts;

import model.*;
import pageobjects.*;
import util.WebConstants;
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
    public void setupTestData(String loginEmail, String loginPassword, String name, String email, String nickname,
                              String password, String confirmPassword, String role) {
        navigationMenu = loginToBeaconoid(webDriver, loginEmail, loginPassword);
        staffPage = accessStaffPage(webDriver, navigationMenu);
        createStaff(staffPage, new Staff(name, email, nickname, password, confirmPassword, role));
        navigationMenu.clickLogoutLink();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @BeforeGroups({"stores_categories", "delete_category_store"})
    @Parameters({"userEmail3", "userPassword3"})
    public void loginStoreManager(String email, String password) {
        navigationMenu = loginToBeaconoid(webDriver, email, password);
    }

    @AfterGroups({"stores_categories", "delete_category_store"})
    public void logoutStoreManager() {
        navigationMenu.clickLogoutLink();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 1, testName = "TC_SR_24", groups = "stores_categories", description = "Check if Store Manager can access Dashboard Page")
    public void checkIfSMCanAccessDasboard() {
        assertNotNull(navigationMenu.getDashboardLink());
        dashboardPage = accessDashboardPage(webDriver, navigationMenu);
        assertEquals(WebConstants.dashboardUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 2, testName = "TC_SR_25", groups = "stores_categories", description = "Check if the Reports option is available in the Navigation Menu for the user with the role Store Manager")
    public void checkIfSMCanAccessReports() {
        assertEquals(WebConstants.dashboardUrl, webDriver.getCurrentUrl());
        assertNull(navigationMenu.getReportLink());
    }

    @Test(priority = 3, testName = "TC_SR_26", groups = "stores_categories", description = "Check if the Staff option is available in the Navigation Menu for the user with the role Store Manager")
    public void checkIfSMCanAccessStaffPage() {
        assertEquals(WebConstants.dashboardUrl, webDriver.getCurrentUrl());
        assertNull(navigationMenu.getReportLink());
    }

    /* Store Manager - Stores Page Test Cases */

    @Test(priority = 4, testName = "TC_SR_27", groups = "stores_categories", description = "Check if Store Manager can access Stores Page")
    public void checkIfSMCanAccessStorePage() {
        assertNotNull(navigationMenu.getStoresLink());
        storesPage = accessStoresPage(webDriver, navigationMenu);
        assertEquals(WebConstants.storesUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 5, testName = "TC_SR_28", groups = "stores_categories", description = "Check if Store Manager can create a new Store")
    @Parameters({"storeName", "storeUniqueCode", "sales"})
    public void checkIfSMCanCreaNewStore(String name, String uniqueCode, String sales) {
        assertEquals(WebConstants.storesUrl, webDriver.getCurrentUrl());
        createStore(webDriver, storesPage, new Stores(name, uniqueCode, sales));

        assertThat(storesPage.getAllStores(), hasItem(allOf(
                hasProperty("name", equalTo(name)),
                hasProperty("storeCode", equalTo(uniqueCode))
        )));
    }

    @Test(priority = 6, testName = "TC_SR_29", groups = "stores_categories", description = "Check if a list of Stores is visible to the Store Manager")
    public void checkIfSMCanSeeAListOfStores() {
        assertEquals(WebConstants.storesUrl, webDriver.getCurrentUrl());
        assertNotNull(storesPage.getAllStores());
    }

    @Test(priority = 7, testName = "TC_SR_30", groups = "stores_categories" ,description = "Check if Store Manager can update an existing Store")
    @Parameters({"storeName", "storeUniqueCode"})
    public void checkIfSMCanUpdaStore(String name, String code) {
        Stores store = storesPage.getStore(storesPage.getAllStores(), name, code);
        updateStore(webDriver, storesPage, store, WebConstants.defaultTestStore);

        assertThat(storesPage.getAllStores(), not(hasItem(allOf(
                hasProperty("name", equalTo(name)),
                hasProperty("storeCode", equalTo(code))
        ))));

        assertThat(storesPage.getAllStores(), hasItem(allOf(
                hasProperty("name", equalTo(WebConstants.defaultTestStore.getName())),
                hasProperty("storeCode", equalTo(WebConstants.defaultTestStore.getStoreCode()))
        )));
    }

    @Test(priority = 23, testName = "TC_SR_31", groups = "delete_category_store", description = "Check if Store Manager can delete an existing Store")
    public void checkIfSMCanDeleteStore() {
        if (!webDriver.getCurrentUrl().equals(WebConstants.storesUrl))
            storesPage = accessStoresPage(webDriver, navigationMenu);
        Stores store = storesPage.getStore(storesPage.getAllStores(), WebConstants.defaultTestStore.getName(),
                WebConstants.defaultTestStore.getStoreCode());
        deleteStore(webDriver, storesPage, store);

        assertThat(storesPage.getAllStores(), not(hasItem(allOf(
                hasProperty("name", equalTo(WebConstants.defaultTestStore.getName())),
                hasProperty("storeCode", equalTo(WebConstants.defaultTestStore.getStoreCode()))
        ))));
    }


    /* Store Manager - Categories Page Test Cases */

    @Test(priority = 8, testName = "TC_SR_32", groups = "stores_categories", description = "Check if Store Manager can access Categories Page")
    public void checkIfSMCanAccessCategoPage() {
        assertNotNull(navigationMenu.getCategoriesLink());
        categoryPage = accessCategoriesPage(webDriver, navigationMenu);
        assertEquals(WebConstants.categoriesUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 9, testName = "TC_SR_33", groups = "stores_categories", description = "Check if Store Manager can create a new Category")
    @Parameters({"categoryName", "categoryDescription"})
    public void checkIfSMCanCreaCategory(String name, String desc) {
        createCategory(webDriver, categoryPage, new Category(name, desc));
        assertThat(categoryPage.getAllCategories(), hasItem(allOf(
                hasProperty("categoryName", equalTo(name)),
                hasProperty("categoryDescription", equalTo(desc))
        )));
    }

    @Test(priority = 10, testName = "TC_SR_34", groups = "stores_categories", description = "Check if a list of Categories is visible to the Store Manager")
    public void checkIfSMCanSeeListOfCatego() {
        assertEquals(WebConstants.categoriesUrl, webDriver.getCurrentUrl());
        assertNotNull(categoryPage.getAllCategories());
    }

    @Test(priority = 11, testName = "TC_SR_35", groups = "stores_categories", description = "Check if Store Manager can update an existing Category")
    @Parameters({"categoryName", "categoryDescription"})
    public void checkIfSMCanUpdaCatego(String name, String desc) {
        Category category = categoryPage.getCategory(categoryPage.getAllCategories(), name, desc);
        updateCategory(webDriver, categoryPage, category, WebConstants.defaultTestCategory);

        assertThat(categoryPage.getAllCategories(), not(hasItem(allOf(
                hasProperty("categoryName", equalTo(name)),
                hasProperty("categoryDescription", equalTo(desc))
        ))));

        assertThat(categoryPage.getAllCategories(), hasItem(allOf(
                hasProperty("categoryName", equalTo(WebConstants.defaultTestCategory.getCategoryName())),
                hasProperty("categoryDescription", equalTo(WebConstants.defaultTestCategory.getCategoryDescription()))
        )));
    }

    @Test(priority = 22, testName = "TC_SR_36", groups = "delete_category_store", description = "Check if Store Manager can delete an existing Category")
    public void checkIfSMCanDeleteCatego() {
        if (!webDriver.getCurrentUrl().equals(WebConstants.categoriesUrl))
            accessCategoriesPage(webDriver, navigationMenu);
        Category category = categoryPage.getCategory(categoryPage.getAllCategories(), WebConstants.defaultTestCategory.getCategoryName(),
                WebConstants.defaultTestCategory.getCategoryDescription());

        deleteCategory(webDriver, categoryPage, category);

        assertThat(categoryPage.getAllCategories(), not(hasItem(allOf(
                hasProperty("categoryName", equalTo(WebConstants.defaultTestCategory.getCategoryName())),
                hasProperty("categoryDescription", equalTo(WebConstants.defaultTestCategory.getCategoryDescription()))
        ))));
    }

    @BeforeGroups("beacons_advertisements")
    @Parameters({"email", "password", "userEmail3", "userPassword3"})
    public void createDefBeaconAndAdver(String sAEmail, String sAPassword, String sMEmail, String sMPassword) throws InterruptedException {
        navigationMenu = loginToBeaconoid(webDriver, sAEmail, sAPassword);
        beaconsPage = accessBeaconsPage(webDriver, navigationMenu);
        createBeacon(webDriver, beaconsPage, WebConstants.defaultTestBeacon);

        advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);
        createAdvertisement(webDriver, advertisementsPage, WebConstants.defaultTestAdvertisement);

        navigationMenu.clickLogoutLink();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        navigationMenu = loginToBeaconoid(webDriver, sMEmail, sMPassword);
    }

    /* Store Manager - Beacons Page Test Cases */

    @Test(priority = 12, testName = "TC_SR_37", groups = "beacons_advertisements", description = "Check if Store Manager can access Beacons Page")
    public void checkIfSMCanAccessBeaconsPage() throws InterruptedException {
        assertNotNull(navigationMenu.getBeaconsLink());
        beaconsPage = accessBeaconsPage(webDriver, navigationMenu);
        assertEquals(WebConstants.beaconsUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 13, testName = "TC_SR_38", groups = "beacons_advertisements", description = "Check if a list of Beacons is visible to the Store Manager")
    public void checkIfSMCanSeeListOfBeacons() {
        assertEquals(WebConstants.beaconsUrl, webDriver.getCurrentUrl());
        assertNotNull(beaconsPage.getRegOtherBeacons(WebConstants.otherBeaconTitle));
    }

    @Test(priority = 14, testName = "TC_SR_39", groups = "beacons_advertisements", description = "Check if Store Manager can create a new Beacon")
    public void checkIfSMCanCreaBeacons() {
        beaconsPage.clickNewBeaconBtn();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.beaconsUrl, webDriver.getCurrentUrl());
        assertEquals(WebConstants.notAuthorisedMsg, beaconsPage.getDangerAlert());
    }

    @Test(priority = 15, testName = "TC_SR_40", groups = "beacons_advertisements", description = "Check if Store Manager can update an existing Beacon")
    public void checkIfSMCanUpdaBeacon() {
        Beacons beacon = beaconsPage.getBeacon(beaconsPage.getRegOtherBeacons(WebConstants.otherBeaconTitle), WebConstants.defaultTestBeacon.getUniqueRef(),
                WebConstants.defaultTestBeacon.getName());
        beaconsPage.clickEditBeaconBtn(beacon.getEditLink());
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.beaconsUrl, webDriver.getCurrentUrl());
        assertEquals(WebConstants.notAuthorisedMsg, beaconsPage.getDangerAlert());
    }

    @Test(priority = 16, testName = "TC_SR_41", groups = "beacons_advertisements", description = "Check if Store Manager can delete an existing Beacon")
    public void checkIfSMCanDelBeacon() {
        Beacons beacon = beaconsPage.getBeacon(beaconsPage.getRegOtherBeacons(WebConstants.otherBeaconTitle), WebConstants.defaultTestBeacon.getUniqueRef(),
                WebConstants.defaultTestBeacon.getName());
        beaconsPage.clickDeleteBeaconBtn(beacon.getDeleteLink());
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.beaconsUrl, webDriver.getCurrentUrl());
        assertEquals(WebConstants.notAuthorisedMsg, beaconsPage.getDangerAlert());
        assertThat(beaconsPage.getRegOtherBeacons(WebConstants.otherBeaconTitle), hasItem(allOf(
                hasProperty("uniqueRef", equalTo(WebConstants.defaultTestBeacon.getUniqueRef())),
                hasProperty("name", equalTo(WebConstants.defaultTestBeacon.getName()))
        )));

    }

    /* Store Manager - Advertisements Page Test Cases */

    @Test(priority = 17, testName = "TC_SR_42", groups = "beacons_advertisements", description = "Check if Store Manager can access Advertisements Page")
    public void checkIfSMCanAccessAdverPage() {
        assertNotNull(navigationMenu.getAdvertisementsLink());
        advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);
        assertEquals(WebConstants.advertisementsUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 18, testName = "TC_SR_43", groups = "beacons_advertisements", description = "Check if a list of Advertisements is visible to the Store Manager")
    public void checkIfSMCanSeeListOfAdver() {
        assertEquals(WebConstants.advertisementsUrl, webDriver.getCurrentUrl());
        assertNotNull(advertisementsPage.getAllAdvertisements());
    }

    @Test(priority = 19, testName = "TC_SR_44", groups = "beacons_advertisements", description = "Check if Store Manager can create a new Advertisement")
    public void checkIfSMCanCreaAdver() {
        assertEquals(WebConstants.advertisementsUrl, webDriver.getCurrentUrl());
        advertisementsPage.clickNewAdvertisementBtn();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.advertisementsUrl, webDriver.getCurrentUrl());
        assertEquals(WebConstants.notAuthorisedMsg, advertisementsPage.getDangerAlert());
    }

    @Test(priority = 20, testName = "TC_SR_45", groups = "beacons_advertisements", description = "Check if Store Manager can update an existing Advertisement")
    public void checkIfSMCanUpdaAdver() {
        assertEquals(WebConstants.advertisementsUrl, webDriver.getCurrentUrl());
        Advertisement advertisement = advertisementsPage.getAdvertisment(advertisementsPage.getAllAdvertisements(),
                WebConstants.defaultTestAdvertisement.getName(), WebConstants.defaultTestAdvertisement.getBeacon());
        advertisementsPage.clickEditAdverBtn(advertisement.getEditLink());
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.advertisementsUrl, webDriver.getCurrentUrl());
        assertEquals(WebConstants.notAuthorisedMsg, advertisementsPage.getDangerAlert());
    }

    @Test(priority = 21, testName = "TC_SR_46", groups = "beacons_advertisements", description = "Check if Store Manager can delete an existing Advertisement")
    public void checkIfSMCanDelAdver() {
        assertEquals(WebConstants.advertisementsUrl, webDriver.getCurrentUrl());
        Advertisement advertisement = advertisementsPage.getAdvertisment(advertisementsPage.getAllAdvertisements(),
                WebConstants.defaultTestAdvertisement.getName(), WebConstants.defaultTestAdvertisement.getBeacon());
        advertisementsPage.clickDeleteAdvBtn(advertisement.getDeleteBtn());
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.advertisementsUrl, webDriver.getCurrentUrl());
        assertEquals(WebConstants.notAuthorisedMsg, advertisementsPage.getDangerAlert());
        assertThat(advertisementsPage.getAllAdvertisements(), hasItem(allOf(
                hasProperty("name", equalTo(WebConstants.defaultTestAdvertisement.getName())),
                hasProperty("beacon", equalTo(WebConstants.defaultTestAdvertisement.getBeacon()))
        )));
    }

    @AfterGroups("beacons_advertisements")
    @Parameters({"email", "password"})
    public void deleteDefBeaconAndAdver(String email, String password) throws InterruptedException {
        navigationMenu.clickLogoutLink();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        navigationMenu = loginToBeaconoid(webDriver, email, password);
        advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);
        deleteAdvertisement(webDriver, advertisementsPage, WebConstants.defaultTestAdvertisement);

        beaconsPage = accessBeaconsPage(webDriver, navigationMenu);
        deleteBeacon(webDriver, beaconsPage, WebConstants.defaultTestBeacon);

        navigationMenu.clickLogoutLink();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /* Removing the Store Manager User */
    @AfterTest
    @Parameters({"email", "password", "store_manager_role"})
    public void deleteStoreManager(String email, String password, String role) {
        navigationMenu = loginToBeaconoid(webDriver, email, password);
        staffPage = accessStaffPage(webDriver, navigationMenu);
        Staff beaconManager = staffPage.getStaffByRole(role);
        deleteStaff(webDriver, beaconManager, staffPage);
    }

}
