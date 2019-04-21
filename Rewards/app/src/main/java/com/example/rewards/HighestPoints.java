package com.example.rewards;

import java.util.Comparator;

public class HighestPoints implements Comparator<Reward> {
    // Used for sorting in ascending order of points awarded
    public int compare(Reward a, Reward b) {
        return a.getAward_points() - (b.getAward_points());
    }
}
