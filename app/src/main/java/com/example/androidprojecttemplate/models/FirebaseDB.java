package com.example.androidprojecttemplate.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseDB {
    private static volatile FirebaseDB instance;

    private FirebaseDB() { };

    public static FirebaseDB getInstance() {
        if (instance == null) {
            synchronized (FirebaseDB.class) {
                if (instance == null) {
                    instance = new FirebaseDB();
                }
            }
        }

        return instance;
    }

    public FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    public FirebaseUser getUser() {
        return getFirebaseAuth().getCurrentUser();
    }

    public String getEmail() {
        return getUser().getEmail();
    }

    public String getUserId() {
        return getUser().getUid(); }
}
