package testscripts;

import model.Staff;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.*;
import util.WebConstants;

import static org.junit.Assert.assertEquals;


/**
 * Created by manodha on 9/4/17.
 */
public class NavigationMenuTest extends FunctionalTest {

    private NavigationMenu navigationMenu;
    private StaffPage staffPage;

    @BeforeTest
    @Parameters({"email", "password"})
    public void setupTestData(String email, String password){
        navigationMenu = loginToBeaconoid(webDriver, email, password);
        assertEquals(WebConstants.baseUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 1, testName = "TC_NM_01")
    public void chkIfDashOnClkBeaconoid() {
        navigationMenu.clickBeconoid();
        assertEquals(WebConstants.dashboardUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 2, testName = "TC_NM_02")
    public void chkIfDashOnClkDashboard() {
        DashboardPage dashboardPage = accessDashboardPage(webDriver, navigationMenu);

    }

    @Test(priority = 3, testName = "TC_NM_03")
    public void chkIfRepOnClickReports(){
        ReportPage reportPage = accessReportPage(webDriver, navigationMenu);

    }

    @Test(priority = 4, testName = "TC_NM_04")
    public void chkIFStrRepOnClickStore(){
        StoreReportPage storeReportPage = accessStoreReportPage(webDriver, navigationMenu);
    }

    @Test(priority = 5, testName = "TC_NM_05")
    public void chkIfSalRepOnClickSales(){
        SalesReportPage salesReportPage = accessSalesReportPage(webDriver, navigationMenu);
    }

    @Test(priority = 6, testName = "TC_NM_06")
    public void chkIfCateRepOnClickCategory(){
        CategoryReportPage categoryReportPage = accessCategoryReportPage(webDriver, navigationMenu);
    }

    @Test(priority = 7, testName = "TC_NM_07")
    public void chkIfSPOnClickStores() {
        StoresPage storesPage = accessStoresPage(webDriver, navigationMenu);
    }

    @Test(priority = 8, testName = "TC_NM_08")
    public void chkIfCPOnClickCategories() {
        CategoryPage categoryPage = accessCategoriesPage(webDriver, navigationMenu);
    }

    @Test(priority = 9, testName = "TC_NM_09")
    public void chkIfBPOnClickBeacons() throws InterruptedException {
        BeaconsPage beaconsPage = accessBeaconsPage(webDriver, navigationMenu);
    }

    @Test(priority = 10, testName = "TC_NM_10")
    public void chkIfAPOnClickAdvertisements() {
        AdvertisementsPage advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);
    }

    @Test(priority = 11, testName = "TC_NM_11")
    public void chkIfSPOnClickStaffs() {
        staffPage = accessStaffPage(webDriver, navigationMenu);
    }

    @Test(priority = 12, testName = "TC_NM_12")
    @Parameters({"email"})
    public void chkIfUNVisibleOnNav(String email){
        if(!webDriver.getCurrentUrl().equals(WebConstants.staffUrl))
            staffPage = accessStaffPage(webDriver, navigationMenu);
        Staff staff = staffPage.getStaffByEmail(email);
        assertEquals(staff.getName(), navigationMenu.getUserName());
    }


    @Test(priority = 12, testName = "TC_NM_13")
    public void checkIfLPOnLogout() {
        LoginPage loginPage = navigationMenu.clickLogoutLink();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds * 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.baseUrl, webDriver.getCurrentUrl());
        assertEquals(WebConstants.loginTitle, loginPage.getTitleText());
    }
}
