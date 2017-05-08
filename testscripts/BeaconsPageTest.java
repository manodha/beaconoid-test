package com.company.testscripts;

import com.company.model.Advertisement;
import com.company.model.Beacons;
import com.company.pageobjects.AdvertisementsPage;
import com.company.pageobjects.BeaconsPage;
import com.company.pageobjects.NavigationMenu;
import com.company.pageobjects.StoresPage;
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
    private List<Beacons> allBeacons;


    @BeforeTest
    @Parameters({"email", "password"})
    public void accessBeaconsPage(String email, String password) {
        navigationMenu = loginToBeaconoid(email, password);
        storesPage = navigationMenu.clickStoresLink();
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(storesUrl, webDriver.getCurrentUrl());
        createStore(storesPage, defaultTestStore);

    }

    @Test(priority = 1, testName = "TC031")
    public void getListOfBeaconsTC031() {
        if (!webDriver.getCurrentUrl().equals(beaconsUrl)) {
            beaconsPage = navigationMenu.clickBeconsLink();
            try {
                Thread.sleep(waitMilliSeconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            assertEquals(beaconsUrl, webDriver.getCurrentUrl());
        }
        createBeacon(beaconsPage, defaultTestBeacon);
        allBeacons = beaconsPage.getAllBeacons();
        assertThat(allBeacons, hasItem(allOf(hasProperty("uniqueRef", equalTo(defaultTestBeacon.getUniqueRef())),
                hasProperty("name", equalTo(defaultTestBeacon.getName())))));
    }

    @Test(priority = 2, testName = "TC032")
    @Parameters({"uniqueRef", "beaconName", "currentStatus", "latitude", "longitude"})
    public void createBeaconTC032(String uniqueRef, String beaconName, String currentStatus, String latitude, String longitude) {
        Beacons beacon = new Beacons(uniqueRef, beaconName, defaultTestStore.getName(), currentStatus, latitude, longitude);
        createBeacon(beaconsPage, beacon);
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

        beaconsPage = navigationMenu.clickBeconsLink();
        assertEquals(beaconsUrl, webDriver.getCurrentUrl());
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Beacons beacon = beaconsPage.getBeacon(beaconsPage.getAllBeacons(), uniqueRefNew, beaconNameNew);
        beaconsPage.clickDeleteBeaconBtn(beacon.getDeleteLink());
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
