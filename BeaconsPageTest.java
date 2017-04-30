package com.company;

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
        Beacons beacon = getBeacon(allBeacons, uniqueRef, beaconName);
        Beacons updateBeacon = new Beacons(uniqueRefNew, beaconNameNew, "", currentStatusNew, latitudeNew, longitudeNew);
        updateBeacon(beacon, updateBeacon);
    }

    @Test(priority = 3)
    @Parameters({"uniqueRefNew", "beaconNameNew"})
    public void deleteBeaconTC(String uniqueRefNew, String beaconNameNew) {
        allBeacons = beaconsPage.getAllBeacons();
        Beacons beacon = getBeacon(allBeacons, uniqueRefNew, beaconNameNew);
        deleteBeacon(beacon);
    }


    private void createBeacon(Beacons newBeacon) {
        assertEquals(beaconsUrl, webDriver.getCurrentUrl());
        beaconsPage.clickNewBeaconBtn();
        assertEquals(addBeaconUrl, webDriver.getCurrentUrl());
        enterBeaconDetails(newBeacon);
    }

    private void updateBeacon(Beacons beacon, Beacons newBeacon) {
        assertEquals(beaconsUrl, webDriver.getCurrentUrl());
        beaconsPage.clickEditBeaconBtn(beacon.getEditLink());
        assertEquals(beacon.getEditLink().getAttribute("href"), webDriver.getCurrentUrl());
        enterBeaconDetails(newBeacon);
    }

    private void deleteBeacon(Beacons beacon) {
        beaconsPage.clickDeleteBeaconBtn(beacon.getDeleteLink());
    }

    private Beacons getBeacon(List<Beacons> beacons, String uniqueRef, String beaconName) {
        assertEquals(beaconsUrl, webDriver.getCurrentUrl());
        for (Beacons beacon : beacons) {
            if (beacon.getUniqueRef().equals(uniqueRef) && beacon.getName().equals(beaconName)) {
                return beacon;
            }
        }
        return null;
    }

    private void enterBeaconDetails(Beacons newBeacon) {
        beaconsPage.enterBeaconsName(newBeacon.getName());
        if (newBeacon.getStoreName().equals("")) {
            beaconsPage.selectStoreByIndex(0);
        } else {
            beaconsPage.selectStoreByName(newBeacon.getStoreName());
        }
        beaconsPage.enterCurrentStatus(newBeacon.getStatus());
        beaconsPage.enterUniqueRef(newBeacon.getUniqueRef());
        beaconsPage.enterLatitude(newBeacon.getLatitude());
        beaconsPage.enterLongitude(newBeacon.getLongitude());
        beaconsPage.clickCreateUpdateBeaconBtn();
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(beaconsUrl, webDriver.getCurrentUrl());
    }

    private void printAllBeacons(List<Beacons> beacons) {
        System.out.println("*********** All Beacons ***********");
        for (Beacons beacon : beacons) {
            printBeacon(beacon);
        }
    }

    private void printBeacon(Beacons beacon) {
        System.out.print("Unique Reference - " + beacon.getUniqueRef());
        System.out.print(" Beacons Name - " + beacon.getName());
        System.out.print(" Store Name - " + beacon.getStoreName());
        System.out.print(" Status - " + beacon.getStatus());
        System.out.print(" Latitude - " + beacon.getLatitude());
        System.out.println(" Longitude - " + beacon.getLongitude());
    }
}
