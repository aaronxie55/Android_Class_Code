package com.example.christopher.recycler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity // NOTE the interfaces here!
        implements View.OnClickListener, View.OnLongClickListener {

    // The above lines are important - them make this class a listener
    // for click and long click events in the ViewHolders (in the recycler

    private final List<Employee> employeeList = new ArrayList<>();  // Main content is here

    private RecyclerView recyclerView; // Layout's recyclerview

    private EmployeesAdapter mAdapter; // Data to recyclerview adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);

        mAdapter = new EmployeesAdapter(employeeList, this);

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Make some data - not always needed - used to fill list
        for (int i = 0; i < 20; i++) {
            employeeList.add(new Employee());
        }
    }

    // From OnClickListener
    @Override
    public void onClick(View v) {  // click listener called by ViewHolder clicks

        int pos = recyclerView.getChildLayoutPosition(v);
        Employee m = employeeList.get(pos);

        Toast.makeText(v.getContext(), "SHORT " + m.toString(), Toast.LENGTH_SHORT).show();
    }

    // From OnLongClickListener
    @Override
    public boolean onLongClick(View v) {  // long click listener called by ViewHolder long clicks
        int pos = recyclerView.getChildLayoutPosition(v);
        Employee m = employeeList.get(pos);
        Toast.makeText(v.getContext(), "LONG " + m.toString(), Toast.LENGTH_SHORT).show();

        return false;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "The back button was pressed - Bye!", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    public void addTop(View v) {
        employeeList.add(0, new Employee());
        mAdapter.notifyDataSetChanged();
    }

    public void addEnd(View v) {
        employeeList.add(new Employee());
        mAdapter.notifyDataSetChanged();
    }

    public void removeTop(View v) {
        if (!employeeList.isEmpty()) {
            employeeList.remove(0);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void removeEnd(View v) {
        if (!employeeList.isEmpty()) {
            employeeList.remove(employeeList.size()-1);
            mAdapter.notifyDataSetChanged();
        }
    }
}