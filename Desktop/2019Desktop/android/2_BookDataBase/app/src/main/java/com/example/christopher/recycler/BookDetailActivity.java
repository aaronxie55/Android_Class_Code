package com.example.christopher.recycler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.Locale;

public class BookDetailActivity extends AppCompatActivity {

    private EditText title;
    private EditText author;
    private EditText isbn;
    private EditText publisher;
    private EditText year;
    private EditText cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.title);
        author = findViewById(R.id.author);
        isbn = findViewById(R.id.isbn);
        publisher = findViewById(R.id.publisher);
        year = findViewById(R.id.year);
        cost = findViewById(R.id.cost);

        // Check to see if a Book object was provided in the activity's intent for editing.
        // Set up the textviews if so.
        Intent intent = getIntent();
        if (intent.hasExtra("BOOK")) {
            Book currentBook = (Book) intent.getSerializableExtra("BOOK");

            title.setText(currentBook.getTitle());
            author.setText(currentBook.getAuthor());
            isbn.setText(currentBook.getIsbn());
            isbn.setFocusable(false);
            publisher.setText(currentBook.getPublisher());
            year.setText(String.format(Locale.US, "%d", currentBook.getYear()));
            cost.setText(String.format(Locale.US, "%.2f", currentBook.getCost()));
        }

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

    public void doSave(View v) {

        String titleData = title.getText().toString();
        String authorData = author.getText().toString();
        String isbnData = isbn.getText().toString();
        String publisherData = publisher.getText().toString();
        int yearData = Integer.parseInt(year.getText().toString().trim());
        double costData = Double.parseDouble(cost.getText().toString().trim());

        Book book = new Book(titleData, authorData, yearData, publisherData, isbnData, costData);

        Intent data = new Intent();
        data.putExtra("BOOK", book);
        setResult(RESULT_OK, data);
        super.onBackPressed();
    }
}
