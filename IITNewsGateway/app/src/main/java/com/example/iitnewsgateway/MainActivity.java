package com.example.iitnewsgateway;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String sourcesUrl =
            "https://newsapi.org/v2/sources?language=en&country=us&category=&apiKey=";
    private static final String articleUrl =
            "https://newsapi.org/v2/everything?sources=cnn&language=en&pageSize=100&apiKey= ABC123xyz";
    private static final String API_Key ="423b2938e0ba47ebad71deb2445fd9e5";
    
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start Service
        // (NewsService)
        // [ insert here ]

        // Create a NewsReceiver
        // object
        // [ insert here ]

        // Setup drawer, adapter
        //    and toggle
        // Make items for the drawer list
        items = new String[15];
        for (int i = 0; i < items.length; i++)
            items[i] = "Menu Item " + (i+1);
        //

        mDrawerLayout = findViewById(R.id.drawer_layout); // <== Important!
        mDrawerList = findViewById(R.id.left_drawer); // <== Important!

        mDrawerToggle = new ActionBarDrawerToggle(   // <== Important!
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );

        mDrawerList.setAdapter(new ArrayAdapter<>(this,   // <== Important!
                R.layout.drawer_list_item, items));

        mDrawerList.setOnItemClickListener(   // <== Important!
                new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectItem(position);
                    }
                }
        );
        // Setup supportActionBar
        if (getSupportActionBar() != null) {  // <== Important!
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        // Setup Fragment List,
        // PageViewer and
        // Adapter
        // insert here

        // Create News Source
        // Downloader Async Task
        // object and execute
        new NewsSourceDownloaderAsyncTask(this).execute("category from the options menu");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState(); // <== IMPORTANT
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig); // <== IMPORTANT
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {  // <== Important!
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
    private void selectItem(int position) {
        Toast.makeText(this, "You picked " + items[position], Toast.LENGTH_SHORT);
        Log.d(TAG, "selectItem: starting News Article Downloader AsyncTask");
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    public void setSources(boolean error, String s) {
        if (error) {
            try {
                JSONObject errorDetails = new JSONObject(s);
                JSONObject jsonObject = new JSONObject(errorDetails.getString("errordetails"));
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(jsonObject.getString("status"));
                builder.setMessage(jsonObject.getString("message"));
                AlertDialog dialog = builder.create();
                dialog.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT);
            Log.d(TAG, "setupDrawer: adding the sources to the drawer");
        }
    }

    public void setArticles(boolean error, String s) {
        if (error) {
            try {
                JSONObject errorDetails = new JSONObject(s);
                JSONObject jsonObject = new JSONObject(errorDetails.getString("errordetails"));
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(jsonObject.getString("status"));
                builder.setMessage(jsonObject.getString("message"));
                AlertDialog dialog = builder.create();
                dialog.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT);
            Log.d(TAG, "setupArticle: adding the articles to the pagev iew");
        }
    }
}
