package com.example.stockwatch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.changeField.setText(Double.toString(oneItem.getChange()));
        holder.changePercentField.setText("(" + Double.toString(oneItem.getChangePercent()) + "%)");
    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }
}
