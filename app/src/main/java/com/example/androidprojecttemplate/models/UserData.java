package com.example.androidprojecttemplate.models;

public class UserData {
    public enum Gender {
        MALE,
        FEMALE
    }

    private int height;         // inches
    private int weight;         // pounds
    private Gender gender;      

    public UserData(int height, int weight, Gender gender) {
        this.height = height;
        this.weight = weight;
        this.gender = gender;
    }

    public int getHeight() { return this.height; }
    public int getWeight() { return this.weight; }
    public Gender getGender() { return this.gender; }

    public void setHeight(int height) { this.height = height; }
    public void setWeight(int weight) { this.weight = weight; }
    public void setGender(Gender gender) { this.gender = gender; }
}
