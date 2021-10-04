package com.demo.irewards.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.demo.irewards.R;
import com.demo.irewards.adapter.ProfileAdapter;
import com.demo.irewards.api.GetAllProfilesAPIAsyncTask;
import com.demo.irewards.entity.Login;
import com.demo.irewards.entity.Profile;
import com.demo.irewards.entity.Reward;
import com.demo.irewards.util.PublicUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1001;

    private ListView listView;
    private ProfileAdapter adapter;

    private Login reqParam;
    private List<Profile> profileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_profile);
        initView();
        setTitle("Inspiration Leaderboard");
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.mipmap.arrow_with_logo);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        listView = findViewById(R.id.list_view);
        adapter = new ProfileAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LeaderboardActivity.this, AwardActivity.class);
                intent.putExtra("profile", profileList.get(position));
                intent.putExtra("position", position);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        reqParam = (Login) getIntent().getSerializableExtra("login");
        if (reqParam == null) {
            PublicUtil.toast("error");
            return;
        }
        new GetAllProfilesAPIAsyncTask(this, reqParam).execute();
    }

    public void setData(ArrayList<Profile> list) {
        profileList = list;
        Collections.sort(profileList);
        adapter.setProfileList(list);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            int position = data.getIntExtra("position", -1);
            Reward reward = (Reward) data.getSerializableExtra("reward");
            profileList.get(position).getRewards().add(reward);
            Collections.sort(profileList);
            adapter.setProfileList(profileList);
            adapter.notifyDataSetChanged();
        }
    }

    public void showPB() {
        findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
    }
    public void dismissPB() {
        findViewById(R.id.progress_bar).setVisibility(View.GONE);
    }
}
