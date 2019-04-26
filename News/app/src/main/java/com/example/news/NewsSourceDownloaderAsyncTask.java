package com.example.news;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import static java.net.HttpURLConnection.HTTP_OK;

public class NewsSourceDownloaderAsyncTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "LoginAPIAyncTask";
    private static final String baseUrl =
            "https://newsapi.org/v2/sources?language=en&country=us&category=";
    private static final String API_KEY = "&apiKey=423b2938e0ba47ebad71deb2445fd9e5";
    private MainActivity mainActivity;

    public NewsSourceDownloaderAsyncTask(MainActivity mainActivity) {
        // Pass in a reference to
        // the MainActivity and
        // the String news
        // category
        this.mainActivity = mainActivity;
        // If the news category
        // string passed in is “all” -
        // set the category data
        // member to an empty
        // string

        // Otherwise, set the
        // category data member
        // to the category string
        // passed in
    }

    @SuppressLint("StaticFieldLeak")
    
    

    @Override
    protected void onPostExecute(String connectionResult) {
        // Parse JSON string of sources into a list of “Source” objects
        ArrayList<Source> sourceObjects = parseJSON(connectionResult);

        // Call “setSources” in MainActivity passing the
        // list of source objects and the list of categories
        mainActivity.setSources(sourceObjects);
    }

    @Override
    protected String doInBackground(String... strings) {
        String category = strings[0];

        String urlString = baseUrl + category + API_KEY;  // Build the full URL

        Uri dataUri = Uri.parse(urlString);
        String urlToUse = dataUri.toString();

        StringBuilder sb = new StringBuilder();

        BufferedReader reader = null;
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            // If successful (HTTP_OK)
            if (responseCode == HTTP_OK) {
                Log.d(TAG, "HTTP okay:");

                // Read the results - use connection's getInputStream
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while (null != (line = reader.readLine())) {
                    sb.append(line).append("\n");
                }

                // Return the results (to onPostExecute)
                return sb.toString();

            } else {
                Log.d(TAG, "HTTP not okay:");

                // Not HTTP_OK - some error occurred - use connection's getErrorStream
                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                String line;
                while (null != (line = reader.readLine())) {
                    sb.append(line).append("\n");
                }

                // Return the results (to onPostExecute)
                return sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private ArrayList<Source> parseJSON(String s) {

        ArrayList<Source> sourceObjects = new ArrayList<>();
        try {
            JSONObject jSource = new JSONObject(s);

            JSONArray sources = new JSONArray(jSource.getString("sources"));
            Log.d(TAG, "parseJSON sources: " + sources.toString());
            for (int j = 0; j < sources.length(); j++) {
                JSONObject object = sources.getJSONObject(j);
                sourceObjects.add(new Source(object.getString("id"),
                        object.getString("name"),
                        object.getString("category")));
            }
            return sourceObjects;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
