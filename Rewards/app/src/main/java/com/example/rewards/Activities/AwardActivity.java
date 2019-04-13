package com.example.rewards.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rewards.AsyncTasks.RewardsAPIAsyncTask;
import com.example.rewards.AsyncTasks.UpdateProfileAPIAsyncTask;
import com.example.rewards.R;
import com.example.rewards.UserProfile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AwardActivity extends AppCompatActivity {

    private TextView nameAward;
    private TextView numberPointsAwardedAward;
    private TextView departmentTextProfileAward;
    private TextView positionTextProfileAward;
    private TextView storyTextAward;
    private EditText commentTextAward;
    private EditText rewardPointsAward;
    private ImageView imageView;

    private TextView charCountText;
    private UserProfile dh;
    private UserProfile source;
    public static int MAX_CHARS = 80;
    private final List<UserProfile> profileList = new ArrayList<>();
    private Intent intent;
    private static final String TAG = "AwardActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award);

        nameAward = findViewById(R.id.nameAward);
        numberPointsAwardedAward = findViewById(R.id.numberPointsAwardedAward);
        departmentTextProfileAward = findViewById(R.id.departmentTextAward);
        positionTextProfileAward = findViewById(R.id.positionTextAward);
        storyTextAward = findViewById(R.id.storyTextAward);
        rewardPointsAward = findViewById(R.id.rewardPointsAward);
        commentTextAward = findViewById(R.id.commentTextAward);
        rewardPointsAward = findViewById(R.id.rewardPointsAward);
        imageView = findViewById(R.id.imageAward);

        intent = getIntent();

        dh = (UserProfile) intent.getSerializableExtra("TARGET");
        source = (UserProfile) intent.getSerializableExtra("SOURCE");

        nameAward.setText(dh.getLast_name() + ", " + dh.getFirst_name() );
        numberPointsAwardedAward.setText(String.valueOf(dh.getPoints_awarded()));
        departmentTextProfileAward.setText(dh.getDepartment());
        positionTextProfileAward.setText(dh.getPosition());
        storyTextAward.setText(dh.getStory());
        byte[] imageBytes = Base64.decode(dh.getImage(),  Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        imageView.setImageBitmap(bitmap);

        charCountText = findViewById(R.id.counter4);
        commentTextAward.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_CHARS)});
        addTextListener();
    }

    private void addTextListener() {
        commentTextAward.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // Nothing to do here
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // Nothing to do here
                String countText = "(" + 0 + " of " + MAX_CHARS + ")";
                charCountText.setText(countText);
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                int len = s.toString().length();
                String countText = "(" + len + " of " + MAX_CHARS + ")";
                charCountText.setText(countText);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.award_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.awardSave:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.drawable.logo);
                builder.setTitle("Save Changes?");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        UserProfile source = (UserProfile) intent.getSerializableExtra("SOURCE");
                        // there should be a comment
                        // can't add a reward to yourself
                        String date = new SimpleDateFormat("MM/dd/yyyy").format(new Date());

                        addReward("A20379665",
                                dh.getUsername(),
                                source.getFirst_name() + " " + source.getLast_name(),
                                date,
                                commentTextAward.getText().toString(),
                                Integer.parseInt(rewardPointsAward.getText().toString()),
                                source.getUsername(),
                                source.getPassword()
                                );
                        Log.d(TAG, "SEE HERE: " + String.valueOf(source.getPoints_to_award()+ rewardPointsAward.getText().toString()));
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateProfile(UserProfile up) {
        Log.d(TAG, "points to award updated: " + up.getPoints_to_award());
        new UpdateProfileAPIAsyncTask(this).execute("A20379665",
                up.getUsername(), up.getPassword(), up.getFirst_name(), up.getLast_name(),
                Integer.toString(up.getPoints_to_award()), up.getDepartment(), up.getStory(), up.getPosition(),
                Boolean.toString(up.isAdministrator_flag()), up.getLocation(), up.getImage());
    }

    public void addReward(String id, String uName, String name, String date, String note, int value, String sName, String sPass) {
        new RewardsAPIAsyncTask(this).execute(id, sName, sPass, uName, name, date, note, Integer.toString(value));
    }

    public static void makeCustomToast(Context context, int time) {
        Toast toast = Toast.makeText(context, "Add Reward Succeeded", time);
        View toastView = toast.getView();
        toastView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        TextView tv = toastView.findViewById(android.R.id.message);
        tv.setPadding(250, 100, 250, 100);
        tv.setTextColor(Color.WHITE);
        toast.show();
    }

    public void addData(boolean error, String connectionResult) {
        if (error) {
            try {
                JSONObject errorDetails = new JSONObject(connectionResult);
                //Log.d(TAG, "addData: " + errorDetails.getString("errordetails"));
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
            dh.setPoints_awarded(dh.getPoints_awarded() + Integer.parseInt(rewardPointsAward.getText().toString()));
            source.setPoints_to_award(source.getPoints_to_award()-Integer.parseInt(rewardPointsAward.getText().toString()));
            updateProfile(source);
            makeCustomToast(AwardActivity.this, Toast.LENGTH_LONG);
            Intent data = new Intent(); // Used to hold results data to be returned to original activity
            data.putExtra("TARGET", dh); // Better be Serializable!
            data.putExtra("SOURCE", source); // Better be Serializable!
            Log.d(TAG, "AWARD POINTS TO GIVE: " + Integer.toString(source.getPoints_to_award()));
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
