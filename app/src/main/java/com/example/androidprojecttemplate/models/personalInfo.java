package com.example.androidprojecttemplate.models;

public class personalInfo {

    private String height;
    private String weight;
    private String gender;

    public personalInfo(String height, String weight, String gender) {
        this.height = height;
        this.weight = weight;
        this.gender = gender;
    }

    // getter and setter methods
    public String getHeight() {
        return height;
    }

    public void setHeight(String newHeight) {
        height = newHeight;
    }
    public String getWeight() {
        return weight;
    }

    public void setWeight(String newWeight) {
        weight = newWeight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String newGender) {
        gender = newGender;
    }
}
