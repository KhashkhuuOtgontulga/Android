package com.example.scoreboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends WearableActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enables Always-on
        setAmbientEnabled();
    }


    public void goTeam(View v) {
        Log.d(TAG, "goTeam: ");
        Intent teamIntent = new Intent(this, TeamActivity.class);
        startActivity(teamIntent);
    }

    public void goSolo(View w) {
        Log.d(TAG, "goSolo: ");
        Intent soloIntent = new Intent(this, SoloActivity.class);
        startActivity(soloIntent);
    }
}
