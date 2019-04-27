package com.example.news;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsService extends Service {

    private static final String TAG = "CountService";
    private boolean running = true;
    private final ArrayList<Article> sourceList = new ArrayList<>();
    static final String ACTION_MSG_TO_SERVICE = "ACTION_MSG_TO_SERVICE";
    static final String ACTION_NEWS_STORY = "ACTION_NEWS_STORY";

    private ServiceReceiver serviceReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class ServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
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
    public void getArticles(Intent intent) {
        // Get the Source from the intent’s extras
        Source s = (Source) intent.getSerializableExtra("source");
        // Create News Article Downloader Async Task object using “this” and
        // the Source as c’tor parameter and execute
        new NewsArticleDownloaderAsyncTask(this).execute(s.getId());
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Create a ServiceReceiver object
        serviceReceiver = new ServiceReceiver();

        // Create IntentFilter for ACTION_MSG_TO_SERVICE
        // messages from the service
        IntentFilter intentFilter = new IntentFilter(ACTION_MSG_TO_SERVICE);

        // Register a ServiceReceiver broadcast receiver object using the intent filter
        registerReceiver(serviceReceiver, intentFilter);

        // Create and start service’s thread
        new Thread(new Runnable() {
            @Override
            // Service thread’s “run” method
            public void run() {
                // While “running”
                while (running) {
                    // While “storylist” is empty
                    // while(empty) {
                    //      Sleep 250 millis
                    //      try {
                    //          Thread.sleep(250);
                    //      } catch (InterruptedException e) {
                    //          e.printStackTrace();
                    //      }
                    // }
                    // Make a new intent object with ACTION_NEWS_STORY action
                    Intent i = new Intent(ACTION_NEWS_STORY);
                    // Add the “storylist” as an extra to the intent

                    // Broadcast the intent

                    // Clear the “storylist”s

            }
            Log.d(TAG, "run: Ending loop");
            }
        }).start();

        // Return START_STICKY
        return Service.START_STICKY;
    }

    public void setArticles(ArrayList<Article> articleObjects) {
        // Clear the article list
        sourceList.clear();

        // Fill the article list using the content of the list passed in
        sourceList.addAll(articleObjects);
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
