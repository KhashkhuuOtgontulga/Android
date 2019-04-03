package com.example.rewards;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class Profile extends AppCompatActivity {

    private TextView nameProfile;
    private TextView usernameProfile;
    private TextView locationProfile;
    private TextView numberPointsAwarded;
    private TextView departmentTextProfile;
    private TextView positionTextProfile;
    private TextView numberPointsProfile;
    private TextView storyTextProfile;

    private UserProfile dh;
    private int UPDATE_PROFILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameProfile = findViewById(R.id.nameProfile);
        usernameProfile = findViewById(R.id.usernameProfile);
        locationProfile = findViewById(R.id.locationProfile);
        numberPointsAwarded = findViewById(R.id.numberPointsAwarded);
        departmentTextProfile = findViewById(R.id.departmentTextProfile);
        positionTextProfile = findViewById(R.id.positionTextProfile);
        numberPointsProfile = findViewById(R.id.numberPointsProfile);
        storyTextProfile = findViewById(R.id.storyTextProfile);

        Intent intent = getIntent();

        dh = (UserProfile) intent.getSerializableExtra(CreateProfile.extraName);

        nameProfile.setText(dh.getFirst_name() + ", " + dh.getLast_name());
        usernameProfile.setText("(" + dh.getUsername() + ")");
        locationProfile.setText(dh.getLocation());
        numberPointsAwarded.setText(String.valueOf(dh.getPoints_awarded()));
        departmentTextProfile.setText(dh.getDepartment());
        positionTextProfile.setText(dh.getPosition());
        numberPointsProfile.setText(String.valueOf(dh.getPoints_to_award()));
        storyTextProfile.setText(dh.getStory());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editField:
                Intent intent = new Intent(this, EditActivity.class);
                intent.putExtra("EDIT", dh);
                startActivityForResult(intent, UPDATE_PROFILE);
                return true;
            case R.id.leaderField:
                Toast.makeText(this, "Going to the leaderboard", Toast.LENGTH_LONG).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UPDATE_PROFILE) {
            if (resultCode == RESULT_OK) {
                dh = (UserProfile) data.getSerializableExtra(EditActivity.extraName);

                nameProfile.setText(dh.getFirst_name() + ", " + dh.getLast_name());
                usernameProfile.setText("(" + dh.getUsername() + ")");
                locationProfile.setText(dh.getLocation());
                numberPointsAwarded.setText(String.valueOf(dh.getPoints_awarded()));
                departmentTextProfile.setText(dh.getDepartment());
                positionTextProfile.setText(dh.getPosition());
                numberPointsProfile.setText(String.valueOf(dh.getPoints_to_award()));
                storyTextProfile.setText(dh.getStory());
            }
        }
    }
}