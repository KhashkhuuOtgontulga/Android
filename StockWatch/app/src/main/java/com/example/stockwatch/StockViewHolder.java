package com.example.stockwatch;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StockViewHolder extends RecyclerView.ViewHolder{

    public TextView symbolField;
    public TextView companyNameField;
    public TextView latestPriceField;
    public TextView changeField;
    public TextView changePercentField;
    public TextView directionField;

    public StockViewHolder(@NonNull View itemView) {
        super(itemView);
        symbolField = itemView.findViewById(R.id.symbolField);
        companyNameField = itemView.findViewById(R.id.companyNameField);
        latestPriceField = itemView.findViewById(R.id.latestPriceField);
        changeField = itemView.findViewById(R.id.changeField);
        changePercentField = itemView.findViewById(R.id.changePercentField);
        directionField = itemView.findViewById(R.id.directionField);
    }
}
