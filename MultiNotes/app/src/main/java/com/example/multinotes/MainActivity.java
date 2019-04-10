package com.example.multinotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity  extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    private final List<Note> noteList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private int MAKE_DATA_CODE = 1;
    private static final String TAG = "MainActivity";
    private Note note;
    private TextView title;
    private TextView text;
    private TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.titleField);
        text = findViewById(R.id.textField);
        date = findViewById(R.id.dateField);

        recyclerView = findViewById(R.id.recycler);

        noteAdapter = new NoteAdapter(noteList, this);

        // connect the recyclerView to the adapter
        recyclerView.setAdapter(noteAdapter);
        /* show the recyclerview */
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setTitle("Multi Notes " + "("+ noteAdapter.getItemCount() + ")");

        note = loadFile();  // Load the JSON containing the product data - if it exists
        if (note != null) { // null means no file was loaded
            title.setText(note.getTitle());
            text.setText(note.getText());
            date.setText(note.getDate());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Note m = noteList.get(pos);

        Intent editIntent = new Intent(this, EditActivity.class);
        editIntent.putExtra("TITLE", m.getTitle());
        editIntent.putExtra("TEXT", m.getText());

        startActivityForResult(editIntent, MAKE_DATA_CODE);
    }

    @Override
    public boolean onLongClick(View v) {
        final int pos = recyclerView.getChildLayoutPosition(v);
        Note m = noteList.get(pos);
        //Toast.makeText(v.getContext(), "LONG " + m.toString(), Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Note " + "'" + m.getTitle() + "'?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (!noteList.isEmpty()) {
                    noteList.remove(pos);
                    noteAdapter.notifyDataSetChanged();
                    setTitle("Multi Notes " + "("+ noteAdapter.getItemCount() + ")");
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
          case R.id.aboutField:
              Intent aboutIntent = new Intent(this, AboutActivity.class);
              startActivity(aboutIntent);
              return true;
          case R.id.noteField:
              Intent editIntent = new Intent(this, EditActivity.class);
              startActivityForResult(editIntent, MAKE_DATA_CODE);
              return true;
          default:
              return super.onOptionsItemSelected(item);
      }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MAKE_DATA_CODE) {
            if (resultCode == RESULT_OK) {
                Note dh = (Note) data.getSerializableExtra(EditActivity.extraName);
                noteList.add(0, dh);
                noteAdapter.notifyDataSetChanged();
                setTitle("Multi Notes " + "("+ noteAdapter.getItemCount() + ")");
            }
          }
    }

    private Note loadFile() {
        Log.d(TAG, "loadFile: Loading JSON File");
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("EE MMM d, hh:mm aa");
        String d3 = sdf.format(new Date());
        note = new Note("NBA", "basketball", "Tue Feb 26, 03:37 PM");
        try {
            InputStream is = getApplicationContext().
                    openFileInput(getString(R.string.file_name));

            JsonReader reader =
                    new JsonReader(new InputStreamReader(is, getString(R.string.encoding)));

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "name":
                        note.setTitle(reader.nextString());
                        break;
                    case "description":
                        note.setText(reader.nextString());
                        break;
                    case "date":
                        note.setDate(reader.nextString());
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();

        } catch (FileNotFoundException e) {
            Toast.makeText(this, getString(R.string.no_file), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return note;
    }

    @Override
    protected void onPause() {
        note.setTitle(title.getText().toString());
        note.setText(text.getText().toString());
        note.setDate(date.getText().toString());
        saveProduct();

        super.onPause();
    }

    private void saveProduct() {

        Log.d(TAG, "saveProduct: Saving JSON File");
        try {
            FileOutputStream fos = getApplicationContext().
                    openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            writer.setIndent("  ");
            writer.beginObject();
            writer.name("title").value(note.getTitle());
            writer.name("text").value(note.getText());
            writer.name("date").value(note.getDate());
            writer.endObject();
            writer.close();


            /// You do not need to do the below - it's just
            /// a way to see the JSON that is created.
            ///
            StringWriter sw = new StringWriter();
            writer = new JsonWriter(sw);
            writer.setIndent("  ");
            writer.beginObject();
            writer.name("title").value(note.getTitle());
            writer.name("text").value(note.getText());
            writer.name("date").value(note.getDate());
            writer.endObject();
            writer.close();
            Log.d(TAG, "saveProduct: JSON:\n" + sw.toString());
            ///
            ///

            Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
