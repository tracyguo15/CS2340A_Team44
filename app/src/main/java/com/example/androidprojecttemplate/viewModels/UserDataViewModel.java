package com.example.androidprojecttemplate.viewModels;

import com.example.androidprojecttemplate.models.UserData;
import com.example.androidprojecttemplate.models.UserData.Gender;

public class UserDataViewModel {
    private static UserDataViewModel instance;
    final private UserData userData;

    public UserDataViewModel() {
        this.userData = new UserData();
        //this.updateData(0, 0);      // should be updated with firebase
    }

    public static synchronized UserDataViewModel getInstance() {
        if (instance == null) {
            instance = new WellnessViewModel();
        }
        return instance;
    }

    public UserData getWellnessData() {
        return this.userData;
    }

    public void updateData(int height, int weight, Gender gender) {
        userData.setHeight(height);
        userData.setWeight(weight);
        userData.setGender(gender);
    }
}