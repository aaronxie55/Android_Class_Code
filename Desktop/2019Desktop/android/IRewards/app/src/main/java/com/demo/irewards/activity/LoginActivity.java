package com.demo.irewards.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;

import com.demo.irewards.App;
import com.demo.irewards.R;
import com.demo.irewards.api.LoginAPIAsyncTask;
import com.demo.irewards.entity.Login;
import com.demo.irewards.util.PublicUtil;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private CheckBox cbRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initData();

        requestPermissions();
    }

    private void initView() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        cbRemember = findViewById(R.id.cb_remember);
    }

    private void initData() {
        boolean remember = PublicUtil.getBoolean("remember");
        if (remember) {
            String username = PublicUtil.getString("username");
            String password = PublicUtil.getString("password");
            etUsername.setText(username);
            etPassword.setText(password);
        }
    }

    public void clickLogin(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etUsername.getWindowToken(), 1);
        Login reqParam = new Login();
        reqParam.setStudentId(App.studentId);
        reqParam.setUsername(etUsername.getText().toString().trim());
        reqParam.setPassword(etPassword.getText().toString().trim());
        if (isInputValid(reqParam)) {
            new LoginAPIAsyncTask(this, reqParam).execute();
        }
    }

    private boolean isInputValid(Login reqParam) {
        if (reqParam.getUsername().isEmpty()) {
            PublicUtil.toast("Username is empty!");
            return false;
        }
        if (reqParam.getPassword().isEmpty()) {
            PublicUtil.toast("Password is empty!");
            return false;
        }
        return true;
    }
    public void clickCreateProfile(View view) {
        Intent intent = new Intent(this, CreateProfileActivity.class);
        startActivity(intent);
    }

    public void showPB() {
        findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
    }
    public void dismissPB() {
        findViewById(R.id.progress_bar).setVisibility(View.GONE);
    }


    /**
     * save username and password to SharedPreference
     */
    public void save(String username, String password) {
        SharedPreferences sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("remember", cbRemember.isChecked());
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }

    /**
     * permission request
     */
    private void requestPermissions() {
        // Camera
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 0);
        }
    }
}
