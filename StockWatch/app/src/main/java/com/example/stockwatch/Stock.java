package com.example.stockwatch;

import java.io.Serializable;
import java.util.Comparator;

public class Stock implements Serializable {
    private String symbol;
    private String companyName;
    private double latestPrice;
    private double change;
    private double changePercent;

    public Stock(String symbol, String companyName, double latestPrice, double change, double changePercent) {
        super();
        this.symbol = symbol;
        this.companyName = companyName;
        this.latestPrice = latestPrice;
        this.change = change;
        this.changePercent = changePercent;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public double getLatestPrice() {
        return latestPrice;
    }

    public void setLatestPrice(double latestPrice) {
        this.latestPrice = latestPrice;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(double changePercent) {
        this.changePercent = changePercent;
    }

    // need to implement equals method in the stock class
    // and not in main activity
    @Override
    public boolean equals(Object obj) {
        Stock other = (Stock)obj;
        return symbol.equals(other.getSymbol());
    }
}
