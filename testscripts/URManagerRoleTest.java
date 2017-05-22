package com.company.testscripts;

import com.company.model.Staff;
import com.company.pageobjects.*;
import com.company.util.Constants;
import org.testng.annotations.*;

import static org.junit.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNull;

/**
 * Created by manodha on 22/5/17.
 */
public class URManagerRoleTest extends FunctionalTest {

    private NavigationMenu navigationMenu;
    private StaffPage staffPage;
    private DashboardPage dashboardPage;
    private ReportPage reportPage;
    private StoreReportPage storeReportPage;
    private CategoryReportPage categoryReportPage;

    @BeforeTest
    @Parameters({"email", "password", "name5", "email5", "nickname5", "password5", "confirmPassword5", "user_report_manager_role"})
    public void createURManager(String loginEmail, String loginPassword, String name, String email, String nickname, String password,
                                String confirmPassword, String role) {
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

    @AfterTest
    @Parameters({"email", "password", "user_report_manager_role"})
    public void deleteURManager(String email, String password, String role) {
        navigationMenu = loginToBeaconoid(email, password);
        staffPage = accessStaffPage(navigationMenu);
        Staff staff = staffPage.getStaffByRole(role);
        deleteStaff(staffPage, staff);
    }


    @BeforeGroups("URManager")
    @Parameters({"email5", "password5"})
    public void loginURManager(String email, String password) {
        navigationMenu = loginToBeaconoid(email, password);
    }

    @AfterGroups("URManager")
    public void logoutURManager() {
        navigationMenu.clickLogoutLink();
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test(priority = 1, testName = "TC119", groups = "URManager")
    public void checkIfURMCantAcessStoresPage() {
        assertEquals(Constants.baseUrl, webDriver.getCurrentUrl());
        assertNull(navigationMenu.getStoresLink());
    }

    @Test(priority = 2, testName = "TC120", groups = "URManager")
    public void checkIfURMCantAccessCategoPage() {
        assertEquals(Constants.baseUrl, webDriver.getCurrentUrl());
        assertNull(navigationMenu.getCategoriesLink());
    }

    @Test(priority = 3, testName = "TC121", groups = "URManager")
    public void checkIfURMCantAccessBeaconPage() {
        assertEquals(Constants.baseUrl, webDriver.getCurrentUrl());
        assertNull(navigationMenu.getBeaconsLink());
    }

    @Test(priority = 4, testName = "TC122", groups = "URManager")
    public void checkIfURMCantAccessAdverPage() {
        assertEquals(Constants.baseUrl, webDriver.getCurrentUrl());
        assertNull(navigationMenu.getAdvertisementsLink());
    }

    @Test(priority = 5, testName = "TC123", groups = "URManager")
    public void checkIfURMCantAccessStaffPage() {
        assertEquals(Constants.baseUrl, webDriver.getCurrentUrl());
        assertNull(navigationMenu.getStaffLink());
    }

    @Test(priority = 6, testName = "TC124", groups = "URManager")
    public void checkIfURMCanAccessDashboard() {
        assertEquals(Constants.baseUrl, webDriver.getCurrentUrl());
        assertNotNull(navigationMenu.getDashboardLink());
        dashboardPage = accessDashboardPage(navigationMenu);
        assertEquals(Constants.dashboardUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 7, testName = "TC125", groups = "URManager")
    public void checkIfURMCanAccessReports() {
        assertNotNull(navigationMenu.getReportLink());
        reportPage = accessReportPage(navigationMenu);

        assertNotNull(navigationMenu.getStoreReportLink());
        storeReportPage = accessStoreReportPage(navigationMenu);

        assertNotNull(navigationMenu.getCategoryReportLink());
        categoryReportPage = accessCategoryReportPage(navigationMenu);

    }


}
