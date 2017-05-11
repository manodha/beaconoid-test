package com.company.testscripts;

import com.company.model.Advertisement;
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
    public void accessAdvertisementsPage(String email, String password) {
        navigationMenu = loginToBeaconoid(email, password);

        // Creating the default store that is required to create the default Beacon
        storesPage = accessStoresPage(navigationMenu);
        createStore(storesPage, defaultTestStore);

        // Creating the default category that is required to create advertisements
        categoryPage = accessCategoriesPage(navigationMenu);
        createCategory(categoryPage, defaultTestCategory);

        // Creating the default beacon that is required to create the advertisement
        beaconsPage = accessBeaconsPage(navigationMenu);
        createBeacon(beaconsPage, defaultTestBeacon);

        // Accessing the Advertisements Page
        advertisementsPage = accessAdvertisementsPage(navigationMenu);
    }

    @Test(priority = 1, testName = "TC041")
    public void checkNoAdvertisementMsgTC041() {
        allAdvertisements = advertisementsPage.getAllAdvertisements();
        if (allAdvertisements == null) {
            assertEquals(advertisementsPage.getNoAdvertisementsTXT(), noAdvertisementTxt);
        }
    }

    @Test(priority = 2, testName = "TC042")
    @Parameters({"adverName", "adverDescription", "adverPrice"})
    public void createAdverWithAFTC042(String adverName, String adverDescription, String adverPrice) {
        assertEquals(advertisementsUrl, webDriver.getCurrentUrl());
        createAdvertisement(advertisementsPage, new Advertisement(adverName, defaultTestBeacon.getName(),
                defaultTestCategory.getCategoryName(), adverDescription, adverPrice));

        allAdvertisements = advertisementsPage.getAllAdvertisements();
        assertThat(allAdvertisements, hasItem(allOf(hasProperty("name", equalTo(adverName)),
                hasProperty("beacon", equalTo(defaultTestBeacon.getName())))));
    }

    @Test(priority = 3, testName = "TC043")
    @Parameters({"adverName"})
    public void getListOfAdvertisementsTC043(String adverName) {
        assertEquals(advertisementsUrl, webDriver.getCurrentUrl());
        allAdvertisements = advertisementsPage.getAllAdvertisements();
        assertThat(allAdvertisements, hasItem(allOf(hasProperty("name", equalTo(adverName)),
                hasProperty("beacon", equalTo(defaultTestBeacon.getName())))));
    }

    @Test(priority = 4, testName = "TC044")
    @Parameters({"adverName"})
    public void canViewAdvertisementTC044(String adverName) {
        assertEquals(advertisementsUrl, webDriver.getCurrentUrl());
        Advertisement advertisement = advertisementsPage.getAdvertisment(advertisementsPage.getAllAdvertisements(), adverName,
                defaultTestBeacon.getName());
        advertisementsPage.clickViewAdverBtn(advertisement.getViewLink());
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test(priority = 5, testName = "TC045")
    @Parameters({"adverName", "adverNameNew", "adverDescriptionNew", "adverPriceNew"})
    public void updateAdvertisementTC045(String adverName, String adverNameNew, String adverDescriptionNew, String adverPriceNew) {
        if (!webDriver.getCurrentUrl().equals(advertisementsUrl))
            accessAdvertisementsPage(navigationMenu);
        allAdvertisements = advertisementsPage.getAllAdvertisements();
        Advertisement oldAdvertisement = advertisementsPage.getAdvertisment(allAdvertisements, adverName,
                defaultTestBeacon.getName());

        // Updating the beacon
        updateAdvertisement(oldAdvertisement, new Advertisement(adverNameNew, defaultTestBeacon.getName(),
                defaultTestCategory.getCategoryName(), adverDescriptionNew, adverPriceNew));

        // Asserting that there are no advertisements with the old name details
        assertThat(allAdvertisements, not(hasItem(allOf(
                hasProperty("name", equalTo(adverName)),
                hasProperty("beacon", equalTo(defaultTestBeacon.getName()))))));

        // Asserting that the details are updated in the old Advertisement
        assertThat(allAdvertisements, hasItem(allOf(
                hasProperty("name", equalTo(adverNameNew)),
                hasProperty("beacon", equalTo(defaultTestBeacon.getName())),
                hasProperty("category", equalTo(defaultTestCategory.getCategoryName())))));

    }

    @Test(priority = 6, testName = "TC046")
    @Parameters({"adverNameNew"})
    public void deleteAdvertisementTC046(String adverNameNew) {
        if (!webDriver.getCurrentUrl().equals(advertisementsUrl))
            accessAdvertisementsPage(navigationMenu);

        // Checking that Advertisement to be deleted is there in the Advertisement List
        allAdvertisements = advertisementsPage.getAllAdvertisements();
        assertThat(allAdvertisements, hasItem(allOf(
                hasProperty("name", equalTo(adverNameNew)),
                hasProperty("beacon", equalTo(defaultTestBeacon.getName())),
                hasProperty("category", equalTo(defaultTestCategory.getCategoryName()))
        )));

        // Deleting the advertisement
        Advertisement advertisement = advertisementsPage.getAdvertisment(allAdvertisements, adverNameNew,
                defaultTestBeacon.getName());
        deleteAdvertisement(advertisementsPage, advertisement);

        // Checking that the deleted advertisement is no longer there in the advertisements list
        allAdvertisements = advertisementsPage.getAllAdvertisements();
        assertThat(allAdvertisements, not(hasItem(allOf(
                hasProperty("name", equalTo(adverNameNew)),
                hasProperty("beacon", equalTo(defaultTestBeacon.getName())),
                hasProperty("category", equalTo(defaultTestCategory.getCategoryName()))
        ))));
    }

    @Test(priority = 7, testName = "TC047")
    @Parameters({"adverName", "adverDescription", "adverPrice"})
    public void canAdverBeCreaWithOutRFTC047(String adverName, String adverDescription, String adverPrice) {
        assertEquals(advertisementsUrl, webDriver.getCurrentUrl());
        advertisementsPage.clickNewAdvertisementBtn();
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(addAdvertisementUrl, webDriver.getCurrentUrl());

        advertisementsPage.createUpdateAdvertisement(new Advertisement("", "", "", "", ""));
        assertEquals(addAdvertisementUrl, webDriver.getCurrentUrl());

        advertisementsPage.createUpdateAdvertisement(new Advertisement(adverName, "", "", "", ""));
        assertEquals(addAdvertisementUrl, webDriver.getCurrentUrl());

        advertisementsPage.createUpdateAdvertisement(new Advertisement(adverName, defaultTestBeacon.getName(), "", "", ""));
        assertEquals(addAdvertisementUrl, webDriver.getCurrentUrl());

        advertisementsPage.createUpdateAdvertisement(new Advertisement(adverName, defaultTestBeacon.getName(), defaultTestCategory.getCategoryName(), "", ""));
        assertEquals(addAdvertisementUrl, webDriver.getCurrentUrl());

        advertisementsPage.createUpdateAdvertisement(new Advertisement(adverName, defaultTestBeacon.getName(), defaultTestCategory.getCategoryName(), adverDescription, ""));
        assertEquals(addAdvertisementUrl, webDriver.getCurrentUrl());

        advertisementsPage.createUpdateAdvertisement(new Advertisement(adverName, defaultTestBeacon.getName(), defaultTestCategory.getCategoryName(), "", adverPrice));
        assertEquals(addAdvertisementUrl, webDriver.getCurrentUrl());

    }

    @Test(priority = 8, testName = "TC048")
    public void checkIfAdverBeCreaWithRFTC048() {
        assertEquals(addAdvertisementUrl, webDriver.getCurrentUrl());
        advertisementsPage.createUpdateAdvertisement(defaultTestAdvertisement);
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(advertisementsUrl, webDriver.getCurrentUrl());
        allAdvertisements = advertisementsPage.getAllAdvertisements();
        assertThat(allAdvertisements, hasItem(allOf(
                hasProperty("name", equalTo(defaultTestAdvertisement.getName())),
                hasProperty("beacon", equalTo(defaultTestAdvertisement.getBeacon())),
                hasProperty("category", equalTo(defaultTestCategory.getCategoryName()))
        )));
    }

    //@Test(priority = 9, testName = "TC049")
    public void canAdverBeCreaWithSameNameTC049() {

    }


    @AfterTest
    public void clearAllTestData() {
        beaconsPage = accessBeaconsPage(navigationMenu);
        deleteBeacon(beaconsPage, defaultTestBeacon);

        categoryPage = accessCategoriesPage(navigationMenu);
        deleteCategory(categoryPage, defaultTestCategory);

        storesPage = accessStoresPage(navigationMenu);
        deleteStore(storesPage, defaultTestStore);

    }


    private void updateAdvertisement(Advertisement oldAdvertisement, Advertisement newAdvertisement) {
        assertEquals(advertisementsUrl, webDriver.getCurrentUrl());
        advertisementsPage.clickEditAdverBtn(oldAdvertisement.getEditLink());
        assertEquals(oldAdvertisement.getEditLink().getAttribute("href"), webDriver.getCurrentUrl());
        advertisementsPage.createUpdateAdvertisement(newAdvertisement);
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(advertisementsUrl, webDriver.getCurrentUrl());
    }

}
