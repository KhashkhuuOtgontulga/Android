package com.example.rewards;

import java.io.Serializable;

public class Reward implements Serializable {
    private String date;
    private String name;
    private String comment;
    private int award_points;

    public Reward(String date, String name, String comment, int award_points) {
        this.date = date;
        this.name = name;
        this.comment = comment;
        this.award_points = award_points;
    }

    public String getDate() {
        return date;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getAward_points() {
        return award_points;
    }

    public void setAward_points(int award_points) {
        this.award_points = award_points;
    }
}
