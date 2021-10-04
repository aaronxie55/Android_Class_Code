package com.example.multi_nodepad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.Serializable;

public class multi_Notes implements Serializable {

    private String title;
    private String text;
    private String time;
    private static int ctr = 1;

    public multi_Notes() {

    }


    public multi_Notes(String a,String b) {
        this.title = a;
        this.text = b;
       // this.title = "title"+ ctr;
        //this.empId = System.currentTimeMillis();
       // this.text = "text"+ ctr;
        ctr++;
    }

    public void  setTitle(String a){
        this.title= a ;
    }

    public void  setText(String b){
        this.text= b ;

    }
    public void  setTime(String c){
        this.time= c ;
    }

    public  String getTitle() {
        return title;
    }

    public  String getText() {
        return text;
    }

    public  String getTime() {
        return time;
    }


    public int count() {
        return ctr;
    }



}
