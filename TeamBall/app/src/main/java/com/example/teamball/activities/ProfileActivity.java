package com.example.teamball.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teamball.R;
import com.example.teamball.Reward;
import com.example.teamball.RewardAdapter;
import com.example.teamball.UserProfile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private TextView rewardHistory;
    private final List<Reward> rewardList = new ArrayList<>();
    private RewardAdapter rewardAdapter;
    private RecyclerView rewardRecycler;

    private ProgressBar progressBar;

    private UserProfile dh;
    private int UPDATE_PROFILE = 1;
    private int LEADER_PROFILE = 2;

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
        rewardHistory = findViewById(R.id.rewardHistory);
        rewardRecycler = findViewById(R.id.rewardRecycler);

        Intent intent = getIntent();

        dh = (UserProfile) intent.getSerializableExtra(CreateProfileActivity.extraName);
        nameProfile.setText(dh.getLast_name() + ", " + dh.getFirst_name());
        usernameProfile.setText("(" + dh.getUsername() + ")");
        locationProfile.setText(dh.getLocation());
        numberPointsAwarded.setText(String.valueOf(dh.getPoints_awarded()));
        departmentTextProfile.setText(dh.getDepartment());
        positionTextProfile.setText(dh.getPosition());
        numberPointsProfile.setText(String.valueOf(dh.getPoints_to_award()));
        Log.d(TAG, "PROFILE TO ADD POINTS: " + String.valueOf(dh.getPoints_to_award()));
        storyTextProfile.setText(dh.getStory());

        rewardRecycler = findViewById(R.id.rewardRecycler);
        rewardAdapter = new RewardAdapter(rewardList);
        // connect the recyclerView to the adapter
        rewardRecycler.setLayoutManager(new LinearLayoutManager(this));
        rewardRecycler.setAdapter(rewardAdapter);
        /* show the recyclerview */
        rewardList.addAll(dh.getRewards());
        Collections.sort(rewardList, Collections.<Reward>reverseOrder(new HighestPoints()));
        rewardAdapter.notifyDataSetChanged();
        rewardHistory.setText("Reward History " + "("+ rewardAdapter.getItemCount() + "):");

        progressBar = findViewById(R.id.progressBar2);
        progressBar.bringToFront();
        progressBar.setVisibility(View.GONE);

        byte[] imageBytes = Base64.decode(dh.getImage(),  Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        imageView.setImageBitmap(bitmap);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.leaderField:
                //Toast.makeText(this, "Going to the leaderboard", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.VISIBLE);
                Intent i = new Intent(this, LeaderboardActivity.class);
                i.putExtra("LEADER", dh);
                startActivityForResult(i, LEADER_PROFILE);
                progressBar.setVisibility(View.GONE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LEADER_PROFILE) {
            if (data == null) {
                // do nothing
            }
            else {
                dh = (UserProfile) data.getSerializableExtra(LeaderboardActivity.extraName);
                Log.d(TAG, "PROFILE POINTS TO GIVE: " + String.valueOf(dh.getPoints_to_award()));
                numberPointsProfile.setText(String.valueOf(dh.getPoints_to_award()));
            }
        }
    }

    public static void makeCustomToast(Context context, int time) {
        Toast toast = Toast.makeText(context, "Success!", time);
        View toastView = toast.getView();
        toastView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        TextView tv = toastView.findViewById(android.R.id.message);
        tv.setPadding(250, 100, 250, 100);
        tv.setTextColor(Color.WHITE);
        toast.show();
    }

    public void sendError(boolean error, String connectionResult) {
        if (error) {
            try {
                JSONObject errorDetails = new JSONObject(connectionResult);
                JSONObject jsonObject = new JSONObject(errorDetails.getString("errordetails"));
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(jsonObject.getString("status"));
                builder.setMessage(jsonObject.getString("message"));
                AlertDialog dialog = builder.create();
                dialog.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            makeCustomToast(this, Toast.LENGTH_LONG);
        }
    }
}