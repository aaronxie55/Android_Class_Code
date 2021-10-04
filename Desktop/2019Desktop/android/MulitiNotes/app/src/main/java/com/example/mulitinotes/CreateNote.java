package com.example.mulitinotes;

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

import java.util.Calendar;

public class CreateNote extends AppCompatActivity {
    private EditText title;
    private EditText note;
    private String ptitle = " ";
    private String pnote = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        title = findViewById(R.id.create_title);
        note = findViewById(R.id.create_note);

        Intent intent = getIntent();
        if(intent.hasExtra("TITLE"))
        {
            ptitle = intent.getStringExtra("TITLE");
            title.setText(ptitle);
        }
        else if (intent.hasExtra("NOTE")) {
            pnote = intent.getStringExtra("NOTE");
            note.setText(pnote);
        }

        note.setMovementMethod(new ScrollingMovementMethod());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu2, menu);
        return true;
    }


    @Override
    public void onBackPressed() {


        if(title.getText().toString().isEmpty()) {
            Toast.makeText(CreateNote.this, "Un-titled activity was not saved!", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }else if(ptitle.isEmpty()&&pnote.isEmpty()){
            super.onBackPressed();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Your note is not saved! \nSave note '" + title.getText() + "'");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    noteSave();
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
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save_note:
                noteSave();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void noteSave(){
        Note_info newNote = new Note_info();
        newNote.setTitle(title.getText().toString());
        newNote.setMessage(note.getText().toString());
        newNote.setDate(Calendar.getInstance().getTime().toString());
        Intent resultIntent = new Intent();
        resultIntent.putExtra("EDIT_NOTE", newNote);
        if(title.getText().toString().isEmpty()) {
            resultIntent.putExtra("STATUS", "NO_CHANGE");
            Toast.makeText(this, "Empty Note Cannot Be Saved", Toast.LENGTH_SHORT).show();
        }
        else if(ptitle.isEmpty() && pnote.isEmpty())
            resultIntent.putExtra("STATUS", "NEW");
        else if(ptitle.equals(title.getText().toString()) && pnote.equals(note.getText().toString()))
            resultIntent.putExtra("STATUS", "NO_CHANGE");
        else
            resultIntent.putExtra("STATUS", "CHANGE");
        setResult(RESULT_OK, resultIntent);
        finish();
    }

}
