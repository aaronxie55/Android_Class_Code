package com.example.stockwatch;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class StockFinancialDataLoader extends AsyncTask<String,Void,String> {

    private MainActivity mainAct;
    private static final String DATAURL_1 = "https://cloud.iexapis.com/stable/stock/";
    private static final String DATAURL_2 = "/quote?token=sk_0677f2b558c6492a9d570f1a4aa4576e";

    StockFinancialDataLoader(MainActivity ma) {
       this.mainAct = ma;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Stock stock = parseJSON(s);
        mainAct.setStockInfo(stock);
    }

    @Override
    protected String doInBackground(String... params) {
        String API_URL = DATAURL_1 + params[0] + DATAURL_2;
        Uri uri = Uri.parse(API_URL);
        String urlToUse = uri.toString();
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            while((line = br.readLine())!=null){
                sb.append(line).append('\n');
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private Stock parseJSON(String s) {
        Stock tStock = new Stock();
        try {
            JSONObject jObject = new JSONObject(s);
            tStock.setCompany(jObject.getString("companyName"));
            tStock.setSymbol(jObject.getString("symbol"));
            tStock.setPriceChange(jObject.getDouble("change"));
            tStock.setPercentage(jObject.getDouble("changePercent"));
            tStock.setPrice(jObject.getDouble("latestPrice"));

            return tStock;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
