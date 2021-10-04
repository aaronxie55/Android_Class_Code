package com.example.multi_nodepad;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

public class myViewHolder extends RecyclerView.ViewHolder {

   public TextView title;

   public TextView text;

   public TextView dateTime;


    public myViewHolder(View view) {

        super(view);
        title = view.findViewById(R.id.title);
        text = view.findViewById(R.id.text);
        dateTime = view.findViewById(R.id.dateTime);
    }



}

