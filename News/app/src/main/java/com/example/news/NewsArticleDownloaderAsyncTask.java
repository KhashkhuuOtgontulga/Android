package com.example.news;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static java.net.HttpURLConnection.HTTP_OK;

public class NewsArticleDownloaderAsyncTask extends AsyncTask<String, Void, String> {
    // Needs data members to store the MainActivity and the
    // string news category
    private static final String TAG = "NewsArticleAPIAyncTask";
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

        ArrayList<Article> articleObjects = parseJSON(connectionResult);

        // Call “setSources” in
        // MainActivity passing the
        // list of source objects
        // and the list of
        // categories
        // Call “setArticles” in NewsService
        mainActivity.setArticles(articleObjects);

    }

    @Override
    protected String doInBackground(String... strings) {
        String source = strings[0];

        String urlString = baseUrl + source + API_KEY;  // Build the full URL

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

    private ArrayList<Article> parseJSON(String s) {

        ArrayList<Article> articleObjects = new ArrayList<>();
        try {
            JSONObject jSource = new JSONObject(s);

            JSONArray articles = new JSONArray(jSource.getString("articles"));
            Log.d(TAG, "parseJSON articles: " + articles.toString());
            for (int j = 0; j < articles.length(); j++) {
                JSONObject object = articles.getJSONObject(j);
                String author = object.getString("author");
                Log.d(TAG, "parseJSON author: " + author);
                if (author.equals("null")){
                    author = "";
                }
                String title = object.getString("title");
                Log.d(TAG, "parseJSON title: " + title);
                if (title.equals("null")) {
                    title = "";
                }

                articleObjects.add(new Article(author,
                        title,
                        object.getString("description"),
                        object.getString("url"),
                        object.getString("urlToImage"),
                        object.getString("publishedAt")));
                Log.d(TAG, "parseJSON: " + object.toString());
            }
            return articleObjects;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
