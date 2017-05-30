package util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by manodha on 28/5/17.
 */
public class APIHandler {

    public static InputStream getInputStream(String url) throws IOException {
        InputStream stream = null;
        URL newUrl = new URL(url);
        URLConnection urlConnection = newUrl.openConnection();

        try {
            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return stream;
        }
        return stream;
    }

    public static String readJsonResponse(InputStream stream) throws IOException {
        if (stream == null) {
            return null;
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            line = line + "\n";
            stringBuilder.append(line);
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }

    public static String getStatus(String response) throws JSONException {
        JSONObject jsonResponse = new JSONObject(response);
        return jsonResponse.getString("status");
    }

    public static String getFailedReason(String response) throws JSONException {
        JSONObject jsonResponse = new JSONObject(response);
        return jsonResponse.getString("reason");
    }

    public static JSONArray getBeacAdversJson(String response) throws JSONException {
        JSONObject jsonResponse = new JSONObject(response);
        if (!jsonResponse.getString("status").equals(APIConstants.failedStatus))
            return jsonResponse.getJSONArray("advertisements");
        return null;
    }
}

