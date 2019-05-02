package com.example.news;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ArrayList<String> sourceList = new ArrayList<>();
    private HashMap<String, Source> newsData = new HashMap<>();
    private Menu opt_menu;
    
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private MyPageAdapter pageAdapter;
    private List<Fragment> fragments;
    private ViewPager pager;

    String currentSource;

    NewsReceiver newsReceiver;

    static final String ACTION_MSG_TO_SERVICE = "ACTION_MSG_TO_SERVICE";
    static final String ACTION_NEWS_STORY = "ACTION_NEWS_STORY";

    private boolean serviceRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (serviceRunning) {
            Toast.makeText(this, "Service Already Running", Toast.LENGTH_SHORT).show();
            return;
        }

        // Start Service (NewsService)
        Intent intent = new Intent(this, NewsService.class);
        Log.d(TAG, "Starting the service");
        startService(intent);
        serviceRunning = true;

        // Create a NewsReceiver object
        newsReceiver = new NewsReceiver();

        // Setup drawer, adapter and toggle
        mDrawerLayout = findViewById(R.id.drawer_layout); // <== Important!
        mDrawerList = findViewById(R.id.drawer_list); // <== Important!

        mDrawerList.setOnItemClickListener(   // <== Important!
                new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectItem(position);
                    }
                }
        );

        mDrawerToggle = new ActionBarDrawerToggle(   // <== Important!
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );

        // Setup supportActionBar
        if (getSupportActionBar() != null) {  // <== Important!
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        // Setup Fragment List, Adapter, and PageViewer
        fragments = new ArrayList<>();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager());
        pager = findViewById(R.id.container); // Get ViewPager
        pager.setAdapter(pageAdapter);  // Set the adapter
        // Create News Source Downloader Async Task object and execute
        new NewsSourceDownloaderAsyncTask(this).execute("");
    }

    // go to NewsService -> onReceive
    public void selectItem(int position) {
        // Set the ViewPager’s background to null
        pager.setBackground(null);

        // Set the current news source object to the
        // selected source (using the index passed in and
        // your list of sources)
        currentSource = newsData.get(sourceList.get(position)).getId();

        // Create an Intent ACTION_MSG_TO_SVC
        Intent intent = new Intent();

        // Add the selected source object as an extra to the intent
        intent.setAction(MainActivity.ACTION_MSG_TO_SERVICE);
        intent.putExtra("source", currentSource);


        // Broadcast the intent
        sendBroadcast(intent);

        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerList);
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

    // You need the below to open the drawer when the toggle is clicked
    // Same method is called when an options menu item is selected.

    public boolean onOptionsItemSelected(MenuItem item) {
        // If a call to the
        // drawerToggle’s
        // onOptionsItemSelected
        // method (passing the
        // provided MenuItem as
        // the parameter) returns
        // true, exit the method by
        // returning true
        if (mDrawerToggle.onOptionsItemSelected(item)) {  // <== Important!
            Log.d(TAG, "onOptionsItemSelected: mDrawerToggle " + item);
            return true;
        }

        // set the action bar title to the name of the news category
        //setTitle(item.getTitle());

        // Create News Source Downloader Async Task object using “this”, and the selected
        // MenuItem’s title string (a news category) as c’tor parameterAnd execute
        Log.d(TAG, "onOptionsItemSelected: " + item.getTitle());
        new NewsSourceDownloaderAsyncTask(this).execute(String.valueOf(item.getTitle()));

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        opt_menu = menu;
        return true;
    }

    public void setSources(ArrayList<Source> sourceObjects, ArrayList<String> categories) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT);
        Log.d(TAG, "setupDrawer: adding the sources to the drawer");
        // Clear the source map (HashMap of source names to Source objects)
        newsData.clear();
        // Clear the list of source names (used to populate the drawer list)
        sourceList.clear();
        for (Source s : sourceObjects) {
            // no duplicate categories
            if (!newsData.containsKey(s.getCategory())) {
                newsData.put(s.getCategory(), s);
            }
            // Fill the list of sources (used to populate the drawer list) using the names of the sources passed in.
            Log.d(TAG, "setSources: " + "category: " + s.getId() + " name: " +  s.getName() +" category: "+ s.getCategory());
            sourceList.add(s.getName());
            // Fill the source map (using the List of Sources passed in) with
            // each news source name(key) and the news source object (value)
            newsData.put(s.getName(), s);
        }
        Collections.sort(categories);
        for (String s : categories)
            opt_menu.add(s);
        // If the activity’s category list is null, set it to a new array using the list
        // of categories passed in. (Add “all” as the first String in that list)
        //newsData.put("all", sourceObjects);
        // Notify the drawer’s array adapter that the dataset has changed.
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, sourceList));
    }

    //////////////////////////////////////////////////////////////////////////////////////////////

    // Standard adapter code here
    private class MyPageAdapter extends FragmentPagerAdapter {
        private long baseId = 0;

        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public long getItemId(int position) {
            // give an ID different from position when position has been changed
            return baseId + position;
        }

        public void notifyChangeInPosition(int n) {
            // shift the ID returned by getItemId outside the range of all previous fragments
            baseId += getCount() + n;
        }


    }

    /////////////////////////////////////////////////////////////
    class NewsReceiver extends BroadcastReceiver {

        // NewsReceiver onReceive -> reDoFragments
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action == null)
                return;
            // If the Intent’s action type is ACTION_NEWS_STORY,
            // Get the Article list from the intent’s extras
            switch (action) {
                case ACTION_NEWS_STORY:
                    ArrayList<Article> articles = (ArrayList) intent.getSerializableExtra("articles");
                    // Call “reDoFragments” passing the list of artiles
                    reDoFragments(articles);
                    break;
                default:
                    Log.d(TAG, "onReceive: Unknown broadcast received");
            }
        }

        // finish
        public void reDoFragments(ArrayList<Article> articles) {
            // Set Activity Title to the name of the current news source
            setTitle(currentSource);

            // For each Fragment in the PageAdapter (use a
            // for loop, up to the adapter’s “getCount()”)
            for (int i = 0; i < pageAdapter.getCount(); i++)
                // Notify the adapter  about the change in position “i”
                pageAdapter.notifyChangeInPosition(i);

            // Clear the fragments list
            fragments.clear();

            // For each Article in the list passed in i = 0 thru “n”
            for (int i = 0; i < articles.size(); i++) {
                // Make a new Fragment using news article “I”
                // Then add the new Fragment
                //(using story “i” as the parameter to “newInstance”) to the Fragments list
                fragments.add(ArticleFragment.newInstance(articles.get(i), i+1, sourceList.size()));
            }

            // Notify the PageAdapter that the dataset changed
            pageAdapter.notifyDataSetChanged();

            // Set the ViewPager’s current item to item “0”
            pager.setCurrentItem(0);
        }
    }

    @Override
    protected void onResume() {
        // Create IntentFilter for ACTION_NEWS_STORY
        // messages from the service
        IntentFilter intentFilter = new IntentFilter(ACTION_NEWS_STORY);

        // Register a NewsReceiver broadcast receiver
        // object using the intent filter
        registerReceiver(newsReceiver, intentFilter);

        // Call super.onResume()
        super.onResume();
    }

    @Override
    protected void onStop() {
        // UnRegister the NewsReceiver object
        unregisterReceiver(newsReceiver);

        // Call super.onStop()
        super.onStop();
    }

    /*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("INPUT", input.getText().toString());
        outState.putString("OUTPUT", output.getText().toString());
        outState.putString("HISTORY", history.getText().toString());

        // call super last
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // call super first
        super.onRestoreInstanceState(savedInstanceState);

        input.setText(savedInstanceState.getString("INPUT"));
        output.setText(savedInstanceState.getString("OUTPUT"));
        history.setText(savedInstanceState.getString("HISTORY"));
    }*/
}
