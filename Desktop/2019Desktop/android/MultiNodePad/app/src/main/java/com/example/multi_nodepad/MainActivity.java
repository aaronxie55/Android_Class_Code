package com.example.multi_nodepad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;


import android.widget.Toast;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity // NOTE the interfaces here!
        implements View.OnClickListener, View.OnLongClickListener {

    private static final int B_REQUEST_CODE = 1;
    private static final String TAG = "MainActivity";
    private TextView userText;
    private TextView textScroll;
    private EditText title;
    private EditText text;
    private multi_Notes notes;
    private RecyclerView recyclerView;

    private int position;

    //   implements View.OnClickListener, View.OnLongClickListener{

        private final List<multi_Notes> notesList = new ArrayList<>();  // Main content is here

        //private RecyclerView recyclerView; // Layout's recyclerview

        private multiNotesAdapter mAdapter; // Data to recyclerview adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        title = findViewById(R.id.title);
        text = findViewById(R.id.text);

        text.setTextIsSelectable(true);

        recyclerView = findViewById(R.id.recycler);

        mAdapter = new multiNotesAdapter(notesList, this);

        recyclerView.setAdapter(mAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

      //  new Async(this).execute();

    }
/*
    @Override
    protected void onResume() {

        notesList.size();
        super.onResume();
        mAdapter.notifyDataSetChanged();

    }
*/
/*
    private multi_Notes loadFile() {

        Log.d(TAG, "loadFile: Loading JSON File");
        notes = new multi_Notes(null,null);
        try {
            InputStream is = getApplicationContext().
                    openFileInput(getString(R.string.file_name));

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject jsonObject = new JSONObject(sb.toString());
            String title = jsonObject.getString("title");
            String text = jsonObject.getString("text");
            notes.setTitle(title);
            notes.setText(text);


        } catch (FileNotFoundException e) {
            Toast.makeText(this, getString(R.string.no_file), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notes;
    }
*/


/*
    @Override
    protected void onPause() {

        saveNote();

        super.onPause();
    }

    */

/*
    private void saveNote() {

        try {
            FileOutputStream fos = getApplicationContext().
                    openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            writer.setIndent("  ");
            writer.beginObject();
            writer.name("text");
            writeNotesArray(writer);
            writer.endObject();
            writer.close();



            Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
*/

/*
    public void writeNotesArray(JsonWriter writer) throws IOException {
        writer.beginArray();
        for (multi_Notes value : notesList) {
            writeNotesObject(writer, value);
        }
        writer.endArray();
    }
*/

/*
    public void writeNotesObject(JsonWriter writer, multi_Notes val) throws IOException{
        writer.beginObject();
        writer.name("title").value(val.getTitle());
        writer.name("time").value(val.getTime());
        writer.name("text").value(val.getText());
        writer.endObject();
    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.About) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;

        } else if (item.getItemId() == R.id.Create) {

            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, MainActivity.class.getSimpleName());
            startActivityForResult(intent, B_REQUEST_CODE);

            return true;

        } else{

            return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == B_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {


                String text = data.getStringExtra("USER_TEXT_IDENTIFIER");

                String text2 = data.getStringExtra("USER_TEXT_IDENTIFIER2");


                notesList.add(0, new multi_Notes(text,text2));

                mAdapter.notifyDataSetChanged();



                 //Log.d(TAG, "onActivityResult: User Text: " + text);
            } else {
                Log.d(TAG, "onActivityResult: result Code: " + resultCode);
            }

        } else {
            Log.d(TAG, "onActivityResult: Request Code " + requestCode);
        }
    }

    @Override
    public void onClick(View view) {

        position = recyclerView.getChildLayoutPosition(view);
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        //intent.putExtra(Intent.EXTRA_TEXT, MainActivity.class.getSimpleName());
        intent.putExtra("TITLE", notesList.get(position).getTitle());
        intent.putExtra("DATE", notesList.get(position).getTime());
        intent.putExtra("TEXT", notesList.get(position).getText());
        startActivityForResult(intent, B_REQUEST_CODE);

    }

    public List<multi_Notes> getNotesList() {
        return notesList;
    }

    @Override
    public boolean onLongClick(View view) {
/*
        position = recyclerView.getChildLayoutPosition(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                notesList.remove(position);
                mAdapter.notifyDataSetChanged();
                position = -1;
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                position = -1;
            }
        });
        builder.setMessage("Are you sure you want to delete this note?");
        builder.setTitle("Delete");
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
        */
return true;
    }

}



