package com.example.teamball;

import java.io.Serializable;
import java.util.ArrayList;

public class UserProfile implements Serializable {
    private String first_name;
    private String last_name;
    private String location;
    private String username;
    private String password;
    private String image;
    private int rating;

    public UserProfile(String first_name, String last_name, String location, String username, String password, String image, int rating) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.location = location;
        this.username = username;
        this.password = password;
        this.image = image;
        this.rating = rating;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}



