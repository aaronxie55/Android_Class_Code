
package com.example.christopher.recycler;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHandler";

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    // DB Name
    private static final String DATABASE_NAME = "BookDB";

    // DB Table Name
    private static final String TABLE_NAME = "BookTable";

    ///DB Columns
    private static final String TITLE = "Title";
    private static final String AUTHOR = "Author";
    private static final String ISBN = "Isbn";
    private static final String PUBLISHER = "Publisher";
    private static final String YEAR = "Year";
    private static final String COST = "Cosy";

    // DB Table Create Code
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    TITLE + " TEXT not null unique," +
                    AUTHOR + " TEXT not null, " +
                    ISBN + " TEXT not null, " +
                    PUBLISHER + " TEXT not null, " +
                    YEAR + " TEXT not null, " +
                    COST + " TEXT not null)";

    private SQLiteDatabase database;
    private MainActivity mainActivity;


    DatabaseHandler(MainActivity context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mainActivity = context;
        database = getWritableDatabase(); // Inherited from SQLiteOpenHelper
        Log.d(TAG, "DatabaseHandler: C'tor DONE");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // onCreate is only called is the DB does not exist
        Log.d(TAG, "onCreate: Making New DB");
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    ArrayList<Book> loadBooks() {

        // Load books - return ArrayList of loaded books
        Log.d(TAG, "loadBooks: START");
        ArrayList<Book> books = new ArrayList<>();

        Cursor cursor = database.query(
                TABLE_NAME,  // The table to query
                new String[]{TITLE, AUTHOR, ISBN, PUBLISHER, YEAR, COST}, // The columns to return
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null); // The sort order

        if (cursor != null) {
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {
                String title = cursor.getString(0);
                String author = cursor.getString(1);
                String isbn = cursor.getString(2);
                String publisher = cursor.getString(3);
                String year = cursor.getString(4);
                String cost = cursor.getString(5);
                Book b = new Book(title, author, Integer.parseInt(year), publisher, isbn, Double.parseDouble(cost));
                books.add(b);
                cursor.moveToNext();
            }
            cursor.close();
        }
        Log.d(TAG, "loadBooks: DONE");

        return books;
    }

    void addBook(Book book) {
        ContentValues values = new ContentValues();

        values.put(TITLE, book.getTitle());
        values.put(AUTHOR, book.getAuthor());
        values.put(ISBN, book.getIsbn());
        values.put(PUBLISHER, book.getPublisher());
        values.put(YEAR, book.getYear());
        values.put(COST, book.getCost());


        long key = database.insert(TABLE_NAME, null, values);
        Log.d(TAG, "addBook: " + key);
    }

    void updateBook(Book book) {
        ContentValues values = new ContentValues();

        values.put(TITLE, book.getTitle());
        values.put(AUTHOR, book.getAuthor());
        values.put(ISBN, book.getIsbn());
        values.put(PUBLISHER, book.getPublisher());
        values.put(YEAR, book.getYear());
        values.put(COST, book.getCost());

        long key = database.update(TABLE_NAME, values, ISBN + " = ?", new String[]{book.getIsbn()});

        Log.d(TAG, "updateBook: " + key);
    }

    void deleteBook(String name) {
        Log.d(TAG, "deleteBook: " + name);

        int cnt = database.delete(TABLE_NAME, ISBN + " = ?", new String[]{name});

        Log.d(TAG, "deleteBook: " + cnt);
    }

    void findBook(HashMap<String, String> params) {
        Log.d(TAG, "findBook: ");

        StringBuilder details = new StringBuilder();
        for (String key : params.keySet()) {
            details.append(key)
                    .append(" = '")
                    .append(params.get(key))
                    .append("' AND ");
        }
        String clause = details.substring(0, details.lastIndexOf("AND"));


        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME + " where " + clause, null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                String title = cursor.getString(0);
                String author = cursor.getString(1);
                String isbn = cursor.getString(2);
                String publisher = cursor.getString(3);
                String year = cursor.getString(4);
                String cost = cursor.getString(5);
                Book b = new Book(title, author, Integer.parseInt(year), publisher, isbn, Double.parseDouble(cost));
                mainActivity.showFindResults(b);
            }
            else {
                mainActivity.showFindResults(null);
            }

            cursor.close();

        }
    }


    void dumpDbToLog() {
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME, null);
        if (cursor != null) {
            cursor.moveToFirst();

            Log.d(TAG, "dumpDbToLog: vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
            for (int i = 0; i < cursor.getCount(); i++) {
                String title = cursor.getString(0);
                String author = cursor.getString(1);
                String isbn = cursor.getString(2);
                String publisher = cursor.getString(3);
                int year = cursor.getInt(4);
                double cost = cursor.getFloat(5);
                Log.d(TAG, "dumpDbToLog: " +
                        String.format("%s %-18s", TITLE + ":", title) +
                        String.format("%s %-18s", AUTHOR + ":", author) +
                        String.format("%s %-18s", ISBN + ":", isbn) +
                        String.format("%s %-18s", PUBLISHER + ":", publisher) +
                        String.format("%s %-18s", YEAR + ":", year) +
                        String.format("%s %-18s", COST + ":", cost));
                cursor.moveToNext();
            }
            cursor.close();
        }

        Log.d(TAG, "dumpDbToLog: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
    }

    void shutDown() {
        database.close();
    }
}
