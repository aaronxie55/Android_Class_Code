package com.example.multinotes;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class viewholder extends RecyclerView.ViewHolder{

    public TextView name;
    public TextView time;
    public TextView message;

    public viewholder(View view) {
        super(view);
        name = view.findViewById(R.id.editTitle);
        time = view.findViewById(R.id.editText);
        message = view.findViewById(R.id.info_text);
    }



}
