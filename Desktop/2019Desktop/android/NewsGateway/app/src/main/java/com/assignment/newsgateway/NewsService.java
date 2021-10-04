package com.assignment.newsgateway;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.assignment.newsgateway.api.Fetcher;
import com.assignment.newsgateway.bean.Article;
import com.assignment.newsgateway.constant.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsService extends Service {

    private Context ctx;

    private Boolean running;

    private List<Article> articleList = new ArrayList<>();
    private BroadcastReceiver serviceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AppConstant.ACTION_MSG_TO_SERVICE.equals(intent.getAction())) {
                String sourceId = intent.getStringExtra("sourceId");
                System.out.println("NewsService receive from MainActivity. sourceId = " + sourceId);
                new NewsArticleDownloader().execute(sourceId);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        ctx = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstant.ACTION_MSG_TO_SERVICE);
        registerReceiver(serviceReceiver, filter);

        running = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    if (articleList.isEmpty()) {
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Intent actionIntent = new Intent();
                        actionIntent.setAction(AppConstant.ACTION_NEWS_STORY);
                        actionIntent.putExtra("articleList", (Serializable) articleList);
                        sendBroadcast(actionIntent);
                        articleList.clear();
                        running = false;
                    }
                }
            }
        }).start();

        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(serviceReceiver);
        running = false;
        super.onDestroy();
    }

    class NewsArticleDownloader extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            Map<String, Object> params = new HashMap<>();
            params.put("sources", strings[0]);
            params.put("language", "en");
            params.put("pageSize", 10);
            params.put("apiKey", AppConstant.API_KEY);
            return Fetcher.getInstance().get("everything", params);
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            try {
                JSONObject respJsonObject = new JSONObject(string);
                String status = respJsonObject.getString("status");
                if ("ok".equals(status)) {
                    int totalResults = respJsonObject.getInt("totalResults");
                    JSONArray articleJsonArray = respJsonObject.getJSONArray("articles");
                    for (int i = 0; i < articleJsonArray.length(); i++) {
                        JSONObject object = articleJsonArray.getJSONObject(i);
                        Article article = new Article(object);
                        articleList.add(article);
                    }
                } else {
                    Toast.makeText(ctx, "Network error!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
