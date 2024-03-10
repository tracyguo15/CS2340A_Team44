package com.example.androidprojecttemplate.viewModels;

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

    public void updateData(int height, int weight, String gender) {
        userData.setHeight(height);
        userData.setWeight(weight);
        userData.setGender(gender);
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
        if (userData.getGender() == "male") {
            return "Male";
        }

        return "Female";
    }
}