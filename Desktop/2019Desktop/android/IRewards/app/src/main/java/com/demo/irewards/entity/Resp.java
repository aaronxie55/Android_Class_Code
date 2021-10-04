package com.demo.irewards.entity;

import java.util.HashMap;
import java.util.Map;

public class Resp {
    public int code;
    public String msg;
    public String json;


    public Resp(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Resp(int code, String msg, String json) {
        this.code = code;
        this.msg = msg;
        this.json = json;
    }
}
