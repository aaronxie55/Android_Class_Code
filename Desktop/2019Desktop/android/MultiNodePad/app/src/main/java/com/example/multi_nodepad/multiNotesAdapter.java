package com.example.multi_nodepad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import java.util.Date;
import java.util.List;


public class multiNotesAdapter extends RecyclerView.Adapter<myViewHolder> {

    //private static final String TAG = "multiNotesAdapter";
    private List<multi_Notes> multi_notesList;
    private MainActivity mainAct;

    public multiNotesAdapter(List<multi_Notes> empList, MainActivity ma) {
        this.multi_notesList = empList;
        this.mainAct = ma;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
       // Log.d(TAG, "onCreateViewHolder: MAKING NEW MyViewHolder");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_my_view_holder, parent, false);

       itemView.setOnClickListener(mainAct);
       itemView.setOnLongClickListener(mainAct);

        return new myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        //Log.d(TAG, "onBindViewHolder: FILLING VIEW HOLDER Employee " + position);

        multi_Notes multi_Notes = multi_notesList.get(position);

        holder.title.setText(multi_Notes.getTitle());
        holder.dateTime.setText(new Date().toString());
        holder.text.setText(multi_Notes.getText());

    }


    @Override
    public int getItemCount() {
        return multi_notesList.size();
    }
}