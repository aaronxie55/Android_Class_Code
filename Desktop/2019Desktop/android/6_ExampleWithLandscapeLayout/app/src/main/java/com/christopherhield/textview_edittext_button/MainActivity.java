package com.christopherhield.textview_edittext_button;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText userText;
    private Button button1;
    private Button button2;
    private TextView output1;
    private TextView output2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind var's to the screen widgets
        userText = (EditText) findViewById(R.id.editText);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        output1 = (TextView) findViewById(R.id.textView1);
        output2 = (TextView) findViewById(R.id.textView2);

        button1.setOnClickListener(new View.OnClickListener() {  // Listener for button 1
            @Override
            public void onClick(View v) {
                String text = userText.getText().toString();
                if (!text.trim().isEmpty())
                    output1.setText("B1: " + text);
            }
        });
    }

    public void button2Clicked(View v) {   // Listener for button 2
        String text = userText.getText().toString();
        if (!text.trim().isEmpty())
            output2.setText("B2: " + text);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString("OUTPUT1", output1.getText().toString());
        outState.putString("OUTPUT2", output2.getText().toString());

        // Call super last
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Call super first
        super.onRestoreInstanceState(savedInstanceState);

        output1.setText(savedInstanceState.getString("OUTPUT1"));
        output2.setText(savedInstanceState.getString("OUTPUT2"));
    }
}
