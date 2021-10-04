package com.christopherhield.notes;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
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
    private EditText name;
    private EditText description;
    private Product product;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.prodName);
        description = findViewById(R.id.prodDesc);

        description.setMovementMethod(new ScrollingMovementMethod());

        description.setTextIsSelectable(true);
    }

    @Override
    protected void onResume() {
        product = loadFile();  // Load the JSON containing the product data - if it exists
        if (product != null) { // null means no file was loaded
            name.setText(product.getName());
            description.setText(product.getDescription());
        }
        super.onResume();
    }

    private Product loadFile() {

        Log.d(TAG, "loadFile: Loading JSON File");
        product = new Product();
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
            String name = jsonObject.getString("name");
            String desc = jsonObject.getString("description");
            product.setName(name);
            product.setDescription(desc);


        } catch (FileNotFoundException e) {
            Toast.makeText(this, getString(R.string.no_file), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return product;
    }

    @Override
    protected void onPause() {
        product.setName(name.getText().toString());
        product.setDescription(description.getText().toString());
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
            writer.name("name").value(product.getName());
            writer.name("description").value(product.getDescription());
            writer.endObject();
            writer.close();


            /// You do not need to do the below - it's just
            /// a way to see the JSON that is created.
            ///

            StringWriter sw = new StringWriter();
            writer = new JsonWriter(sw);
            writer.setIndent("  ");
            writer.beginObject();
            writer.name("name").value(product.getName());
            writer.name("description").value(product.getDescription());
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
