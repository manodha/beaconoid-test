package com.company.testscripts;

import com.company.model.Advertisement;
import com.company.pageobjects.*;
import com.company.util.APIConstants;
import com.company.util.APIHandler;
import com.company.util.WebConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

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
    private int numOfFetchesBefore;
    private int numOfClicksBefore;
    private int numOfClients;
    private String response;

    @BeforeTest
    @Parameters({"email", "password"})
    public void setupTestEnv(String email, String password) {
        navigationMenu = loginToBeaconoid(webDriver, email, password);

        storesPage = accessStoresPage(webDriver, navigationMenu);
        createStore(webDriver, storesPage, WebConstants.defaultTestStore);

        categoryPage = accessCategoriesPage(webDriver, navigationMenu);
        createCategory(webDriver, categoryPage, WebConstants.defaultTestCategory);

        beaconsPage = accessBeaconsPage(webDriver, navigationMenu);
        createBeacon(webDriver, beaconsPage, WebConstants.defaultTestBeacon);

        advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);
        createAdvertisement(webDriver, advertisementsPage, WebConstants.defaultTestAdvertisement);
        defaultAdverUrl = webDriver.getCurrentUrl();

        reportPage = accessReportPage(webDriver, navigationMenu);
    }

    @Test(priority = 1, testName = "TC132")
    public void checkIfUnUserCanFetchAdver() throws JSONException {
        numOfClients = reportPage.getTotalClients();
        numOfFetchesBefore = reportPage.getTotalFetches();
        response = fetchBeaconAdvers("", WebConstants.defaultTestBeacon.getUniqueRef());

        assertEquals(APIConstants.failedStatus, APIHandler.getStatus(response));
        assertEquals(APIConstants.wrongParameters, APIHandler.getFailedReason(response));
        assertEquals(numOfClients, reportPage.getTotalClients());
        assertEquals(numOfFetchesBefore, reportPage.getTotalFetches());
    }

    @Test(priority = 2, testName = "TC133")
    public void checkIfUnUserCanClkAdver() throws JSONException {
        numOfClicksBefore = reportPage.getTotalClients();
        numOfClicksBefore = reportPage.getTotalClicks();
        response = clickAdvertisement("", advertisementsPage.getAdvertisementID(defaultAdverUrl));

        assertEquals(APIConstants.failedStatus, APIHandler.getStatus(response));
        assertEquals(APIConstants.wrongParameters, APIHandler.getFailedReason(response));
        assertEquals(numOfClients, reportPage.getTotalClients());
        assertEquals(numOfClicksBefore, reportPage.getTotalClicks());
    }

    @Test(priority = 3, testName = "TC134")
    @Parameters({"clientEmail"})
    public void checkIfClientCountIncreOnNewUser(String email) throws JSONException {
        numOfClients = reportPage.getTotalClients();
        numOfFetchesBefore = reportPage.getTotalFetches();
        response = fetchBeaconAdvers(email, WebConstants.defaultTestBeacon.getUniqueRef());

        assertEquals(APIConstants.successStatus, APIHandler.getStatus(response));
        assertEquals(++numOfFetchesBefore, reportPage.getTotalFetches());
        assertEquals(++numOfClients, reportPage.getTotalClients());
    }

    @Test(priority = 4, testName = "TC135")
    @Parameters({"clientEmail"})
    public void checkIfClientCountIncreOnExistingUser(String email) throws JSONException {
        numOfClients = reportPage.getTotalClients();
        numOfFetchesBefore = reportPage.getTotalFetches();
        response = fetchBeaconAdvers(email, WebConstants.defaultTestBeacon.getUniqueRef());

        assertEquals(APIConstants.successStatus, APIHandler.getStatus(response));
        assertEquals(++numOfFetchesBefore, reportPage.getTotalFetches());
        assertEquals(numOfClients, reportPage.getTotalClients());
    }

    @Test(priority = 5, testName = "TC136")
    @Parameters({"clientEmail"})
    public void checkIfFetchCountIncreOnFetchAdver(String email) throws JSONException {
        numOfFetchesBefore = reportPage.getTotalFetches();
        response = fetchBeaconAdvers(email, WebConstants.defaultTestBeacon.getUniqueRef());
        assertEquals(APIConstants.successStatus, APIHandler.getStatus(response));
        assertEquals(++numOfFetchesBefore, reportPage.getTotalFetches());
    }

    @Test(priority = 6, testName = "TC141")
    @Parameters({"clientEmail"})
    public void checkIfAdversAreFetchedOnFetchBeacon(String email) throws JSONException {
        numOfFetchesBefore = reportPage.getTotalFetches();
        response = fetchBeaconAdvers(email, WebConstants.defaultTestBeacon.getUniqueRef());
        JSONArray jsonArray = APIHandler.getBeacAdversJson(response);
        List<Advertisement> advertisements = advertisementsPage.getBeaconAdvers(jsonArray);
        advertisementsPage.printAllAdvertisements(advertisements);
        assertEquals(APIConstants.successStatus, APIHandler.getStatus(response));
        assertNotNull(advertisements);
        assertThat(advertisements, hasItem(allOf(
                hasProperty("name", equalTo(WebConstants.defaultTestAdvertisement.getName())),
                hasProperty("description", equalTo(WebConstants.defaultTestAdvertisement.getDescription()))
        )));
        assertEquals(++numOfFetchesBefore, reportPage.getTotalFetches());
    }

    @Test(priority = 7, testName = "TC137")
    @Parameters({"clientEmail"})
    public void checkIfClkCountIncreOnClkAdver(String email) throws JSONException {
        numOfClicksBefore = reportPage.getTotalClicks();
        response = clickAdvertisement(email, advertisementsPage.getAdvertisementID(defaultAdverUrl));
        assertEquals(APIConstants.successStatus, APIHandler.getStatus(response));
        assertEquals(++numOfClicksBefore, reportPage.getTotalClicks());
    }

    @Test(priority = 8, testName = "TC140")
    @Parameters({"clientEmail"})
    public void checkIncorrectParamsMessage(String email) throws JSONException {
        numOfFetchesBefore = reportPage.getTotalFetches();
        response = fetchBeaconAdvers("", "");
        assertEquals(APIConstants.failedStatus, APIHandler.getStatus(response));
        assertEquals(APIConstants.wrongParameters, APIHandler.getFailedReason(response));
        assertEquals(numOfFetchesBefore, reportPage.getTotalFetches());

        numOfFetchesBefore = reportPage.getTotalFetches();
        response = fetchBeaconAdvers(email, "");
        assertEquals(APIConstants.failedStatus, APIHandler.getStatus(response));
        assertEquals(APIConstants.wrongParameters, APIHandler.getFailedReason(response));
        assertEquals(numOfFetchesBefore, reportPage.getTotalFetches());

        numOfFetchesBefore = reportPage.getTotalFetches();
        response = fetchBeaconAdvers("", WebConstants.defaultTestBeacon.getUniqueRef());
        assertEquals(APIConstants.failedStatus, APIHandler.getStatus(response));
        assertEquals(APIConstants.wrongParameters, APIHandler.getFailedReason(response));
        assertEquals(numOfFetchesBefore, reportPage.getTotalFetches());

        numOfClicksBefore = reportPage.getTotalClicks();
        response = clickAdvertisement("", "");
        assertEquals(APIConstants.failedStatus, APIHandler.getStatus(response));
        assertEquals(APIConstants.wrongParameters, APIHandler.getFailedReason(response));
        assertEquals(numOfClicksBefore, reportPage.getTotalClicks());

        numOfClicksBefore = reportPage.getTotalClicks();
        response = clickAdvertisement(email, "");
        assertEquals(APIConstants.failedStatus, APIHandler.getStatus(response));
        assertEquals(APIConstants.wrongParameters, APIHandler.getFailedReason(response));
        assertEquals(numOfClicksBefore, reportPage.getTotalClicks());

        numOfClicksBefore = reportPage.getTotalClicks();
        response = clickAdvertisement("", advertisementsPage.getAdvertisementID(defaultAdverUrl));
        assertEquals(APIConstants.failedStatus, APIHandler.getStatus(response));
        assertEquals(APIConstants.wrongParameters, APIHandler.getFailedReason(response));
        assertEquals(numOfClicksBefore, reportPage.getTotalClicks());

    }

    @BeforeGroups("DelTestAdverAndBeacon")
    public void delTestAdverAndBeacon() {
        advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);
        deleteAdvertisement(webDriver, advertisementsPage, WebConstants.defaultTestAdvertisement);

        beaconsPage = accessBeaconsPage(webDriver, navigationMenu);
        deleteBeacon(webDriver, beaconsPage, WebConstants.defaultTestBeacon);

        reportPage = accessReportPage(webDriver, navigationMenu);
    }

    @Test(priority = 9, testName = "TC138", groups = "DelTestAdverAndBeacon")
    @Parameters({"clientEmail"})
    public void checkIfDelAdverCanBeClicked(String email) throws JSONException {
        numOfClicksBefore = reportPage.getTotalClicks();
        response = clickAdvertisement(email, advertisementsPage.getAdvertisementID(defaultAdverUrl));

        assertEquals(APIConstants.failedStatus, APIHandler.getStatus(response));
        assertEquals(APIConstants.noAdvertisement, APIHandler.getFailedReason(response));
        assertEquals(numOfClicksBefore, reportPage.getTotalClicks());
    }

    @Test(priority = 10, testName = "TC139", groups = "DelTestAdverAndBeacon")
    @Parameters({"clientEmail"})
    public void checkIfAdverCanBeFetched(String email) throws JSONException {
        numOfFetchesBefore = reportPage.getTotalFetches();
        response = fetchBeaconAdvers(email, WebConstants.defaultTestBeacon.getUniqueRef());

        assertEquals(APIConstants.failedStatus, APIHandler.getStatus(response));
        assertEquals(APIConstants.noBeacon, APIHandler.getFailedReason(response));
        assertEquals(numOfFetchesBefore, reportPage.getTotalFetches());
    }

    @AfterTest
    public void clearTestEnv() {
        categoryPage = accessCategoriesPage(webDriver, navigationMenu);
        deleteCategory(webDriver, categoryPage, WebConstants.defaultTestCategory);

        storesPage = accessStoresPage(webDriver, navigationMenu);
        deleteStore(webDriver, storesPage, WebConstants.defaultTestStore);
    }

    private String fetchBeaconAdvers(String email, String uniqueRef) {
        String fetchUrl = APIConstants.adverAPI + "email=" + email + "&beacon_id=" + uniqueRef;
        String response = null;
        try {
            InputStream stream = APIHandler.getInputStream(fetchUrl);
            response = APIHandler.readJsonResponse(stream);
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
            response = APIHandler.readJsonResponse(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
