package com.example.stockwatch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.view.Gravity.CENTER_HORIZONTAL;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener{

    private static final String TAG = "MainActivity";
    private final List<Stock> stockList = new ArrayList<Stock>();

    private RecyclerView recyclerView;
    private StockAdapter stockAdapter;

    private SwipeRefreshLayout swiper; // The SwipeRefreshLayout

    private static final String stockURL = "http://www.marketwatch.com/investing/stock/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        stockAdapter = new StockAdapter(stockList, this);
        recyclerView.setAdapter(stockAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swiper = findViewById(R.id.swiper);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });

        for (int i = 0; i < 4; i++) {
            stockList.add(new Stock("TSLA", "Apple",
                    152.34, 1.25, 5.13));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        // go to the internet and display the stock information
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

    @Override
    public boolean onLongClick(View v) {
        Toast.makeText(v.getContext(), "Deleting a stock", Toast.LENGTH_SHORT).show();

        final int pos = recyclerView.getChildLayoutPosition(v);
        Stock s = stockList.get(pos);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_baseline_delete_outline_24px);
        builder.setTitle("Delete Stock");
        builder.setMessage("Delete Stock Symbol " + s.getSymbol() + "?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (!stockList.isEmpty()) {
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

    private void doRefresh() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            Toast.makeText(this, "Cannot access ConnectivityManager", Toast.LENGTH_SHORT).show();
        }

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            stockAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Updating stock info", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Network Connection");
            builder.setMessage("Stocks Cannot Be Updated Without a Network Connection");

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        swiper.setRefreshing(false);
    }

    public boolean addStock(MenuItem item) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            Toast.makeText(this, "Cannot access ConnectivityManager", Toast.LENGTH_SHORT).show();
            return false;
        }

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            final EditText input;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Stock Selection");
            builder.setMessage("Please enter a Stock Symbol:");

            input = new EditText(this);
            input.setGravity(CENTER_HORIZONTAL);

            builder.setView(input);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    displayToast();
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
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Network Connection");
            builder.setMessage("Stocks Cannot Be Added Without a Network Connection");

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        return false;
    }

    public void displayToast(){
        Toast.makeText(this, "Added stock", Toast.LENGTH_SHORT).show();
    }
}
