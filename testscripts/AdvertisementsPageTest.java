package com.company.testscripts;

import com.company.model.Advertisement;
import com.company.pageobjects.AdvertisementsPage;
import com.company.pageobjects.NavigationMenu;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.AssertJUnit.assertEquals;

/**
 * Created by manodha on 1/5/17.
 */
public class AdvertisementsPageTest extends FunctionalTest {

    private NavigationMenu navigationMenu;
    private AdvertisementsPage advertisementsPage;
    private List<Advertisement> allAdvertisements;


    @BeforeTest
    @Parameters({"email", "password"})
    public void accessAdvertisementsPage(String email, String password) {
        navigationMenu = loginToBeaconoid(email, password);
        advertisementsPage = navigationMenu.clickAdvertisementsLink();
        assertEquals(advertisementsUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 1)
    @Parameters({"adverName", "adverBeacon", "adverCategory", "adverDescription", "adverPrice"})
    public void createAdvertisementTC(String adverName, String adverBeacon, String adverCategory, String adverDescription, String adverPrice) {

        createAdvertisement(advertisementsPage, new Advertisement(adverName, adverBeacon, adverCategory, adverDescription, adverPrice));

    }


    @Test(priority = 2)
    @Parameters({"adverName", "adverBeacon", "adverNameNew", "adverBeaconNew", "adverCategoryNew", "adverDescriptionNew", "adverPriceNew"})
    public void updateAdvertisementTC(String adverName, String adverBeacon, String adverNameNew, String adverBeaconNew,
                                      String adverCategoryNew, String adverDescriptionNew, String adverPriceNew) {
        assertEquals(advertisementsUrl, webDriver.getCurrentUrl());
        allAdvertisements = advertisementsPage.getAllAdvertisements();
        Advertisement oldAdvertisement = advertisementsPage.getAdvertisment(allAdvertisements, adverName, adverBeacon);
        updateAdvertisement(oldAdvertisement, new Advertisement(adverNameNew, adverBeaconNew, adverCategoryNew, adverDescriptionNew, adverPriceNew));

    }

    @Test(priority = 3)
    @Parameters({"adverNameNew", "adverBeaconNew"})
    public void deleteAdvertisementTC(String adverName, String advBeacon) {
        webDriver.get(advertisementsUrl);
        allAdvertisements = advertisementsPage.getAllAdvertisements();
        Advertisement advertisement = advertisementsPage.getAdvertisment(allAdvertisements, adverName, advBeacon);
        advertisementsPage.clickDeleteAdvBtn(advertisement.getDeleteBtn());
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
