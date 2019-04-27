package com.example.news;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.example.news.NewsService.ACTION_MSG_TO_SERVICE;
import static com.example.news.NewsService.ACTION_NEWS_STORY;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start Service (NewsService)
        Intent intent = new Intent(MainActivity.this, NewsService.class);
        startService(intent);

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

    public void selectItem(int position) {
        // Set the ViewPager’s background to null
        pager.setBackground(null);

        // Set the current news source object to the
        // selected source (using the index passed in and
        // your list of sources)
        currentSource = newsData.get(sourceList.get(position)).getId();

        // Create an Intent ACTION_MSG_TO_SVC
        Intent intent = new Intent(ACTION_MSG_TO_SERVICE);

        // Add the selected source object as an extra to the intent
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

    public void setSources(ArrayList<Source> sourceObjects) {
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
            // Fill the list of sources (used to populate the drawer list)
            // using the names of the sources passed in.
            Log.d(TAG, "setSources: " + "category: " + s.getId() + " name: " +  s.getName() +" category: "+ s.getCategory());
            sourceList.add(s.getName());
            // Fill the source map (using the List of Sources passed in) with
            // each news source name(key) and the news source object (value)
            newsData.put(s.getName(), s);
        }
        // Create a list of unique news category names
        // taken from the source objects
        ArrayList<String> tempList = new ArrayList<>(newsData.keySet());
        Collections.sort(tempList);
        for (String s : tempList)
            opt_menu.add(s);
        // If the activity’s category list is null, set it to a new array using the list
        // of categories passed in. (Add “all” as the first String in that list)
        // newsData.put("All", sourceObjects);
        // Notify the drawer’s array adapter that the dataset has changed.
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, sourceList));
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * A placeholder fragment containing a simple view.
     * It doesn't do much, but this is just an example
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";


        /**
         * Returns a new instance of this fragment for the given section
         * number. The 'sectionNumber' parameter indicates what page to
         * display: 1, 2 , 3, etc.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {

            PlaceholderFragment fragment = new PlaceholderFragment();

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            return fragment;
        }


        // The onCreateView is like Activity's onCreate for a Fragment
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            // Use the inflater passed in to build (inflate) the fragment
            View rootView = inflater.inflate(R.layout.fragment_article, container, false);

            // Get  reference to the textview in the page layout
            TextView headline = rootView.findViewById(R.id.headline);
            TextView date = rootView.findViewById(R.id.date);
            TextView author = rootView.findViewById(R.id.author);
            ImageView image = rootView.findViewById(R.id.imageView);
            TextView text = rootView.findViewById(R.id.text);

            // The fragment's arguments (getArguments()) contains a field with the key
            // ARG_SECTION_NUMBER that holds the value to put in the textview
            if (getArguments() != null){
                headline.setText(getString(R.string.section_format,
                        getArguments().getInt(ARG_SECTION_NUMBER)));
            }
            return rootView;
        }
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
                    reDoFragements(articles);
                    break;
                default:
                    Log.d(TAG, "onReceive: Unknown broadcast received");
            }
        }

        public void reDoFragements(ArrayList<Article> articles) {
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
}
