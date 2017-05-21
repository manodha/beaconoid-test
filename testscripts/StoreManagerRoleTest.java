package com.company.testscripts;

import com.company.model.Staff;
import com.company.model.Stores;
import com.company.pageobjects.DashboardPage;
import com.company.pageobjects.NavigationMenu;
import com.company.pageobjects.StaffPage;
import com.company.pageobjects.StoresPage;
import com.company.util.Constants;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

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

        navigationMenu = loginToBeaconoid(email, password);
    }


    @Test(priority = 1, testName = "TC096", groups = "StoreManager")
    public void checkIfSMCanAccessDasboard() {
        assertNotNull(navigationMenu.getDashboardLink());
        dashboardPage = accessDashboardPage(navigationMenu);
        assertEquals(Constants.dashboardUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 2, testName = "TC097", groups = "StoreManager")
    public void checkIfSMCanAccessReports() {
        assertEquals(Constants.dashboardUrl, webDriver.getCurrentUrl());
        assertNull(navigationMenu.getReportLink());
    }

    @Test(priority = 3, testName = "TC098", groups = "StoreManager")
    public void checkIfSMCanAccessStaffPage() {
        assertEquals(Constants.dashboardUrl, webDriver.getCurrentUrl());
        assertNull(navigationMenu.getReportLink());
    }

    @Test(priority = 4, testName = "TC099", groups = "StoreManager")
    public void checkIfSMCanAccessStorePage() {
        assertNotNull(navigationMenu.getStoresLink());
        storesPage = accessStoresPage(navigationMenu);
        assertEquals(Constants.storesUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 5, testName = "TC100", groups = "StoreManager")
    @Parameters({"storeName", "storeUniqueCode", "imgUrl"})
    public void checkIfSMCanCreaNewStore(String name, String uniqueCode, String imgUrl) {
        assertEquals(Constants.storesUrl, webDriver.getCurrentUrl());
        createStore(storesPage, new Stores(name, uniqueCode, imgUrl));

        assertThat(storesPage.getAllStores(), hasItem(allOf(
                hasProperty("name", equalTo(name)),
                hasProperty("storeCode", equalTo(uniqueCode))
        )));
    }

    @Test(priority = 6, testName = "TC101", groups = "StoreManager")
    public void checkIfSMCanSeeAListOfStores() {
        assertEquals(Constants.storesUrl, webDriver.getCurrentUrl());
        assertNotNull(storesPage.getAllStores());
    }

    @Test(priority = 7, testName = "TC102", groups = "StoreManager")
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

    @Test(priority = 8, testName = "TC103", groups = "StoreManager")
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
}
