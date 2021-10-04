package com.demo.irewards.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.irewards.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PublicUtil {

    public static Context CONTEXT;

    public static void init(Context context) {
        CONTEXT = context;
    }

    public static void toast(String msg) {
        View view = LayoutInflater.from(CONTEXT).inflate(R.layout.layout_toast, null);
        TextView textView = view.findViewById(R.id.toast_content);
        textView.setText(msg);
        Toast toast = new Toast(CONTEXT);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    public static String getString(String key) {
        SharedPreferences sp = CONTEXT.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString(key, null);
    }
    public static boolean getBoolean(String key) {
        SharedPreferences sp = CONTEXT.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("YY/MM/DD");
        return sdf.format(new Date());
    }

    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = getImageDataWithBase64(base64Data);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    public static byte[] getImageDataWithBase64(String base64Data) {
        byte[] data;
        if (TextUtils.isEmpty(base64Data)) {
            return null;
        } else if (base64Data.startsWith("data:image")) {
            data = android.util.Base64.decode(base64Data.split(",")[1], Base64.DEFAULT);
        } else {
            data = android.util.Base64.decode(base64Data, Base64.DEFAULT);
        }
        return data;
    }
}
