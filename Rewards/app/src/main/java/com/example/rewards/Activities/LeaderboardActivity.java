package com.example.rewards.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.rewards.AsyncTasks.GetAllProfilesAPIAyncTask;
import com.example.rewards.ProfileAdapter;
import com.example.rewards.R;
import com.example.rewards.SortByPoints;
import com.example.rewards.UserProfile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class LeaderboardActivity extends AppCompatActivity
        implements View.OnClickListener{

    private final List<UserProfile> profileList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProfileAdapter profileAdapter;
    private static final String TAG = "LeaderboardActivity";
    private LeaderboardActivity leaderboardActivity;

    public LeaderboardActivity() {
        leaderboardActivity = this;
        recyclerView = findViewById(R.id.recycler);

        profileAdapter = new ProfileAdapter(profileList, this);

        // connect the recyclerView to the adapter
        recyclerView.setAdapter(profileAdapter);
        /* show the recyclerview */
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();

        UserProfile up = (UserProfile) intent.getSerializableExtra("LEADER");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        // load the profiles from the AWS database and display the profiles
        new GetAllProfilesAPIAyncTask(this).execute("A20379665" );
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "Going to the awards activity", Toast.LENGTH_LONG).show();
    }

    public void insertProfile(UserProfile up) {
        Log.d(TAG, "insertStock: profilelist with name: " + up.getFirst_name() + ", " + up.getLast_name());
        profileList.add(0, up);
        Collections.sort(profileList, new SortByPoints());
        profileAdapter.notifyDataSetChanged();
    }

    public void initiateData(ArrayList<UserProfile> upList) {
        profileList.addAll(upList);
        profileAdapter.notifyDataSetChanged();
    }
}
