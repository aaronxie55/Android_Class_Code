package com.example.multinotes;

import androidx.annotation.NonNull;

public class textinfo {
    private String title;
    private String info;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInfo() {
        return info;
    }

    public String getTitle() {
        return title;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NonNull
    @Override
    public String toString() {
        return title + " : " + info + " : " + date;
    }
}
