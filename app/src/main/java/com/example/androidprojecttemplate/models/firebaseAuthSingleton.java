package com.example.androidprojecttemplate.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class firebaseAuthSingleton {
    private static volatile firebaseAuthSingleton uniqueInstance;

    FirebaseAuth firebaseAuth;

    private firebaseAuthSingleton() { };

    public static firebaseAuthSingleton getInstance() {
        if (uniqueInstance == null) {
            synchronized (firebaseAuthSingleton.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new firebaseAuthSingleton();
                }
            }
        }

        return uniqueInstance;
    }

    public FirebaseAuth getTheInstanceFromFirebase() {
        return FirebaseAuth.getInstance();
    }

    public FirebaseUser getUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public String getEmail() {
        return FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }

}
