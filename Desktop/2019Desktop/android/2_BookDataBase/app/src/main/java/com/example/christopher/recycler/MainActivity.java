package com.example.christopher.recycler;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    // The above lines are important - them make this class a listener
    // for click and long click events in the ViewHolders (in the recycler

    private static final String TAG = "MainActivity";
    private List<Book> bookList = new ArrayList<>();  // Main content is here

    private RecyclerView recyclerView; // Layout's recyclerview

    private BookAdapter mAdapter; // Data to recyclerview adapter

    private static final int ADD_CODE = 1;
    private static final int UPDATE_CODE = 2;
    private static final int FIND_CODE = 3;

    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        mAdapter = new BookAdapter(bookList, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHandler = new DatabaseHandler(this);

        databaseHandler.dumpDbToLog();
        ArrayList<Book> list = databaseHandler.loadBooks();
        bookList.addAll(list);
        Collections.sort(bookList);
        mAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onDestroy() {
        databaseHandler.shutDown();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {  // click listener called by ViewHolder clicks

        int pos = recyclerView.getChildLayoutPosition(v);
        Book book = bookList.get(pos);
        Intent intent = new Intent(this, BookDetailActivity.class);
        intent.putExtra("BOOK", book);
        startActivityForResult(intent, UPDATE_CODE);
    }

    @Override
    public boolean onLongClick(View v) {  // long click listener called by ViewHolder long clicks
        final int pos = recyclerView.getChildLayoutPosition(v);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                databaseHandler.deleteBook(bookList.get(pos).getIsbn());
                bookList.remove(pos);
                mAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        builder.setMessage("Delete Book " + bookList.get(pos).getTitle() + "?");
        builder.setTitle("Delete Book");

        AlertDialog dialog = builder.create();
        dialog.show();


        return true;
    }

    public void doAdd(View v) {
        Intent intent = new Intent(this, BookDetailActivity.class);
        startActivityForResult(intent, ADD_CODE);
    }

    public void doFind(View v) {
        Intent intent = new Intent(this, BookFindActivity.class);
        startActivityForResult(intent, FIND_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: ");

        // Unknown request code problem
        if (requestCode != ADD_CODE && requestCode != UPDATE_CODE && requestCode != FIND_CODE) {
            Log.d(TAG, "onActivityResult: Unknown Request Code: " + requestCode);
            return;
        }

        // Bad result problem
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "Error adding/updating new Book", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the book
        Book book = null;
        if (data.hasExtra("BOOK")) {
            book = (Book) data.getSerializableExtra("BOOK");
        }

        // Make db request
        switch (requestCode) {
            case ADD_CODE:
                databaseHandler.addBook(book);
                bookList.add(book);
                break;
            case UPDATE_CODE:
                databaseHandler.updateBook(book);
                bookList.remove(book);
                bookList.add(book);
                break;
            case FIND_CODE:
                HashMap<String, String> params;
                if (data.hasExtra("FIND")) {
                    params = (HashMap<String, String>) data.getSerializableExtra("FIND");
                    if (params.isEmpty())
                        showWarning("No search criteria specified");
                    else
                        databaseHandler.findBook(params);
                } else {
                    showWarning("Your search failed");
                }

                break;
        }

        Collections.sort(bookList);
        mAdapter.notifyDataSetChanged();
    }

    public void showFindResults(Book b) {
        if (b == null) {
            showWarning("No books matched search criteria");

            return;
        }

        // Dialog with a layout
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.book_list_entry, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setMessage("Please enter the values:");
        builder.setTitle("Find Results:");
        builder.setIcon(R.drawable.search);


        ((TextView) view.findViewById(R.id.title)).setText(b.getTitle());
        ((TextView) view.findViewById(R.id.author)).setText(b.getAuthor());
        ((TextView) view.findViewById(R.id.isbn)).setText(b.getIsbn());
        ((TextView) view.findViewById(R.id.publisher_year)).setText(
                String.format(Locale.getDefault(), "%s, %d",
                        b.getPublisher(), b.getYear()));
        ((TextView) view.findViewById(R.id.cost)).setText(String.format(Locale.US, "$%.2f", b.getCost()));
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showWarning(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(R.drawable.warning);

        builder.setMessage(msg);
        builder.setTitle("Search Failed");

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
