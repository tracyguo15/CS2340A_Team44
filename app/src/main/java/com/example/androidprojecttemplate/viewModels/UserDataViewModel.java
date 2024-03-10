package com.example.androidprojecttemplate.viewModels;

import com.example.androidprojecttemplate.models.UserData;

public class UserDataViewModel {
    private static UserDataViewModel instance;
    final private UserData userData;

    public UserDataViewModel(int height, int weight, int gender) {
        this.userData = new UserData();
        this.updateData(height, weight, gender);
    }

    public static synchronized UserDataViewModel getInstance() {
        if (instance == null) {
            instance = new UserDataViewModel();
        }
        return instance;
    }

    public UserData getWellnessData() {
        return this.userData;
    }

    public void updateData(int height, int weight, String gender) {
        userData.setHeight(height);
        userData.setWeight(weight);
        userData.setGender(gender);
    }
}