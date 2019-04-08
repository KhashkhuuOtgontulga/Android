package com.example.rewards;

import java.io.Serializable;

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

    public UserProfile(String first_name, String last_name, String username, String password, String location, boolean administrator_flag, int points_awarded, String department, String position, int points_to_award, String story) {
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
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
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

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isAdministrator_flag() {
        return administrator_flag;
    }

    public void setAdministrator_flag(boolean administrator_flag) {
        this.administrator_flag = administrator_flag;
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

    public void setDepartment(String department) {
        this.department = department;
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

    public void setStory(String story) {
        this.story = story;
    }
}



