package com.example.androidprojecttemplate.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthSingleton {
    private static volatile FirebaseAuthSingleton instance;

    FirebaseAuth firebaseAuth;

    private FirebaseAuthSingleton() { };


    public static FirebaseAuthSingleton getInstance() {
        if (instance == null) {
            synchronized (FirebaseAuthSingleton.class) {
                if (instance == null) {
                    instance = new FirebaseAuthSingleton();
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
