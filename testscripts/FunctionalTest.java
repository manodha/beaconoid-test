package com.company.testscripts;

import com.company.model.Advertisement;
import com.company.model.Beacons;
import com.company.model.Category;
import com.company.model.Stores;
import com.company.pageobjects.*;
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
public class FunctionalTest {

    protected static WebDriver webDriver;
    static int waitMilliSeconds = 1500;

    static Stores defaultTestStore = new Stores("TGI Fridays Fountain Gate", "TGI001",
            "https://tgifridays.com/image.jpg");
    static Category defaultTestCategory = new Category("Restaurants & Cafes",
            "Including but not limited to Fast Food Places");
    static Beacons defaultTestBeacon = new Beacons("BE010", "TGI BEACON", "TGI Fridays Fountain Gate",
            "Active", "38.0180° S", "145.3039° E");
    static Advertisement defaultTestAdvertisement = new Advertisement("50% Deal on Onion Rings", "TGI BEACON",
            "Restaurants & Cafes", "50% off on the side onion rings when purchased with meal", "$10.00");

    /* Production base URL */
    /*static String baseUrl = "https://www.beaconoid.me/";*/

    /* Development base URL*/
    static String baseUrl = "http://localhost:3000/";

    static String loginUrl = baseUrl + "users/sign_in";
    static String dashboardUrl = baseUrl + "dashboard";
    static String storesUrl = baseUrl + "stores";
    static String addStoreUrl = baseUrl + "stores/new";
    static String categoriesUrl = baseUrl + "categories";
    static String addCategoryUrl = baseUrl + "categories/new";
    static String beaconsUrl = baseUrl + "beacons";
    static String advertisementsUrl = baseUrl + "advertisements";
    static String addBeaconUrl = baseUrl + "beacons/new";
    static String duplicateBeaconDanger = "";
    private static String addAdvertisementUrl = baseUrl + "advertisements/new";

    //protected static String settingsUrl = baseUrl + ""

    @BeforeSuite
    public static void setUp() {
        System.setProperty("webdriver.gecko.driver", "/Users/manodha/Selenium Web Driver/geckodriver");
        webDriver = new FirefoxDriver();
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterSuite
    public static void tearDown() {
        webDriver.manage().deleteAllCookies();
        webDriver.close();
        webDriver.quit();
    }

    static void deleteStore(StoresPage storesPage, Stores store) {
        Stores testStore = storesPage.getStore(storesPage.getAllStores(), store.getName(), store.getStoreCode());
        storesPage.clickDeleteButton(testStore.getDeleteBtn());
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void deleteCategory(CategoryPage categoryPage, Category category) {
        Category testCategory = categoryPage.getCategory(categoryPage.getAllCategories(), category.getCategoryName(),
                category.getCategoryDescription());
        categoryPage.clickDeleteCategoryBtn(testCategory.getDeleteButton());
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void deleteBeacon(BeaconsPage beaconsPage, Beacons beacon) {
        Beacons testBeacon = beaconsPage.getBeacon(beaconsPage.getAllBeacons(), beacon.getUniqueRef(),
                beacon.getName());
        beaconsPage.clickDeleteBeaconBtn(testBeacon.getDeleteLink());
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void deleteAdvertisement(AdvertisementsPage advertisementsPage, Advertisement advertisement) {
        Advertisement testAdvertisement = advertisementsPage.getAdvertisment(advertisementsPage.getAllAdvertisements(),
                advertisement.getName(), advertisement.getBeacon());
        advertisementsPage.clickDeleteAdvBtn(testAdvertisement.getDeleteBtn());
        try {
            Thread.sleep(waitMilliSeconds);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    NavigationMenu loginToBeaconoid(String email, String password) {
        webDriver.get(loginUrl);
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        NavigationMenu navigationMenu = loginPage.login();
        assertEquals("Signed in successfully.", navigationMenu.getSucessAlert());
        return navigationMenu;
    }

    StoresPage accessStoresPage(NavigationMenu navigationMenu) {
        StoresPage storesPage = navigationMenu.clickStoresLink();
        assertEquals(storesUrl, webDriver.getCurrentUrl());
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return storesPage;
    }

    void createStore(StoresPage storesPage, Stores store) {
        assertEquals(storesUrl, webDriver.getCurrentUrl());
        storesPage.clickNewStore();
        assertEquals(addStoreUrl, webDriver.getCurrentUrl());
        storesPage.createUpdateStore(store);
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(storesUrl, webDriver.getCurrentUrl());
    }

    CategoryPage accessCategoriesPage(NavigationMenu navigationMenu) {
        CategoryPage categoryPage = navigationMenu.clickCatogoriesLink();
        assertEquals(categoriesUrl, webDriver.getCurrentUrl());
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return categoryPage;
    }

    void createCategory(CategoryPage categoryPage, Category category) {
        assertEquals(categoriesUrl, webDriver.getCurrentUrl());
        categoryPage.clickNewCategoryBtn();
        assertEquals(addCategoryUrl, webDriver.getCurrentUrl());
        categoryPage.createUpdateCategory(category);
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(categoriesUrl, webDriver.getCurrentUrl());
    }

    BeaconsPage accessBeaconsPage(NavigationMenu navigationMenu) {
        BeaconsPage beaconsPage = navigationMenu.clickBeconsLink();
        assertEquals(beaconsUrl, webDriver.getCurrentUrl());
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return beaconsPage;
    }

    void createBeacon(BeaconsPage beaconsPage, Beacons newBeacon) {
        assertEquals(beaconsUrl, webDriver.getCurrentUrl());
        beaconsPage.clickNewBeaconBtn();
        assertEquals(addBeaconUrl, webDriver.getCurrentUrl());
        beaconsPage.createUpdateBeacon(newBeacon);
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(beaconsUrl, webDriver.getCurrentUrl());
    }

    BeaconAdvPage viewBeaconAdvertisements(BeaconsPage beaconsPage, Beacons beacon) {
        assertEquals(beaconsUrl, webDriver.getCurrentUrl());
        BeaconAdvPage beaconAdvPage = beaconsPage.clickViewAdvertisementsLink(beacon.getAdvertisementsLink());
        assertEquals(beacon.getAdvertisementsLink().getAttribute("href"), webDriver.getCurrentUrl());
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(beaconAdvPage.getBeaconName(), beacon.getName());
        assertEquals(beaconAdvPage.getBeaconUniqueRef(), beacon.getUniqueRef());
        return beaconAdvPage;
    }

    AdvertisementsPage accessAdvertisementsPage(NavigationMenu navigationMenu) {
        AdvertisementsPage advertisementsPage = navigationMenu.clickAdvertisementsLink();
        assertEquals(advertisementsUrl, webDriver.getCurrentUrl());
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return advertisementsPage;
    }

    void createAdvertisement(AdvertisementsPage advertisementsPage, Advertisement advertisement) {
        assertEquals(advertisementsUrl, webDriver.getCurrentUrl());
        advertisementsPage.clickNewAdvertisementBtn();
        assertEquals(addAdvertisementUrl, webDriver.getCurrentUrl());
        advertisementsPage.createUpdateAdvertisement(advertisement);
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(advertisementsUrl, webDriver.getCurrentUrl());
    }


}
