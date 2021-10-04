package com.demo.irewards.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.demo.irewards.App;
import com.demo.irewards.R;
import com.demo.irewards.api.CreateProfileAPIAsyncTask;
import com.demo.irewards.entity.Profile;
import com.demo.irewards.util.LocationUtil;
import com.demo.irewards.util.PublicUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;



public class CreateProfileActivity extends AppCompatActivity {

    public enum CreateProfileType {
        CREATE, UPDATE
    }

    private static final int GALLERY_REQUEST_CODE = 1001;
    private static final int CAMERA_REQUEST_CODE = 1002;

    private CreateProfileType type;
    private Profile profile;

    private EditText etUsername;
    private EditText etPassword;
    private CheckBox cbAdmin;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etDepartment;
    private EditText etPosition;
    private TextView tvStory;
    private EditText etStory;
    private ImageView ivAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        initView();
        initData();
        location();
        listener();
    }

    private void initView() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        cbAdmin = findViewById(R.id.cb_admin);
        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etDepartment = findViewById(R.id.et_department);
        etPosition = findViewById(R.id.et_position);
        tvStory = findViewById(R.id.tv_story);
        etStory = findViewById(R.id.et_story);
        ivAvatar = findViewById(R.id.iv_avatar);
    }

    private void initData() {
        profile = (Profile) getIntent().getSerializableExtra("profile");
        if (profile == null) {
            type = CreateProfileType.CREATE;
            profile = new Profile();
            profile.setPointsToAward(1000);
            return;
        }
        type = CreateProfileType.UPDATE;
        ivAvatar.setImageBitmap(PublicUtil.base64ToBitmap(profile.getImageBytes()));
        etUsername.setText(profile.getUsername());
        etUsername.setEnabled(false);
        etPassword.setText(profile.getPassword());
        cbAdmin.setChecked(profile.isAdmin());
        etFirstName.setText(profile.getFirstName());
        etLastName.setText(profile.getLastName());
        etDepartment.setText(profile.getDepartment());
        etPosition.setText(profile.getPosition());
        tvStory.setText("Your Story: (" + profile.getStory().length() + " of " + "360)");
        etStory.setText(profile.getStory());
    }

    private void location() {
        LocationUtil locationUtil = new LocationUtil(this);
        locationUtil.setCallback(new LocationUtil.LocationCallback() {
            @Override
            public void onSuccess(String City, String state) {
                profile.setLocation(City + ", " + state);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setTitle("Create Profile");
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
                profile.setStudentId(App.studentId);
                profile.setUsername(etUsername.getText().toString().trim());
                profile.setPassword(etPassword.getText().toString().trim());
                profile.setAdmin(cbAdmin.isChecked());
                profile.setFirstName(etFirstName.getText().toString());
                profile.setLastName(etLastName.getText().toString());
                profile.setDepartment(etDepartment.getText().toString());
                profile.setPosition(etPosition.getText().toString());
                profile.setStory(etStory.getText().toString());
                if (isInputValid(profile)) {
                    new AlertDialog.Builder(this)
                            .setIcon(R.mipmap.logo)
                            .setMessage("Save Changes?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new CreateProfileAPIAsyncTask(CreateProfileActivity.this, profile, type).execute();
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

    public void setBackResult(Profile profile) {
        Intent intent = new Intent();
        intent.putExtra("profile", profile);
        setResult(RESULT_OK, intent);
    }

    public void clickChoosePicture(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.logo)
                .setTitle("Profile Picture")
                .setMessage("Take picture from:")
                .setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("GALLERY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, GALLERY_REQUEST_CODE);
                    }
                })
                .setPositiveButton("CAMERA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(getPackageManager()) == null) {
                            PublicUtil.toast( "No camera!");
                            return;
                        }
                        startActivityForResult(intent, CAMERA_REQUEST_CODE);
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GALLERY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    try {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = { MediaStore.Images.Media.DATA };
                        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIdx = cursor.getColumnIndex(filePathColumn[0]);
                        String path = cursor.getString(columnIdx);
                        cursor.close();
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        ivAvatar.setImageBitmap(bitmap);
                        profile.setImageBytes(PublicUtil.bitmapToBase64(bitmap));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CAMERA_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    ivAvatar.setImageBitmap(bitmap);
                    profile.setImageBytes(PublicUtil.bitmapToBase64(bitmap));
                }
                break;
        }
    }

    private boolean isInputValid(Profile reqParam) {
        if (TextUtils.isEmpty(reqParam.getImageBytes())) {
            PublicUtil.toast("Please choose picture first!");
            return false;
        }
        if (TextUtils.isEmpty(reqParam.getUsername())) {
            PublicUtil.toast("Please input username first!");
            return false;
        }
        if (TextUtils.isEmpty(reqParam.getPassword())) {
            PublicUtil.toast("Please input password first!");
            return false;
        }
        if (TextUtils.isEmpty(reqParam.getFirstName())) {
            PublicUtil.toast( "Please input first name first!");
            return false;
        }
        if (TextUtils.isEmpty(reqParam.getLastName())) {
            PublicUtil.toast("Please input last name first!");
            return false;
        }
        if (TextUtils.isEmpty(reqParam.getDepartment())) {
            PublicUtil.toast("Please input department first!");
            return false;
        }
        if (TextUtils.isEmpty(reqParam.getLocation())) {
            PublicUtil.toast("You may not give the location permission or get location failed!");
            return false;
        }
        if (TextUtils.isEmpty(reqParam.getStory())) {
            PublicUtil.toast("Please input story first!");
            return false;
        }
        if (TextUtils.isEmpty(reqParam.getPosition())) {
            PublicUtil.toast( "Please input position first!");
            return false;
        }

        return true;
    }

    private void listener() {
        etStory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                tvStory.setText("Your Story: (" + s.length() + " of " + "360)");
            }
        });
    }

    public void showPB() {
        findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
    }
    public void dismissPB() {
        findViewById(R.id.progress_bar).setVisibility(View.GONE);
    }
}
