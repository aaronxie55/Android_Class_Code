package com.demo.irewards.api;

import android.content.Intent;
import android.os.AsyncTask;

import com.demo.irewards.activity.AwardActivity;
import com.demo.irewards.activity.LeaderboardActivity;
import com.demo.irewards.entity.Award;
import com.demo.irewards.entity.Resp;
import com.demo.irewards.util.PublicUtil;

import java.net.HttpURLConnection;

public class RewardsAPIAsyncTask extends AsyncTask<Void, Void, Resp> {

    private AwardActivity activity;
    private Award reqParam;

    public RewardsAPIAsyncTask(AwardActivity activity, Award reqParam) {
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
        return APIRequester.getInstance().post("/rewards", reqParam.toJsonObject());
    }

    @Override
    protected void onPostExecute(Resp resp) {
        super.onPostExecute(resp);
        activity.dismissPB();
        if (resp.code == HttpURLConnection.HTTP_OK) {
            activity.setResult();
            PublicUtil.toast(resp.json);
        } else {
            PublicUtil.toast(resp.msg);
        }
    }
}
