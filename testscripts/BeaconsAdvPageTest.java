package com.company.testscripts;

import com.company.model.Advertisement;
import com.company.model.Beacons;
import com.company.model.Category;
import com.company.model.Stores;
import com.company.pageobjects.*;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.AssertJUnit.assertEquals;

/**
 * Created by manodha on 2/5/17.
 */
public class BeaconsAdvPageTest extends FunctionalTest {

    private NavigationMenu navigationMenu;
    private BeaconAdvPage beaconAdvPage;
    private Beacons testBeacon;
    private BeaconsPage beaconsPage;
    private StoresPage storesPage;
    private CategoryPage categoryPage;
    private List<Advertisement> beaconAdvList = new ArrayList<>();


    private String beaconUrl;
    private String addBeaconAdverUrl = "/advertisements/new";


    @BeforeTest
    @Parameters({"emailAdmin", "passwordAdmin"})
    public void accessBeaconsAdvPage(String emailAdmin, String passwordAdmin) {
        navigationMenu = loginToBeaconoid(emailAdmin, passwordAdmin);
        assertEquals(baseUrl, webDriver.getCurrentUrl());

        storesPage = navigationMenu.clickStoresLink();
        createStore(storesPage, defaultTestStore);

        categoryPage = navigationMenu.clickCatogoriesLink();
        createCategory(categoryPage, defaultTestCategory);

        beaconsPage = navigationMenu.clickBeconsLink();
        createBeacon(beaconsPage, defaultTestBeacon);

        testBeacon = beaconsPage.getBeacon(beaconsPage.getAllBeacons(), defaultTestBeacon.getUniqueRef(), defaultTestBeacon.getName());
        beaconUrl = testBeacon.getAdvertisementsLink().getAttribute("href");
        addBeaconAdverUrl = beaconUrl + addBeaconAdverUrl;

        beaconAdvPage = viewBeaconAdvertisements(beaconsPage, testBeacon);
    }

    @Test(priority = 1)
    @Parameters({"adverName", "adverDescription", "adverPrice"})
    public void createBeaconAdvertisementTC(String adverName, String adverDescription, String adverPrice) {
        assertEquals(beaconUrl, webDriver.getCurrentUrl());
        beaconAdvPage.clickNewAdvertisementBtn();
        assertEquals(addBeaconAdverUrl, webDriver.getCurrentUrl());
        beaconAdvPage.createUpdateAdvertisement(new Advertisement(adverName, defaultTestBeacon.getName(),
                defaultTestCategory.getCategoryName(), adverDescription, adverPrice));
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test(priority = 2)
    @Parameters({"adverName", "adverNameNew", "adverDescriptionNew", "adverPriceNew"})
    public void updateBeaconAdvertisement(String adverName, String adverNameNew, String adverDescriptionNew, String adverPriceNew) {
        if (!webDriver.getCurrentUrl().equals(beaconUrl))
            webDriver.get(beaconUrl);
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Advertisement advertisement = beaconAdvPage.getAdvertisment(beaconAdvPage.getAllAdvertisements(), adverName, defaultTestBeacon.getName());
        beaconAdvPage.clickEditAdverBtn(advertisement.getEditLink());
        beaconAdvPage.createUpdateAdvertisement(new Advertisement(adverNameNew, defaultTestBeacon.getName(), defaultTestCategory.getCategoryName(),
                adverDescriptionNew, adverPriceNew));
    }

    @Test(priority = 3)
    @Parameters({"adverNameNew"})
    public void deleteBeaconAdvertisement(String adverName) {
        if (!webDriver.getCurrentUrl().equals(beaconUrl))
            webDriver.get(beaconUrl);
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Advertisement advertisement = beaconAdvPage.getAdvertisment(beaconAdvPage.getAllAdvertisements(), adverName, defaultTestBeacon.getName());
        beaconAdvPage.clickDeleteAdvBtn(advertisement.getDeleteBtn());
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterTest
    public void clearAllTestData() {
        navigationMenu.clickBeconsLink();
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(beaconsUrl, webDriver.getCurrentUrl());
        testBeacon = beaconsPage.getBeacon(beaconsPage.getAllBeacons(), defaultTestBeacon.getUniqueRef(), defaultTestBeacon.getName());
        beaconsPage.clickDeleteBeaconBtn(testBeacon.getDeleteLink());
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        navigationMenu.clickCatogoriesLink();
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(categoriesUrl, webDriver.getCurrentUrl());
        Category testCategory = categoryPage.getCategory(categoryPage.getAllCategories(), defaultTestCategory.getCategoryName(),
                defaultTestCategory.getCategoryDescription());
        categoryPage.clickDeleteCategoryBtn(testCategory.getDeleteButton());
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        navigationMenu.clickStoresLink();
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(storesUrl, webDriver.getCurrentUrl());
        Stores testStore = storesPage.getStore(storesPage.getAllStores(), defaultTestStore.getName(), defaultTestStore.getStoreCode());
        storesPage.clickDeleteButton(testStore.getDeleteBtn());
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
