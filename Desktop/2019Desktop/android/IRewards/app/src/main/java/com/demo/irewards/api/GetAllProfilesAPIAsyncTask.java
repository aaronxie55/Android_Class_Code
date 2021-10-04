package com.demo.irewards.api;

import android.os.AsyncTask;

import com.demo.irewards.activity.LeaderboardActivity;
import com.demo.irewards.entity.Login;
import com.demo.irewards.entity.Profile;
import com.demo.irewards.entity.Resp;
import com.demo.irewards.util.PublicUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class GetAllProfilesAPIAsyncTask extends AsyncTask<Void, Void, Resp> {

    private LeaderboardActivity activity;
    private Login reqParam;

    public GetAllProfilesAPIAsyncTask(LeaderboardActivity activity, Login reqParam) {
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
        return APIRequester.getInstance().post("/allprofiles", reqParam.toJsonObject());
    }

    @Override
    protected void onPostExecute(Resp resp) {
        super.onPostExecute(resp);
        if (resp.code == HttpURLConnection.HTTP_OK) {
            ArrayList<Profile> list = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(resp.json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    Profile profile = new Profile(jsonArray.getJSONObject(i));
                    list.add(profile);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            activity.setData(list);
        } else {
            PublicUtil.toast(resp.msg);
        }
        activity.dismissPB();
    }
}
