package com.example.rewards.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.rewards.AsyncTasks.LoginAPIAsyncTask;
import com.example.rewards.R;
import com.example.rewards.SharedPreference;
import com.example.rewards.UserProfile;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private SharedPreference prefs;
    private EditText data1;
    private EditText data2;
    public static final String extraName = "DATA HOLDER";
    //SharedPreferences settings;
    //SharedPreferences.Editor editor;
    //private LeaderboardActivity leaderboardActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.icon);

        data1 = findViewById(R.id.userText);
        data2 = findViewById(R.id.passText);


        prefs = new SharedPreference(this);
        data1.setText(prefs.getValue(getString(R.string.data1Key)));
        data2.setText(prefs.getValue(getString(R.string.data2Key)));

        //settings = getSharedPreferences("mysettings", 0);
        //editor = settings.edit();
        //leaderboardActivity = new LeaderboardActivity();
    }

    public void itemClicked(View w) {
        CheckBox c1 = (CheckBox) w;
        if (c1.isChecked()) {
            saveAll(w);
        }
        else {
            clearAll();
        }
        Log.d(TAG, "itemClicked: " + Boolean.toString(c1.isChecked()));
        // save the state of the checkbox

        /*// Save
        boolean checkBoxValue = c1.isChecked();
        editor.putBoolean("c1", checkBoxValue);
        editor.commit();;

        // Load
        c1.setChecked(settings.getBoolean("c1", true));*/
    }

    public void login(View w) {
        // if the username and password match in a database
        // access the database to log into the profile activity
        // else say that the username or password is incorrect
        data1.getText().toString();
        data2.getText().toString();
        String sId = "A20379665";
        String uName = ((EditText) findViewById(R.id.userText)).getText().toString();
        String pswd = ((EditText) findViewById(R.id.passText)).getText().toString();
        Log.d(TAG, "login clicked: ");
        new LoginAPIAsyncTask(this).execute(sId, uName, pswd);
    }

    public void createProfile(View v) {
        Intent intent = new Intent(this, CreateProfileActivity.class);
        startActivity(intent);
    }

    public void saveAll(View v) {
        Log.d(TAG, "saveAll: ");
        save1(v);
        save2(v);
    }

    public void save1(View v) {
        Log.d(TAG, "save1: ");
        String d1 = data1.getText().toString();
        prefs.save(getString(R.string.data1Key), d1);
    }

    public void save2(View v) {
        Log.d(TAG, "save2: ");
        String d2 = data2.getText().toString();
        prefs.save(getString(R.string.data2Key), d2);
    }

    public void clearAll() {
        Log.d(TAG, "clearAll: ");

        prefs.clearAll();
        data1.setText("");
        data2.setText("");

    }

    public void sendResults(String s) throws JSONException {
        Log.d(TAG, "login going to the profile: " + s);
        JSONObject json = new JSONObject(s);
        UserProfile up = new UserProfile(json.getString("firstName"),
                json.getString("lastName"),
                json.getString("username"),
                json.getString("password"),
                json.getString("location"),
                Boolean.parseBoolean(json.getString("admin")),
                0,
                json.getString("department"),
                json.getString("position"),
                Integer.parseInt(json.getString("pointsToAward")),
                json.getString("story"));
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(extraName, up); // Better be Serializable!
        startActivity(intent);
        Log.d(TAG, "supposed to start the activity: ");
    }
}
