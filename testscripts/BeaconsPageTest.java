package com.company.testscripts;

import com.company.model.Beacons;
import com.company.pageobjects.*;
import com.company.util.Constants;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.testng.AssertJUnit.assertEquals;

/**
 * Created by manodha on 30/4/17.
 */
public class BeaconsPageTest extends FunctionalTest {

    private NavigationMenu navigationMenu;
    private BeaconsPage beaconsPage;
    private StoresPage storesPage;
    private CategoryPage categoryPage;
    private List<Beacons> allBeacons;
    private AdvertisementsPage advertisementsPage;


    @BeforeTest
    @Parameters({"email", "password"})
    public void accessBeaconsPage(String email, String password) {
        navigationMenu = loginToBeaconoid(email, password);
        storesPage = navigationMenu.clickStoresLink();
        assertEquals(Constants.storesUrl, webDriver.getCurrentUrl());
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        createStore(storesPage, Constants.defaultTestStore);

        categoryPage = navigationMenu.clickCatogoriesLink();
        assertEquals(Constants.categoriesUrl, webDriver.getCurrentUrl());
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        createCategory(categoryPage, Constants.defaultTestCategory);

        beaconsPage = navigationMenu.clickBeconsLink();
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.beaconsUrl, webDriver.getCurrentUrl());

    }

    @Test(priority = 1, testName = "TC032")
    @Parameters({"uniqueRef", "beaconName", "currentStatus", "latitude", "longitude"})
    public void createBeaconTC032(String uniqueRef, String beaconName, String currentStatus, String latitude, String longitude) {
        Beacons beacon = new Beacons(uniqueRef, beaconName, Constants.defaultTestStore.getName(), currentStatus, latitude, longitude);
        createBeacon(beaconsPage, beacon);
        allBeacons = beaconsPage.getAllBeacons();
        assertThat(allBeacons, hasItem(allOf(hasProperty("uniqueRef", equalTo(uniqueRef)),
                hasProperty("name", equalTo(beaconName)))));
    }

    @Test(priority = 2, testName = "TC031")
    @Parameters({"uniqueRef", "beaconName",})
    public void getListOfBeaconsTC031(String uniqueRef, String beaconName) {
        allBeacons = beaconsPage.getAllBeacons();
        assertThat(allBeacons, hasItem(allOf(hasProperty("uniqueRef", equalTo(uniqueRef)),
                hasProperty("name", equalTo(beaconName)))));
    }

    @Test(priority = 3, testName = "TC033")
    @Parameters({"uniqueRef", "beaconName", "uniqueRefNew", "beaconNameNew", "currentStatusNew", "latitudeNew", "longitudeNew"})
    public void updateBeaconTC033(String uniqueRef, String beaconName, String uniqueRefNew, String beaconNameNew,
                               String currentStatusNew, String latitudeNew, String longitudeNew) {
        allBeacons = beaconsPage.getAllBeacons();
        Beacons beacon = beaconsPage.getBeacon(allBeacons, uniqueRef, beaconName);
        updateBeacon(beaconsPage, beacon, new Beacons(uniqueRefNew, beaconNameNew, Constants.defaultTestStore.getName(), currentStatusNew, latitudeNew, longitudeNew));
        allBeacons = beaconsPage.getAllBeacons();
        assertThat(allBeacons, hasItem(allOf(hasProperty("uniqueRef", equalTo(uniqueRefNew)),
                hasProperty("name", equalTo(beaconNameNew)))));
    }

    @Test(priority = 4, testName = "TC039")
    @Parameters({"uniqueRefNew", "beaconNameNew"})
    public void viewBeaconAdvertisementsTC039(String uniqueRefNew, String beaconNameNew) {
        allBeacons = beaconsPage.getAllBeacons();
        Beacons beacon = beaconsPage.getBeacon(allBeacons, uniqueRefNew, beaconNameNew);
        viewBeaconAdvertisements(beaconsPage, beacon);
    }

    @Test(priority = 5, testName = "TC034")
    @Parameters({"uniqueRefNew", "beaconNameNew"})
    public void deleteBeaconTC034(String uniqueRefNew, String beaconNameNew) {
        AdvertisementsPage advertisementsPage = accessAdvertisementsPage(navigationMenu);


        assertThat(advertisementsPage.getAllAdvertisements(), not(hasItem(
                hasProperty("beacon", equalTo(beaconNameNew
                )))));

        beaconsPage = accessBeaconsPage(navigationMenu);
        deleteBeacon(beaconsPage, new Beacons(uniqueRefNew, beaconNameNew));

        allBeacons = beaconsPage.getAllBeacons();
        assertThat(allBeacons, not(hasItem(allOf(hasProperty("uniqueRef", equalTo(uniqueRefNew)),
                hasProperty("name", equalTo(beaconNameNew)
                )))));
    }

    @Test(priority = 6, testName = "TC036")
    @Parameters({"uniqueRef", "beaconName", "currentStatus", "latitude", "longitude"})
    public void canBeaconsBeCreaWithOutRFTC036(String uniqueRef, String beaconName, String currentStatus, String latitude,
                                               String longitude) {
        assertEquals(Constants.beaconsUrl, webDriver.getCurrentUrl());
        beaconsPage.clickNewBeaconBtn();
        assertEquals(Constants.addBeaconUrl, webDriver.getCurrentUrl());
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        beaconsPage.createUpdateBeacon(new Beacons("", "", "", "", "", ""));
        assertEquals(Constants.addBeaconUrl, webDriver.getCurrentUrl());

        beaconsPage.createUpdateBeacon(new Beacons(uniqueRef, "", "", "", "", ""));
        assertEquals(Constants.addBeaconUrl, webDriver.getCurrentUrl());

        beaconsPage.createUpdateBeacon(new Beacons(uniqueRef, beaconName, "", "", "", ""));
        assertEquals(Constants.addBeaconUrl, webDriver.getCurrentUrl());

        beaconsPage.createUpdateBeacon(new Beacons(uniqueRef, beaconName, Constants.defaultTestStore.getName(), "", "", ""));
        assertEquals(Constants.addBeaconUrl, webDriver.getCurrentUrl());

        beaconsPage.createUpdateBeacon(new Beacons(uniqueRef, beaconName, Constants.defaultTestStore.getName(), currentStatus, "", ""));
        assertEquals(Constants.addBeaconUrl, webDriver.getCurrentUrl());

        beaconsPage.createUpdateBeacon(new Beacons(uniqueRef, beaconName, Constants.defaultTestStore.getName(), currentStatus, latitude, ""));
        assertEquals(Constants.addBeaconUrl, webDriver.getCurrentUrl());

    }

    @Test(priority = 7, testName = "TC037")
    public void canBeaconsBeCreaWithAllFTC037() {
        assertEquals(Constants.addBeaconUrl, webDriver.getCurrentUrl());
        beaconsPage.createUpdateBeacon(Constants.defaultTestBeacon);
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.beaconsUrl, webDriver.getCurrentUrl());
        allBeacons = beaconsPage.getAllBeacons();
        assertThat(allBeacons, hasItem(allOf(
                hasProperty("uniqueRef", equalTo(Constants.defaultTestBeacon.getUniqueRef())),
                hasProperty("name", equalTo(Constants.defaultTestBeacon.getName()
                )))));
    }

    //@Test(priority = 8, testName = "TC038")
    @Parameters({"uniqueRef", "currentStatus", "latitude", "longitude"})
    public void checkIfBeaconBeCreaWithNameTC038(String uniqueRef, String currentStatus, String latitude, String longitude) {
        assertEquals(Constants.beaconsUrl, webDriver.getCurrentUrl());
        createBeacon(beaconsPage, new Beacons(uniqueRef, Constants.defaultTestBeacon.getName(), Constants.defaultTestBeacon.getStoreName(),
                currentStatus, latitude, longitude));
        allBeacons = beaconsPage.getAllBeacons();
        assertThat(allBeacons, hasItem(allOf(
                hasProperty("uniqueRef", equalTo(uniqueRef)),
                hasProperty("name", equalTo(Constants.defaultTestBeacon.getName()
                )))));

        assertEquals(Constants.duplicateBeaconDanger, beaconsPage.getDangerAlert());
    }

    @Test(priority = 9, testName = "TC040")
    public void checkIfBeaconBeAssiAdverTC040() {
        advertisementsPage = accessAdvertisementsPage(navigationMenu);
        createAdvertisement(advertisementsPage, Constants.defaultTestAdvertisement);
        assertThat(advertisementsPage.getAllAdvertisements(), hasItem(
                hasProperty("beacon", equalTo(Constants.defaultTestBeacon.getName()
                ))));

    }

    //@Test(priority = 10, testName = "TC035")
    public void checkIfBeaconBeDelAssiToAdv() {
        beaconsPage = accessBeaconsPage(navigationMenu);
        deleteBeacon(beaconsPage, Constants.defaultTestBeacon);

    }

    @AfterTest
    public void clearAllTestData() {
        advertisementsPage = accessAdvertisementsPage(navigationMenu);
        deleteAdvertisement(advertisementsPage, Constants.defaultTestAdvertisement);

        beaconsPage = accessBeaconsPage(navigationMenu);
        deleteBeacon(beaconsPage, Constants.defaultTestBeacon);

        categoryPage = accessCategoriesPage(navigationMenu);
        deleteCategory(categoryPage, Constants.defaultTestCategory);

        storesPage = accessStoresPage(navigationMenu);
        deleteStore(storesPage, Constants.defaultTestStore);
    }

}
