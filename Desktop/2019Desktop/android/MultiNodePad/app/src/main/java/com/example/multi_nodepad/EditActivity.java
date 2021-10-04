package com.example.multi_nodepad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditActivity extends AppCompatActivity {


    private EditText editText_Title;
    private EditText editText_Text;
    private EditText textScroll;
    private String getPreviousTitle = "" , getPreviousContent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //TextView textView = findViewById(R.id.activityLabel);
        editText_Title = findViewById(R.id.title);
        editText_Text  = findViewById(R.id.text);

        textScroll = findViewById(R.id.text);
        textScroll.setMovementMethod(new ScrollingMovementMethod());


        /*
        Intent intent = getIntent();
        if (intent.hasExtra("TITLE")) {
            getPreviousTitle = intent.getStringExtra("TITLE");
            editText_Title.setText(getPreviousTitle);
        }
        if (intent.hasExtra("NOTE")) {
            getPreviousContent = intent.getStringExtra("NOTE");
            editText_Text.setText(getPreviousContent);
        }

        editText_Text.setMovementMethod(new ScrollingMovementMethod());


*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu2, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.Save) {

            if (editText_Title.getText().toString().trim().equals("")){


                finish();

                Toast.makeText(EditActivity.this,"un-titled activity was not saved", Toast.LENGTH_LONG).show();

                return true;

            }
            else {

                Intent data = new Intent(); // Used to hold results data to be returned to original activity

                data.putExtra("USER_TEXT_IDENTIFIER", editText_Title.getText().toString());

                data.putExtra("USER_TEXT_IDENTIFIER2", editText_Text.getText().toString());

                setResult(RESULT_OK, data);

                finish();

               // setTitle("Multi" + );

                return true;

            }


        }
        else{

            return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void onBackPressed() {
        // Pressing the back arrow closes the current activity, returning us to the original activity

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Your note is not saved ! \n Save note set DVR");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                Intent data = new Intent(); // Used to hold results data to be returned to original activity

                data.putExtra("USER_TEXT_IDENTIFIER", editText_Title.getText().toString());

                data.putExtra("USER_TEXT_IDENTIFIER2", editText_Text.getText().toString());

                setResult(RESULT_OK, data);

                finish();

            }
        });


        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                finish();
            }

        });


        AlertDialog dialog = builder.create();

        dialog.show();

    }
/*
    public void saveNote(){
        multi_Notes newNote = new multi_Notes();
        newNote.setTitle(editText_Title.getText().toString());
        newNote.setText(editText_Text.getText().toString());
        newNote.setTime(Calendar.getInstance().getTime().toString());
        Intent resultIntent = new Intent();

        resultIntent.putExtra("EDIT_NOTE", newNote);

        if(editText_Title.getText().toString().isEmpty()) {

            resultIntent.putExtra("STATUS", "NO_CHANGE");
            Toast.makeText(this, "Empty Note Cannot Be Saved", Toast.LENGTH_SHORT).show();
        }
        else if(getPreviousTitle.isEmpty() && getPreviousContent.isEmpty())
            resultIntent.putExtra("STATUS", "NEW");
        else if(getPreviousTitle.equals(editText_Title.getText().toString()) && getPreviousContent.equals(editText_Text.getText().toString()))
            resultIntent.putExtra("STATUS", "NO_CHANGE");
        else
            resultIntent.putExtra("STATUS", "CHANGE");
        setResult(RESULT_OK, resultIntent);
        finish();
    }
*/



}



