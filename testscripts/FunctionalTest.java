package testscripts;

import model.*;
import pageobjects.*;
import util.WebConstants;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * Created by manodha on 27/3/17.
 */
public class FunctionalTest{

    protected static WebDriver webDriver;

    @BeforeSuite
    public static void setUp() {
        System.setProperty("webdriver.gecko.driver", "/Users/manodha/Selenium Web Driver/geckodriver");
        webDriver = new FirefoxDriver();
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterSuite
    public static void tearDown() {
        closeBrowserInstance(webDriver);
    }

    static void closeBrowserInstance(WebDriver webDriver) {
        webDriver.manage().deleteAllCookies();
        webDriver.close();
        webDriver.quit();
    }

    static void deleteStore(WebDriver webDriver, StoresPage storesPage, Stores store) {
        Stores testStore = storesPage.getStore(storesPage.getAllStores(), store.getName(), store.getStoreCode());
        storesPage.clickDeleteButton(testStore.getDeleteBtn());
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.storesUrl, webDriver.getCurrentUrl());
    }

    static void deleteCategory(WebDriver webDriver, CategoryPage categoryPage, Category category) {
        Category testCategory = categoryPage.getCategory(categoryPage.getAllCategories(), category.getCategoryName(),
                category.getCategoryDescription());
        categoryPage.clickDeleteCategoryBtn(testCategory.getDeleteButton());
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.categoriesUrl, webDriver.getCurrentUrl());
    }

    static void deleteBeacon(WebDriver webDriver, BeaconsPage beaconsPage, Beacons beacon) {
        Beacons testBeacon = beaconsPage.getBeacon(beaconsPage.getRegOtherBeacons(WebConstants.otherBeaconTitle), beacon.getUniqueRef(),
                beacon.getName());
        beaconsPage.clickDeleteBeaconBtn(testBeacon.getDeleteLink());
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.beaconsUrl, webDriver.getCurrentUrl());
    }

    static void deleteAdvertisement(WebDriver webDriver, AdvertisementsPage advertisementsPage, Advertisement advertisement) {
        Advertisement testAdvertisement = advertisementsPage.getAdvertisment(advertisementsPage.getAllAdvertisements(),
                advertisement.getName(), advertisement.getBeacon());
        advertisementsPage.clickDeleteAdvBtn(testAdvertisement.getDeleteBtn());
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.advertisementsUrl, webDriver.getCurrentUrl());
    }

    @AfterMethod
    public void printResult(ITestResult result) {
        System.out.println("******************************");
        System.out.println("Method Name - " + result.getName());
        System.out.print("Status - ");
        try {
            if (result.getStatus() == ITestResult.SUCCESS) {

                //Do something here
                System.out.println("Passed");
            } else if (result.getStatus() == ITestResult.FAILURE) {
                //Do something here
                System.out.println("Failed");

            } else if (result.getStatus() == ITestResult.SKIP) {

                System.out.println("Skiped");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    NavigationMenu loginToBeaconoid(WebDriver webDriver, String email, String password) {
        webDriver.get(WebConstants.loginUrl);
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        NavigationMenu navigationMenu = loginPage.login();
        assertEquals("Signed in successfully.", navigationMenu.getSucessAlert());
        return navigationMenu;
    }

    StoresPage accessStoresPage(WebDriver webDriver, NavigationMenu navigationMenu) {
        StoresPage storesPage = navigationMenu.clickStoresLink();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.storesUrl, webDriver.getCurrentUrl());
        return storesPage;
    }

    void createStore(WebDriver webDriver, StoresPage storesPage, Stores store) {
        assertEquals(WebConstants.storesUrl, webDriver.getCurrentUrl());
        storesPage.clickNewStore();
        assertEquals(WebConstants.addStoreUrl, webDriver.getCurrentUrl());
        storesPage.createUpdateStore(store);
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.storesUrl, webDriver.getCurrentUrl());
        assertEquals(WebConstants.creaStoreSucess, storesPage.getSucessAlert());
    }

    void updateStore(WebDriver webDriver, StoresPage storesPage, Stores oldStore, Stores newStore) {
        assertEquals(WebConstants.storesUrl, webDriver.getCurrentUrl());
        String editLink = oldStore.getEditLink().getAttribute("href");
        storesPage.clickEditButton(oldStore);
        assertEquals(editLink, webDriver.getCurrentUrl());
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        storesPage.createUpdateStore(newStore);
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.storesUrl, webDriver.getCurrentUrl());
    }

    CategoryPage accessCategoriesPage(WebDriver webDriver, NavigationMenu navigationMenu) {
        CategoryPage categoryPage = navigationMenu.clickCatogoriesLink();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.categoriesUrl, webDriver.getCurrentUrl());
        return categoryPage;
    }


    void createCategory(WebDriver webDriver, CategoryPage categoryPage, Category category) {
        assertEquals(WebConstants.categoriesUrl, webDriver.getCurrentUrl());
        categoryPage.clickNewCategoryBtn();
        assertEquals(WebConstants.addCategoryUrl, webDriver.getCurrentUrl());
        categoryPage.createUpdateCategory(category);
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.categoriesUrl, webDriver.getCurrentUrl());
    }

    void updateCategory(WebDriver webDriver, CategoryPage categoryPage, Category oldCategory, Category newCategory) {
        assertEquals(WebConstants.categoriesUrl, webDriver.getCurrentUrl());
        String editLink = oldCategory.getEditButton().getAttribute("href");
        categoryPage.clickEditCategoryBtn(oldCategory.getEditButton());
        assertEquals(editLink, webDriver.getCurrentUrl());
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        categoryPage.createUpdateCategory(newCategory);
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.categoriesUrl, webDriver.getCurrentUrl());
    }

    BeaconsPage accessBeaconsPage(WebDriver webDriver, NavigationMenu navigationMenu) throws InterruptedException {
        BeaconsPage beaconsPage = navigationMenu.clickBeconsLink();
        Thread.sleep(WebConstants.waitMilliSeconds);
        assertEquals(WebConstants.beaconsUrl, webDriver.getCurrentUrl());
        return beaconsPage;
    }

    void createBeacon(WebDriver webDriver, BeaconsPage beaconsPage, Beacons newBeacon) throws InterruptedException {
        assertEquals(WebConstants.beaconsUrl, webDriver.getCurrentUrl());
        beaconsPage.clickNewBeaconBtn();
        assertEquals(WebConstants.addBeaconUrl, webDriver.getCurrentUrl());
        Thread.sleep(WebConstants.waitMilliSeconds);
        beaconsPage.createUpdateBeacon(newBeacon);
        Thread.sleep(WebConstants.waitMilliSeconds);
        assertEquals(WebConstants.beaconsUrl, webDriver.getCurrentUrl());
    }

    void updateBeacon(WebDriver webDriver, BeaconsPage beaconsPage, Beacons beacon, Beacons newBeacon) throws InterruptedException {
        assertEquals(WebConstants.beaconsUrl, webDriver.getCurrentUrl());
        String editLink = beacon.getEditLink().getAttribute("href");
        beaconsPage.clickEditBeaconBtn(beacon.getEditLink());
        assertEquals(editLink, webDriver.getCurrentUrl());
        Thread.sleep(WebConstants.waitMilliSeconds);
        beaconsPage.createUpdateBeacon(newBeacon);
        Thread.sleep(WebConstants.waitMilliSeconds);
        assertEquals(WebConstants.beaconsUrl, webDriver.getCurrentUrl());
    }


    BeaconAdvPage viewBeaconAdvertisements(WebDriver webDriver, BeaconsPage beaconsPage, Beacons beacon) throws InterruptedException {
        assertEquals(WebConstants.beaconsUrl, webDriver.getCurrentUrl());
        String viewLink =beacon.getAdvertisementsLink().getAttribute("href");
        BeaconAdvPage beaconAdvPage = beaconsPage.clickViewAdvertisementsLink(beacon.getAdvertisementsLink());
        assertEquals(viewLink, webDriver.getCurrentUrl());
        Thread.sleep(WebConstants.waitMilliSeconds);
        assertEquals(beaconAdvPage.getBeaconName(), beacon.getName());
        assertEquals(beaconAdvPage.getBeaconUniqueRef(), beacon.getUniqueRef());
        return beaconAdvPage;
    }

    AdvertisementsPage accessAdvertisementsPage(WebDriver webDriver, NavigationMenu navigationMenu) {
        AdvertisementsPage advertisementsPage = navigationMenu.clickAdvertisementsLink();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.advertisementsUrl, webDriver.getCurrentUrl());
        return advertisementsPage;
    }

    void createAdvertisement(WebDriver webDriver, AdvertisementsPage advertisementsPage, Advertisement advertisement) {
        assertEquals(WebConstants.advertisementsUrl, webDriver.getCurrentUrl());
        advertisementsPage.clickNewAdvertisementBtn();
        assertEquals(WebConstants.addAdvertisementUrl, webDriver.getCurrentUrl());
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.newAdverTitle, advertisementsPage.getTitle());
        advertisementsPage.createUpdateAdvertisement(advertisement);
        try {
            Thread.sleep(WebConstants.waitMilliSeconds+1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.editAdverTitle, advertisementsPage.getTitle());
    }

    void updateAdvertisement(WebDriver webDriver, AdvertisementsPage advertisementsPage, Advertisement oldAdvertisement, Advertisement newAdvertisement) {
        String editLink = oldAdvertisement.getEditLink().getAttribute("href");
        assertEquals(WebConstants.advertisementsUrl, webDriver.getCurrentUrl());
        advertisementsPage.clickEditAdverBtn(oldAdvertisement.getEditLink());
        assertEquals(editLink, webDriver.getCurrentUrl());
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        advertisementsPage.createUpdateAdvertisement(newAdvertisement);
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(editLink, webDriver.getCurrentUrl());
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    StaffPage accessStaffPage(WebDriver webDriver, NavigationMenu navigationMenu) {
        StaffPage staffPage = navigationMenu.clickStaffLink();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.staffUrl, webDriver.getCurrentUrl());
        return staffPage;
    }

    void createStaff(StaffPage staffPage, Staff staff) {
        assertEquals(WebConstants.staffUrl, webDriver.getCurrentUrl());
        staffPage.clickNewStaffBtn();
        assertEquals(WebConstants.addStaffUrl, webDriver.getCurrentUrl());
        staffPage.creaUpdateStaff(staff);
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.staffUrl, webDriver.getCurrentUrl());
    }

    void deleteStaff(WebDriver webDriver, Staff staff, StaffPage staffPage) {
        assertEquals(WebConstants.staffUrl, webDriver.getCurrentUrl());
        staffPage.clickDeleteStaffBtn(staff.getDeleteBtn());
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.staffUrl, webDriver.getCurrentUrl());
    }


    DashboardPage accessDashboardPage(WebDriver webDriver, NavigationMenu navigationMenu) {
        DashboardPage dashboardPage = navigationMenu.clickDashboardLink();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.dashboardUrl, webDriver.getCurrentUrl());
        return dashboardPage;
    }


    ReportPage accessReportPage(WebDriver webDriver, NavigationMenu navigationMenu) {
        ReportPage reportPage = navigationMenu.clickReportLink();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.reportsUrl, webDriver.getCurrentUrl());
        return reportPage;
    }

    StoreReportPage accessStoreReportPage(WebDriver webDriver, NavigationMenu navigationMenu) {
        StoreReportPage storeReportPage = navigationMenu.clickStoreReportLink();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.storeReportsUrl, webDriver.getCurrentUrl());
        return storeReportPage;
    }

    SalesReportPage accessSalesReportPage(WebDriver webDriver, NavigationMenu navigationMenu) {
        SalesReportPage salesReportPage = navigationMenu.clickSalesReportLink();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.salesReportsUrl, webDriver.getCurrentUrl());
        return salesReportPage;
    }

    CategoryReportPage accessCategoryReportPage(WebDriver webDriver, NavigationMenu navigationMenu) {
        CategoryReportPage categoryReportPage = navigationMenu.clickCategoryReportLink();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.categoryReportsUrl, webDriver.getCurrentUrl());
        return categoryReportPage;
    }


}
