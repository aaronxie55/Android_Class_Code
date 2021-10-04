package com.example.christopher.recycler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;

public class BookFindActivity extends AppCompatActivity {

    private EditText title;
    private EditText author;
    private EditText isbn;
    private EditText publisher;
    private EditText year;
    private EditText cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_find);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.title);
        author = findViewById(R.id.author);
        isbn = findViewById(R.id.isbn);
        publisher = findViewById(R.id.publisher);
        year = findViewById(R.id.year);
        cost = findViewById(R.id.cost);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Respond to the action bar's Up/Home button
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void doFind(View v) {

        HashMap<String, String> values = new HashMap<>();
        if (!title.getText().toString().trim().isEmpty())
            values.put("TITLE", title.getText().toString().trim());
        if (!author.getText().toString().trim().isEmpty())
            values.put("AUTHOR", author.getText().toString().trim());
        if (!isbn.getText().toString().trim().isEmpty())
            values.put("ISBN", isbn.getText().toString().trim());
        if (!publisher.getText().toString().trim().isEmpty())
            values.put("PUBLISHER", publisher.getText().toString().trim());
        if (!year.getText().toString().trim().isEmpty())
            values.put("YEAR", year.getText().toString().trim());
        if (!cost.getText().toString().trim().isEmpty())
            values.put("COST", cost.getText().toString().trim());

        Intent data = new Intent();
        data.putExtra("FIND", values);
        setResult(RESULT_OK, data);
        super.onBackPressed();
    }
}
