package com.company.testscripts;

import com.company.model.*;
import com.company.pageobjects.*;
import com.company.util.Constants;
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
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void deleteCategory(CategoryPage categoryPage, Category category) {
        Category testCategory = categoryPage.getCategory(categoryPage.getAllCategories(), category.getCategoryName(),
                category.getCategoryDescription());
        categoryPage.clickDeleteCategoryBtn(testCategory.getDeleteButton());
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void deleteBeacon(BeaconsPage beaconsPage, Beacons beacon) {
        Beacons testBeacon = beaconsPage.getBeacon(beaconsPage.getAllBeacons(), beacon.getUniqueRef(),
                beacon.getName());
        beaconsPage.clickDeleteBeaconBtn(testBeacon.getDeleteLink());
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.beaconsUrl, webDriver.getCurrentUrl());
    }

    static void deleteAdvertisement(AdvertisementsPage advertisementsPage, Advertisement advertisement) {
        Advertisement testAdvertisement = advertisementsPage.getAdvertisment(advertisementsPage.getAllAdvertisements(),
                advertisement.getName(), advertisement.getBeacon());
        advertisementsPage.clickDeleteAdvBtn(testAdvertisement.getDeleteBtn());
        try {
            Thread.sleep(Constants.waitMilliSeconds);

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
        webDriver.get(Constants.loginUrl);
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        NavigationMenu navigationMenu = loginPage.login();
        assertEquals("Signed in successfully.", navigationMenu.getSucessAlert());
        return navigationMenu;
    }

    StoresPage accessStoresPage(NavigationMenu navigationMenu) {
        StoresPage storesPage = navigationMenu.clickStoresLink();
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.storesUrl, webDriver.getCurrentUrl());
        return storesPage;
    }

    void createStore(StoresPage storesPage, Stores store) {
        assertEquals(Constants.storesUrl, webDriver.getCurrentUrl());
        storesPage.clickNewStore();
        assertEquals(Constants.addStoreUrl, webDriver.getCurrentUrl());
        storesPage.createUpdateStore(store);
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.storesUrl, webDriver.getCurrentUrl());
    }

    CategoryPage accessCategoriesPage(NavigationMenu navigationMenu) {
        CategoryPage categoryPage = navigationMenu.clickCatogoriesLink();
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.categoriesUrl, webDriver.getCurrentUrl());
        return categoryPage;
    }

    void createCategory(CategoryPage categoryPage, Category category) {
        assertEquals(Constants.categoriesUrl, webDriver.getCurrentUrl());
        categoryPage.clickNewCategoryBtn();
        assertEquals(Constants.addCategoryUrl, webDriver.getCurrentUrl());
        categoryPage.createUpdateCategory(category);
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.categoriesUrl, webDriver.getCurrentUrl());
    }

    BeaconsPage accessBeaconsPage(NavigationMenu navigationMenu) {
        BeaconsPage beaconsPage = navigationMenu.clickBeconsLink();
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.beaconsUrl, webDriver.getCurrentUrl());
        return beaconsPage;
    }

    void createBeacon(BeaconsPage beaconsPage, Beacons newBeacon) {
        assertEquals(Constants.beaconsUrl, webDriver.getCurrentUrl());
        beaconsPage.clickNewBeaconBtn();
        assertEquals(Constants.addBeaconUrl, webDriver.getCurrentUrl());
        beaconsPage.createUpdateBeacon(newBeacon);
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.beaconsUrl, webDriver.getCurrentUrl());
    }

    void updateBeacon(BeaconsPage beaconsPage, Beacons beacon, Beacons newBeacon) {
        assertEquals(Constants.beaconsUrl, webDriver.getCurrentUrl());
        beaconsPage.clickEditBeaconBtn(beacon.getEditLink());
        assertEquals(beacon.getEditLink().getAttribute("href"), webDriver.getCurrentUrl());
        beaconsPage.createUpdateBeacon(newBeacon);
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.beaconsUrl, webDriver.getCurrentUrl());
    }

    BeaconAdvPage viewBeaconAdvertisements(BeaconsPage beaconsPage, Beacons beacon) {
        assertEquals(Constants.beaconsUrl, webDriver.getCurrentUrl());
        BeaconAdvPage beaconAdvPage = beaconsPage.clickViewAdvertisementsLink(beacon.getAdvertisementsLink());
        assertEquals(beacon.getAdvertisementsLink().getAttribute("href"), webDriver.getCurrentUrl());
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(beaconAdvPage.getBeaconName(), beacon.getName());
        assertEquals(beaconAdvPage.getBeaconUniqueRef(), beacon.getUniqueRef());
        return beaconAdvPage;
    }

    AdvertisementsPage accessAdvertisementsPage(NavigationMenu navigationMenu) {
        AdvertisementsPage advertisementsPage = navigationMenu.clickAdvertisementsLink();
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.advertisementsUrl, webDriver.getCurrentUrl());
        return advertisementsPage;
    }

    void createAdvertisement(AdvertisementsPage advertisementsPage, Advertisement advertisement) {
        assertEquals(Constants.advertisementsUrl, webDriver.getCurrentUrl());
        advertisementsPage.clickNewAdvertisementBtn();
        assertEquals(Constants.addAdvertisementUrl, webDriver.getCurrentUrl());
        advertisementsPage.createUpdateAdvertisement(advertisement);
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.advertisementsUrl, webDriver.getCurrentUrl());
    }

    void updateAdvertisement(AdvertisementsPage advertisementsPage, Advertisement oldAdvertisement, Advertisement newAdvertisement) {
        assertEquals(Constants.advertisementsUrl, webDriver.getCurrentUrl());
        advertisementsPage.clickEditAdverBtn(oldAdvertisement.getEditLink());
        assertEquals(oldAdvertisement.getEditLink().getAttribute("href"), webDriver.getCurrentUrl());
        advertisementsPage.createUpdateAdvertisement(newAdvertisement);
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.advertisementsUrl, webDriver.getCurrentUrl());
    }

    StaffPage accessStaffPage(NavigationMenu navigationMenu) {
        StaffPage staffPage = navigationMenu.clickStaffLink();
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.staffUrl, webDriver.getCurrentUrl());
        return staffPage;
    }

    void createStaff(StaffPage staffPage, Staff staff) {
        assertEquals(Constants.staffUrl, webDriver.getCurrentUrl());
        staffPage.clickNewStaffBtn();
        assertEquals(Constants.addStaffUrl, webDriver.getCurrentUrl());
        staffPage.creaUpdateStaff(staff);
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.staffUrl, webDriver.getCurrentUrl());
    }

    void deleteStaff(StaffPage staffPage, Staff staff) {
        assertEquals(Constants.staffUrl, webDriver.getCurrentUrl());
        staffPage.clickDeleteStaffBtn(staff.getDeleteBtn());
        try {
            Thread.sleep(Constants.waitMilliSeconds);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.staffUrl, webDriver.getCurrentUrl());
    }

    DashboardPage accessDashboardPage(NavigationMenu navigationMenu) {
        DashboardPage dashboardPage = navigationMenu.clickDashboardLink();
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.dashboardUrl, webDriver.getCurrentUrl());
        return dashboardPage;
    }


}
