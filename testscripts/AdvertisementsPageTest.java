package com.company.testscripts;

import com.company.model.Advertisement;
import com.company.view.AdvertisementsPage;
import com.company.view.NavigationMenu;
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
        Advertisement advertisement = new Advertisement(adverName, "", "", adverDescription, adverPrice);
        createAdvertisement(advertisement);
    }

    @Test(priority = 2)
    @Parameters({"adverName", "adverBeacon", "adverNameNew", "adverBeaconNew", "adverCategoryNew", "adverDescriptionNew", "adverPriceNew"})
    public void updateAdvertisementTC(String adverName, String adverBeacon, String adverNameNew, String adverBeaconNew,
                                      String adverCategoryNew, String adverDescriptionNew, String adverPriceNew) {
        allAdvertisements = advertisementsPage.getAllAdvertisements();
        printAllAdvertisements(allAdvertisements);
        Advertisement advertisement = getAdvertisment(allAdvertisements, adverName, adverBeacon);
        printAdvertisement(advertisement);
        Advertisement newAdvertisement = new Advertisement(adverNameNew, adverBeaconNew, adverCategoryNew, adverDescriptionNew, adverPriceNew);
        updateAdvertisement(advertisement, newAdvertisement);
    }

    @Test(priority = 3)
    @Parameters({"adverNameNew", "adverBeaconNew"})
    public void deleteAdvertisementTC(String adverName, String advBeacon) {
        webDriver.get(advertisementsUrl);
        allAdvertisements = advertisementsPage.getAllAdvertisements();
        Advertisement advertisement = getAdvertisment(allAdvertisements, adverName, advBeacon);
        deleteAdvertisement(advertisement);
    }


    private void createAdvertisement(Advertisement advertisement) {
        assertEquals(advertisementsUrl, webDriver.getCurrentUrl());
        advertisementsPage.clickNewAdvertisementBtn();
        enterAdvertismentDetails(advertisement);
    }


    private void updateAdvertisement(Advertisement oldAdvertisement, Advertisement newAdvertisement) {
        assertEquals(advertisementsUrl, webDriver.getCurrentUrl());
        advertisementsPage.clickEditAdverBtn(oldAdvertisement.getEditLink());
        assertEquals(oldAdvertisement.getEditLink().getAttribute("href"), webDriver.getCurrentUrl());
        enterAdvertismentDetails(newAdvertisement);

    }

    private void deleteAdvertisement(Advertisement advertisement) {
        advertisementsPage.clickDeleteAdvBtn(advertisement.getDeleteBtn());
    }

    private Advertisement getAdvertisment(List<Advertisement> advertisements, String name, String beaconName) {
        for (Advertisement advertisement : advertisements) {
            if (advertisement.getName().equals(name) && advertisement.getBeacon().equals(beaconName)) {
                return advertisement;
            }
        }
        return null;
    }

    private void enterAdvertismentDetails(Advertisement advertisement) {
        advertisementsPage.enterAdverName(advertisement.getName());
        if (advertisement.getBeacon().equals("")) {
            advertisementsPage.selectAdverBeaconByIndex(1);
        } else {
            advertisementsPage.selectAdverBeaconByName(advertisement.getBeacon());
        }
        if (advertisement.getCategory().equals("")) {
            advertisementsPage.selectAdverCategoryByIndex(1);
        } else {
            advertisementsPage.selectAdverCategoryByName(advertisement.getCategory());
        }
        advertisementsPage.enterAdvDescription(advertisement.getDescription());
        advertisementsPage.enterAdvPrice(advertisement.getPrice());
        advertisementsPage.clickCreateUpdateAdverBtn();
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(advertisementsUrl, webDriver.getCurrentUrl());
    }


    private void printAllAdvertisements(List<Advertisement> advertisements) {
        System.out.println("*********** All Advertisements ***********");
        for (Advertisement advertisement : advertisements) {
            printAdvertisement(advertisement);
        }
    }

    private void printAdvertisement(Advertisement advertisement) {
        System.out.print("Name - " + advertisement.getName());
        System.out.print(" Beacon - " + advertisement.getBeacon());
        System.out.print(" Category- " + advertisement.getCategory());
        System.out.print(" Description - " + advertisement.getDescription());
        System.out.println(" Price - " + advertisement.getPrice());
    }
}
