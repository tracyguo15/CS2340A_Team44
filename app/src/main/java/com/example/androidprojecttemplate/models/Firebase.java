package com.example.androidprojecttemplate.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Firebase {
    private static volatile Firebase instance;

    FirebaseAuth firebaseAuth;

    private Firebase() { };

    public static Firebase getInstance() {
        if (instance == null) {
            synchronized (Firebase.class) {
                if (instance == null) {
                    instance = new Firebase();
                }
            }
        }

        return instance;
    }

    public FirebaseAuth getFirebaseInstance() {
        return FirebaseAuth.getInstance();
    }

    public FirebaseUser getUser() {
        return getFirebaseInstance().getCurrentUser();
    }

    public String getEmail() {
        return getUser().getEmail();
    }
}
