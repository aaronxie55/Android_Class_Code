package com.demo.irewards.api;

import com.demo.irewards.entity.Errordetail;
import com.demo.irewards.entity.Resp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

public class APIRequester {

    private static volatile APIRequester instance = null;
    public static APIRequester getInstance() {
        if (instance == null) {
            synchronized (APIRequester.class) {
                if (instance == null) {
                    instance = new APIRequester();
                }
            }
        }
        return instance;
    }

    static String BASE_URL = "http://inspirationrewardsapi-env.6mmagpm2pv.us-east-2.elasticbeanstalk.com";

    Resp get(String path, Map<String, Object> params) {
        HttpURLConnection conn = null;
        try {
            String url = BASE_URL + path + "?";
            for (String key : params.keySet()) {
                url = url + key + "=" + params.get(key) + "&";
            }
            url = url.substring(0, url.length() - 1);

            URL httpUrl = new URL(url);
            conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                return new Resp(responseCode, getStringFromInputStream(is));
            } else {
                return new Resp(responseCode, getStringFromInputStream(conn.getErrorStream()));
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return new Resp(0, "error");
    }

    Resp put(String path, JSONObject params) {
        return request("PUT", path, params);
    }

    Resp post(String path, JSONObject params) {
        return request("POST", path, params);
    }

    Resp request(String method, String path, JSONObject params) {

        HttpURLConnection conn = null;
        try {
            URL httpUrl = new URL(BASE_URL + path);
            conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod(method);
            conn.setDoOutput(true);

            String postJson = params.toString();
            System.out.println("\n======= request start =======");
            System.out.println(postJson);
            System.out.println("=======  end  =======");

            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(postJson.getBytes());

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                String respJson = getStringFromInputStream(is);
                System.out.println("\n======= response start =======");
                System.out.println(respJson);
                System.out.println("=======  end  =======");
                return new Resp(responseCode, "success", respJson);
            } else {
                String errorJson = getStringFromInputStream(conn.getErrorStream());
                System.out.println("\n======= response start =======");
                System.out.println(errorJson);
                JSONObject jsonObject = new JSONObject(errorJson);
                JSONObject errordetail = jsonObject.getJSONObject("errordetails");
                String message = errordetail.getString("message");
                System.out.println("=======  end  =======");
                return new Resp(responseCode, message, errorJson);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return new Resp(0, "error");
    }

    private String getStringFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        is.close();
        String state = os.toString();
        os.close();
        return state;
    }
}
