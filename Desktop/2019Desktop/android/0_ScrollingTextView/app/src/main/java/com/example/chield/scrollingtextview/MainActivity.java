package com.example.chield.scrollingtextview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView1 = findViewById(R.id.oneLIne);
        textView1.setText("This is a one-line TextView");

        TextView textView2 = findViewById(R.id.multiLine);
        StringBuilder sb = new StringBuilder("This is a multi-line TextView without scrolling\n");
        for (int i = 0; i < 20; i++)
            sb.append("Line #" + i + "\n");
        textView2.setText(sb.toString());

        TextView textView3 = findViewById(R.id.multiLineScroll);
        // This next line is required for proper scrolling behavior

        textView3.setMovementMethod(new ScrollingMovementMethod());

        sb = new StringBuilder("This is a multi-line TextView WITH scrolling\n");
        for (int i = 0; i < 20; i++)
            sb.append("Line #" + i + "\n");
        textView3.setText(sb.toString());

    }
}
