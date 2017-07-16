package com.example.rahul.redditclient;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Rahul on 15-07-2017.
 */

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private static String after = null;

    String mStringURL;
    /**
     * Query the USGS dataset and return a list of {@link Reddit} objects.
     */
    public static List<Reddit> fetchRedditData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Reddit}s
        List<Reddit> reddits = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Reddit}s
        return reddits;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl+"&after=" + after );
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the reddit JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Reddit} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Reddit> extractFeatureFromJson(String redditJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(redditJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding reddits to
        List<Reddit> reddits = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(redditJSON);

            JSONObject data = baseJsonResponse.getJSONObject("data");

            after = data.getString("after");

            JSONArray children = data.getJSONArray("children");

            for(int i=0;i<children.length();i++)
            {
                JSONObject children_obj1 = children.getJSONObject(i);

                JSONObject children_obj = children_obj1.getJSONObject("data");

                String thumbnail = children_obj.getString("thumbnail");

                String title = children_obj.getString("title");

                String comments= children_obj.getString("num_comments");

                double timestamp = children_obj.getDouble("created");

                String date = new SimpleDateFormat("dd-MMM hh:mm a", Locale.US).format(new Date((long) (timestamp * 1000)));

                Reddit reddit = new Reddit(thumbnail,date,title,comments);

                // Add the new {@link Reddit} to the list of reddits.
                reddits.add(reddit);
            }








            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or reddits).


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the reddit JSON results", e);
        }

        // Return the list of reddits
        return reddits;
    }




}
