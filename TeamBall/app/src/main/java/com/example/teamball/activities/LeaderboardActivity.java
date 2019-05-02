package com.example.teamball.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.teamball.AsyncTasks.GetAllProfilesAPIAyncTask;
import com.example.teamball.ProfileAdapter;
import com.example.teamball.R;
import com.example.teamball.Reward;
import com.example.teamball.SortByPoints;
import com.example.teamball.UserProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public final class LeaderboardActivity extends AppCompatActivity
        implements View.OnClickListener{

    private final List<UserProfile> profileList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProfileAdapter profileAdapter;
    private static final String TAG = "LeaderboardActivity";
    private UserProfile up;
    private static final int ADD_CODE = 1;
    private int pos;
    public static final String extraName = "DATA HOLDER";
    private UserProfile source;

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
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("TARGET", temp);
        i.putExtra("SOURCE", up);
        startActivityForResult(i, ADD_CODE);
    }


    public void initiateData(ArrayList<UserProfile> upList) {
        profileList.addAll(upList);
        Collections.sort(profileList, Collections.reverseOrder(new SortByPoints()));
        profileAdapter.notifyDataSetChanged();

        Log.d(TAG, "initiateData added data: ");
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
                                    profile.getString("location"),
                                    profile.getString("username"),
                                    profile.getString("password"),
                                    profile.getString("imageBytes"),
                                    Integer.parseInt(profile.getString("pointsToAward")))); v
                } catch (JSvfgvb
            if (resultCode == RESULT_OK) {
                UserProfile dh = (UserProfile) data.getSerializableExtra("TARGET");
                profileList.remove(pos);
                profileList.add(pos, dh);
                Collections.sort(profileList, Collections.<UserProfile>reverseOrder(new SortByPoints()));
                profileAdapter.notifyDataSetChanged();

                Intent up = new Intent();
                source = (UserProfile) data.getSerializableExtra("SOURCE");
                up.putExtra(extraName, source); // Better be Serializable!
                Log.d(TAG, "LEADER POINTS TO GIVE: " + Integer.toString(source.getPoints_to_award()));
                setResult(RESULT_OK, up);
            }
        }
    }
}
