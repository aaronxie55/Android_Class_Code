package com.example.stockwatch;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class StocksAdapter extends RecyclerView.Adapter<MyViewHolder>{

        private List<Stock> stockList;
        private MainActivity mainAct;

        StocksAdapter(List<Stock> stockList, MainActivity ma){
            this.stockList = stockList;
            mainAct=ma;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stock_list_row,viewGroup,false);
            view.setOnClickListener(mainAct);
            view.setOnLongClickListener(mainAct);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
            Stock stock = stockList.get(i);
            if (stock.getPriceChange() < 0){
                holder.Company.setTextColor(Color.RED);
                holder.Symbol.setTextColor(Color.RED);
                holder.price.setTextColor(Color.RED);
                holder.priceChange.setTextColor(Color.RED);
                holder.percentage.setTextColor(Color.RED);
                holder.arrow.setImageResource(R.drawable.ic_arrow_down);
                holder.arrow.setColorFilter(Color.RED);
            }
            else {
                holder.Company.setTextColor(Color.GREEN);
                holder.Symbol.setTextColor(Color.GREEN);
                holder.price.setTextColor(Color.GREEN);
                holder.priceChange.setTextColor(Color.GREEN);
                holder.percentage.setTextColor(Color.GREEN);
                holder.arrow.setImageResource(R.drawable.ic_arrow_up);
                holder.arrow.setColorFilter(Color.GREEN);
            }
            holder.Company.setText(stock.getCompany());
            holder.Symbol.setText(stock.getSymbol());
            holder.price.setText(String.format(Locale.US, "%.2f", stock.getPrice()));
            holder.priceChange.setText(String.format(Locale.US, "%.2f", stock.getPriceChange()));
            holder.percentage.setText(String.format(Locale.US, "(%.2f%%)", stock.getPercentage()));
        }

        @Override
        public int getItemCount() {
            return stockList.size();
        }

}


