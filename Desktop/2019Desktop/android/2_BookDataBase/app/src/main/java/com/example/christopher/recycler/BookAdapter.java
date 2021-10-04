package com.example.christopher.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Locale;

public class BookAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private static final String TAG = "BookAdapter";
    private List<Book> bookList;
    private MainActivity mainAct;

    BookAdapter(List<Book> empList, MainActivity ma) {
        this.bookList = empList;
        mainAct = ma;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_list_entry, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthor());
        holder.isbn.setText(book.getIsbn());
        holder.pub_year.setText(
                String.format(Locale.getDefault(), "%s, %d",
                        book.getPublisher(), book.getYear()));
        holder.cost.setText(String.format(Locale.US, "$%.2f", book.getCost()));
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

}