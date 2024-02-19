package com.example.androidprojecttemplate.viewmodels;

public class LoginViewModel {
    private static LoginViewModel instance;
    final private UserData userData;

    public LoginViewModel() {
        userData = new UserData();
        //this.updateData(0,0);
    }

    public static synchronized LoginViewModel getInstance() {
        if (instance == null) {
            instance = new LoginViewModel();
        }
        return instance;
    }

    public User getUserData() {
        return userData;
    }


    public void updateData() {
        //wellnessData.setSleepHours(sleep);
        //wellnessData.setFitnessMinutes(fitness);
    }
}

