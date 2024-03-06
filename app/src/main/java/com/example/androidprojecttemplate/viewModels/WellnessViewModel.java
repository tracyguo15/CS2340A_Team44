package com.example.androidprojecttemplate.viewmodels;

import com.example.androidprojecttemplate.models.WellnessData;

public class WellnessViewModel {
    private static WellnessViewModel instance;
    final private WellnessData wellnessData;



    // DO NOT MODIFY METHOD
    public WellnessViewModel() {
        wellnessData = new WellnessData();
        this.updateData(0,0);
    }

    // DO NOT MODIFY METHOD
    public static synchronized WellnessViewModel getInstance() {
        if (instance == null) {
            instance = new WellnessViewModel();
        }
        return instance;
    }

    // DO NOT MODIFY METHOD
    public WellnessData getWellnessData() {
        return wellnessData;
    }


    // TODO 2 : Implement updateData function using helpers in wellnessData to update model
    public void updateData(int sleep, int fitness) {
        wellnessData.setSleepHours(sleep);
        wellnessData.setFitnessMinutes(fitness);
    }
}

