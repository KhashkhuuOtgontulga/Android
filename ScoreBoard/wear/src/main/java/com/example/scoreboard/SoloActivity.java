package com.example.scoreboard;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SoloActivity extends WearableActivity {

    private static final String TAG = "Solo Activity";
    private TextView you;
    private Integer yourScore = 0;
    private Integer finalScore;
    private int increment_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solo);

        you = findViewById(R.id.youView);

        Log.d(TAG, "onCreate: ");

        setSettings();

        // Enables Always-on
        setAmbientEnabled();
    }

    public void setSettings() {
        finalScore = 32;
        increment_value = 2;

        Toast.makeText(this,"Game to 32", Toast.LENGTH_SHORT).show();
    }

    public void addHome (View w) {
        yourScore += increment_value;
        you.setText(Integer.toString(yourScore));
        whoWon(w, yourScore);
    }

    public void whoWon(View w, int score) {
        if(score == finalScore) {
            Toast.makeText(this, "You Win!", Toast.LENGTH_LONG).show();
            resetPoints(w);
            ask();
        }
    }

    public void ask() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to play again?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void resetPoints (View w) {
        yourScore = 0;
        you.setText("0");
    }
}
