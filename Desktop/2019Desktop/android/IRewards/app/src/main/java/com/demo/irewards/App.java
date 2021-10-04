package com.demo.irewards;

import android.app.Application;

import com.demo.irewards.util.PublicUtil;

public class App extends Application {

    public static final String studentId = "20325450";

    @Override
    public void onCreate() {
        super.onCreate();

        PublicUtil.init(this);
    }
}
