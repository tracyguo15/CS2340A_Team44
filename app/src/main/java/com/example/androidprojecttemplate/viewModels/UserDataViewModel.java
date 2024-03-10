package com.example.androidprojecttemplate.viewmodels;

import com.example.androidprojecttemplate.models.UserData;

public class UserDataViewModel {
    private static UserDataViewModel instance;
    final private UserData userData;

    public UserDataViewModel() {
        this.userData = new UserData();
        //this.updateData(height, weight, gender);
    }

    public static synchronized UserDataViewModel getInstance() {
        if (instance == null) {
            instance = new UserDataViewModel();
        }
        return instance;
    }

    public UserData getUserData() {
        return this.userData;
    }

    public void updateData(int height, int weight, String gender, int age) {
        userData.setHeight(height);
        userData.setWeight(weight);
        userData.setGender(gender);
        userData.setAge(age);
    }

    public String heightText() {
        int feet = userData.getHeight() / 12;
        int remainingInches = userData.getHeight() % 12;
        
        String result;
        if (feet == 0) {
            result = remainingInches + "\"";
        } else if (remainingInches == 0) {
            result = feet + "\'";
        } else {
            result = feet + "\' " + remainingInches + "\"";
        }
        
        return result;
    }

    public String weightText() {
        return String.format("%d lbs.", userData.getWeight());
    }

    public String genderText() {
        if (userData.getGender().equals("male")) {
            return "Male";
        }

        return "Female";
    }

    public String calorieGoalText() {
        int height = (int)Math.round(userData.getHeight() * 2.54);       // in cm
        int weight = (int)Math.round(userData.getWeight() * 0.454);      // in kg
        int age = userData.getAge();

        int calorieGoal = (int)Math.round((10 * weight) + (6.25 * height) - (5 * age));

        switch (userData.getGender()) {
            case "male":
                calorieGoal += 5;
                break;
            case "female":
                calorieGoal -= 161;
                break;
            default:
                break;
        }

        return String.format("Calorie goal: %d", calorieGoal);
    }
}