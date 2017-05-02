package com.company.testscripts;

import com.company.model.Beacons;
import com.company.pageobjects.BeaconAdvPage;
import com.company.pageobjects.BeaconsPage;
import com.company.pageobjects.NavigationMenu;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.AssertJUnit.assertEquals;

/**
 * Created by manodha on 30/4/17.
 */
public class BeaconsPageTest extends FunctionalTest {

    private NavigationMenu navigationMenu;
    private BeaconsPage beaconsPage;
    private BeaconAdvPage beaconAdvPage;
    private List<Beacons> allBeacons;


    @BeforeTest
    @Parameters({"email", "password"})
    public void accessBeaconsPage(String email, String password) {
        navigationMenu = loginToBeaconoid(email, password);
        beaconsPage = navigationMenu.clickBeconsLink();
        assertEquals(beaconsUrl, webDriver.getCurrentUrl());
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        allBeacons = beaconsPage.getAllBeacons();
    }

    @Test(priority = 1)
    @Parameters({"uniqueRef", "beaconName", "store", "currentStatus", "latitude", "longitude"})
    public void createBeaconTC(String uniqueRef, String beaconName, String store, String currentStatus, String latitude, String longitude) {
        Beacons beacon = new Beacons(uniqueRef, beaconName, "", currentStatus, latitude, longitude);
        createBeacon(beacon);
    }

    @Test(priority = 2)
    @Parameters({"uniqueRef", "beaconName", "uniqueRefNew", "beaconNameNew", "storeNew", "currentStatusNew", "latitudeNew", "longitudeNew"})
    public void updateBeaconTC(String uniqueRef, String beaconName, String uniqueRefNew, String beaconNameNew, String storeNew,
                               String currentStatusNew, String latitudeNew, String longitudeNew) {
        allBeacons = beaconsPage.getAllBeacons();
        Beacons beacon = beaconsPage.getBeacon(allBeacons, uniqueRef, beaconName);
        updateBeacon(beacon, new Beacons(uniqueRefNew, beaconNameNew, "", currentStatusNew, latitudeNew, longitudeNew));

    }

    @Test(priority = 3)
    @Parameters({"uniqueRefNew", "beaconNameNew"})
    public void viewBeaconAdvertisementsTC(String uniqueRefNew, String beaconNameNew) {
        allBeacons = beaconsPage.getAllBeacons();
        Beacons beacon = beaconsPage.getBeacon(allBeacons, uniqueRefNew, beaconNameNew);
        viewAdvertisements(beacon);
    }

    @Test(priority = 4)
    @Parameters({"uniqueRefNew", "beaconNameNew"})
    public void deleteBeaconTC(String uniqueRefNew, String beaconNameNew) {
        webDriver.get(beaconsUrl);
        allBeacons = beaconsPage.getAllBeacons();
        Beacons beacon = beaconsPage.getBeacon(allBeacons, uniqueRefNew, beaconNameNew);
        beaconsPage.clickDeleteBeaconBtn(beacon.getDeleteLink());
    }


    private void createBeacon(Beacons newBeacon) {
        assertEquals(beaconsUrl, webDriver.getCurrentUrl());
        beaconsPage.clickNewBeaconBtn();
        assertEquals(addBeaconUrl, webDriver.getCurrentUrl());
        beaconsPage.createUpdateBeacon(newBeacon);
    }

    private void updateBeacon(Beacons beacon, Beacons newBeacon) {
        assertEquals(beaconsUrl, webDriver.getCurrentUrl());
        beaconsPage.clickEditBeaconBtn(beacon.getEditLink());
        assertEquals(beacon.getEditLink().getAttribute("href"), webDriver.getCurrentUrl());
        beaconsPage.createUpdateBeacon(newBeacon);
    }

    private void viewAdvertisements(Beacons beacon) {
        assertEquals(beaconsUrl, webDriver.getCurrentUrl());
        beaconAdvPage = beaconsPage.clickViewAdvertisementsLink(beacon.getAdvertisementsLink());
        assertEquals(beacon.getAdvertisementsLink().getAttribute("href"), webDriver.getCurrentUrl());
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(beaconAdvPage.getBeaconName(), beacon.getName());
        assertEquals(beaconAdvPage.getBeaconUniqueRef(), beacon.getUniqueRef());
    }

}
