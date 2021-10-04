package com.example.stockwatch;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class StockInternetDataLoader extends AsyncTask<Void,Void,String> {

    private MainActivity mainAct;
    private static final String DATAURL = "https://api.iextrading.com/1.0/ref-data/symbols";
    StockInternetDataLoader(MainActivity mainActivity) {

        this.mainAct =mainActivity;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        HashMap<String,String> hashMap = paresJSON(s);
        mainAct.setStockData(hashMap);
    }

    @Override
    protected String doInBackground(Void... voids) {
        Uri uri = Uri.parse(DATAURL);
        String urlToUse = uri.toString();
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line=br.readLine())!=null){
                sb.append(line).append('\n');
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private HashMap<String, String> paresJSON(String s) {
        HashMap<String,String> hashMap = new HashMap<>();
        try {
            JSONArray jArray = new JSONArray(s);
            for (int i = 0; i < jArray.length(); i++){
                JSONObject jsonObject = (JSONObject) jArray.get(i);
                hashMap.put(jsonObject.getString("symbol"),
                        jsonObject.getString("name"));
            }
            return hashMap;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
