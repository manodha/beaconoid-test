package com.company.testscripts;

import com.company.model.Advertisement;
import com.company.model.Beacons;
import com.company.pageobjects.*;
import com.company.util.Constants;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNull;

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
        assertEquals(Constants.baseUrl, webDriver.getCurrentUrl());

        storesPage = accessStoresPage(navigationMenu);
        createStore(storesPage, Constants.defaultTestStore);

        categoryPage = accessCategoriesPage(navigationMenu);
        createCategory(categoryPage, Constants.defaultTestCategory);

        beaconsPage = accessBeaconsPage(navigationMenu);
        createBeacon(beaconsPage, Constants.defaultTestBeacon);

        testBeacon = beaconsPage.getBeacon(beaconsPage.getAllBeacons(), Constants.defaultTestBeacon.getUniqueRef(), Constants.defaultTestBeacon.getName());
        beaconUrl = testBeacon.getAdvertisementsLink().getAttribute("href");
        addBeaconAdverUrl = beaconUrl + addBeaconAdverUrl;

        beaconAdvPage = viewBeaconAdvertisements(beaconsPage, testBeacon);
    }

    @Test(priority = 1, testName = "TC126")
    public void checkIFNoAdverMsgIsShown() {
        assertEquals(beaconUrl, webDriver.getCurrentUrl());
        assertNull(beaconAdvPage.getAllAdvertisements());
        assertEquals(Constants.noAdvertisementTxt, beaconAdvPage.getNoAdvertisementsTXT());
    }


    @Test(priority = 2, testName = "TC127")
    @Parameters({"adverName", "adverDescription", "adverImage", "adverPrice"})
    public void checkIfBeaconAdverCanBeCrea(String name, String desc, String image, String price) {
        assertEquals(beaconUrl, webDriver.getCurrentUrl());
        beaconAdvPage.clickNewAdvertisementBtn();
        assertEquals(addBeaconAdverUrl, webDriver.getCurrentUrl());
        beaconAdvPage.createUpdateAdvertisement(new Advertisement(name, Constants.defaultTestBeacon.getName(),
                Constants.defaultTestCategory.getCategoryName(), desc, image, price));

        if (!webDriver.getCurrentUrl().equals(beaconUrl)) {
            webDriver.get(beaconUrl);
            try {
                Thread.sleep(Constants.waitMilliSeconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertEquals(beaconUrl, webDriver.getCurrentUrl());
        assertThat(beaconAdvPage.getAllAdvertisements(), hasItem(allOf(
                hasProperty("name", equalTo(name)),
                hasProperty("beacon", equalTo(Constants.defaultTestBeacon.getName()))
        )));

    }

    @Test(priority = 3, testName = "TC128")
    public void checkIfListBeaconAdverIsShown() {
        assertEquals(beaconUrl, webDriver.getCurrentUrl());
        assertNotNull(beaconAdvPage.getAllAdvertisements());
    }

    @Test(priority = 4, testName = "TC129")
    @Parameters({"adverName", "adverNameNew", "adverDescriptionNew", "adverImageNew", "adverPriceNew"})
    public void checkIfBeaconAdverCanBeUpda(String adverName, String adverNameNew, String adverDescriptionNew, String image, String adverPriceNew) {

        Advertisement advertisement = beaconAdvPage.getAdvertisment(beaconAdvPage.getAllAdvertisements(), adverName, Constants.defaultTestBeacon.getName());
        beaconAdvPage.clickEditAdverBtn(advertisement.getEditLink());
        beaconAdvPage.createUpdateAdvertisement(new Advertisement(adverNameNew, Constants.defaultTestBeacon.getName(), Constants.defaultTestCategory.getCategoryName(),
                adverDescriptionNew, image, adverPriceNew));

        if (!webDriver.getCurrentUrl().equals(beaconUrl)) {
            webDriver.get(beaconUrl);
            try {
                Thread.sleep(Constants.waitMilliSeconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertEquals(beaconUrl, webDriver.getCurrentUrl());

        assertThat(beaconAdvPage.getAllAdvertisements(), not(hasItem(allOf(
                hasProperty("name", equalTo(adverName)),
                hasProperty("beacon", equalTo(Constants.defaultTestBeacon.getName()))
        ))));

        assertThat(beaconAdvPage.getAllAdvertisements(), hasItem(allOf(
                hasProperty("name", equalTo(adverNameNew)),
                hasProperty("beacon", equalTo(Constants.defaultTestBeacon.getName()))
        )));
    }

    @Test(priority = 5, testName = "TC130")
    @Parameters({"adverNameNew"})
    public void checkIfBeaconAdverCanBeDel(String adverName) {
        assertEquals(beaconUrl, webDriver.getCurrentUrl());
        Advertisement advertisement = beaconAdvPage.getAdvertisment(beaconAdvPage.getAllAdvertisements(), adverName, Constants.defaultTestBeacon.getName());
        beaconAdvPage.clickDeleteAdvBtn(advertisement.getDeleteBtn());

        if (!webDriver.getCurrentUrl().equals(beaconUrl)) {
            webDriver.get(beaconUrl);
            try {
                Thread.sleep(Constants.waitMilliSeconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        assertEquals(beaconUrl, webDriver.getCurrentUrl());

        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertThat(beaconAdvPage.getAllAdvertisements(), not(hasItem(allOf(
                hasProperty("name", equalTo(adverName)),
                hasProperty("beacon", equalTo(Constants.defaultTestBeacon.getName()))
        ))));
    }

    @Test(priority = 6, testName = "TC131")
    public void checkIfBeaconPageIsOnBack() {
        if (!webDriver.getCurrentUrl().equals(beaconUrl)) {
            webDriver.get(beaconUrl);
            try {
                Thread.sleep(Constants.waitMilliSeconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertEquals(beaconUrl, webDriver.getCurrentUrl());
        beaconAdvPage.clickBackLink();
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.beaconsUrl, webDriver.getCurrentUrl());
    }

    @AfterTest
    public void clearAllTestData() {
        beaconsPage = accessBeaconsPage(navigationMenu);
        deleteBeacon(beaconsPage, Constants.defaultTestBeacon);

        categoryPage = accessCategoriesPage(navigationMenu);
        deleteCategory(categoryPage, Constants.defaultTestCategory);

        storesPage = accessStoresPage(navigationMenu);
        deleteStore(storesPage, Constants.defaultTestStore);
    }
}
