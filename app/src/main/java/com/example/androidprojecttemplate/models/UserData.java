package com.example.androidprojecttemplate.models;

public class UserData {
    private int height;         // inches
    private int weight;         // pounds
    private String gender;

    /*
    public UserData(int height, int weight, String gender) {
        this.height = height;
        this.weight = weight;
        this.gender = gender;
    }*/

    public int getHeight() { return this.height; }
    public int getWeight() { return this.weight; }
    public String getGender() { return this.gender; }

    public void setHeight(int height) { this.height = height; }
    public void setWeight(int weight) { this.weight = weight; }
    public void setGender(String gender) { this.gender = gender; }
}
