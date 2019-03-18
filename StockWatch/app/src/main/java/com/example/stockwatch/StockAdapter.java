package com.example.stockwatch;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.ContentValues.TAG;

public class StockAdapter extends RecyclerView.Adapter<StockViewHolder> {

    private List<Stock> stockList;
    private MainActivity mainAct;

    public StockAdapter(List<Stock> stockList, MainActivity ma) {
        this.stockList = stockList;
        mainAct = ma;
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_list_entry, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new StockViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        Stock oneItem = stockList.get(position);
        holder.symbolField.setText(oneItem.getSymbol());
        holder.companyNameField.setText(oneItem.getCompanyName());
        holder.latestPriceField.setText(Double.toString(oneItem.getLatestPrice()));
        holder.changePercentField.setText("(" + Double.toString(oneItem.getChangePercent()) + "%)");

        if (oneItem.getChange() > 0) {
            // green color and up arrow
            // 0x65FF00
            holder.symbolField.setTextColor(Color.GREEN);
            holder.companyNameField.setTextColor(Color.GREEN);
            holder.latestPriceField.setTextColor(Color.GREEN);
            holder.changeField.setTextColor(Color.GREEN);
            holder.changePercentField.setTextColor(Color.GREEN);
            holder.changeField.setText("▲ " + Double.toString(oneItem.getChange()));

        }
        else {
            // red color and down arrow
            // 0xFF0000
            holder.symbolField.setTextColor(Color.RED);
            holder.companyNameField.setTextColor(Color.RED);
            holder.latestPriceField.setTextColor(Color.RED);
            holder.changeField.setTextColor(Color.RED);
            holder.changePercentField.setTextColor(Color.RED);
            holder.changeField.setText("▼ " + Double.toString(oneItem.getChange()));
        }
    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }
}
