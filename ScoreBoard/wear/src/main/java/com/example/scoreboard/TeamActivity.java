package com.example.scoreboard;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TeamActivity extends WearableActivity {

    private static final String TAG = "Team Activity";
    private TextView home;
    private TextView guest;
    private Integer homeScore = 0;
    private Integer guestScore = 0;
    private Integer finalScore;
    private int increment_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        home = findViewById(R.id.youView);
        guest = findViewById(R.id.guest);

        Log.d(TAG, "onCreate: ");

        getInput();

        // Enables Always-on
        setAmbientEnabled();
    }

    public void getInput() {
        final EditText input;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Game to:");

        input = new EditText(this);
        input.setGravity(Gravity.CENTER_HORIZONTAL);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                setSettings(input);
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

    public void setSettings(EditText editText) {
        String d1 = editText.getText().toString();
        Integer input = Integer.parseInt(d1);

        finalScore = input;
        increment_value = 1;

        Toast.makeText(this,"Game to " + d1, Toast.LENGTH_LONG).show();
    }

    public void addHome (View w) {
        homeScore += increment_value;
        home.setText(Integer.toString(homeScore));
        whoWon(w,homeScore, "Home");
    }

    public void addGuest (View w) {
        guestScore += increment_value;
        guest.setText(Integer.toString(guestScore));
        whoWon(w, guestScore, "Guest");
    }

    public void whoWon(View w, int score, String people) {
        if(score == finalScore) {
            Toast.makeText(this, people + " Team Wins!", Toast.LENGTH_LONG).show();
            resetPoints(w);
            ask();
        }
    }

    public void ask() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to play again?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getInput();
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
        homeScore = 0;
        guestScore = 0;
        guest.setText("0");
        home.setText("0");
    }
}
