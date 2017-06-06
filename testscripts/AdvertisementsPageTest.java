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
import static org.junit.Assert.assertThat;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertEquals;

/**
 * Created by manodha on 1/5/17.
 */
public class AdvertisementsPageTest extends FunctionalTest {

    private NavigationMenu navigationMenu;
    private AdvertisementsPage advertisementsPage;
    private StoresPage storesPage;
    private BeaconsPage beaconsPage;
    private CategoryPage categoryPage;
    private List<Advertisement> allAdvertisements;


    @BeforeTest
    @Parameters({"email", "password"})
    public void setupTestData(String email, String password) throws InterruptedException {
        navigationMenu = loginToBeaconoid(webDriver, email, password);

        // Creating the default store that is required to create the default Beacon
        storesPage = accessStoresPage(webDriver, navigationMenu);
        createStore(webDriver, storesPage, WebConstants.defaultTestStore);

        // Creating the default category that is required to create advertisements
        categoryPage = accessCategoriesPage(webDriver, navigationMenu);
        createCategory(webDriver, categoryPage, WebConstants.defaultTestCategory);

        // Creating the default beacon that is required to create the advertisement
        beaconsPage = accessBeaconsPage(webDriver, navigationMenu);
        createBeacon(webDriver, beaconsPage, WebConstants.defaultTestBeacon);
    }

    @Test(priority = 1, testName = "TC_AP_01", description = "Verify that user can see the title Advertisements when the Advertisements Page is accessed from the Navigation Menu")
    public void chkIfUserCanAccessAdverPage(){
        advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);
        assertEquals(WebConstants.adverIndexTitle, advertisementsPage.getTitle());
    }

    @Test(priority = 2, testName = "TC_AP_02", description = "Verify that the message 'No advertisement found.'  is visible in the Advertisements Table if there are no advertisements created.")
    public void chkIfNoAdverMsgIsShown() {
        // Getting all the advertisments
        allAdvertisements = advertisementsPage.getAllAdvertisements();
        if (allAdvertisements == null) {
            assertEquals(advertisementsPage.getNoAdvertisementsTXT(), WebConstants.noAdverTxt);
        }
    }

    @Test(priority = 3, testName = "TC_AP_03", description = "Verify that user can not create an Advertisement if the required fields are not filled")
    @Parameters({"adverName", "adverDescription", "adverImage", "adverPrice"})
    public void chkIfAdverBeCreaWORF(String adverName, String adverDescription, String image, String adverPrice) throws InterruptedException {
        assertEquals(WebConstants.advertisementsUrl, webDriver.getCurrentUrl());
        advertisementsPage.clickNewAdvertisementBtn();
        Thread.sleep(WebConstants.waitMilliSeconds);
        assertEquals(WebConstants.addAdvertisementUrl, webDriver.getCurrentUrl());

        advertisementsPage.createUpdateAdvertisement(new Advertisement("", "", "", "", "", ""));
        assertEquals(WebConstants.addAdvertisementUrl, webDriver.getCurrentUrl());

        advertisementsPage.createUpdateAdvertisement(new Advertisement(adverName, "", "", "", "", ""));
        assertEquals(WebConstants.addAdvertisementUrl, webDriver.getCurrentUrl());

        advertisementsPage.createUpdateAdvertisement(new Advertisement(adverName, WebConstants.defaultTestBeacon.getName(), "", "", "", ""));
        assertEquals(WebConstants.addAdvertisementUrl, webDriver.getCurrentUrl());

        advertisementsPage.createUpdateAdvertisement(new Advertisement(adverName, WebConstants.defaultTestBeacon.getName(), WebConstants.defaultTestCategory.getCategoryName(), "", "", ""));
        assertEquals(WebConstants.addAdvertisementUrl, webDriver.getCurrentUrl());

        advertisementsPage.createUpdateAdvertisement(new Advertisement(adverName, WebConstants.defaultTestBeacon.getName(), WebConstants.defaultTestCategory.getCategoryName(), adverDescription, "", ""));
        assertEquals(WebConstants.addAdvertisementUrl, webDriver.getCurrentUrl());

        advertisementsPage.createUpdateAdvertisement(new Advertisement(adverName, WebConstants.defaultTestBeacon.getName(), WebConstants.defaultTestCategory.getCategoryName(), adverDescription, image, ""));
        assertEquals(WebConstants.addAdvertisementUrl, webDriver.getCurrentUrl());

        if (!webDriver.getCurrentUrl().equals(WebConstants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);

        assertThat(advertisementsPage.getAllAdvertisements(), not(hasItem(allOf(
                hasProperty("name", equalTo(adverName)),
                hasProperty("beacon", equalTo(WebConstants.defaultTestAdvertisement.getBeacon())),
                hasProperty("category", equalTo(WebConstants.defaultTestCategory.getCategoryName()))
        ))));

    }


    @Test(priority = 4, testName = "TC_AP_04", description = "Verify that user can create an Advertisement if all the required fields are filled")
    @Parameters({"adverName", "adverDescription", "adverImage", "adverPrice"})
    public void chkIfAdverBeCreaWRF(String adverName, String adverDescription, String image, String adverPrice) {
        if (!webDriver.getCurrentUrl().equals(WebConstants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);

        createAdvertisement(webDriver, advertisementsPage, new Advertisement(adverName, WebConstants.defaultTestBeacon.getName(),
                WebConstants.defaultTestCategory.getCategoryName(), adverDescription, image, adverPrice));

        if (!webDriver.getCurrentUrl().equals(WebConstants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);

        assertThat(advertisementsPage.getAllAdvertisements(), hasItem(allOf(
                hasProperty("name", equalTo(adverName)),
                hasProperty("beacon", equalTo(WebConstants.defaultTestBeacon.getName()))
        )));
    }


    @Test(priority = 5, testName = "TC_AP_05", description = "Verify that user can not create an Advertisement with the same name and beacon name of a exisitng Advertisement")
    @Parameters({"adverName", "adverDescription", "adverImage", "adverPrice"})
    public void chkIfAdverCanBeCreaWSNB(String name, String desc, String image, String price) throws InterruptedException {
        if (!webDriver.getCurrentUrl().equals(WebConstants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);
        advertisementsPage.clickNewAdvertisementBtn();
        assertEquals(WebConstants.addAdvertisementUrl, webDriver.getCurrentUrl());
        Thread.sleep(WebConstants.waitMilliSeconds);
        advertisementsPage.createUpdateAdvertisement(new Advertisement(name, WebConstants.defaultTestBeacon.getName(),
                WebConstants.defaultTestCategory.getCategoryName(), desc, image, price));
        Thread.sleep(WebConstants.waitMilliSeconds);

        assertEquals(WebConstants.addAdvertisementUrl, webDriver.getCurrentUrl());
        assertEquals(WebConstants.sameAdverNameBeacon, advertisementsPage.getDangerAlert());

        if (!webDriver.getCurrentUrl().equals(WebConstants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);

        int sameAdverCount=0;
        allAdvertisements = advertisementsPage.getAllAdvertisements();
        for(Advertisement adver : allAdvertisements){
            if(adver.getName().equals(name) && adver.getBeacon().equals(WebConstants.defaultTestBeacon.getName()))
                sameAdverCount++;
        }
        assertEquals(1, sameAdverCount);
    }


    @Test(priority = 6, testName = "TC_AP_06", description = "Verify that the created Advertisement in the Test Case TC_AP_04 will be in the Advertisements Table")
    @Parameters({"adverName"})
    public void chkIfAdverIsInAdverTable(String adverName) {
        if (!webDriver.getCurrentUrl().equals(WebConstants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);
        allAdvertisements = advertisementsPage.getAllAdvertisements();

        assertNotNull(allAdvertisements);

        assertThat(allAdvertisements, hasItem(allOf(
                hasProperty("name", equalTo(adverName)),
                hasProperty("beacon", equalTo(WebConstants.defaultTestBeacon.getName()))
        )));
    }

    @Test(priority = 7, testName = "TC_AP_07", description = "Verify that user can not update an Advertisement if the required fields are not filled")
    @Parameters({"adverName", "adverNameNew", "adverDescriptionNew", "adverImageNew", "adverPriceNew"})
    public void chkIfAdverBeUpdaWORF(String adverName, String adverNameNew, String adverDescriptionNew, String image, String adverPriceNew) throws InterruptedException {
        assertEquals(WebConstants.advertisementsUrl, webDriver.getCurrentUrl());
        Advertisement advertisement = advertisementsPage.getAdvertisment(advertisementsPage.getAllAdvertisements(), adverName,
                WebConstants.defaultTestBeacon.getName());
        String editLink = advertisement.getEditLink().getAttribute("href");
        advertisementsPage.clickEditAdverBtn(advertisement.getEditLink());
        Thread.sleep(WebConstants.waitMilliSeconds);
        assertEquals(editLink, webDriver.getCurrentUrl());

        advertisementsPage.createUpdateAdvertisement(new Advertisement("", "", "", "", "", ""));
        assertEquals(editLink, webDriver.getCurrentUrl());

        advertisementsPage.createUpdateAdvertisement(new Advertisement(adverNameNew, "", "", "", "", ""));
        assertEquals(editLink, webDriver.getCurrentUrl());

        advertisementsPage.createUpdateAdvertisement(new Advertisement(adverNameNew, WebConstants.defaultTestBeacon.getName(), "", "", "", ""));
        assertEquals(editLink, webDriver.getCurrentUrl());

        advertisementsPage.createUpdateAdvertisement(new Advertisement(adverNameNew, WebConstants.defaultTestBeacon.getName(), WebConstants.defaultTestCategory.getCategoryName(), "", "", ""));
        assertEquals(editLink, webDriver.getCurrentUrl());

        advertisementsPage.createUpdateAdvertisement(new Advertisement(adverNameNew, WebConstants.defaultTestBeacon.getName(), WebConstants.defaultTestCategory.getCategoryName(), adverDescriptionNew, "", ""));
        assertEquals(editLink, webDriver.getCurrentUrl());

        advertisementsPage.createUpdateAdvertisement(new Advertisement(adverName, WebConstants.defaultTestBeacon.getName(), WebConstants.defaultTestCategory.getCategoryName(), adverDescriptionNew, image, ""));
        assertEquals(editLink, webDriver.getCurrentUrl());

        if (!webDriver.getCurrentUrl().equals(WebConstants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);

        assertThat(advertisementsPage.getAllAdvertisements(), not(hasItem(allOf(
                hasProperty("name", equalTo(adverNameNew)),
                hasProperty("beacon", equalTo(WebConstants.defaultTestAdvertisement.getBeacon())),
                hasProperty("category", equalTo(WebConstants.defaultTestCategory.getCategoryName()))
        ))));

    }

    @Test(priority = 8, testName = "TC_AP_08", description = "Verify that user can update an Advertisement successfully if the required fields are filled")
    @Parameters({"adverName", "adverNameNew", "adverDescriptionNew", "adverImageNew", "adverPriceNew"})
    public void chkIfAdverBeUpdaWRF(String adverName, String adverNameNew, String adverDescriptionNew, String image, String adverPriceNew) {
        if (!webDriver.getCurrentUrl().equals(WebConstants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);

        Advertisement oldAdvertisement = advertisementsPage.getAdvertisment(advertisementsPage.getAllAdvertisements(),
                adverName, WebConstants.defaultTestBeacon.getName());

        // Updating the advertisement
        updateAdvertisement(webDriver, advertisementsPage, oldAdvertisement, new Advertisement(adverNameNew, WebConstants.defaultTestBeacon.getName(),
                WebConstants.defaultTestCategory.getCategoryName(), adverDescriptionNew, image, adverPriceNew));

        if (!webDriver.getCurrentUrl().equals(WebConstants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);

        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        allAdvertisements = advertisementsPage.getAllAdvertisements();


        // Asserting that there are no advertisements with the old name details
        assertThat(allAdvertisements, not(hasItem(allOf(
                hasProperty("name", equalTo(adverName)),
                hasProperty("beacon", equalTo(WebConstants.defaultTestBeacon.getName()))))));

        // Asserting that the details are updated in the old Advertisement
        assertThat(allAdvertisements, hasItem(allOf(
                hasProperty("name", equalTo(adverNameNew)),
                hasProperty("beacon", equalTo(WebConstants.defaultTestBeacon.getName())),
                hasProperty("category", equalTo(WebConstants.defaultTestCategory.getCategoryName())))));
    }

    @BeforeGroups("UpdaAdverWSN")
    public void creaDefaultAdver(){
        if (!webDriver.getCurrentUrl().equals(WebConstants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);
        createAdvertisement(webDriver, advertisementsPage, WebConstants.defaultTestAdvertisement);
    }


    @Test(priority = 9, testName = "TC_AP_09", groups = "UpdaAdverWSN", description = "Verify that user can not update an Advertisement with the same name and beacon name of a exisitng Advertisement")
    @Parameters({"adverNameNew"})
    public void chkIfAdverCanBeUpdaWSNB(String name) throws InterruptedException {
        if (!webDriver.getCurrentUrl().equals(WebConstants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);
        Advertisement advertisement = advertisementsPage.getAdvertisment(advertisementsPage.getAllAdvertisements(), name,
                WebConstants.defaultTestBeacon.getName());
        String editLink = advertisement.getEditLink().getAttribute("href");
        advertisementsPage.clickEditAdverBtn(advertisement.getEditLink());
        assertEquals(editLink, webDriver.getCurrentUrl());
        Thread.sleep(WebConstants.waitMilliSeconds);
        advertisementsPage.createUpdateAdvertisement(WebConstants.defaultTestAdvertisement);
        Thread.sleep(WebConstants.waitMilliSeconds);

        assertEquals(editLink, webDriver.getCurrentUrl());
        assertEquals(WebConstants.sameAdverNameBeacon, advertisementsPage.getDangerAlert());

        if (!webDriver.getCurrentUrl().equals(WebConstants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);

        int sameAdverCount=0;
        allAdvertisements = advertisementsPage.getAllAdvertisements();
        for(Advertisement adver : allAdvertisements){
            if(adver.getName().equals(name) && adver.getBeacon().equals(WebConstants.defaultTestBeacon.getName()))
                sameAdverCount++;
        }
        assertEquals(1, sameAdverCount);
    }


    @Test(priority = 10, testName = "TC_AP_10", description = "Verify that user can delete an Advertisement")
    @Parameters({"adverNameNew"})
    public void chkIfAdverCanBeDel(String adverNameNew) {
        if (!webDriver.getCurrentUrl().equals(WebConstants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);

        // Checking that Advertisement to be deleted is there in the Advertisement List
        assertThat(advertisementsPage.getAllAdvertisements(), hasItem(allOf(
                hasProperty("name", equalTo(adverNameNew)),
                hasProperty("beacon", equalTo(WebConstants.defaultTestBeacon.getName())),
                hasProperty("category", equalTo(WebConstants.defaultTestCategory.getCategoryName()))
        )));
        // Deleting the advertisement
        Advertisement advertisement = advertisementsPage.getAdvertisment(advertisementsPage.getAllAdvertisements(),
                adverNameNew, WebConstants.defaultTestBeacon.getName());
        deleteAdvertisement(webDriver, advertisementsPage, advertisement);

        // Checking that the deleted advertisement is no longer there in the advertisements list
        assertThat(advertisementsPage.getAllAdvertisements(), not(hasItem(allOf(
                hasProperty("name", equalTo(adverNameNew)),
                hasProperty("beacon", equalTo(WebConstants.defaultTestBeacon.getName())),
                hasProperty("category", equalTo(WebConstants.defaultTestCategory.getCategoryName()))
        ))));
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
