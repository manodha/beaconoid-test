package com.company.testscripts;

import com.company.pageobjects.*;
import com.company.util.APIConstants;
import com.company.util.APIHandler;
import com.company.util.WebConstants;
import org.json.JSONException;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.testng.AssertJUnit.assertEquals;

/**
 * Created by manodha on 28/5/17.
 */
public class APITest extends FunctionalTest {

    private WebDriver apiDriver;
    private ReportPage reportPage;
    private NavigationMenu navigationMenu;
    private StoresPage storesPage;
    private BeaconsPage beaconsPage;
    private CategoryPage categoryPage;
    private AdvertisementsPage advertisementsPage;
    private String defaultAdverUrl;

    @BeforeTest
    @Parameters({"email", "password"})
    public void setup(String email, String password) {
        navigationMenu = loginToBeaconoid(email, password);

        storesPage = accessStoresPage(navigationMenu);
        createStore(storesPage, WebConstants.defaultTestStore);

        categoryPage = accessCategoriesPage(navigationMenu);
        createCategory(categoryPage, WebConstants.defaultTestCategory);

        beaconsPage = accessBeaconsPage(navigationMenu);
        createBeacon(beaconsPage, WebConstants.defaultTestBeacon);

        advertisementsPage = accessAdvertisementsPage(navigationMenu);
        createAdvertisement(advertisementsPage, WebConstants.defaultTestAdvertisement);

        defaultAdverUrl = webDriver.getCurrentUrl();

        reportPage = accessReportPage(navigationMenu);
    }

    @Test(priority = 1)
    @Parameters({"clientEmail"})
    public void checkIfFetchCountIncreOnFetchAdver(String email) throws JSONException {
        int numOfFetchesBefore = reportPage.getTotalFetches();
        String response = fetchAdvertisements(email, WebConstants.defaultTestBeacon.getUniqueRef());
        assertEquals(APIConstants.successStatus, APIHandler.getStatus(response));
        assertEquals(++numOfFetchesBefore, reportPage.getTotalFetches());
    }

    @Test(priority = 2)
    @Parameters({"clientEmail"})
    public void checkIfClickCountIncreOnClickAdver(String email) throws JSONException {
        int numOfClicksBefore = reportPage.getTotalClicks();
        String response = clickAdvertisement(email, advertisementsPage.getAdvertisementID(defaultAdverUrl));
        assertEquals(APIConstants.successStatus, APIHandler.getStatus(response));
        assertEquals(++numOfClicksBefore, reportPage.getTotalClicks());
    }

    private String fetchAdvertisements(String email, String uniqueRef) {
        String fetchUrl = APIConstants.adverAPI + "email=" + email + "&beacon_id=" + uniqueRef;
        String response = null;
        try {
            InputStream stream = APIHandler.getInputStream(fetchUrl);
            response = APIHandler.getJSONResponse(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private String clickAdvertisement(String email, String adverID) {
        String clickUrl = APIConstants.adverAPI + "email=" + email + "&advertisement_id=" + adverID;
        String response = null;
        try {
            InputStream stream = APIHandler.getInputStream(clickUrl);
            response = APIHandler.getJSONResponse(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


    @AfterTest
    public void clearTestData() {
        //closeBrowserInstance(apiDriver);

        advertisementsPage = accessAdvertisementsPage(navigationMenu);
        deleteAdvertisement(advertisementsPage, WebConstants.defaultTestAdvertisement);

        beaconsPage = accessBeaconsPage(navigationMenu);
        deleteBeacon(beaconsPage, WebConstants.defaultTestBeacon);

        categoryPage = accessCategoriesPage(navigationMenu);
        deleteCategory(categoryPage, WebConstants.defaultTestCategory);

        storesPage = accessStoresPage(navigationMenu);
        deleteStore(storesPage, WebConstants.defaultTestStore);
    }
}
