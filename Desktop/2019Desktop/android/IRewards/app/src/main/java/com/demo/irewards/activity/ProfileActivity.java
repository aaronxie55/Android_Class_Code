package com.demo.irewards.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.demo.irewards.R;
import com.demo.irewards.adapter.RewardAdapter;
import com.demo.irewards.entity.Login;
import com.demo.irewards.entity.Profile;

public class ProfileActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1001;

    private ListView listView;
    private RewardAdapter adapter;

    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initView();
    }

    private void initView() {
        listView = findViewById(R.id.list_view);
        profile = (Profile) getIntent().getSerializableExtra("profile");
        adapter = new RewardAdapter(this, profile);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setTitle("Your Profile");
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.mipmap.arrow_with_logo);
        getMenuInflater().inflate(R.menu.menu_edit_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_list:
                Intent intent = new Intent(this, LeaderboardActivity.class);
                Login login = new Login();
                login.setStudentId(profile.getStudentId());
                login.setUsername(profile.getUsername());
                login.setPassword(profile.getPassword());
                intent.putExtra("login", login);
                startActivity(intent);
                break;
            case R.id.action_edit:
                Intent _intent = new Intent(this, CreateProfileActivity.class);
                _intent.putExtra("profile", profile);
                startActivityForResult(_intent, REQUEST_CODE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            profile = (Profile) data.getSerializableExtra("profile");
            adapter.setProfile(profile);
            adapter.notifyDataSetChanged();
        }
    }
}
