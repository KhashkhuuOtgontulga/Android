package com.example.teamball;

import java.io.Serializable;
import java.util.ArrayList;

public class UserProfile implements Serializable {
    private String username;
    private String password;
    private String first_name;
    private String last_name;
    private int points_to_award;
    private String department;
    private String story;
    private String position;
    private boolean administrator_flag;
    private String location;
    private int points_awarded;
    private String image;
    private ArrayList<Reward> rewards;

    public UserProfile(String first_name, String last_name, String username, String password, String location, boolean administrator_flag, int points_awarded, String department, String position, int points_to_award, String story, String image, ArrayList<Reward> rewards) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.password = password;
        this.location = location;
        this.administrator_flag = administrator_flag;
        this.points_awarded = points_awarded;
        this.department = department;
        this.position = position;
        this.points_to_award = points_to_award;
        this.story = story;
        this.image = image;
        this.rewards = rewards;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public boolean isAdministrator_flag() {
        return administrator_flag;
    }

    public int getPoints_awarded() {
        return points_awarded;
    }

    public void setPoints_awarded(int points_awarded) {
        this.points_awarded = points_awarded;
    }

    public String getDepartment() {
        return department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getPoints_to_award() {
        return points_to_award;
    }

    public void setPoints_to_award(int points_to_award) {
        this.points_to_award = points_to_award;
    }

    public String getStory() {
        return story;
    }

    public ArrayList<Reward> getRewards() {
        return rewards;
    }
}



