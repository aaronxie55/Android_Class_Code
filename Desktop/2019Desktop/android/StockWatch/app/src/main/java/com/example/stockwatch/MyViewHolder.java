package com.example.stockwatch;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView Company;
    public TextView Symbol;
    public TextView price;
    public TextView priceChange;
    public TextView percentage;
    public ImageView arrow;

    public MyViewHolder(@NonNull View itemView) {

        super(itemView);
        Company = itemView.findViewById(R.id.companyView);
        Symbol = itemView.findViewById(R.id.symbolView);
        price = itemView.findViewById(R.id.priceView);
        priceChange = itemView.findViewById(R.id.pricechangeView);
        percentage = itemView.findViewById(R.id.percentageView);
        arrow = itemView.findViewById(R.id.arrowView);

    }

}
