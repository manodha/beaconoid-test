package com.company.util;

/**
 * Created by manodha on 28/5/17.
 */
public class APIConstants {
    /* Production API Url */
    //public static String apiUrl = "api.beaconoid.me/api/v1/"

    /* Development API Url */
    public static String apiUrl = "http://localhost:3001/api/v1/";

    public static String adverAPI = apiUrl + "advertisements?";

    /* API Response Statuses */
    public static String successStatus = "success";
    public static String failedStatus = "failed";

    /* Failed reasons */
    public static String noAdvertisement = "advertisement not found";
    public static String noBeacon = "beacon not found";
    public static String wrongParameters = "please pass the right parameters";

}
