package util;

import model.Advertisement;
import model.Beacons;
import model.Category;
import model.Stores;

/**
 * Created by manodha on 18/5/17.
 */
public class WebConstants {
    public static int waitMilliSeconds = 2500;
    public static Stores defaultTestStore = new Stores("TGI Fridays Fountain Gate", "TGI001",
            "56789");

    public static Category defaultTestCategory = new Category("Restaurants & Cafes",
            "Including but not limited to Fast Food Places");

    public static Beacons defaultTestBeacon = new Beacons("BE010", "TGI BEACON", "TGI Fridays Fountain Gate",
            "Active", "38.0180° S", "145.3039° E");

    public static Advertisement defaultTestAdvertisement = new Advertisement("50% Deal on Onion Rings", "TGI BEACON",
            "Restaurants & Cafes", "50% off on the side onion rings when purchased with meal", "", "$10.00");

    /* Production base URL */
    //public static String baseUrl = "https://www.beaconoid.me/";


    /* Development URL*/
    public static String baseUrl = "http://localhost:3000/";


    public static String reportsUrl = baseUrl + "report";
    public static String storeReportsUrl = baseUrl + "report/store";
    public static String categoryReportsUrl = baseUrl + "report/category";

    public static String addAdvertisementUrl = baseUrl + "advertisements/new";
    public static String newAdverTitle = "New Advertisement";
    public static String editAdverTitle = "Edit Advertisement";
    public static String advertisementsUrl = baseUrl + "advertisements";

    public static String addBeaconUrl = baseUrl + "beacons/new";
    public static String beaconsUrl = baseUrl + "beacons";

    public static String addCategoryUrl = baseUrl + "categories/new";
    public static String categoriesUrl = baseUrl + "categories";
    public static String cantDelCateMsg = " cannot be deleted. Active advertisements have been assigned to this category.";

    public static String storesUrl = baseUrl + "stores";
    public static String addStoreUrl = baseUrl + "stores/new";
    public static String creaStoreSucess = "The Store has been created!";
    public static String updaStoreSucess = " has been updated!";
    public static String delStoreError = " has not been deleted! Beacons are assigned to this store.";
    public static String delStoreSucess = " has been deleted!";

    public static String dashboardUrl = baseUrl + "dashboard";


    public static String loginUrl = baseUrl + "users/sign_in";
    public static String loginTitle = "Please Sign In";

    public static String duplicateBeaconDanger = "Name has already been taken";
    public static String noAdvertisementTxt = "No advertisement found.";

    public static String staffUrl = baseUrl + "staffs";
    public static String addStaffUrl = baseUrl + "staffs/new";
    public static String deleteStaffMsg = " has been deleted!";
    public static String notAuthorisedMsg = "You are not authorized to perform this action.";

}
