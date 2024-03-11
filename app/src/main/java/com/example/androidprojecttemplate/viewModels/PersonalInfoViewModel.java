package com.example.androidprojecttemplate.viewModels;

import com.example.androidprojecttemplate.views.CreateAccountActivity;
import com.example.androidprojecttemplate.views.PersonalInfo;

public class PersonalInfoViewModel {
    private static PersonalInfoViewModel instance;

    final private PersonalInfo theData;

    public PersonalInfoViewModel() {
        theData = new PersonalInfo();
    }

    public static synchronized PersonalInfoViewModel getInstance() {
        if (instance == null) {
            instance = new PersonalInfoViewModel();
        }

        return instance;
    }
}
