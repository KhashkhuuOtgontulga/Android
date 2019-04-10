package com.example.rewards.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rewards.AsyncTasks.UpdateProfileAPIAsyncTask;
import com.example.rewards.R;
import com.example.rewards.UserProfile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "EditProfileActivity";
    private TextView username;
    private EditText password;
    private EditText first_name;
    private EditText last_name;
    private CheckBox administrator_flag;
    private EditText department;
    private EditText position;
    private EditText story;
    private TextView charCountText;
    private ImageView imageView;

    private UserProfile up;

    public static final String extraName = "DATA HOLDER";
    public static int MAX_CHARS = 360;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        username = findViewById(R.id.nonEdit);
        password = findViewById(R.id.passProfile);
        first_name = findViewById(R.id.firstNameEdit);
        last_name = findViewById(R.id.lastNameEdit);
        administrator_flag = findViewById(R.id.administrator2);
        department = findViewById(R.id.departmentEdit);
        position = findViewById(R.id.positionAward);
        story = findViewById(R.id.storyEdit);
        charCountText = findViewById(R.id.counter2);
        imageView = findViewById(R.id.imageProfile2);

        Intent intent = getIntent();

        UserProfile dh = (UserProfile) intent.getSerializableExtra("EDIT");

        username.setText(dh.getUsername());
        password.setText(dh.getPassword());
        first_name.setText(dh.getFirst_name());
        last_name.setText(dh.getLast_name());
        administrator_flag.setChecked(dh.isAdministrator_flag());
        department.setText(dh.getDepartment());
        position.setText(dh.getPosition());
        story.setText(dh.getStory());
        imageView.setImageResource(R.drawable.default_photo);

        story.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_CHARS)});
        addTextListener();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.arrow_with_logo);
    }

    private void addTextListener() {
        story.addTextChangedListener(new TextWatcher() {

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
        getMenuInflater().inflate(R.menu.editprofile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveField:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.drawable.logo);
                builder.setTitle("Save Changes?");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        up = new UserProfile(first_name.getText().toString(),
                                last_name.getText().toString(),
                                username.getText().toString(),
                                password.getText().toString(),
                                "Chicago, Illinois",
                                administrator_flag.isChecked(),
                                0,
                                department.getText().toString(),
                                position.getText().toString(),
                                1000,
                                story.getText().toString());
                        updateProfile(up);
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
        Bitmap origBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        origBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        String encodedfile = Base64.encodeToString(b, Base64.DEFAULT);
        Log.d(TAG, "updateProfile password: " + up.getPassword());
        new UpdateProfileAPIAsyncTask(this).execute("A20379665",
                up.getUsername(), up.getPassword(), up.getFirst_name(), up.getLast_name(),
                "", up.getDepartment(), up.getStory(), up.getPosition(),
                Boolean.toString(up.isAdministrator_flag()), up.getLocation(), encodedfile);
    }

    public static void makeCustomToast(Context context, int time) {
        Toast toast = Toast.makeText(context, "User Update Successful", time);
        View toastView = toast.getView();
        toastView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        TextView tv = toastView.findViewById(android.R.id.message);
        tv.setPadding(250, 100, 250, 100);
        tv.setTextColor(Color.WHITE);
        toast.show();
    }

    public void sendResults(boolean error, String connectionResult) {
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
            makeCustomToast(EditProfileActivity.this, Toast.LENGTH_LONG);
            Intent data = new Intent(); // Used to hold results data to be returned to original activity
            data.putExtra(extraName, up); // Better be Serializable!
            setResult(RESULT_OK, data);
            finish(); // This closes the current activity, returning us to the original activity
        }
    }
}
