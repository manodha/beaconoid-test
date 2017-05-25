package com.company.testscripts;

import com.company.model.Advertisement;
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
        createStore(storesPage, Constants.defaultTestStore);

        // Creating the default category that is required to create advertisements
        categoryPage = accessCategoriesPage(navigationMenu);
        createCategory(categoryPage, Constants.defaultTestCategory);

        // Creating the default beacon that is required to create the advertisement
        beaconsPage = accessBeaconsPage(navigationMenu);
        createBeacon(beaconsPage, Constants.defaultTestBeacon);

        // Accessing the Advertisements Page
        advertisementsPage = accessAdvertisementsPage(navigationMenu);
    }

    @Test(priority = 1, testName = "TC041")
    public void checkNoAdvertisementMsgTC041() {
        allAdvertisements = advertisementsPage.getAllAdvertisements();
        if (allAdvertisements == null) {
            assertEquals(advertisementsPage.getNoAdvertisementsTXT(), Constants.noAdvertisementTxt);
        }
    }

    @Test(priority = 2, testName = "TC042")
    @Parameters({"adverName", "adverDescription", "adverImage", "adverPrice"})
    public void createAdverWithAFTC042(String adverName, String adverDescription, String image, String adverPrice) {
        assertEquals(Constants.advertisementsUrl, webDriver.getCurrentUrl());
        createAdvertisement(advertisementsPage, new Advertisement(adverName, Constants.defaultTestBeacon.getName(),
                Constants.defaultTestCategory.getCategoryName(), adverDescription, image, adverPrice));

        if (!webDriver.getCurrentUrl().equals(Constants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(navigationMenu);

        allAdvertisements = advertisementsPage.getAllAdvertisements();
        assertThat(allAdvertisements, hasItem(allOf(hasProperty("name", equalTo(adverName)),
                hasProperty("beacon", equalTo(Constants.defaultTestBeacon.getName())))));
    }

    @Test(priority = 3, testName = "TC043")
    @Parameters({"adverName"})
    public void getListOfAdvertisementsTC043(String adverName) {
        assertEquals(Constants.advertisementsUrl, webDriver.getCurrentUrl());
        allAdvertisements = advertisementsPage.getAllAdvertisements();
        assertThat(allAdvertisements, hasItem(allOf(hasProperty("name", equalTo(adverName)),
                hasProperty("beacon", equalTo(Constants.defaultTestBeacon.getName())))));
    }

    //@Test(priority = 4, testName = "TC044")
    @Parameters({"adverName"})
    public void canViewAdvertisementTC044(String adverName) {
        assertEquals(Constants.advertisementsUrl, webDriver.getCurrentUrl());
        Advertisement advertisement = advertisementsPage.getAdvertisment(advertisementsPage.getAllAdvertisements(), adverName,
                Constants.defaultTestBeacon.getName());
        advertisementsPage.clickViewAdverBtn(advertisement.getViewLink());
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test(priority = 5, testName = "TC045")
    @Parameters({"adverName", "adverNameNew", "adverDescriptionNew", "adverImageNew", "adverPriceNew"})
    public void updateAdvertisementTC045(String adverName, String adverNameNew, String adverDescriptionNew, String image, String adverPriceNew) {
        if (!webDriver.getCurrentUrl().equals(Constants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(navigationMenu);

        Advertisement oldAdvertisement = advertisementsPage.getAdvertisment(advertisementsPage.getAllAdvertisements(),
                adverName, Constants.defaultTestBeacon.getName());

        // Updating the advertisement
        updateAdvertisement(advertisementsPage, oldAdvertisement, new Advertisement(adverNameNew, Constants.defaultTestBeacon.getName(),
                Constants.defaultTestCategory.getCategoryName(), adverDescriptionNew, image, adverPriceNew));

        if (!webDriver.getCurrentUrl().equals(Constants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(navigationMenu);

        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        allAdvertisements = advertisementsPage.getAllAdvertisements();
        // Asserting that there are no advertisements with the old name details
        assertThat(allAdvertisements, not(hasItem(allOf(
                hasProperty("name", equalTo(adverName)),
                hasProperty("beacon", equalTo(Constants.defaultTestBeacon.getName()))))));

        // Asserting that the details are updated in the old Advertisement
        assertThat(allAdvertisements, hasItem(allOf(
                hasProperty("name", equalTo(adverNameNew)),
                hasProperty("beacon", equalTo(Constants.defaultTestBeacon.getName())),
                hasProperty("category", equalTo(Constants.defaultTestCategory.getCategoryName())))));

    }

    @Test(priority = 6, testName = "TC046")
    @Parameters({"adverNameNew"})
    public void deleteAdvertisementTC046(String adverNameNew) {

        if (!webDriver.getCurrentUrl().equals(Constants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(navigationMenu);

        // Checking that Advertisement to be deleted is there in the Advertisement List
        assertThat(advertisementsPage.getAllAdvertisements(), hasItem(allOf(
                hasProperty("name", equalTo(adverNameNew)),
                hasProperty("beacon", equalTo(Constants.defaultTestBeacon.getName())),
                hasProperty("category", equalTo(Constants.defaultTestCategory.getCategoryName()))
        )));
        // Deleting the advertisement
        Advertisement advertisement = advertisementsPage.getAdvertisment(advertisementsPage.getAllAdvertisements(),
                adverNameNew, Constants.defaultTestBeacon.getName());
        deleteAdvertisement(advertisementsPage, advertisement);

        // Checking that the deleted advertisement is no longer there in the advertisements list
        assertThat(advertisementsPage.getAllAdvertisements(), not(hasItem(allOf(
                hasProperty("name", equalTo(adverNameNew)),
                hasProperty("beacon", equalTo(Constants.defaultTestBeacon.getName())),
                hasProperty("category", equalTo(Constants.defaultTestCategory.getCategoryName()))
        ))));
    }

    @Test(priority = 7, testName = "TC047")
    @Parameters({"adverName", "adverDescription", "adverImage", "adverPrice"})
    public void canAdverBeCreaWithOutRFTC047(String adverName, String adverDescription, String image, String adverPrice) {
        assertEquals(Constants.advertisementsUrl, webDriver.getCurrentUrl());
        advertisementsPage.clickNewAdvertisementBtn();
        try {
            Thread.sleep(Constants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Constants.addAdvertisementUrl, webDriver.getCurrentUrl());

        advertisementsPage.createUpdateAdvertisement(new Advertisement("", "", "", "", "", ""));
        assertEquals(Constants.addAdvertisementUrl, webDriver.getCurrentUrl());

        advertisementsPage.createUpdateAdvertisement(new Advertisement(adverName, "", "", "", "", ""));
        assertEquals(Constants.addAdvertisementUrl, webDriver.getCurrentUrl());

        advertisementsPage.createUpdateAdvertisement(new Advertisement(adverName, Constants.defaultTestBeacon.getName(), "", "", "", ""));
        assertEquals(Constants.addAdvertisementUrl, webDriver.getCurrentUrl());

        advertisementsPage.createUpdateAdvertisement(new Advertisement(adverName, Constants.defaultTestBeacon.getName(), Constants.defaultTestCategory.getCategoryName(), "", "", ""));
        assertEquals(Constants.addAdvertisementUrl, webDriver.getCurrentUrl());

        advertisementsPage.createUpdateAdvertisement(new Advertisement(adverName, Constants.defaultTestBeacon.getName(), Constants.defaultTestCategory.getCategoryName(), adverDescription, "", ""));
        assertEquals(Constants.addAdvertisementUrl, webDriver.getCurrentUrl());

        advertisementsPage.createUpdateAdvertisement(new Advertisement(adverName, Constants.defaultTestBeacon.getName(), Constants.defaultTestCategory.getCategoryName(), adverDescription, image, ""));
        assertEquals(Constants.addAdvertisementUrl, webDriver.getCurrentUrl());

        if (!webDriver.getCurrentUrl().equals(Constants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(navigationMenu);

        assertThat(advertisementsPage.getAllAdvertisements(), not(hasItem(allOf(
                hasProperty("name", equalTo(adverName)),
                hasProperty("beacon", equalTo(Constants.defaultTestAdvertisement.getBeacon())),
                hasProperty("category", equalTo(Constants.defaultTestCategory.getCategoryName()))
        ))));


    }

    @Test(priority = 8, testName = "TC048")
    public void checkIfAdverBeCreaWithRFTC048() {
        assertEquals(Constants.advertisementsUrl, webDriver.getCurrentUrl());
        createAdvertisement(advertisementsPage, Constants.defaultTestAdvertisement);

        if (!webDriver.getCurrentUrl().equals(Constants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(navigationMenu);

        assertThat(advertisementsPage.getAllAdvertisements(), hasItem(allOf(
                hasProperty("name", equalTo(Constants.defaultTestAdvertisement.getName())),
                hasProperty("beacon", equalTo(Constants.defaultTestAdvertisement.getBeacon())),
                hasProperty("category", equalTo(Constants.defaultTestCategory.getCategoryName()))
        )));
    }

    //@Test(priority = 9, testName = "TC049")
    public void canAdverBeCreaWithSameNameTC049() {

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
