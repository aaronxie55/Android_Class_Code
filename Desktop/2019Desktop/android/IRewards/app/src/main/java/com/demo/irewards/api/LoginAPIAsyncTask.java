package com.demo.irewards.api;

import android.content.Intent;
import android.os.AsyncTask;

import com.demo.irewards.activity.LoginActivity;
import com.demo.irewards.activity.ProfileActivity;
import com.demo.irewards.entity.Login;
import com.demo.irewards.entity.Profile;
import com.demo.irewards.entity.Resp;
import com.demo.irewards.util.PublicUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class LoginAPIAsyncTask extends AsyncTask<Void, Void, Resp> {

    private LoginActivity activity;
    private Login reqParam;

    public LoginAPIAsyncTask(LoginActivity activity, Login reqParam) {
        this.activity = activity;
        this.reqParam = reqParam;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.showPB();
    }

    @Override
    protected Resp doInBackground(Void... voids) {
        return APIRequester.getInstance().post("/login", reqParam.toJsonObject());
    }

    @Override
    protected void onPostExecute(Resp resp) {
        super.onPostExecute(resp);
        activity.dismissPB();
        if (resp.code == HttpURLConnection.HTTP_OK) {
            try {
                activity.save(reqParam.getUsername(), reqParam.getPassword());
                Intent intent = new Intent(activity, ProfileActivity.class);
                Profile profile = new Profile(new JSONObject(resp.json));
                profile.setOldPassword(reqParam.getPassword());
                profile.setPassword(reqParam.getPassword());
                intent.putExtra("profile", profile);
                activity.startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            PublicUtil.toast(resp.msg);
        }
    }
}
