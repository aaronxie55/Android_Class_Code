package com.example.TemperatureConverter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.text.DecimalFormat;
import android.text.method.ScrollingMovementMethod;
import android.widget.RadioButton;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButton;
    private RadioButton rbutton1;
    private RadioButton rbutton2;
    private Button mButton1;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sharedPreferences = getSharedPreferences("MY_PREFS", MODE_PRIVATE);

        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(this);
        mButton1 = (Button) findViewById(R.id.button3);
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView history = findViewById(R.id.textView5);
                history.setText("");
            }
        });
        RadioGroup rgroup = (RadioGroup) findViewById(R.id.radioGroup);
        rbutton1 = (RadioButton) findViewById(R.id.radioButtondef);
        if (sharedPreferences.getBoolean("first", true))
        {
            sharedPreferences = getSharedPreferences("MY_PREFS",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("f2c", true);
            editor.putBoolean("c2f", false);;
            editor.putBoolean("first",false).apply();
        }

        rbutton2 = (RadioButton) findViewById(R.id.radioButton2);
        rbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textChange1 = findViewById(R.id.textView2);
                textChange1.setText("Fahrenheit Degrees: ");
                TextView textChange2 = findViewById(R.id.textView3);
                textChange2.setText("Celsius Degrees: ");
                saveRadioButtons();
            }
        });

        rbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textChange1 = findViewById(R.id.textView2);
                textChange1.setText("Celsius Degrees: ");
                TextView textChange2 = findViewById(R.id.textView3);
                textChange2.setText("Fahrenheit Degrees: ");
                saveRadioButtons();
            }
        });
        loadRadioButtons();
        TextView textView5 = findViewById(R.id.textView5);
        textView5.setMovementMethod(new ScrollingMovementMethod());

    }


    public void onClick(View v)
    {
        TextView tcheck = findViewById(R.id.textView2);
        String tresult = (String) tcheck.getText();



        loadRadioButtons();
        if (tresult.contains("F")) {
            EditText user_input = findViewById(R.id.editText);
            TextView history = (TextView) findViewById(R.id.textView5);
            String text = user_input.getText().toString();
            double input = Double.parseDouble(user_input.getText().toString());
            double result = (input - 32) / 1.8;
            TextView cresult = (TextView) findViewById(R.id.textView6);
            cresult.setText(new DecimalFormat("##.#").format(result));
            StringBuilder sb =new StringBuilder();
            history.setText(("F to C: " + new DecimalFormat("##.#").format(input)+ " -> " + new DecimalFormat("##.#").format(result) + "\n")+history.getText());
        }
        else if(tresult.contains("C")){
            EditText user_input = findViewById(R.id.editText);
            TextView history = (TextView) findViewById(R.id.textView5);
            String text = user_input.getText().toString();
            double input = Double.parseDouble(user_input.getText().toString());
            double result = (input * 1.8) + 32;
            TextView cresult = (TextView) findViewById(R.id.textView6);
            cresult.setText(new DecimalFormat("##.#").format(result));
            StringBuilder sb =new StringBuilder();
            history.setText(("C to F: " + String.format("%.1f", input)+ " -> " + new DecimalFormat("##.#").format(result) + "\n")+history.getText());
        }

    }

    protected void onSaveInstanceState(Bundle outState) {

        TextView history = findViewById(R.id.textView5);
        outState.putString("HISTORY", history.getText().toString());


        super.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        TextView history = findViewById(R.id.textView5);
        super.onRestoreInstanceState(savedInstanceState);

        history.setText(savedInstanceState.getString("HISTORY"));
    }

    protected void saveRadioButtons(){
        sharedPreferences = getSharedPreferences("MY_PREFS",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("f2c", rbutton1.isChecked());
        editor.putBoolean("c2f", rbutton2.isChecked());
        editor.apply();
    }
    protected void loadRadioButtons() {


        sharedPreferences = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        rbutton1.setChecked(sharedPreferences.getBoolean("f2c", false));
        rbutton2.setChecked(sharedPreferences.getBoolean("c2f", false));
    }


}
