package com.demo.irewards.api;

import android.os.AsyncTask;
import android.util.Log;

import com.demo.irewards.activity.CreateProfileActivity;
import com.demo.irewards.activity.CreateProfileActivity.CreateProfileType;
import com.demo.irewards.entity.Profile;
import com.demo.irewards.entity.Resp;
import com.demo.irewards.util.PublicUtil;

import java.net.HttpURLConnection;

public class CreateProfileAPIAsyncTask extends AsyncTask<Void, Void, Resp> {

    private CreateProfileActivity activity;

    private CreateProfileType type;
    private Profile reqParam;

    public CreateProfileAPIAsyncTask(CreateProfileActivity activity, Profile reqParam, CreateProfileType type) {
        this.activity = activity;
        this.reqParam = reqParam;
        this.type = type;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.showPB();
    }

    @Override
    protected Resp doInBackground(Void... voids) {
        switch (type) {
            case CREATE:
                return APIRequester.getInstance().post("/profiles", reqParam.toJsonObject());
            case UPDATE:
                return APIRequester.getInstance().put("/profiles", reqParam.toJsonObject());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Resp resp) {
        super.onPostExecute(resp);
        activity.dismissPB();
        if (resp.code == HttpURLConnection.HTTP_OK) {
            if (type == CreateProfileType.CREATE) {
                PublicUtil.toast( "User Create Successful");
            } else {
                activity.setBackResult(reqParam);
                PublicUtil.toast("User Update Successful");
            }
        } else {
            PublicUtil.toast(resp.msg);
        }
    }
}
