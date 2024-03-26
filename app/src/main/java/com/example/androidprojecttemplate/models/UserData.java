package com.example.androidprojecttemplate.models;

public class UserData {
    private int height;         // inches
    private int weight;         // pounds
    private String gender;

    private int age;

    public int getHeight() {
        return this.height;
    }

    public int getWeight() {
        return this.weight;
    }

    public String getGender() {
        return this.gender;
    }

    public int getAge() {
        return this.age;
    }

    public void setHeight(int height) {
        this.height = height;
        if (height < 0) {
            throw new IllegalArgumentException("Height is negative");
        }
    }

    public void setWeight(int weight) {
        this.weight = weight;
        if (age < 0) {
            throw new IllegalArgumentException("Weight is negative");
        }
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
        if (age < 0) {
            throw new IllegalArgumentException("Age is negative");
        }
    }
}
