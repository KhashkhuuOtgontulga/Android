package com.example.rewards.AsyncTasks;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.rewards.Activities.CreateProfileActivity;
import com.example.rewards.Activities.LeaderboardActivity;
import com.example.rewards.Activities.LoginActivity;
import com.example.rewards.UserProfile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static java.net.HttpURLConnection.HTTP_OK;

public class CreateProfileAPIAsyncTask extends AsyncTask<String, Integer, String> {

    private static final String TAG = "LoginAPIAyncTask";
    private static final String baseUrl =
            "http://inspirationrewardsapi-env.6mmagpm2pv.us-east-2.elasticbeanstalk.com";
    private static final String loginEndPoint ="/profiles";
    @SuppressLint("StaticFieldLeak")
    private CreateProfileActivity createProfileActivity;

    public CreateProfileAPIAsyncTask(CreateProfileActivity la) {
        createProfileActivity = la;
    }

    @Override
    protected void onPostExecute(String connectionResult) {
        if (connectionResult.contains("error")) // If there is "error" in the results...
            Log.d(TAG, "create profile error: " + connectionResult);
        else
            Log.d(TAG, "create profile success: " + connectionResult);
    }

    @Override
    protected String doInBackground(String... strings) {
        String stuId = strings[0];
        String uName = strings[1];
        String pswd = strings[2];
        String fName = strings[3];
        String lName = strings[4];
        int pToAward = 1000;
        String dep = strings[6];
        String story = strings[7];
        String position = strings[8];
        String temp2 = strings[9];
        boolean admin = Boolean.parseBoolean(temp2);
        String location = strings[10];
        String iBytes = strings[11];
        JSONArray rRecords = new JSONArray();

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("studentId", stuId);
            jsonObject.put("username", uName);
            jsonObject.put("password", pswd);
            jsonObject.put("firstName", fName);
            jsonObject.put("lastName", lName);
            jsonObject.put("pointsToAward", pToAward);
            jsonObject.put("department", dep);
            jsonObject.put("story", story);
            jsonObject.put("position", position);
            jsonObject.put("admin", admin);
            jsonObject.put("location", location);
            jsonObject.put("imageBytes", iBytes);
            jsonObject.put("rewardRecords", rRecords);

            Log.d(TAG, "createProfile async task doInBackground");
            return doAPICall(jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String doAPICall(JSONObject jsonObject) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            Log.d(TAG, "createProfile doAPICall: ");
            String urlString = baseUrl + loginEndPoint;  // Build the full URL

            Uri uri = Uri.parse(urlString);    // Convert String url to URI
            URL url = new URL(uri.toString()); // Convert URI to URL

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");  // POST - others might use PUT, DELETE, GET

            // Set the Content-Type and Accept properties to use JSON data
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();

            // Write the JSON (as String) to the open connection
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(jsonObject.toString());
            out.close();

            int responseCode = connection.getResponseCode();

            StringBuilder result = new StringBuilder();

            // If successful (HTTP_OK)
            if (responseCode == HTTP_OK) {
                Log.d(TAG, "createProfile doAPICall success: ");
                // Read the results - use connection's getInputStream
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while (null != (line = reader.readLine())) {
                    result.append(line).append("\n");
                }

                // Return the results (to onPostExecute)
                return result.toString();

            } else {
                Log.d(TAG, "createProfile doAPICall fail: ");
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
    }
}
