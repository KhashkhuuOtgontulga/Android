package com.example.rewards;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateProfile extends AppCompatActivity {

    private int REQUEST_IMAGE_GALLERY = 1;
    public static final String extraName = "DATA HOLDER";

    private EditText username;
    private EditText password;
    private EditText first_name;
    private EditText last_name;
    private EditText administrator_flag;
    private EditText department;
    private EditText position;
    private EditText story;
    private TextView counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createprofile);

        username = findViewById(R.id.usernameProfile);
        password = findViewById(R.id.passEdit);
        first_name = findViewById(R.id.firstNameEdit);
        last_name = findViewById(R.id.lastName);
        department = findViewById(R.id.departmentEdit);
        position = findViewById(R.id.positionEdit);
        story = findViewById(R.id.story);
        counter = findViewById(R.id.counter);

        counter.setText("Hello");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.createprofile_menu, menu);
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
                        UserProfile up = new UserProfile(first_name.getText().toString(),
                                last_name.getText().toString(),
                                username.getText().toString(),
                                "Chicago, Illinois",
                                0,
                                department.getText().toString(),
                                position.getText().toString(),
                                1000,
                                story.getText().toString());

                        Intent intent = new Intent(CreateProfile.this, Profile.class);
                        intent.putExtra(extraName, up);
                        startActivity(intent);
                        makeCustomToast(CreateProfile.this, Toast.LENGTH_LONG);
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

    public void doGallery(View v) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_IMAGE_GALLERY);
    }


    public static void makeCustomToast(Context context, int time) {
        Toast toast = Toast.makeText(context, "User Create Successful", time);
        View toastView = toast.getView();
        toastView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        TextView tv = toastView.findViewById(android.R.id.message);
        tv.setPadding(250, 100, 250, 100);
        tv.setTextColor(Color.WHITE);
        toast.show();
    }
}
