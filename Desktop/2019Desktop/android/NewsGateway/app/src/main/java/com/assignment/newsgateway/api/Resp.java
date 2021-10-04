package com.assignment.newsgateway.api;

public class Resp {
    public int code;
    public String msg;
    public String data;


    public Resp(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Resp(int code, String msg, String data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
