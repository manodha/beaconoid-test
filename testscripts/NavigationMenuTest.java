package testscripts;

import model.Staff;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.*;
import util.WebConstants;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


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

    @Test(priority = 1, testName = "TC_NM_01", description = "Verify if the dashboard is displayed when user click on the \"Beaconoid\" . https://www.beaconoid.me/dashboard")
    public void chkIfDashOnClkBeaconoid() {
        navigationMenu.clickBeconoid();
        assertEquals(WebConstants.dashboardUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 2, testName = "TC_NM_02", description = "Verify that user is redirected to the \"Dashboard\" if the user clicks on the option Dashboard in the navigation menu https://www.beaconoid.me/dashboard")
    public void chkIfDashOnClkDashboard() {
        DashboardPage dashboardPage = accessDashboardPage(webDriver, navigationMenu);

    }

    @Test(priority = 3, testName = "TC_NM_03", description = "Verify that user is redirected to the Reports Page if the user clicks on the option \"Reports\" in the navigation menu https://www.beaconoid.me/report")
    public void chkIfRepOnClickReports(){
        ReportPage reportPage = accessReportPage(webDriver, navigationMenu);

    }

    @Test(priority = 4, testName = "TC_NM_04", description = "Verify that user is redirected to the Store Reports Page if the user clicks on the option \"Store\" under Reports in the navigation menu https://www.beaconoid.me/report/store")
    public void chkIFStrRepOnClickStore(){
        StoreReportPage storeReportPage = accessStoreReportPage(webDriver, navigationMenu);
    }

    @Test(priority = 5, testName = "TC_NM_05", description = "Verify that user is redirected to the Sales Reports Page if the user clicks on the option \"Sales\" under Reports in the navigation menu https://www.beaconoid.me/report/sales")
    public void chkIfSalRepOnClickSales(){
        SalesReportPage salesReportPage = accessSalesReportPage(webDriver, navigationMenu);
    }

    @Test(priority = 6, testName = "TC_NM_06", description = "Verify that user is redirected to the Category Reports Page if the user clicks on the option \"Category\" under Reports in the navigation menu https://www.beaconoid.me/report/category")
    public void chkIfCateRepOnClickCategory(){
        CategoryReportPage categoryReportPage = accessCategoryReportPage(webDriver, navigationMenu);
    }

    @Test(priority = 7, testName = "TC_NM_07", description = "Verify that user is directed to the Stores Page if the user clicks on the option \"Stores\" in the navigation menu https://www.beaconoid.me/stores")
    public void chkIfSPOnClickStores() {
        StoresPage storesPage = accessStoresPage(webDriver, navigationMenu);
    }

    @Test(priority = 8, testName = "TC_NM_08", description = "Verify that user is directed to the Categories page if the user clicks on the option \"Categories\" in the navigation menu https://www.beaconoid.me/categories")
    public void chkIfCPOnClickCategories() {
        CategoryPage categoryPage = accessCategoriesPage(webDriver, navigationMenu);
    }

    @Test(priority = 9, testName = "TC_NM_09", description = "Verify that user is directed to the Beacons page if the user clicks on the option \"Beacons\" in the navigation menu https://www.beaconoid.me/beacons")
    public void chkIfBPOnClickBeacons() throws InterruptedException {
        BeaconsPage beaconsPage = accessBeaconsPage(webDriver, navigationMenu);
    }

    @Test(priority = 10, testName = "TC_NM_10", description = "Verify that user is directed to the Advertisements page if the user clicks on the option \"Advertisements\" in the navigation menu https://www.beaconoid.me/advertisements")
    public void chkIfAPOnClickAdvertisements() {
        AdvertisementsPage advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);
    }

    @Test(priority = 11, testName = "TC_NM_11", description = "Verify that user is directed to the Staffs page if the user clicks on the option \"Staffs\" in the navigation menu https://www.beaconoid.me/staffs")
    public void chkIfSPOnClickStaffs() {
        staffPage = accessStaffPage(webDriver, navigationMenu);
    }

    @Test(priority = 12, testName = "TC_NM_12", description = "Verify the logged in User's name is shown in the Navigation Bar.")
    @Parameters({"email"})
    public void chkIfUNVisibleOnNav(String email){
        if(!webDriver.getCurrentUrl().equals(WebConstants.staffUrl))
            staffPage = accessStaffPage(webDriver, navigationMenu);
        Staff staff = staffPage.getStaffByEmail(email);
        assertEquals(staff.getName(), navigationMenu.getUserName());
    }

    @Test(priority = 13, testName = "TC_NM_13", description = "Verify if the User is directed to the Loign Page when clicked on the Logout button on top of the Navigation Bar")
    public void checkIfLPOnLogout() {
        LoginPage loginPage = navigationMenu.clickLogoutLink();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds * 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.baseUrl, webDriver.getCurrentUrl());
        assertTrue(loginPage.isLoginPage());
    }
}
