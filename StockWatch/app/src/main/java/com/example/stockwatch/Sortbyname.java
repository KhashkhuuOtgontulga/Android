package com.example.stockwatch;

import java.util.Comparator;

public class Sortbyname implements Comparator<Stock>
{
    // Used for sorting in ascending order of
    // roll name
    public int compare(Stock a, Stock b)
    {
        return a.getSymbol().compareTo(b.getSymbol());
    }
}
