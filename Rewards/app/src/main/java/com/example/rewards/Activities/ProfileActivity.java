package com.example.rewards.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rewards.AsyncTasks.DeleteAllProfileAPIAsyncTask;
import com.example.rewards.AsyncTasks.DeleteProfileAPIAsyncTask;
import com.example.rewards.R;
import com.example.rewards.UserProfile;

import static android.view.Gravity.CENTER_HORIZONTAL;

public class ProfileActivity extends AppCompatActivity {

    private TextView nameProfile;
    private TextView usernameProfile;
    private TextView passwordProfile;
    private TextView locationProfile;
    private TextView numberPointsAwarded;
    private TextView departmentTextProfile;
    private TextView positionTextProfile;
    private TextView numberPointsProfile;
    private TextView storyTextProfile;
    private ImageView imageView;

    private UserProfile dh;
    private int UPDATE_PROFILE = 1;

    private EditText et;
    private String input;

    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameProfile = findViewById(R.id.nameAward);
        usernameProfile = findViewById(R.id.usernameProfile);
        locationProfile = findViewById(R.id.locationProfile);
        numberPointsAwarded = findViewById(R.id.numberPointsAwardedAward);
        departmentTextProfile = findViewById(R.id.departmentTextAward);
        positionTextProfile = findViewById(R.id.positionTextAward);
        numberPointsProfile = findViewById(R.id.numberPointsProfile);
        storyTextProfile = findViewById(R.id.storyTextAward);
        imageView = findViewById(R.id.imageAward);

        Intent intent = getIntent();

        dh = (UserProfile) intent.getSerializableExtra(CreateProfileActivity.extraName);

        nameProfile.setText(dh.getLast_name() + ", " + dh.getFirst_name());
        usernameProfile.setText("(" + dh.getUsername() + ")");
        locationProfile.setText(dh.getLocation());
        numberPointsAwarded.setText(String.valueOf(dh.getPoints_awarded()));
        departmentTextProfile.setText(dh.getDepartment());
        positionTextProfile.setText(dh.getPosition());
        numberPointsProfile.setText(String.valueOf(dh.getPoints_to_award()));
        storyTextProfile.setText(dh.getStory());
        imageView.setImageResource(R.drawable.default_photo);

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
            case R.id.deleteField:
                //Toast.makeText(this, "Going to the leaderboard", Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                et = new EditText(this);
                et.setGravity(CENTER_HORIZONTAL);
                // same thing as putting android:textAllCaps="true"
                // in the editText of an xml file
                builder.setView(et);

                builder.setIcon(R.drawable.logo);
                builder.setTitle("Delete");
                builder.setMessage("Enter a username to delete: ");
                builder.setPositiveButton("All Users",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                    input = et.getText().toString();
                                    Log.d(TAG, "onOptionsItemSelected: " + input);
                                    deleteAllUsers(input);
                            }
                        }
                );
                builder.setNegativeButton("One User",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                input = et.getText().toString();
                                Log.d(TAG, "onOptionsItemSelected: " + input);
                                deleteUser(input);
                            }
                        }
                );
                builder.setNeutralButton("Cancel",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.dismiss();
                            }
                        }
                );
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteAllUsers(String input) {
        new DeleteAllProfileAPIAsyncTask(this).execute("A20379665", dh.getUsername(), dh.getPassword(), input);
    }

    public void deleteUser(String input) {
        new DeleteProfileAPIAsyncTask(this).execute("A20379665", dh.getUsername(), dh.getPassword(), input);
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