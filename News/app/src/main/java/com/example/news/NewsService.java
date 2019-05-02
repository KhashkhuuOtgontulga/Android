package com.example.news;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

import static com.example.news.MainActivity.ACTION_MSG_TO_SERVICE;

public class NewsService extends Service {

    private static final String TAG = "CountService";
    private boolean running = true;
    private final ArrayList<Article> articleList = new ArrayList<>();

    private ServiceReceiver serviceReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class ServiceReceiver extends BroadcastReceiver {

        // onReceive -> getArticles
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Log.d(TAG, "onReceive: " + action);
            if (action == null)
                return;
            // If the Intent’s action type is ACTION_MSG_TO_SERVICE,
            switch (action) {
                case ACTION_MSG_TO_SERVICE:
                    getArticles(intent);
                    break;
                default:
                    Log.d(TAG, "onReceive: Unknown broadcast received");
            }
        }
    }

    // getArticles -> NewsArticle Async Task's doInBackground()
    public void getArticles(Intent intent) {
        // Get the Source from the intent’s extras
        String s = intent.getStringExtra("source");
        // Create News Article Downloader Async Task object using “this” and
        // the Source as c’tor parameter and execute
        new NewsArticleDownloaderAsyncTask(this).execute(s);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "inside onStartCommand: ");

        // Create a ServiceReceiver object
        serviceReceiver = new ServiceReceiver();

        // Create IntentFilter for ACTION_MSG_TO_SERVICE messages from the service
        IntentFilter intentFilter = new IntentFilter(ACTION_MSG_TO_SERVICE);

        // Register a ServiceReceiver broadcast receiver object using the intent filter
        registerReceiver(serviceReceiver, intentFilter);

        // Create and start service’s thread
        // thread -> mainactivity's onReceive
        new Thread(new Runnable() {
            @Override
            // Service thread’s “run” method
            public void run() {
                // While “running”
                while (running) {
                    // While “storylist” is empty
                    Log.d(TAG, "Hello - I'm running properly in my own thread!");

                    while(articleList.isEmpty()) {
                        // Sleep 250 millis
                        try {
                            Thread.sleep(250);
                            //Log.d(TAG, "Article is empty");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d(TAG, "Article is being filled");
                    // storylist is not empty
                    // Make a new intent object with ACTION_NEWS_STORY action
                    Intent i = new Intent();
                    // Add the “storylist” as an extra to the intent
                    i.setAction(MainActivity.ACTION_NEWS_STORY);
                    i.putExtra("articles", articleList);
                    // Broadcast the intent
                    sendBroadcast(i);
                    // Clear the “storylist”s
                    articleList.clear();
                }
                Log.d(TAG, "run: Ending loop");
            } // run function
        }).start();

        // Return START_STICKY
        return Service.START_STICKY;
    }

    // setArticles not full once we add the articles -> sourceList is no longer empty
    public void setArticles(ArrayList<Article> articleObjects) {
        // Clear the article list
        articleList.clear();

        // Fill the article list using the content of the list passed in
        articleList.addAll(articleObjects);
    }

    @Override
    public void onDestroy() {
        // Unregister the service receiver
        unregisterReceiver(serviceReceiver);

        // Set the service’s thread’s running flag to false
        running = false;

        // Call super.destroy
        super.onDestroy();
    }
}
