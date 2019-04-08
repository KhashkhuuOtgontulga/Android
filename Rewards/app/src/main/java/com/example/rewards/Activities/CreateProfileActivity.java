package com.example.rewards.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
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

import com.example.rewards.AsyncTasks.CreateProfileAPIAsyncTask;
import com.example.rewards.R;
import com.example.rewards.UserProfile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class CreateProfileActivity extends AppCompatActivity {

    private int REQUEST_IMAGE_GALLERY = 1;
    private int REQUEST_IMAGE_CAPTURE = 2;
    public static final String extraName = "DATA HOLDER";
    public static int MAX_CHARS = 360;

    private EditText username;
    private EditText password;
    private EditText first_name;
    private EditText last_name;
    private CheckBox administrator_flag;
    private EditText department;
    private EditText position;
    private EditText story;

    private TextView charCountText;
    private ImageView imageView;
    private File currentImageFile;
    private static final String TAG = "CreateProfileActivity";

    //private LeaderboardActivity leaderboardActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createprofile);

        username = findViewById(R.id.usernameProfile);
        password = findViewById(R.id.passEdit);
        first_name = findViewById(R.id.firstNameEdit);
        last_name = findViewById(R.id.lastName);

        administrator_flag = findViewById(R.id.administrator);
        department = findViewById(R.id.departmentEdit);
        position = findViewById(R.id.positionEdit);
        story = findViewById(R.id.story);
        charCountText = findViewById(R.id.counter);

        story.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_CHARS)});
        addTextListener();

        imageView = findViewById(R.id.imageProfile2);
        imageView.setImageResource(R.drawable.default_photo);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.arrow_with_logo);

        //leaderboardActivity = new LeaderboardActivity();
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
                                password.getText().toString(),
                                "Chicago, Illinois",
                                administrator_flag.isChecked(),
                                0,
                                department.getText().toString(),
                                position.getText().toString(),
                                        1000,
                                story.getText().toString());
                        createProfile(up);
                        makeCustomToast(CreateProfileActivity.this, Toast.LENGTH_LONG);
                        // add the user profile to the database
                        // then start the login activity
                        Intent intent = new Intent(CreateProfileActivity.this, ProfileActivity.class);
                        intent.putExtra(extraName, up); // Better be Serializable!
                        startActivity(intent);
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

    private void createProfile(UserProfile up) {
        Bitmap origBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        origBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        String encodedfile = Base64.encodeToString(b, Base64.DEFAULT);
        Log.d(TAG, "createProfile activity and method: ");
        new CreateProfileAPIAsyncTask(this).execute("A20379665",
                up.getUsername(), up.getPassword(), up.getFirst_name(), up.getLast_name(),
                "", up.getDepartment(), up.getStory(), up.getPosition(),
                Boolean.toString(up.isAdministrator_flag()), up.getLocation(), encodedfile);
    }

    public void pickOption(final View w) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.logo);
        builder.setTitle("Profile Picture");
        builder.setMessage("Take picture from: ");
        builder.setPositiveButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        doCamera(w);
                    }
                }
        );
        builder.setNegativeButton("Gallery",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doGallery(w);
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
    }

    public void doGallery(View v) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_IMAGE_GALLERY);
    }

    public void doCamera(View v) {
        currentImageFile = new File(getExternalCacheDir(), "appimage_" + System.currentTimeMillis() + ".jpg");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentImageFile));
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            try {
                processGallery(data);
            } catch (Exception e) {
                Toast.makeText(this, "onActivityResult: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                processCamera();
            } catch (Exception e) {
                Toast.makeText(this, "onActivityResult: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    private void processCamera() {
        Uri selectedImage = Uri.fromFile(currentImageFile);
        imageView.setImageURI(selectedImage);
        Bitmap bm = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        currentImageFile.delete();
    }

    private void processGallery(Intent data) {
        Uri galleryImageUri = data.getData();
        if (galleryImageUri == null)
            return;

        InputStream imageStream = null;
        try {
            imageStream = getContentResolver().openInputStream(galleryImageUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

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
