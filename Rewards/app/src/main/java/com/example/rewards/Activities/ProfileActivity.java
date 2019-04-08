package com.example.rewards.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rewards.R;
import com.example.rewards.UserProfile;

public class ProfileActivity extends AppCompatActivity {

    private TextView nameProfile;
    private TextView usernameProfile;
    private TextView locationProfile;
    private TextView numberPointsAwarded;
    private TextView departmentTextProfile;
    private TextView positionTextProfile;
    private TextView numberPointsProfile;
    private TextView storyTextProfile;
    private ImageView imageView;

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
        imageView = findViewById(R.id.imageProfile);

        Intent intent = getIntent();

        dh = (UserProfile) intent.getSerializableExtra(CreateProfileActivity.extraName);

        nameProfile.setText(dh.getFirst_name() + ", " + dh.getLast_name());
        usernameProfile.setText("(" + dh.getUsername() + ")");
        locationProfile.setText(dh.getLocation());
        numberPointsAwarded.setText(String.valueOf(dh.getPoints_awarded()));
        departmentTextProfile.setText(dh.getDepartment());
        positionTextProfile.setText(dh.getPosition());
        numberPointsProfile.setText(String.valueOf(dh.getPoints_to_award()));
        storyTextProfile.setText(dh.getStory());
        imageView.setImageResource(R.drawable.login_people);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.icon);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editField:
                Intent intent = new Intent(this, EditProfileActivity.class);
                intent.putExtra("EDIT", dh);
                startActivityForResult(intent, UPDATE_PROFILE);
                return true;
            case R.id.leaderField:
                //Toast.makeText(this, "Going to the leaderboard", Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, LeaderboardActivity.class);
                i.putExtra("LEADER", dh);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UPDATE_PROFILE) {
            if (resultCode == RESULT_OK) {
                dh = (UserProfile) data.getSerializableExtra(EditProfileActivity.extraName);

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