package com.example.rewards.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
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
import com.example.rewards.Reward;
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
    private UserProfile up;
    private static final int ADD_CODE = 1;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        Intent intent = getIntent();

        up = (UserProfile) intent.getSerializableExtra("LEADER");

        recyclerView = findViewById(R.id.recycler);

        profileAdapter = new ProfileAdapter(profileList, up, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(profileAdapter);

        profileAdapter.notifyDataSetChanged();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.arrow_with_logo);

        // load the profiles from the AWS database and display the profiles
        new GetAllProfilesAPIAyncTask(this).execute("A20379665", up.getUsername(), up.getPassword());
    }

    @Override
    public void onClick(View v) {
        pos = recyclerView.getChildLayoutPosition(v);
        UserProfile temp = profileList.get(pos);
        if(temp.getFirst_name().equals(up.getFirst_name())) {
            return;
        }
        Toast.makeText(this, "Going to the awards activity", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, AwardActivity.class);
        i.putExtra("TARGET", temp);
        i.putExtra("SOURCE", up);
        startActivityForResult(i, ADD_CODE);
    }

    public void initiateData(ArrayList<UserProfile> upList) {
        profileList.addAll(upList);
        Collections.sort(profileList, Collections.reverseOrder(new SortByPoints()));
        profileAdapter.notifyDataSetChanged();
    }

    public void printProfiles(boolean error, String connectionResult) {
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
            JSONArray jsonArrProfiles = null;
            try {
                jsonArrProfiles = new JSONArray(connectionResult);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ArrayList<UserProfile> userProfilesList = new ArrayList<>();
            ArrayList<Reward> temp = new ArrayList<>();

            if (jsonArrProfiles == null)
                return;

            for (int i = 0; i < jsonArrProfiles.length(); i++)
            {
                JSONObject profile;
                try {
                    profile = jsonArrProfiles.getJSONObject(i);
                    int total = 0;
                    try{
                        JSONArray rewards = new JSONArray(profile.getString("rewards"));
                        Log.d(TAG, "rewards: " + rewards.toString());
                        for (int j = 0; j < rewards.length(); j++) {
                            JSONObject giver = rewards.getJSONObject(j);
                            temp.add(new Reward(giver.getString("date"),
                                    giver.getString("name"),
                                    giver.getString("notes"),
                                    Integer.parseInt(giver.getString("value"))));
                            total += Integer.parseInt(giver.getString("value"));
                        }
                        Log.d(TAG, "total: " + Integer.toString(total));
                    } catch (JSONException e) {
                        // do nothing
                    }
                    //Log.d(TAG, "rewards: " + profile.getString("rewards"));
                    Log.d(TAG, "printProfiles: " + profile.toString());
                    userProfilesList.add(
                            new UserProfile(profile.getString("firstName"),
                                    profile.getString("lastName"),
                                    profile.getString("username"),
                                    profile.getString("password"),
                                    profile.getString("location"),
                                    Boolean.parseBoolean(profile.getString("admin")),
                                    total,
                                    profile.getString("department"),
                                    profile.getString("position"),
                                    Integer.parseInt(profile.getString("pointsToAward")),
                                    profile.getString("story"),
                                    profile.getString("imageBytes"),
                                    temp));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            initiateData(userProfilesList);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_CODE) {
            if (resultCode == RESULT_OK) {
                UserProfile dh = (UserProfile) data.getSerializableExtra("OBJECT");
                profileList.remove(pos);
                profileList.add(pos, dh);
                Collections.sort(profileList, Collections.<UserProfile>reverseOrder(new SortByPoints()));
                profileAdapter.notifyDataSetChanged();
            }
        }
    }
}
