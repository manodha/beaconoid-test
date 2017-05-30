package testscripts;

import model.Advertisement;
import model.Category;
import model.Stores;
import pageobjects.*;
import util.WebConstants;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;

import java.util.concurrent.TimeUnit;

import static org.testng.AssertJUnit.assertEquals;

/**
 * Created by manodha on 29/5/17.
 */
public class ReportPageTest extends FunctionalTest {

    private NavigationMenu defNavMenu;
    private WebDriver reportDriver;
    private ReportPage reportPage;

    private StoresPage storesPage;
    private CategoryPage categoryPage;
    private BeaconsPage beaconsPage;
    private AdvertisementsPage advertisementsPage;


    @BeforeTest
    @Parameters({"email", "password"})
    public void setupTestEnv(String email, String password) {
        defNavMenu = loginToBeaconoid(webDriver, email, password);

        reportDriver = new FirefoxDriver();
        reportDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        NavigationMenu reportNavMenu = loginToBeaconoid(reportDriver, email, password);
        reportPage = accessReportPage(reportDriver, reportNavMenu);
    }

    @Test(priority = 1, testName = "TC142")
    @Parameters({"storeName", "storeUniqueCode", "sales"})
    public void checkIfCountIncreOnStoreCrea(String name, String uniqueCode, String sales) {
        int storeCount = reportPage.getTotalStores();
        if (!webDriver.getCurrentUrl().equals(WebConstants.storesUrl))
            storesPage = accessStoresPage(webDriver, defNavMenu);
        createStore(webDriver, storesPage, new Stores(name, uniqueCode, sales));
        assertEquals(++storeCount, reportPage.getTotalStores());
    }

    @Test(priority = 2, testName = "TC143")
    @Parameters({"storeName", "storeUniqueCode"})
    public void checkIfCountIncreOnStoreUpda(String name, String uniqueCode) {
        int storeCount = reportPage.getTotalStores();
        Stores store = storesPage.getStore(storesPage.getAllStores(), name, uniqueCode);
        updateStore(webDriver, storesPage, store, WebConstants.defaultTestStore);
        assertEquals(storeCount, reportPage.getTotalStores());
    }

    @Test(priority = 3, testName = "TC144")
    @Parameters({"categoryName", "categoryDescription"})
    public void checkIfCountIncreOnCateCrea(String name, String desc) {
        int cateCount = reportPage.getTotalCategories();
        if (!webDriver.getCurrentUrl().equals(WebConstants.categoriesUrl))
            categoryPage = accessCategoriesPage(webDriver, defNavMenu);
        createCategory(webDriver, categoryPage, new Category(name, desc));
        assertEquals(++cateCount, reportPage.getTotalCategories());
    }

    @Test(priority = 4, testName = "TC145")
    @Parameters({"categoryName", "categoryDescription"})
    public void checkIfCountIncreOnCateUpda(String name, String desc) {
        int cateCount = reportPage.getTotalCategories();
        Category category = categoryPage.getCategory(categoryPage.getAllCategories(), name, desc);
        updateCategory(webDriver, categoryPage, category, WebConstants.defaultTestCategory);
        assertEquals(cateCount, reportPage.getTotalCategories());
    }

    @BeforeGroups("Advertisements")
    public void creaDefBeacon() {
        if (!webDriver.getCurrentUrl().equals(WebConstants.beaconsUrl))
            beaconsPage = accessBeaconsPage(webDriver, defNavMenu);
        createBeacon(webDriver, beaconsPage, WebConstants.defaultTestBeacon);
    }

    @Test(priority = 5, testName = "TC146", groups = "Advertisements")
    @Parameters({"adverName", "adverDescription", "adverImage", "adverPrice"})
    public void checkIfCountIncreOnAdverCrea(String name, String desc, String image, String price) {
        int adverCount = reportPage.getTotalAdvertisements();
        if (!webDriver.getCurrentUrl().equals(WebConstants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(webDriver, defNavMenu);
        createAdvertisement(webDriver, advertisementsPage, new Advertisement(name, WebConstants.defaultTestBeacon.getName(),
                WebConstants.defaultTestCategory.getCategoryName(), desc, image, price));
        assertEquals(++adverCount, reportPage.getTotalAdvertisements());
    }

    @Test(priority = 6, testName = "TC147", groups = "Advertisements")
    @Parameters({"adverName"})
    public void checkIfCountIncreOnAdverUpda(String name) {
        int adverCount = reportPage.getTotalAdvertisements();
        if (!webDriver.getCurrentUrl().equals(WebConstants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(webDriver, defNavMenu);
        Advertisement advertisement = advertisementsPage.getAdvertisment(advertisementsPage.getAllAdvertisements(),
                name, WebConstants.defaultTestBeacon.getName());

        updateAdvertisement(webDriver, advertisementsPage, advertisement, WebConstants.defaultTestAdvertisement);
        assertEquals(adverCount, reportPage.getTotalAdvertisements());
    }

    @Test(priority = 7, testName = "TC148", groups = "Advertisements")
    public void checkIfCountDecreOnDelAdver() {
        int adverCount = reportPage.getTotalAdvertisements();
        if (!webDriver.getCurrentUrl().equals(WebConstants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(webDriver, defNavMenu);
        deleteAdvertisement(webDriver, advertisementsPage, WebConstants.defaultTestAdvertisement);
        assertEquals(--adverCount, reportPage.getTotalAdvertisements());
    }

    @AfterGroups("Advertisements")
    public void delDefBeacon() {
        if (!webDriver.getCurrentUrl().equals(WebConstants.beaconsUrl))
            beaconsPage = accessBeaconsPage(webDriver, defNavMenu);
        deleteBeacon(webDriver, beaconsPage, WebConstants.defaultTestBeacon);
    }


    @Test(priority = 8, testName = "TC149")
    public void checkIfCountDecreOnDelCate() {
        int cateCount = reportPage.getTotalCategories();
        if (!webDriver.getCurrentUrl().equals(WebConstants.categoriesUrl))
            categoryPage = accessCategoriesPage(webDriver, defNavMenu);
        deleteCategory(webDriver, categoryPage, WebConstants.defaultTestCategory);
        assertEquals(--cateCount, reportPage.getTotalCategories());
    }

    @Test(priority = 9, testName = "TC150")
    public void checkIfCountDecreOnDelStore() {
        int storeCount = reportPage.getTotalStores();
        if (!webDriver.getCurrentUrl().equals(WebConstants.storesUrl))
            storesPage = accessStoresPage(webDriver, defNavMenu);
        deleteStore(webDriver, storesPage, WebConstants.defaultTestStore);
        assertEquals(--storeCount, reportPage.getTotalStores());
    }

    @AfterTest
    public void clearTestEnv() {
        closeBrowserInstance(reportDriver);
    }
}
