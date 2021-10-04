package com.example.multinotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private EditText etitle;
    private EditText emessage;
    private EditText edate;
    private textinfo text;


    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etitle = findViewById(R.id.editTitle);
        emessage = findViewById(R.id.info_text);
        edate = findViewById(R.id.editText);

        text = loadFile();  // Load the JSON containing the product data - if it exists
        if (text != null) { // null means no file was loaded
            etitle.setText(text.getTitle());
            emessage.setText(text.getInfo());
            edate.setText(text.getDate());
        }
        else
        {
            Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
        }

    }

    private textinfo loadFile() {
        text = new textinfo();
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

            String title = jsonObject.getString("name");
            String desc = jsonObject.getString("description");
            String date = jsonObject.getString("datetime");

            text.setTitle(title);
            text.setInfo(desc);
            text.setDate(date);

        }
        catch (FileNotFoundException e) {
            Toast.makeText(this, getString(R.string.no_file), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    protected void onPause() {
        text.setTitle(etitle.getText().toString());
        text.setInfo(emessage.getText().toString());
        text.setDate(edate.getText().toString());
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
            writer.name("name").value(text.getTitle());
            writer.name("description").value(text.getInfo());
            writer.name("datetime").value(text.getDate());
            writer.endObject();
            writer.close();

        } catch (Exception e) {
            e.getStackTrace();
        }
    }
























    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.sys_about:
                Toast.makeText(this, "You want to do A", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.edit_button:
                Toast.makeText(this, "You have chosen B", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
