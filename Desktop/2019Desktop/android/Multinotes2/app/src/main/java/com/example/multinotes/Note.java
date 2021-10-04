package com.example.multinotes;

import java.util.Calendar;

public class Note {

    private String title;
    private String message;
    private static int ctr = 1;

    public Note(String title, String message) {
        this.title = title;
        this.message = message;

        ctr++;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static int getCtr() {
        return ctr;
    }

    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

