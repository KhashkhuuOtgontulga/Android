package com.example.multinotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {

    public static final String extraName = "DATA HOLDER";
    private EditText editText;
    private EditText editText2;
    private Intent intent;
    private String title;
    private String text;
    private Menu optionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editText = findViewById(R.id.titleField);
        editText2 = findViewById(R.id.textField);

        intent = getIntent();

        title = intent.getStringExtra("TITLE");
        text = intent.getStringExtra("TEXT");

        editText.setText(title);
        editText2.setText(text);

        editText2.setMovementMethod(new ScrollingMovementMethod());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        optionsMenu = menu;
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Your note is not saved!");
        builder.setMessage("Save note "+ "'" + intent.getStringExtra("TITLE") + "'?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                onOptionsItemSelected(optionsMenu.findItem(R.id.saveField));
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveField:
                 String d1 = editText.getText().toString();
                 String d2 = editText2.getText().toString();
                 SimpleDateFormat sdf = new SimpleDateFormat("EE MMM d, hh:mm aa");
                 String d3 = sdf.format(new Date());

                // we do not have a title
                if(d1.trim().matches("") ){
                    Toast.makeText(EditActivity.this,
                            "The un-titled activity was not saved.",
                            Toast.LENGTH_LONG).show();
                    finish(); // This closes the current activity, returning us to the original activity
                }
                // no changes in the note
                else if (d1.equals(title) && d2.equals(text)) {
                    finish();
                }
                else {
                    Note dh = new Note(d1, d2, d3);
                    Intent data = new Intent(); // Used to hold results data to be returned to original activity
                    data.putExtra(extraName, dh); // Better be Serializable!
                    setResult(RESULT_OK, data);
                    finish(); // This closes the current activity, returning us to the original activity
                }
                 return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
