package com.example.rewards;

import java.util.Comparator;

public class SortByPoints implements Comparator<UserProfile>{
    // Used for sorting in ascending order of points awarded
    public int compare(UserProfile a, UserProfile b) {
        return a.getPoints_awarded() - (b.getPoints_awarded());
    }
}
