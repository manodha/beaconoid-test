package com.company.testscripts;

import com.company.pageobjects.*;
import com.company.util.APIConstants;
import com.company.util.APIHandler;
import com.company.util.WebConstants;
import org.json.JSONException;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

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
    private int numOfFetchesBefore;
    private int numOfClicksBefore;
    private int numOfClients;
    private String response;

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

    @Test(priority = 1, testName = "TC132")
    public void checkIfUnUserCanFetchAdver() throws JSONException {
        numOfFetchesBefore = reportPage.getTotalFetches();
        response = fetchAdvertisements("", WebConstants.defaultTestBeacon.getUniqueRef());
        assertEquals(APIConstants.failedStatus, APIHandler.getStatus(response));
        assertEquals(APIConstants.wrongParameters, APIHandler.getFailedReason(response));
        assertEquals(numOfFetchesBefore, reportPage.getTotalFetches());
    }

    @Test(priority = 2, testName = "TC133")
    public void checkIfUnUserCanClkAdver() throws JSONException {
        numOfClicksBefore = reportPage.getTotalClicks();
        response = clickAdvertisement("", advertisementsPage.getAdvertisementID(defaultAdverUrl));
        assertEquals(APIConstants.failedStatus, APIHandler.getStatus(response));
        assertEquals(APIConstants.wrongParameters, APIHandler.getFailedReason(response));
        assertEquals(numOfClicksBefore, reportPage.getTotalClicks());
    }

    @Test(priority = 3, testName = "TC134")
    @Parameters({"clientEmail"})
    public void checkIfClientCountIncreOnNewUser(String email) {
        numOfClients = reportPage.getTotalClients();
        response = fetchAdvertisements(email, WebConstants.defaultTestBeacon.getUniqueRef());
        assertEquals(++numOfClients, reportPage.getTotalClients());
    }

    @Test(priority = 4, testName = "TC135")
    @Parameters({"clientEmail"})
    public void checkIfClientCountIncreOnExistingUser(String email) {
        numOfClients = reportPage.getTotalClients();
        response = fetchAdvertisements(email, WebConstants.defaultTestBeacon.getUniqueRef());
        assertEquals(numOfClients, reportPage.getTotalClients());
    }

    @Test(priority = 5, testName = "T136")
    @Parameters({"clientEmail"})
    public void checkIfFetchCountIncreOnFetchAdver(String email) throws JSONException {
        numOfFetchesBefore = reportPage.getTotalFetches();
        response = fetchAdvertisements(email, WebConstants.defaultTestBeacon.getUniqueRef());
        assertEquals(APIConstants.successStatus, APIHandler.getStatus(response));
        assertEquals(++numOfFetchesBefore, reportPage.getTotalFetches());
    }

    @Test(priority = 6, testName = "TC137")
    @Parameters({"clientEmail"})
    public void checkIfClkCountIncreOnClkAdver(String email) throws JSONException {
        numOfClicksBefore = reportPage.getTotalClicks();
        response = clickAdvertisement(email, advertisementsPage.getAdvertisementID(defaultAdverUrl));
        assertEquals(APIConstants.successStatus, APIHandler.getStatus(response));
        assertEquals(++numOfClicksBefore, reportPage.getTotalClicks());
    }

    @Test(priority = 7, testName = "TC140")
    @Parameters({"clientEmail"})
    public void checkIncorrectParamsMessage(String email) throws JSONException {
        response = fetchAdvertisements("", "");
        assertEquals(APIConstants.failedStatus, APIHandler.getStatus(response));
        assertEquals(APIConstants.wrongParameters, APIHandler.getFailedReason(response));

        response = fetchAdvertisements(email, "");
        assertEquals(APIConstants.failedStatus, APIHandler.getStatus(response));
        assertEquals(APIConstants.wrongParameters, APIHandler.getFailedReason(response));

        response = fetchAdvertisements("", WebConstants.defaultTestBeacon.getUniqueRef());
        assertEquals(APIConstants.failedStatus, APIHandler.getStatus(response));
        assertEquals(APIConstants.wrongParameters, APIHandler.getFailedReason(response));

        response = clickAdvertisement("", "");
        assertEquals(APIConstants.failedStatus, APIHandler.getStatus(response));
        assertEquals(APIConstants.wrongParameters, APIHandler.getFailedReason(response));

        response = clickAdvertisement(email, "");
        assertEquals(APIConstants.failedStatus, APIHandler.getStatus(response));
        assertEquals(APIConstants.wrongParameters, APIHandler.getFailedReason(response));

        response = clickAdvertisement("", advertisementsPage.getAdvertisementID(defaultAdverUrl));
        assertEquals(APIConstants.failedStatus, APIHandler.getStatus(response));
        assertEquals(APIConstants.wrongParameters, APIHandler.getFailedReason(response));

    }

    @BeforeGroups("DelTestAdverAndBeacon")
    public void delTestAdverAndBeacon() {
        advertisementsPage = accessAdvertisementsPage(navigationMenu);
        deleteAdvertisement(advertisementsPage, WebConstants.defaultTestAdvertisement);

        beaconsPage = accessBeaconsPage(navigationMenu);
        deleteBeacon(beaconsPage, WebConstants.defaultTestBeacon);

        reportPage = accessReportPage(navigationMenu);
    }

    @Test(priority = 8, testName = "TC138", groups = "DelTestAdverAndBeacon")
    @Parameters({"clientEmail"})
    public void checkIfDelAdverCanBeClicked(String email) throws JSONException {
        numOfClicksBefore = reportPage.getTotalClicks();
        response = clickAdvertisement(email, advertisementsPage.getAdvertisementID(defaultAdverUrl));

        assertEquals(APIConstants.failedStatus, APIHandler.getStatus(response));
        assertEquals(APIConstants.noAdvertisement, APIHandler.getFailedReason(response));
        assertEquals(numOfClicksBefore, reportPage.getTotalClicks());
    }

    @Test(priority = 9, testName = "TC139", groups = "DelTestAdverAndBeacon")
    @Parameters({"clientEmail"})
    public void checkIfAdverCanBeFetched(String email) throws JSONException {
        numOfFetchesBefore = reportPage.getTotalFetches();
        response = fetchAdvertisements(email, WebConstants.defaultTestBeacon.getUniqueRef());

        assertEquals(APIConstants.failedStatus, APIHandler.getStatus(response));
        assertEquals(APIConstants.noBeacon, APIHandler.getFailedReason(response));
        assertEquals(numOfFetchesBefore, reportPage.getTotalFetches());
    }

    @AfterTest
    public void clearTestData() {
        categoryPage = accessCategoriesPage(navigationMenu);
        deleteCategory(categoryPage, WebConstants.defaultTestCategory);

        storesPage = accessStoresPage(navigationMenu);
        deleteStore(storesPage, WebConstants.defaultTestStore);
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
}
