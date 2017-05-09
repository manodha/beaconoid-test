package com.company.testscripts;

import com.company.model.Advertisement;
import com.company.model.Beacons;
import com.company.pageobjects.*;
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
        assertEquals(storesUrl, webDriver.getCurrentUrl());
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        createStore(storesPage, defaultTestStore);

        categoryPage = navigationMenu.clickCatogoriesLink();
        assertEquals(categoriesUrl, webDriver.getCurrentUrl());
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        createCategory(categoryPage, defaultTestCategory);

        beaconsPage = navigationMenu.clickBeconsLink();
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(beaconsUrl, webDriver.getCurrentUrl());

    }

    @Test(priority = 1, testName = "TC032")
    @Parameters({"uniqueRef", "beaconName", "currentStatus", "latitude", "longitude"})
    public void createBeaconTC032(String uniqueRef, String beaconName, String currentStatus, String latitude, String longitude) {
        Beacons beacon = new Beacons(uniqueRef, beaconName, defaultTestStore.getName(), currentStatus, latitude, longitude);
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
        updateBeacon(beacon, new Beacons(uniqueRefNew, beaconNameNew, defaultTestStore.getName(), currentStatusNew, latitudeNew, longitudeNew));
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
        AdvertisementsPage advertisementsPage = navigationMenu.clickAdvertisementsLink();
        assertEquals(advertisementsUrl, webDriver.getCurrentUrl());
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Advertisement> allAdvertisements = advertisementsPage.getAllAdvertisements();
        assertThat(allAdvertisements, not(hasItem(hasProperty("beacon", equalTo(beaconNameNew)))));

        beaconsPage = accessBeaconsPage(navigationMenu);
        Beacons beacon = beaconsPage.getBeacon(beaconsPage.getAllBeacons(), uniqueRefNew, beaconNameNew);
        beaconsPage.clickDeleteBeaconBtn(beacon.getDeleteLink());
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test(priority = 6, testName = "TC036")
    @Parameters({"uniqueRef", "beaconName", "currentStatus", "latitude", "longitude"})
    public void canBeaconsBeCreaWithOutRFTC036(String uniqueRef, String beaconName, String currentStatus, String latitude, String longitude) {
        assertEquals(beaconsUrl, webDriver.getCurrentUrl());
        beaconsPage.clickNewBeaconBtn();
        assertEquals(addBeaconUrl, webDriver.getCurrentUrl());
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        beaconsPage.createUpdateBeacon(new Beacons("", "", "", "", "", ""));
        assertEquals(addBeaconUrl, webDriver.getCurrentUrl());

        beaconsPage.createUpdateBeacon(new Beacons(uniqueRef, "", "", "", "", ""));
        assertEquals(addBeaconUrl, webDriver.getCurrentUrl());

        beaconsPage.createUpdateBeacon(new Beacons(uniqueRef, beaconName, "", "", "", ""));
        assertEquals(addBeaconUrl, webDriver.getCurrentUrl());

        beaconsPage.createUpdateBeacon(new Beacons(uniqueRef, beaconName, defaultTestStore.getName(), "", "", ""));
        assertEquals(addBeaconUrl, webDriver.getCurrentUrl());

        beaconsPage.createUpdateBeacon(new Beacons(uniqueRef, beaconName, defaultTestStore.getName(), currentStatus, "", ""));
        assertEquals(addBeaconUrl, webDriver.getCurrentUrl());

        beaconsPage.createUpdateBeacon(new Beacons(uniqueRef, beaconName, defaultTestStore.getName(), currentStatus, latitude, ""));
        assertEquals(addBeaconUrl, webDriver.getCurrentUrl());

    }

    @Test(priority = 7, testName = "TC037")
    public void canBeaconsBeCreaWithAllFTC037() {
        assertEquals(addBeaconUrl, webDriver.getCurrentUrl());
        beaconsPage.createUpdateBeacon(defaultTestBeacon);
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(beaconsUrl, webDriver.getCurrentUrl());
        allBeacons = beaconsPage.getAllBeacons();
        assertThat(allBeacons, hasItem(allOf(hasProperty("uniqueRef", equalTo(defaultTestBeacon.getUniqueRef())),
                hasProperty("name", equalTo(defaultTestBeacon.getName())))));
    }

    //@Test(priority = 8, testName = "TC038")
    @Parameters({"uniqueRef", "currentStatus", "latitude", "longitude"})
    public void checkIfBeaconBeCreaWithNameTC038(String uniqueRef, String currentStatus, String latitude, String longitude) {
        assertEquals(beaconsUrl, webDriver.getCurrentUrl());
        createBeacon(beaconsPage, new Beacons(uniqueRef, defaultTestBeacon.getName(), defaultTestBeacon.getStoreName(),
                currentStatus, latitude, longitude));
        allBeacons = beaconsPage.getAllBeacons();
        assertThat(allBeacons, hasItem(allOf(hasProperty("uniqueRef", equalTo(uniqueRef)),
                hasProperty("name", equalTo(defaultTestBeacon.getName())))));

        assertEquals(duplicateBeaconDanger, beaconsPage.getDangerAlert());
    }

    @Test(priority = 9, testName = "TC040")
    public void checkIfBeaconBeAssiAdverTC040() {
        advertisementsPage = navigationMenu.clickAdvertisementsLink();
        assertEquals(advertisementsUrl, webDriver.getCurrentUrl());
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        createAdvertisement(advertisementsPage, defaultTestAdvertisement);
        List<Advertisement> allAdvertisements = advertisementsPage.getAllAdvertisements();
        assertThat(allAdvertisements, hasItem(hasProperty("beacon", equalTo(defaultTestBeacon.getName()))));

    }

    //@Test(priority = 10, testName = "TC035")
    public void checkIfBeaconBeDelAssiToAdv() {
        beaconsPage = accessBeaconsPage(navigationMenu);
        deleteBeacon(beaconsPage, defaultTestBeacon);

    }

    @AfterTest
    public void clearAllTestData() {
        advertisementsPage = accessAdvertisementsPage(navigationMenu);
        deleteAdvertisement(advertisementsPage, defaultTestAdvertisement);

        beaconsPage = accessBeaconsPage(navigationMenu);
        deleteBeacon(beaconsPage, defaultTestBeacon);

        categoryPage = accessCategoriesPage(navigationMenu);
        deleteCategory(categoryPage, defaultTestCategory);

        storesPage = accessStoresPage(navigationMenu);
        deleteStore(storesPage, defaultTestStore);
    }

    private void updateBeacon(Beacons beacon, Beacons newBeacon) {
        assertEquals(beaconsUrl, webDriver.getCurrentUrl());
        beaconsPage.clickEditBeaconBtn(beacon.getEditLink());
        assertEquals(beacon.getEditLink().getAttribute("href"), webDriver.getCurrentUrl());
        beaconsPage.createUpdateBeacon(newBeacon);
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(beaconsUrl, webDriver.getCurrentUrl());
    }


}
