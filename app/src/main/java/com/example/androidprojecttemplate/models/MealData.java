package com.example.androidprojecttemplate.models;

public class MealData {
    private int calories;
    private String username;
    private String date;

    public int getCalories() { return this.calories; }
    public String getUsername() { return this.username; }
    public String getDate() { return this.date; }
    
    public void setCalories(int calories) { this.calories = calories; }
    public void setUsername(String username) { this.username = username; }
    public void setDate(String date) { this.date = date; }
}
