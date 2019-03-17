package com.example.stockwatch;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;

public class StockDownloaderAsyncTask extends AsyncTask<String, Integer, String> {

    private MainActivity mainActivity;
    private static String STATS_URL;

    public StockDownloaderAsyncTask(MainActivity ma) {
        mainActivity = ma;
    }

    // order of operations
    // 1. onPreExecute()
    // 2. doInBackground()
    // 3. onPostExecute()
    // 4. parseJson()
    // 5. insertStock()

    @Override
    protected void onPreExecute() {
        Toast.makeText(mainActivity, "Loading Stock Data...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String s) {
        //ArrayList<Stock> countryList =
        Log.d(TAG, "I am in onPostExecute");
        mainActivity.insertStock(parseJSON(s));
    }

    @Override
    protected String doInBackground(String... params) {
        // make stock_symbol a local variable
        // and not a private attribute for some reason
        // it works to get the first element of params
        // making it private made my program not read it
        String stock_symbol = params[0];
        Log.d(TAG, "stock symbol is " + stock_symbol);
        STATS_URL = "https://api.iextrading.com/1.0/stock/" + stock_symbol + "/quote?displayPercent=true";
        Uri dataUri = Uri.parse(STATS_URL);
        String urlToUse = dataUri.toString();
        Log.d(TAG, "URL is " + STATS_URL);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            Log.d(TAG, "doInBackground: ResponseCode: " + conn.getResponseCode());

            conn.setRequestMethod("GET");

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "doInBackground: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        return sb.toString();
    }


    // return a Stock to add it to the database and stocklist
    private Stock parseJSON(String s) {

        try {
            JSONObject jStock = new JSONObject(s);
            String symbol = jStock.getString("symbol");
            String name = jStock.getString("companyName");

            String lP = jStock.getString("latestPrice");
            double latestPrice = 0;
            if (lP != null && !lP.trim().isEmpty())
                latestPrice = Double.parseDouble(lP);

            String c = jStock.getString("change");
            double change = 0;
            if (c != null && !c.trim().isEmpty())
                change = Double.parseDouble(c);

            DecimalFormat df = new DecimalFormat("#.##");

            String cP = jStock.getString("changePercent");
            double changePercent = 0;
            if (cP != null && !cP.trim().isEmpty())
                // For example, String '3.22918' -> Double 3.22 -> String '3.22' -> Double 3.22
                changePercent = Double.parseDouble(df.format(Double.parseDouble(cP)));

            Log.d(TAG, "Stocks to add to database and list: " + symbol + name + lP + c + cP);

            return new Stock(symbol, name, latestPrice, change, changePercent);
        } catch (Exception e) {
            Log.d(TAG, "parseJSON exception: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
