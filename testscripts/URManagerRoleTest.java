package testscripts;

import model.Staff;
import pageobjects.*;
import util.WebConstants;
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

    @AfterTest
    @Parameters({"email", "password", "user_report_manager_role"})
    public void deleteURManager(String email, String password, String role) {
        navigationMenu = loginToBeaconoid(webDriver, email, password);
        staffPage = accessStaffPage(webDriver, navigationMenu);
        Staff staff = staffPage.getStaffByRole(role);
        deleteStaff(webDriver, staff, staffPage);
    }


    @BeforeGroups("URManager")
    @Parameters({"email5", "password5"})
    public void loginURManager(String email, String password) {
        navigationMenu = loginToBeaconoid(webDriver, email, password);
    }

    @AfterGroups("URManager")
    public void logoutURManager() {
        navigationMenu.clickLogoutLink();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test(priority = 1, testName = "TC_SR_47", groups = "URManager", description = "Check if a the Stores option is available for the user with the role User Report Manager")
    public void checkIfURMCantAcessStoresPage() {
        assertEquals(WebConstants.baseUrl, webDriver.getCurrentUrl());
        assertNull(navigationMenu.getStoresLink());
    }

    @Test(priority = 2, testName = "TC_SR_48", groups = "URManager", description = "Check if a the Categories option is available for the user with the role User Report Manager")
    public void checkIfURMCantAccessCatePage() {
        assertEquals(WebConstants.baseUrl, webDriver.getCurrentUrl());
        assertNull(navigationMenu.getCategoriesLink());
    }

    @Test(priority = 3, testName = "TC_SR_49", groups = "URManager", description = "Check if a the Beacons option is available for the user with the role User Report Manager")
    public void checkIfURMCantAccessBeaconPage() {
        assertEquals(WebConstants.baseUrl, webDriver.getCurrentUrl());
        assertNull(navigationMenu.getBeaconsLink());
    }

    @Test(priority = 4, testName = "TC_SR_50", groups = "URManager", description = "Check if a the Advertisements option is available for the user with the role User Report Manager")
    public void checkIfURMCantAccessAdverPage() {
        assertEquals(WebConstants.baseUrl, webDriver.getCurrentUrl());
        assertNull(navigationMenu.getAdvertisementsLink());
    }

    @Test(priority = 5, testName = "TC_SR_51", groups = "URManager", description = "Check if a the Staff option is available for the user with the role User Report Manager")
    public void checkIfURMCantAccessStaffPage() {
        assertEquals(WebConstants.baseUrl, webDriver.getCurrentUrl());
        assertNull(navigationMenu.getStaffLink());
    }

    @Test(priority = 6, testName = "TC_SR_52", groups = "URManager", description = "Check if User Report Manager can access Dashboard Page")
    public void checkIfURMCanAccessDashboard() {
        assertEquals(WebConstants.baseUrl, webDriver.getCurrentUrl());
        assertNotNull(navigationMenu.getDashboardLink());
        dashboardPage = accessDashboardPage(webDriver, navigationMenu);
        assertEquals(WebConstants.dashboardUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 7, testName = "TC_SR_53", groups = "URManager", description = "Check if User Report Manager can access Reports Page")
    public void checkIfURMCanAccessReports() {
        assertNotNull(navigationMenu.getReportLink());
        reportPage = accessReportPage(webDriver, navigationMenu);

        assertNotNull(navigationMenu.getStoreReportLink());
        storeReportPage = accessStoreReportPage(webDriver, navigationMenu);

        assertNotNull(navigationMenu.getCategoryReportLink());
        categoryReportPage = accessCategoryReportPage(webDriver, navigationMenu);
    }

}
