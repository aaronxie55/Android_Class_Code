package com.christopherhield.radiobutton_toast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void radioClicked(View v) {
        String selectionText = ((RadioButton) v).getText().toString();
        Toast.makeText(this, "1 You Selected " + selectionText, Toast.LENGTH_SHORT).show();

    }

    public void radioClicked2(View v) {
        String selectionText = ((RadioButton) v).getText().toString();
        Toast.makeText(this, "2 You Selected " + selectionText, Toast.LENGTH_SHORT).show();
    }

    public void toggleClicked(View v) {
        String selectionText = ((ToggleButton) v).getText().toString();
        Toast.makeText(this, "Toggle Button Clicked:  " + selectionText, Toast.LENGTH_SHORT).show();
    }

}
