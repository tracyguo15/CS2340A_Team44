package com.example.androidprojecttemplate.viewModel;

//import android.text.TextUtils;
import com.example.androidprojecttemplate.models.FirebaseDB;

import androidx.annotation.NonNull;

import com.example.androidprojecttemplate.views.LoginPageActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPageViewModel {
    private static LoginPageViewModel instance;
    private final LoginPageActivity data;

    private static int temp = 0;

    // Below is for the login information. It uses singleton design principle
    // since only one call to get the reference from the actual database is made.
    // The variable is just used as a placeholder for that actual reference,
    // but the reference is made only once
    private FirebaseAuth firebaseAuth;

    public LoginPageViewModel() {
        data = new LoginPageActivity();
        firebaseAuth = FirebaseDB.getInstance().getFirebaseAuth();
    }

    public static synchronized LoginPageViewModel getInstance() {
        if (instance == null) {
            instance = new LoginPageViewModel();
        }

        return instance;
    }

    public int toHomeScreenMethodFromLogin(String username, String password) {
        // Can now login the user through firebase
        firebaseAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            temp = 1;
                        } else {
                            temp = 2;
                        }
                    }
                });

        return temp;
    }
}
