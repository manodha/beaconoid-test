package testscripts;

import model.Advertisement;
import model.Beacons;
import model.Category;
import model.Stores;
import org.testng.annotations.*;
import pageobjects.*;
import util.WebConstants;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
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
    public void setupTestData(String email, String password) {
        navigationMenu = loginToBeaconoid(webDriver, email, password);
        storesPage = accessStoresPage(webDriver, navigationMenu);
        createStore(webDriver, storesPage, WebConstants.defaultTestStore);

        categoryPage = accessCategoriesPage(webDriver, navigationMenu);
        createCategory(webDriver, categoryPage, WebConstants.defaultTestCategory);
    }


    @Test(priority = 1, testName = "TC_BP_01")
    public void chkIfUserCanAccessBP() throws InterruptedException {
        beaconsPage = accessBeaconsPage(webDriver, navigationMenu);
        assertEquals(WebConstants.beaconsUrl, webDriver.getCurrentUrl());
        assertEquals(WebConstants.beaconIndexTitle, beaconsPage.getIndexTitle());
    }

    @Test(priority = 2, testName = "TC_BP_02")
    public void chkIfNoOtherBeaconMsgIsShown(){
        assertEquals(WebConstants.beaconsUrl, webDriver.getCurrentUrl());
        assertEquals(WebConstants.noOtherBeaconTxt, beaconsPage.getNoOtherBeaconTxt());
    }

    @Test(priority = 3, testName = "TC_BP_03")
    @Parameters({"uniqueRef", "beaconName", "currentStatus", "latitude"})
    public void checkIfBeaconBeCreaWORF(String uniqueRef, String beaconName, String currentStatus, String latitude) throws InterruptedException {
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


        if(!webDriver.getCurrentUrl().equals(WebConstants.beaconsUrl))
            beaconsPage = accessBeaconsPage(webDriver, navigationMenu);

        assertThat(beaconsPage.getRegOtherBeacons(WebConstants.otherBeaconTitle), not(hasItem(allOf(
                hasProperty("uniqueRef", equalTo(uniqueRef)),
                hasProperty("name", equalTo(beaconName))
        ))));

    }

    @Test(priority = 4, testName = "TC_BP_04")
    @Parameters({"uniqueRef", "beaconName", "currentStatus", "latitude", "longitude"})
    public void checkIfBeaconBeCreaWRF(String uniqueRef, String beaconName, String currentStatus, String latitude,
                                       String longitude) throws InterruptedException {
        if(!webDriver.getCurrentUrl().equals(WebConstants.beaconsUrl))
            beaconsPage = accessBeaconsPage(webDriver, navigationMenu);

        Beacons beacon = new Beacons(uniqueRef, beaconName, WebConstants.defaultTestStore.getName(), currentStatus, latitude, longitude);
        createBeacon(webDriver, beaconsPage, beacon);
        allBeacons = beaconsPage.getRegOtherBeacons(WebConstants.otherBeaconTitle);
        assertThat(allBeacons, hasItem(allOf(hasProperty("uniqueRef", equalTo(uniqueRef)),
                hasProperty("name", equalTo(beaconName)))));
    }

    @Test(priority = 5, testName = "TC_BP_05")
    @Parameters({"uniqueRef", "beaconName", "currentStatus", "latitude", "longitude"})
    public void checkIfBeaconBeCreaWSN(String uniqueRef, String beaconName, String currentStatus, String latitude,
                                       String longitude) throws InterruptedException {
        assertEquals(WebConstants.beaconsUrl, webDriver.getCurrentUrl());
        beaconsPage.clickNewBeaconBtn();
        assertEquals(WebConstants.addBeaconUrl, webDriver.getCurrentUrl());
        beaconsPage.createUpdateBeacon(new Beacons(uniqueRef, beaconName, WebConstants.defaultTestBeacon.getStoreName(),
                currentStatus, latitude, longitude));
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.duplicateBeaconDanger, beaconsPage.getDangerAlert());

        // TODO Uncomment below line when the issue get resolved
        // if(!webDriver.getCurrentUrl().equals(WebConstants.beaconsUrl))
            beaconsPage = accessBeaconsPage(webDriver, navigationMenu);

        allBeacons = beaconsPage.getRegOtherBeacons(WebConstants.otherBeaconTitle);
        int numOfBeaconWSN = 0;
        for(Beacons beacon : allBeacons){
            if(beacon.getName().equals(beaconName))
                numOfBeaconWSN++;
        }
        // Verifying that there is only one beacon with the same name.
        assertEquals(1, numOfBeaconWSN);
    }

    @Test(priority = 6, testName = "TC_BP_06")
    @Parameters({"uniqueRef", "beaconName",})
    public void chkIfOBeaconIsInOBeaconTable(String uniqueRef, String beaconName) throws InterruptedException {
        if(!webDriver.getCurrentUrl().equals(WebConstants.beaconsUrl))
            beaconsPage = accessBeaconsPage(webDriver, navigationMenu);
        allBeacons = beaconsPage.getRegOtherBeacons(WebConstants.otherBeaconTitle);
        assertThat(allBeacons, hasItem(allOf(hasProperty("uniqueRef", equalTo(uniqueRef)),
                hasProperty("name", equalTo(beaconName)))));
    }

    @Test(priority = 7, testName = "TC_BP_07")
    @Parameters({"uniqueRef", "beaconName", "uniqueRefNew", "beaconNameNew", "currentStatusNew", "latitudeNew", "longitudeNew"})
    public void chkIfBeaconBeUpdaWORF(String oldRef, String oldName, String ref, String name, String status, String latitude,
                                         String longitude) throws InterruptedException {
        if(!webDriver.getCurrentUrl().equals(WebConstants.beaconsUrl))
            beaconsPage = accessBeaconsPage(webDriver, navigationMenu);
        Beacons beacon = beaconsPage.getBeacon(beaconsPage.getRegOtherBeacons(WebConstants.otherBeaconTitle), oldRef, oldName);
        String editlink = beacon.getEditLink().getAttribute("href");
        beaconsPage.clickEditBeaconBtn(beacon.getEditLink());
        Thread.sleep(WebConstants.waitMilliSeconds);
        assertEquals(editlink, webDriver.getCurrentUrl());

        beaconsPage.createUpdateBeacon(new Beacons("", "", "", "", "", ""));
        assertEquals(editlink, webDriver.getCurrentUrl());

        beaconsPage.createUpdateBeacon(new Beacons(ref, "", "", "", "", ""));
        assertEquals(editlink, webDriver.getCurrentUrl());

        beaconsPage.createUpdateBeacon(new Beacons(ref, name, "", "", "", ""));
        assertEquals(editlink, webDriver.getCurrentUrl());

        beaconsPage.createUpdateBeacon(new Beacons(ref, name, WebConstants.defaultTestStore.getName(), "", "", ""));
        assertEquals(editlink, webDriver.getCurrentUrl());

        beaconsPage.createUpdateBeacon(new Beacons(ref, name, WebConstants.defaultTestStore.getName(), status, "", ""));
        assertEquals(editlink, webDriver.getCurrentUrl());

        beaconsPage.createUpdateBeacon(new Beacons(ref, name, WebConstants.defaultTestStore.getName(), status, latitude, ""));
        assertEquals(editlink, webDriver.getCurrentUrl());

        if(!webDriver.getCurrentUrl().equals(WebConstants.beaconsUrl))
            beaconsPage = accessBeaconsPage(webDriver, navigationMenu);

        assertThat(beaconsPage.getRegOtherBeacons(WebConstants.otherBeaconTitle), not(hasItem(allOf(
                hasProperty("uniqueRef", equalTo(ref)),
                hasProperty("name", equalTo(name))
        ))));

    }

    @Test(priority = 8, testName = "TC_BP_08")
    @Parameters({"uniqueRef", "beaconName", "uniqueRefNew", "beaconNameNew", "currentStatusNew", "latitudeNew", "longitudeNew"})
    public void chkIfBeaconBeUpdaWRF(String uniqueRef, String beaconName, String uniqueRefNew, String beaconNameNew,
                                       String currentStatusNew, String latitudeNew, String longitudeNew) throws InterruptedException {

        if(!webDriver.getCurrentUrl().equals(WebConstants.beaconsUrl))
            beaconsPage = accessBeaconsPage(webDriver, navigationMenu);

        Beacons beacon = beaconsPage.getBeacon(beaconsPage.getRegOtherBeacons(WebConstants.otherBeaconTitle), uniqueRef, beaconName);
        updateBeacon(webDriver, beaconsPage, beacon, new Beacons(uniqueRefNew, beaconNameNew, WebConstants.defaultTestStore.getName(), currentStatusNew, latitudeNew, longitudeNew));

        allBeacons = beaconsPage.getRegOtherBeacons(WebConstants.otherBeaconTitle);

        assertThat(allBeacons, not(hasItem(allOf(
                hasProperty("uniqueRef", equalTo(uniqueRef)),
                hasProperty("name", equalTo(beaconName))
        ))));

        assertThat(allBeacons, hasItem(allOf(
                hasProperty("uniqueRef", equalTo(uniqueRefNew)),
                hasProperty("name", equalTo(beaconNameNew))
        )));
    }

    @BeforeGroups("UpdateBeaconWSN")
    public void creaDefBeacon() throws InterruptedException {
        if(!webDriver.getCurrentUrl().equals(WebConstants.beaconsUrl))
            beaconsPage = accessBeaconsPage(webDriver, navigationMenu);
        createBeacon(webDriver, beaconsPage, WebConstants.defaultTestBeacon);
    }

    @Test(priority = 9, testName = "TC_BP_09", groups = "UpdateBeaconWSN")
    @Parameters({"uniqueRefNew", "beaconNameNew"})
    public void chkIfBeaconBeUpdaWSN(String ref, String name) throws InterruptedException {
        if(!webDriver.getCurrentUrl().equals(WebConstants.beaconsUrl))
            beaconsPage = accessBeaconsPage(webDriver, navigationMenu);

        Beacons beacon = beaconsPage.getBeacon(beaconsPage.getRegOtherBeacons(WebConstants.otherBeaconTitle), ref, name);
        String editLink = beacon.getEditLink().getAttribute("href");
        beaconsPage.clickEditBeaconBtn(beacon.getEditLink());
        assertEquals(editLink, webDriver.getCurrentUrl());
        Thread.sleep(WebConstants.waitMilliSeconds);
        beaconsPage.createUpdateBeacon(WebConstants.defaultTestBeacon);
        Thread.sleep(WebConstants.waitMilliSeconds);

        assertEquals(WebConstants.sameBeaconName, beaconsPage.getDangerAlert());
        // TODO uncomment the below line when the issue is fixed.
        //assertEquals(editLink, webDriver.getCurrentUrl());

        assertEquals(WebConstants.sameBeaconName, beaconsPage.getDangerAlert());

        if(!webDriver.getCurrentUrl().equals(WebConstants.beaconsUrl))
            beaconsPage = accessBeaconsPage(webDriver, navigationMenu);

        allBeacons = beaconsPage.getRegOtherBeacons(WebConstants.otherBeaconTitle);
        int numOfBeaconWSN = 0;
        for(Beacons beac : allBeacons){
            if(beac.getName().equals(WebConstants.defaultTestBeacon.getName()))
                numOfBeaconWSN++;
        }
        // Verifying that there is only one beacon with the same name.
        assertEquals(1, numOfBeaconWSN);
    }

    @BeforeGroups("DeleBeaconWithNoAdver")
    @Parameters({"beaconNameNew"})
    public void creaDeAdver(String beaconName) throws InterruptedException {
        if (!webDriver.getCurrentUrl().equals(WebConstants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);

        //Verifying that there are no advertisements with the given beacon name
        assertThat(advertisementsPage.getAllAdvertisements(), not(hasItem(
                hasProperty("beacon", equalTo(beaconName))
        )));

        // Creating the default advertisement
        createAdvertisement(webDriver, advertisementsPage, WebConstants.defaultTestAdvertisement);

        if (!webDriver.getCurrentUrl().equals(WebConstants.beaconsUrl))
            advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);

        // Verifying that default advertisment has been created
        assertThat(advertisementsPage.getAllAdvertisements(), not(hasItem(
                hasProperty("beacon", equalTo(WebConstants.defaultTestAdvertisement.getName()))
        )));

    }

    @Test(priority = 10, testName = "TC_BP_10", groups = "DeleBeaconWithNoAdver")
    @Parameters({"uniqueRefNew", "beaconNameNew"})
    public void checkIfBeaconWOAdverCanBeDel(String uniqueRefNew, String beaconNameNew) throws InterruptedException {
        // Accessing the Beacons Page
        if(!webDriver.getCurrentUrl().equals(WebConstants.beaconsUrl))
            beaconsPage = accessBeaconsPage(webDriver, navigationMenu);

        // Deleting the beacon
        Beacons beacon = beaconsPage.getBeacon(beaconsPage.getRegOtherBeacons(WebConstants.otherBeaconTitle), uniqueRefNew, beaconNameNew);
        deleteBeacon(webDriver, beaconsPage, beacon);

        // Verify that the deleted beacon no longer exist in the Other Beacons table.
        assertThat(beaconsPage.getRegOtherBeacons(WebConstants.otherBeaconTitle), not(hasItem(allOf(
                hasProperty("uniqueRef", equalTo(uniqueRefNew)),
                hasProperty("name", equalTo(beaconNameNew))
        ))));
    }


    @Test(priority = 11, testName = "TC_BP_11")
    public void chkIfUCanViewBeaconAver() throws InterruptedException {
        // Accessing the Beacons Page
        if(!webDriver.getCurrentUrl().equals(WebConstants.beaconsUrl))
            beaconsPage = accessBeaconsPage(webDriver, navigationMenu);

        // Viewing the Beacon Advertisement Page
        Beacons beacon = beaconsPage.getBeacon(beaconsPage.getRegOtherBeacons(WebConstants.otherBeaconTitle), WebConstants.defaultTestBeacon.getUniqueRef(),
                WebConstants.defaultTestBeacon.getName());
        BeaconAdvPage beaconAdvPage = viewBeaconAdvertisements(webDriver, beaconsPage, beacon);

        // Verifying the Advertisement List is not null
        assertNotNull(beaconAdvPage.getAllAdvertisements());

        // Verifying that Default Advertisement exists under the default beacon.
        assertThat(beaconAdvPage.getAllAdvertisements(), not(hasItem(allOf(
                hasProperty("name", equalTo(WebConstants.defaultTestAdvertisement.getName())),
                hasProperty("name", equalTo(WebConstants.defaultTestAdvertisement.getBeacon()))
        ))));
    }

    @Test(priority = 12, testName = "TC_BP_12")
    public void chkIfBeaconWAdverCanBeDel() throws InterruptedException {
        if(!webDriver.getCurrentUrl().equals(WebConstants.beaconsUrl))
            beaconsPage = accessBeaconsPage(webDriver, navigationMenu);

        // Deleting the beacon
        Beacons beacon = beaconsPage.getBeacon(beaconsPage.getRegOtherBeacons(WebConstants.otherBeaconTitle), WebConstants.defaultTestBeacon.getUniqueRef(),
                WebConstants.defaultTestBeacon.getName());
        deleteBeacon(webDriver, beaconsPage, beacon);

        // Verifying is the successful message is shown
        assertEquals(beacon.getName()+WebConstants.delBeaconError, beaconsPage.getDangerAlert());

        // Verifying that the beacon still exists in the Beacon List
        assertThat(beaconsPage.getRegOtherBeacons(WebConstants.otherBeaconTitle), hasItem(allOf(
                hasProperty("uniqueRef", equalTo(WebConstants.defaultTestBeacon.getUniqueRef())),
                hasProperty("name", equalTo(WebConstants.defaultTestBeacon.getName()))
        )));
    }


    @Test(priority = 13, testName = "TC_BP_13")
    public void chkIfUnregBeaconsAreInURBL(){
        assertEquals(WebConstants.beaconsUrl, webDriver.getCurrentUrl());
        assertNotNull(beaconsPage.getUnregisteredList());
    }

    @Test(priority = 14, testName = "TC_BP_14")
    public void chkIfNoRegBeaconMsgIsShown(){
        assertEquals(WebConstants.beaconsUrl, webDriver.getCurrentUrl());
        assertEquals(WebConstants.noRegBeaconTxt, beaconsPage.getNoRegBeaconTxt());
    }


    // TODO Implement the Test Cases TC_BP_15 - Test Cases TC_BP_25

    @Test(priority = 15, testName = "TC_BP_15")
    @Parameters({"uniqueRef", "beaconName", "currentStatus", "latitude", "longitude"})
    public void chkIfUnregBeaconBeReg(String ref, String name, String status, String latitude, String longitude){

    }



    @AfterTest
    public void clearAllTestData() throws InterruptedException {
        if (!webDriver.getCurrentUrl().equals(WebConstants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);
        List<Advertisement> advertisements = advertisementsPage.getAllAdvertisements();
        for(Advertisement advertisement : advertisements){
            deleteAdvertisement(webDriver, advertisementsPage, advertisement);
        }

        if (!webDriver.getCurrentUrl().equals(WebConstants.beaconsUrl))
            beaconsPage = accessBeaconsPage(webDriver, navigationMenu);
        List<Beacons> beacons = beaconsPage.getRegOtherBeacons(WebConstants.otherBeaconTitle);
        for(Beacons beacon : beacons){
            deleteBeacon(webDriver, beaconsPage, beacon);
        }

        if (!webDriver.getCurrentUrl().equals(WebConstants.categoriesUrl))
            categoryPage = accessCategoriesPage(webDriver, navigationMenu);
        List<Category> allCategories = categoryPage.getAllCategories();
        for(Category category: allCategories){
            deleteCategory(webDriver, categoryPage, category);
        }

        if (!webDriver.getCurrentUrl().equals(WebConstants.storesUrl))
            storesPage = accessStoresPage(webDriver, navigationMenu);
        List<Stores> stores = storesPage.getAllStores();
        for(Stores store: stores){
            deleteStore(webDriver, storesPage, store);
        }
    }

}
