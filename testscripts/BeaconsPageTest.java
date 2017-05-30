package testscripts;

import model.Beacons;
import pageobjects.*;
import util.WebConstants;
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
        navigationMenu = loginToBeaconoid(webDriver, email, password);
        storesPage = navigationMenu.clickStoresLink();
        assertEquals(WebConstants.storesUrl, webDriver.getCurrentUrl());
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        createStore(webDriver, storesPage, WebConstants.defaultTestStore);

        categoryPage = navigationMenu.clickCatogoriesLink();
        assertEquals(WebConstants.categoriesUrl, webDriver.getCurrentUrl());
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        createCategory(webDriver, categoryPage, WebConstants.defaultTestCategory);

        beaconsPage = navigationMenu.clickBeconsLink();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.beaconsUrl, webDriver.getCurrentUrl());

    }

    @Test(priority = 1, testName = "TC032")
    @Parameters({"uniqueRef", "beaconName", "currentStatus", "latitude", "longitude"})
    public void checkIfBeaconBeCreaWRF(String uniqueRef, String beaconName, String currentStatus, String latitude, String longitude) {
        Beacons beacon = new Beacons(uniqueRef, beaconName, WebConstants.defaultTestStore.getName(), currentStatus, latitude, longitude);
        createBeacon(webDriver, beaconsPage, beacon);
        allBeacons = beaconsPage.getOtherBeaconsList();
        assertThat(allBeacons, hasItem(allOf(hasProperty("uniqueRef", equalTo(uniqueRef)),
                hasProperty("name", equalTo(beaconName)))));
    }

    @Test(priority = 2, testName = "TC031")
    @Parameters({"uniqueRef", "beaconName",})
    public void checkIfUCanSeeAListOfBeacon(String uniqueRef, String beaconName) {
        allBeacons = beaconsPage.getOtherBeaconsList();
        assertThat(allBeacons, hasItem(allOf(hasProperty("uniqueRef", equalTo(uniqueRef)),
                hasProperty("name", equalTo(beaconName)))));
    }

    @Test(priority = 3, testName = "TC033")
    @Parameters({"uniqueRef", "beaconName", "uniqueRefNew", "beaconNameNew", "currentStatusNew", "latitudeNew", "longitudeNew"})
    public void checkIfBeaconCanBeUpda(String uniqueRef, String beaconName, String uniqueRefNew, String beaconNameNew,
                                       String currentStatusNew, String latitudeNew, String longitudeNew) {
        allBeacons = beaconsPage.getOtherBeaconsList();
        Beacons beacon = beaconsPage.getBeacon(allBeacons, uniqueRef, beaconName);
        updateBeacon(webDriver, beaconsPage, beacon, new Beacons(uniqueRefNew, beaconNameNew, WebConstants.defaultTestStore.getName(), currentStatusNew, latitudeNew, longitudeNew));
        allBeacons = beaconsPage.getOtherBeaconsList();
        assertThat(allBeacons, hasItem(allOf(hasProperty("uniqueRef", equalTo(uniqueRefNew)),
                hasProperty("name", equalTo(beaconNameNew)))));
    }

    @Test(priority = 4, testName = "TC039")
    @Parameters({"uniqueRefNew", "beaconNameNew"})
    public void checkIfUCanSeeBeaconAdver(String uniqueRefNew, String beaconNameNew) {
        allBeacons = beaconsPage.getOtherBeaconsList();
        Beacons beacon = beaconsPage.getBeacon(allBeacons, uniqueRefNew, beaconNameNew);
        viewBeaconAdvertisements(beaconsPage, beacon);
    }

    @Test(priority = 5, testName = "TC034")
    @Parameters({"uniqueRefNew", "beaconNameNew"})
    public void checkIfBeaconCanBeDel(String uniqueRefNew, String beaconNameNew) {
        if (!webDriver.getCurrentUrl().equals(WebConstants.beaconsUrl))
            advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);

        assertThat(advertisementsPage.getAllAdvertisements(), not(hasItem(
                hasProperty("beacon", equalTo(beaconNameNew))
        )));

        beaconsPage = accessBeaconsPage(webDriver, navigationMenu);
        deleteBeacon(webDriver, beaconsPage, new Beacons(uniqueRefNew, beaconNameNew));

        if (!webDriver.getCurrentUrl().equals(WebConstants.beaconsUrl))
            beaconsPage = accessBeaconsPage(webDriver, navigationMenu);

        assertThat(beaconsPage.getOtherBeaconsList(), not(hasItem(allOf(
                hasProperty("uniqueRef", equalTo(uniqueRefNew)),
                hasProperty("name", equalTo(beaconNameNew))
        ))));
    }

    @Test(priority = 6, testName = "TC036")
    @Parameters({"uniqueRef", "beaconName", "currentStatus", "latitude", "longitude"})
    public void checkIfBeaconBeCreaWORF(String uniqueRef, String beaconName, String currentStatus, String latitude,
                                        String longitude) {
        assertEquals(WebConstants.beaconsUrl, webDriver.getCurrentUrl());
        beaconsPage.clickNewBeaconBtn();
        assertEquals(WebConstants.addBeaconUrl, webDriver.getCurrentUrl());
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        beaconsPage.createUpdateBeacon(new Beacons("", "", "", "", "", ""));
        assertEquals(WebConstants.addBeaconUrl, webDriver.getCurrentUrl());

        beaconsPage.createUpdateBeacon(new Beacons(uniqueRef, "", "", "", "", ""));
        assertEquals(WebConstants.addBeaconUrl, webDriver.getCurrentUrl());

        beaconsPage.createUpdateBeacon(new Beacons(uniqueRef, beaconName, "", "", "", ""));
        assertEquals(WebConstants.addBeaconUrl, webDriver.getCurrentUrl());

        beaconsPage.createUpdateBeacon(new Beacons(uniqueRef, beaconName, WebConstants.defaultTestStore.getName(), "", "", ""));
        assertEquals(WebConstants.addBeaconUrl, webDriver.getCurrentUrl());

        beaconsPage.createUpdateBeacon(new Beacons(uniqueRef, beaconName, WebConstants.defaultTestStore.getName(), currentStatus, "", ""));
        assertEquals(WebConstants.addBeaconUrl, webDriver.getCurrentUrl());

        beaconsPage.createUpdateBeacon(new Beacons(uniqueRef, beaconName, WebConstants.defaultTestStore.getName(), currentStatus, latitude, ""));
        assertEquals(WebConstants.addBeaconUrl, webDriver.getCurrentUrl());

    }

    @Test(priority = 7, testName = "TC037")
    public void checkIfBeaconCanBeCreaWF() {
        assertEquals(WebConstants.addBeaconUrl, webDriver.getCurrentUrl());
        beaconsPage.createUpdateBeacon(WebConstants.defaultTestBeacon);
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.beaconsUrl, webDriver.getCurrentUrl());
        allBeacons = beaconsPage.getOtherBeaconsList();
        assertThat(allBeacons, hasItem(allOf(
                hasProperty("uniqueRef", equalTo(WebConstants.defaultTestBeacon.getUniqueRef())),
                hasProperty("name", equalTo(WebConstants.defaultTestBeacon.getName()
                )))));
    }

    @Test(priority = 8, testName = "TC038")
    @Parameters({"uniqueRef", "currentStatus", "latitude", "longitude"})
    public void checkIfBeaconBeCreaWSN(String uniqueRef, String currentStatus, String latitude, String longitude) {
        assertEquals(WebConstants.beaconsUrl, webDriver.getCurrentUrl());
        beaconsPage.clickNewBeaconBtn();
        assertEquals(WebConstants.addBeaconUrl, webDriver.getCurrentUrl());
        beaconsPage.createUpdateBeacon(new Beacons(uniqueRef, WebConstants.defaultTestBeacon.getName(), WebConstants.defaultTestBeacon.getStoreName(),
                currentStatus, latitude, longitude));
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.duplicateBeaconDanger, beaconsPage.getDangerAlert());

        beaconsPage = accessBeaconsPage(webDriver, navigationMenu);

        assertThat(beaconsPage.getOtherBeaconsList(), not(hasItem(allOf(
                hasProperty("uniqueRef", equalTo(uniqueRef)),
                hasProperty("name", equalTo(WebConstants.defaultTestBeacon.getName()))
        ))));
    }

    @Test(priority = 9, testName = "TC040")
    public void checkIfBeaconBeAssiAdver() {
        if (!webDriver.getCurrentUrl().equals(WebConstants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);

        createAdvertisement(webDriver, advertisementsPage, WebConstants.defaultTestAdvertisement);

        if (!webDriver.getCurrentUrl().equals(WebConstants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);

        assertThat(advertisementsPage.getAllAdvertisements(), hasItem(
                hasProperty("beacon", equalTo(WebConstants.defaultTestBeacon.getName()
                ))));

    }

    //@Test(priority = 10, testName = "TC035")
    public void checkIfBeaconBeDelAssiToAdv() {
        beaconsPage = accessBeaconsPage(webDriver, navigationMenu);
        deleteBeacon(webDriver, beaconsPage, WebConstants.defaultTestBeacon);

    }

    @AfterTest
    public void clearAllTestData() {
        advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);
        deleteAdvertisement(webDriver, advertisementsPage, WebConstants.defaultTestAdvertisement);

        beaconsPage = accessBeaconsPage(webDriver, navigationMenu);
        deleteBeacon(webDriver, beaconsPage, WebConstants.defaultTestBeacon);

        categoryPage = accessCategoriesPage(webDriver, navigationMenu);
        deleteCategory(webDriver, categoryPage, WebConstants.defaultTestCategory);

        storesPage = accessStoresPage(webDriver, navigationMenu);
        deleteStore(webDriver, storesPage, WebConstants.defaultTestStore);
    }

}
