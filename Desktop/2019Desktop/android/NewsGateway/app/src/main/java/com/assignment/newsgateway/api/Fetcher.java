package com.assignment.newsgateway.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

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

public class Fetcher {

    static String BASE_URL = "https://newsapi.org/v2/";
    private static volatile Fetcher instance = null;

    public static Fetcher getInstance() {
        if (instance == null) {
            synchronized (Fetcher.class) {
                if (instance == null) {
                    instance = new Fetcher();
                }
            }
        }
        return instance;
    }

    public String get(String path) {
        return get(path, null);
    }

    public String get(String path, Map<String, Object> params) {
        return request("GET", path, params, null);
    }

    public String post(String path, Map<String, Object> data) {
        return post(path, null, data);
    }

    public String post(String path, Map<String, Object> params, Map<String, Object> data) {
        return request("POST", path, params, data);
    }

    String request(String method, String path, Map<String, Object> params, Map<String, Object> data) {
        HttpURLConnection conn = null;
        String respStr = "{ \"status\": \"error\" }";
        try {
            String urlStr = BASE_URL + path + formatParams(params);

            System.out.println("\n>>>>>>>>>>>>>>>>> request start >>>>>>>>>>>>>>>>");
            System.out.println(urlStr);
            System.out.println("  >>>>>>>>>>>>>>>>>>>>> end >>>>>>>>>>>>>>>>>>>>>>");

            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");

            if (method.equals("POST")) {
                conn.setDoOutput(true);
                String jsonStr = new JSONObject(data).toString();
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(jsonStr.getBytes());
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                respStr = getStringFromInputStream(is);
                System.out.println("\n<<<<<<<<<<<<<<<< response start <<<<<<<<<<<<<<<<");
                System.out.println(respStr);
                System.out.println("  <<<<<<<<<<<<<<<<<<<<< end <<<<<<<<<<<<<<<<<<<<<<");
            } else {
                respStr = getStringFromInputStream(conn.getErrorStream());
                System.out.println(respStr);
                System.out.println("\n<<<<<<<<<<<<<<<< response start <<<<<<<<<<<<<<<<");
                System.out.println(respStr);
                System.out.println("  <<<<<<<<<<<<<<<<<<<<< end <<<<<<<<<<<<<<<<<<<<<<");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return respStr;
    }

    private String formatParams(Map<String, Object> params) {
        if (params == null) return "";
        String urlStr = "?";
        for (String key : params.keySet()) {
            urlStr = urlStr + key + "=" + params.get(key) + "&";
        }
        urlStr = urlStr.substring(0, urlStr.length() - 1);
        return urlStr;
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

    public Bitmap loadImage(String urlStr) {
        HttpURLConnection conn = null;
        Bitmap bitmap = null;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println(">>>>>>>>>>>>> loadImage: \n" + urlStr + "\n<<<<<<<<<<<<<");
            if (conn != null) {
                conn.disconnect();
            }
        }
        return bitmap;
    }

}
