package com.example.mulitinotes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonWriter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {


    private List<Note_info> note_infoList = new ArrayList<>();
    private RecyclerView recyclerView;

    private Note_infoAdapter nAdapter;


    private static final int REQUEST_CODE = 123;

    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        recyclerView = findViewById(R.id.recyc);
        nAdapter = new Note_infoAdapter(note_infoList, this);
        recyclerView.setAdapter(nAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        new myAsyncTask(this).execute();

    }

    @Override
    protected void onResume(){
        note_infoList.size();
        super.onResume();
        nAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onPause(){
        saveNotes();
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        position = recyclerView.getChildLayoutPosition(v);
        Intent intent = new Intent(MainActivity.this, CreateNote.class);
        intent.putExtra("TITLE", note_infoList.get(position).getTitle());
        intent.putExtra("NOTE", note_infoList.get(position).getMessage());
        intent.putExtra("DATE", note_infoList.get(position).getDate());
        startActivityForResult(intent, REQUEST_CODE);

    }

    @Override
    public boolean onLongClick(View v) {

        position = recyclerView.getChildLayoutPosition(v);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                note_infoList.remove(position);
                nAdapter.notifyDataSetChanged();
                position = -1;
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                position = -1;
            }
        });

        builder.setMessage("Delete Note" + "'" + note_infoList.get(position).getTitle() + "'?");


        AlertDialog dialog = builder.create();
        dialog.show();

        return false;
    }


    private void saveNotes(){
        try
        {
            FileOutputStream fos =getApplicationContext().openFileOutput("Note_Saved.json", Context.MODE_PRIVATE);

            JsonWriter jw = new JsonWriter(new OutputStreamWriter(fos, "UTF-8"));
            jw.setIndent("  ");
            jw.beginObject();
            jw.name("notes");
            jwArray(jw);
            jw.endObject();
            jw.close();

        }
        catch (Exception e)
        {
            e.getStackTrace();
        }
    }

    public void jwArray(JsonWriter jw) throws IOException{
        jw.beginArray();
        for (Note_info note_info : note_infoList)
        {
            jwObject(jw, note_info);
        }
        jw.endArray();
    }

    public void jwObject(JsonWriter jw, Note_info note_info) throws IOException
    {
        jw.beginObject();
        jw.name("title").value(note_info.getTitle());
        jw.name("date").value(note_info.getDate());
        jw.name("note").value(note_info.getMessage());
        jw.endObject();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.create_button:
                Intent intent1 = new Intent(MainActivity.this, CreateNote.class);
                startActivityForResult(intent1, REQUEST_CODE);
                return true;
            case R.id.info_button:
                Intent intent = new Intent(MainActivity.this, AboutPage.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                Note_info edit_note = (Note_info) data.getExtras().getSerializable("EDIT_NOTE");
                String status = data.getStringExtra("STATUS");
                if(status.equals("NO_CHANGE")){ }
                else if(status.equals("CHANGE")){
                    note_infoList.remove(position);
                    note_infoList.add(0, edit_note);
                }
                else if(status.equals("NEW")){
                    note_infoList.add(0, edit_note);
                }
            }
        }
    }

    public List<Note_info> getNote_infoList()
    {
        return note_infoList;
    }

}
