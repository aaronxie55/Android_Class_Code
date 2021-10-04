package com.example.christopher.nobundlesave;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText numText;
    private TextView history;
    private int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null)
            Toast.makeText(this, "NULL", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "NOT NULL", Toast.LENGTH_LONG).show();

        // Bind a variable to the screen widgets
        numText = (EditText) findViewById(R.id.phoneNum);
        history = (TextView) findViewById(R.id.history);
    }

    public void buttonClicked(View v) {

        String newText = numText.getText().toString();
        String historyText = history.getText().toString();
        history.setText(newText + "\n" + historyText);
        numText.setText("");

        value += Integer.parseInt(newText);
        Toast.makeText(this, "Current value: " + value, Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString("HISTORY", history.getText().toString());
        outState.putInt("VALUE", value);

        // Call super last
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Call super first
        super.onRestoreInstanceState(savedInstanceState);

        history.setText(savedInstanceState.getString("HISTORY"));
        value = savedInstanceState.getInt("VALUE");
    }
}
