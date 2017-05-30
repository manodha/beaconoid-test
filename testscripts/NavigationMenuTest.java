package testscripts;

import pageobjects.LoginPage;
import pageobjects.NavigationMenu;
import util.WebConstants;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * Created by manodha on 9/4/17.
 */
public class NavigationMenuTest extends FunctionalTest {

    private NavigationMenu navigationMenu;


    @Test(testName = "TC008", priority = 1)
    @Parameters({"email", "password"})
    public void isDashboard(String email, String password) {
        navigationMenu = loginToBeaconoid(webDriver, email, password);
        assertEquals(WebConstants.baseUrl, webDriver.getCurrentUrl());
        assertNotNull(navigationMenu.getLogoutLink());

    }

    @Test(testName = "TC010", priority = 2)
    public void clickBeaconoid() {
        navigationMenu.clickBeconsWebConsole();
        assertEquals(WebConstants.dashboardUrl, webDriver.getCurrentUrl());

    }

    @Test(testName = "TC011", priority = 3)
    public void clickDashboard() {
        navigationMenu.clickDashboardLink();
        assertEquals(WebConstants.dashboardUrl, webDriver.getCurrentUrl());
    }

    @Test(testName = "TC012", priority = 4)
    public void clickStores() {
        navigationMenu.clickStoresLink();
        assertEquals(WebConstants.storesUrl, webDriver.getCurrentUrl());
    }

    @Test(testName = "TC013", priority = 5)
    public void clickCatogories() {
        navigationMenu.clickCatogoriesLink();
        assertEquals(WebConstants.categoriesUrl, webDriver.getCurrentUrl());
    }

    @Test(testName = "TC014", priority = 6)
    public void clickBeacons() {
        navigationMenu.clickBeconsLink();
        assertEquals(WebConstants.beaconsUrl, webDriver.getCurrentUrl());
    }

    @Test(testName = "TC015", priority = 7)
    public void clickAdvertisements() {
        navigationMenu.clickAdvertisementsLink();
        assertEquals(WebConstants.advertisementsUrl, webDriver.getCurrentUrl());
    }

    @Test(testName = "TC016", priority = 8)
    public void clickStaff() {
        navigationMenu.clickStaffLink();
        assertEquals(WebConstants.staffUrl, webDriver.getCurrentUrl());
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(testName = "TC009", priority = 9)
    public void clickLogout() {
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
