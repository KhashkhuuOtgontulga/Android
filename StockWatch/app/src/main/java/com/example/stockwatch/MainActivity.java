package com.example.stockwatch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.Gravity.CENTER_HORIZONTAL;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "MainActivity";
    private static final String stockURL = "http://www.marketwatch.com/investing/stock/";

    private final List<Stock> stockList = new ArrayList<>();
    private HashMap<String, String> sData;

    private RecyclerView recyclerView;
    private StockAdapter stockAdapter;
    private SwipeRefreshLayout swiper; // The SwipeRefreshLayout
    private DataBaseHandler databaseHandler;

    private EditText et;
    private String input;

    private static final int ADD_CODE = 1;
    private static final int UPDATE_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // basic setup
        recyclerView = findViewById(R.id.recycler);
        stockAdapter = new StockAdapter(stockList, this);
        recyclerView.setAdapter(stockAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseHandler = new DataBaseHandler(this);

        // refresher
        swiper = findViewById(R.id.swiper);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh method to doRefresh()");
                doRefresh();
            }
        });

        // load the stock symbol and name into the Hashmap
        new NameDownloaderAsyncTask(this).execute();

        // load the stocks of our database
        // so we do not lose our stocks
        // everytime we exit the app and enter it again
        // the user expects to have the same stocks
        // they added to the app to still be there
        ArrayList<Stock> list = databaseHandler.loadStocks();
        for (Stock s: list){
            new StockDownloaderAsyncTask(this).execute(s.getSymbol(), Integer.toString(ADD_CODE));
        }
    }

    // create the menu to add a stock
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    // go to the internet and display the stock information on MarketWatch
    // it shouldn't display information when there is
    // no internet connection
    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Stock s = stockList.get(pos);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            Toast.makeText(this, "Cannot access ConnectivityManager", Toast.LENGTH_SHORT).show();
        }

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            Toast.makeText(v.getContext(), "Going to the website", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(stockURL + s.getSymbol()));
            startActivity(i);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Network Connection");
            builder.setMessage("Cannot Go to MarketWatch for " + s.getSymbol() + " Without a Network Connection");

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    // to delete a stock
    @Override
    public boolean onLongClick(View v) {
        Toast.makeText(v.getContext(), "Deleting a stock", Toast.LENGTH_SHORT).show();

        final int pos = recyclerView.getChildLayoutPosition(v);
        final Stock s = stockList.get(pos);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_baseline_delete_outline_24px);
        builder.setTitle("Delete Stock");
        builder.setMessage("Delete Stock Symbol " + s.getSymbol() + "?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (!stockList.isEmpty()) {
                    //delete from the database too so everytime
                    //you run the app again, you load the same
                    //stocks as before
                    databaseHandler.deleteStock(s.getSymbol());
                    stockList.remove(pos);
                    stockAdapter.notifyDataSetChanged();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }

    // refresh if there is a internet connection
    private void doRefresh() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            Toast.makeText(this, "Cannot access ConnectivityManager", Toast.LENGTH_SHORT).show();
        }

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            ArrayList<Stock> list = databaseHandler.loadStocks();
            for (Stock s: list){
                new StockDownloaderAsyncTask(this).execute(s.getSymbol(), Integer.toString(UPDATE_CODE));
            }
            Toast.makeText(this, "New stock info", Toast.LENGTH_LONG).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Network Connection");
            builder.setMessage("Stocks Cannot Be Updated Without a Network Connection");

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        swiper.setRefreshing(false);
    }

    // when the user presses the menu icon to add a stock
    // they enter a symbol or name
    // then we download the stock information
    // when they press okay to add the stock
    public boolean addStock(MenuItem item) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            Toast.makeText(this, "Cannot access ConnectivityManager", Toast.LENGTH_SHORT).show();
            return false;
        }

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        // Connected to Network?
        if (netInfo != null && netInfo.isConnected()) {
            // Display Add Stock Symbol Entry Dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            et = new EditText(this);
            et.setGravity(CENTER_HORIZONTAL);
            // same thing as putting android:textAllCaps="true"
            // in the editText of an xml file
            et.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            builder.setView(et);

            builder.setTitle("Stock Selection");
            builder.setMessage("Please enter a Stock Symbol:");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    input = et.getText().toString().toUpperCase();
                    Log.d(TAG, "onClick addStock input: " + input);

                    // used an arraylist instead of an array so I don't hardcode a fixed size
                    // sometimes having multiple stocksfound goes index out of bounds with an
                    // array but not an arraylist because it dynamically grows
                    final ArrayList<String> sArray = new ArrayList<String>();
                    int i = 0;

                    for (Map.Entry<String, String> e : sData.entrySet()) {
                        if (e.getKey().startsWith(input) || e.getValue().split(" ")[0].startsWith(input)) {
                            Log.d(TAG, "Results "+ i + ": " + e.getKey() + " " + e.getValue());
                            sArray.add(e.getKey() + " - " + e.getValue());
                            i++;
                        }
                    }
                    // One stock found
                    if(i == 1) {
                        Log.d(TAG, "One found");
                        // its a key
                        if(sData.containsKey(input)) {
                            // Use selected symbol to execute StockDownloader AsyncTask
                            downloadStock(input, Integer.toString(ADD_CODE));
                        }
                        else {
                            // its a value
                            downloadStock(sArray.get(0).split(" ")[0], Integer.toString(ADD_CODE));
                        }
                    }
                    // Multiple stocks found
                    else if (i > 1) {
                        Log.d(TAG, "Multiple found");
                        multipleResults(sArray);
                    }
                    // No stock found
                    else {
                        Log.d(TAG, "Nothing found");
                        noStockFound();
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Network Connection");
            builder.setMessage("Stocks Cannot Be Added Without a Network Connection");

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        return false;
    }

    public void multipleResults(final ArrayList<String> sArray) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Make a selection");
        final String[] array = sArray.toArray(new String[sArray.size()]);

        builder.setItems(array, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // make sArray[which] to a String by sArray[which].toString
                // then split it to get the first index which is the symbol of the company
                Toast.makeText(MainActivity.this, "You selected: " + array[which].toString().split(" ")[0], Toast.LENGTH_SHORT).show();
                downloadStock(array[which].toString().split(" ")[0], Integer.toString(ADD_CODE));
            }
        });

        builder.setNegativeButton("Nevermind", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(MainActivity.this, "You changed your mind!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    // get the key given a value
    // in our case, get the symbol when we know the company name
    public static<K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }


    public void noStockFound() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Symbol Not Found: " + et.getText().toString().toUpperCase());
        adb.setMessage("Data for stock symbol");

        AlertDialog d = adb.create();
        d.show();
    }

    // called by addStock
    public void downloadStock(String key, String code) {
        new StockDownloaderAsyncTask(this).execute(key, code);
        Log.d(TAG, "downloadStock: " + key);
    }

    // after returning from StockDownloaderAsyncTask above
    // add a stock to the database
    // and to the stocklist to display it
    public void insertStock(Stock s, int code) {
        //Log.d(TAG, "insertStock: before checking contains");
        switch (code) {
            case ADD_CODE:
                // simply check if the array list contains
                // that stock. but you need to implement
                // equals method in the stock class
                if(stockList.contains(s)) {
                    Log.d(TAG, "insertStock: stocklist contains " + s.getCompanyName());
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setIcon(R.drawable.ic_baseline_warning_24px);
                    builder.setTitle("Duplicate Stock");
                    builder.setMessage("Stock Symbol " + s.getSymbol() + " is already displayed");

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    Log.d(TAG, "insertStock: stocklist does not contain " + s.getCompanyName());
                    stockList.add(0, s);
                    Collections.sort(stockList, new Sortbyname());
                    databaseHandler.addStock(s);
                    stockAdapter.notifyDataSetChanged();
                }
                break;
            case UPDATE_CODE:
                stockList.remove(s);
                stockList.add(0, s);
                Collections.sort(stockList, new Sortbyname());
                stockAdapter.notifyDataSetChanged();
                break;
        }
    }

    // after returning from NameDownloaderAsyncTask
    // initialize the hashmap sData so we can check if we
    // have found a stock or not when we add a stock
    public void initiateData(HashMap<String, String> data) {
        sData = data;
    }
}
