package com.example.iitnewsgateway;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.net.HttpURLConnection.HTTP_OK;

public class NewsArticleDownloaderAsyncTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "LoginAPIAyncTask";
    private static final String baseUrl =
            "https://newsapi.org/v2/everything?sources=";
    private static final String API_KEY = "&language=en&pageSize=100&apiKey=423b2938e0ba47ebad71deb2445fd9e5";
    private MainActivity mainActivity;

    public NewsArticleDownloaderAsyncTask(MainActivity mainActivity) {
        // Pass in a reference to
        // the NewsService and a
        // news Source object
        this.mainActivity = mainActivity;
        // Set the NewsService
        // and news Source data
        // fields using the
        // parameters passed in
    }

    @SuppressLint("StaticFieldLeak")



    @Override
    protected void onPostExecute(String connectionResult) {
        // Parse JSON string of
        // sources into a list of
        // “Article” objects

        // Call “setArticles” in NewsService
        if (connectionResult.contains("error")) // If there is "error" in the results...
            mainActivity.setArticles(true, connectionResult);
        else
            mainActivity.setArticles(false, connectionResult);
    }

    @Override
    protected String doInBackground(String... strings) {
        String source = strings[0];

        Log.d(TAG, "login async task doInBackground: ");
        try {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                // Connect to newsapi.org
                // Make a news article
                // query for the specified
                // news source
                String urlString = baseUrl + source + API_KEY;  // Build the full URL
                Log.d(TAG, "login doAPICall: " + urlString);

                Uri uri = Uri.parse(urlString);    // Convert String url to URI
                URL url = new URL(uri.toString()); // Convert URI to URL

                int responseCode = connection.getResponseCode();

                StringBuilder result = new StringBuilder();

                // If successful (HTTP_OK)
                if (responseCode == HTTP_OK) {
                    Log.d(TAG, "HTTP okay:");

                    // Read the results - use connection's getInputStream
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while (null != (line = reader.readLine())) {
                        result.append(line).append("\n");
                    }

                    // Return the results (to onPostExecute)
                    return result.toString();

                } else {
                    Log.d(TAG, "HTTP not okay:");

                    // Not HTTP_OK - some error occurred - use connection's getErrorStream
                    reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    String line;
                    while (null != (line = reader.readLine())) {
                        result.append(line).append("\n");
                    }

                    // Return the results (to onPostExecute)
                    return result.toString();
                }
            } catch (Exception e) {
                // Some exception occurred! Log it.
                Log.d(TAG, "doAuth: " + e.getClass().getName() + ": " + e.getMessage());

            } finally { // Close everything!
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(TAG, "doInBackground: Error closing stream: " + e.getMessage());
                    }
                }
            }
            return "Some error has occurred"; // Return an error message if Exception occurred

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
