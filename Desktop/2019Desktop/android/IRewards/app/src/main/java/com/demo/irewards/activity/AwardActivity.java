package com.demo.irewards.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.irewards.App;
import com.demo.irewards.R;
import com.demo.irewards.api.CreateProfileAPIAsyncTask;
import com.demo.irewards.api.RewardsAPIAsyncTask;
import com.demo.irewards.entity.Award;
import com.demo.irewards.entity.Profile;
import com.demo.irewards.entity.Reward;
import com.demo.irewards.util.PublicUtil;

public class AwardActivity extends AppCompatActivity {

    private TextView tvName;
    private ImageView ivAvatar;
    private TextView tvAwarded;
    private TextView tvDepartment;
    private TextView tvPosition;
    private TextView tvStory;
    private EditText etPointsSend;
    private TextView tvCommentLength;
    private EditText etComment;

    private Profile profile;
    private Award award;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award);
        initView();
        bindData();
        listener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.mipmap.arrow_with_logo);
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                award.getTarget().setValue(Integer.parseInt(etPointsSend.getText().toString().trim()));
                award.getTarget().setNotes(etComment.getText().toString());
                if (isInputValid(award.getTarget().getValue(), award.getTarget().getNotes())) {
                    new AlertDialog.Builder(this)
                            .setIcon(R.mipmap.logo)
                            .setTitle("Add Rewards Points?")
                            .setMessage("Add rewards for " + profile.getLastName() + ", " + profile.getFirstName())
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new RewardsAPIAsyncTask(AwardActivity.this, award).execute();
                                }
                            })
                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {}
                            }).show();
                }
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initView() {
        tvName = findViewById(R.id.tv_name);
        ivAvatar = findViewById(R.id.iv_avatar);
        tvAwarded = findViewById(R.id.tv_awarded);
        tvDepartment = findViewById(R.id.tv_department);
        tvPosition = findViewById(R.id.tv_position);
        tvStory = findViewById(R.id.tv_story);
        etPointsSend = findViewById(R.id.et_points_send);
        tvCommentLength = findViewById(R.id.tv_comment_length);
        etComment = findViewById(R.id.et_comment);
    }

    private void bindData() {
        profile = (Profile) getIntent().getSerializableExtra("profile");
        setTitle(profile.getLastName() + " " + profile.getFirstName());

        award = new Award();
        Award.Source source = new Award.Source();
        source.setStudentId(App.studentId);
        source.setUsername(PublicUtil.getString("username"));
        source.setPassword(PublicUtil.getString("password"));
        award.setSource(source);
        Award.Target target = new Award.Target();
        target.setName(profile.getLastName() + ", " + profile.getFirstName());
        target.setUsername(profile.getUsername());
        target.setStudentId(profile.getStudentId());
        target.setDate(PublicUtil.getDate());
        award.setTarget(target);

        tvName.setText(profile.getLastName() + ", " + profile.getFirstName());
        ivAvatar.setImageBitmap(PublicUtil.base64ToBitmap(profile.getImageBytes()));
        float awarded = 0;
        if (profile.getRewards() != null) {
            for (int i = 0; i < profile.getRewards().size(); i++) {
                awarded += profile.getRewards().get(i).getValue();
            }
        }
        tvAwarded.setText(awarded + "");
        tvDepartment.setText(profile.getDepartment());
        tvPosition.setText(profile.getPosition());
        tvStory.setText(profile.getStory());
    }

    public void setResult() {
        Intent intent = new Intent(this, LeaderboardActivity.class);
        int position = getIntent().getIntExtra("position", -1);
        intent.putExtra("position", position);
        Reward reward = new Reward();
        reward.setName(profile.getLastName() + ", " + profile.getFirstName());
        reward.setStudentId(App.studentId);
        reward.setDate(award.getTarget().getDate());
        reward.setUsername(award.getSource().getUsername());
        reward.setNotes(award.getTarget().getNotes());
        reward.setValue(award.getTarget().getValue());
        intent.putExtra("reward", reward);
        setResult(RESULT_OK, intent);
    }

    private void listener() {
        etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                tvCommentLength.setText("Comment: (" + s.length() + " of " + "80)");
            }
        });
    }

    private boolean isInputValid(int pointsSend, String comment) {
        if (pointsSend == 0) {
            PublicUtil.toast("Reward points to send should not be 0!");
            return false;
        }
        if (TextUtils.isEmpty(comment)) {
            PublicUtil.toast("Comment should not be empty!");
            return false;
        }
        return true;
    }

    public void showPB() {
        findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
    }
    public void dismissPB() {
        findViewById(R.id.progress_bar).setVisibility(View.GONE);
    }
}
