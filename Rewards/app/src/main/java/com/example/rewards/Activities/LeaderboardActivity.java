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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class LeaderboardActivity extends AppCompatActivity
        implements View.OnClickListener{

    private final List<UserProfile> profileList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProfileAdapter profileAdapter;
    private static final String TAG = "LeaderboardActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        recyclerView = findViewById(R.id.recycler);

        profileAdapter = new ProfileAdapter(profileList, this);

        // connect the recyclerView to the adapter
        recyclerView.setAdapter(profileAdapter);
        /* show the recyclerview */
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();

        UserProfile up = (UserProfile) intent.getSerializableExtra("LEADER");

        // load the profiles from the AWS database and display the profiles
        new GetAllProfilesAPIAyncTask(this).execute("A20379665", up.getUsername(), up.getPassword());
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "Going to the awards activity", Toast.LENGTH_LONG).show();
    }

    public void initiateData(ArrayList<UserProfile> upList) {
        profileList.addAll(upList);
        Collections.sort(profileList, new SortByPoints());
        profileAdapter.notifyDataSetChanged();
    }

    public void printProfiles(String s) {
        Log.d(TAG, "printProfiles: " + s);
        JSONArray jsonArr = null;
        try {
            jsonArr = new JSONArray(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<UserProfile> userProfilesList = new ArrayList<>();

        for (int i = 0; i < jsonArr.length(); i++)
        {
            JSONObject jsonObj = null;
            try {
                jsonObj = jsonArr.getJSONObject(i);
                Log.d(TAG, "printProfiles: " + jsonObj.toString());
                userProfilesList.add(
                        new UserProfile(jsonObj.getString("firstName"),
                                jsonObj.getString("lastName"),
                                jsonObj.getString("username"),
                                jsonObj.getString("password"),
                                jsonObj.getString("location"),
                                Boolean.parseBoolean(jsonObj.getString("admin")),
                                0,
                                jsonObj.getString("department"),
                                jsonObj.getString("position"),
                                Integer.parseInt(jsonObj.getString("pointsToAward")),
                                jsonObj.getString("story")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println(jsonObj);
        }
        initiateData(userProfilesList);
    }
}
