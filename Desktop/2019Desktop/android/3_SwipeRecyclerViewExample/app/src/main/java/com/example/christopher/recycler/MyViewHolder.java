package com.example.christopher.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Christopher on 1/30/2017.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
    TextView empId;
    TextView department;

    MyViewHolder(View view) {
        super(view);
        name = view.findViewById(R.id.name);
        empId = view.findViewById(R.id.empId);
        department = view.findViewById(R.id.department);
    }

}
