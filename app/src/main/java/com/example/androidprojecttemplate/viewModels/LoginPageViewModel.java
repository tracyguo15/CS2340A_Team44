package com.example.androidprojecttemplate.viewModels;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.example.androidprojecttemplate.views.LoginPageActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPageViewModel {
    private static LoginPageViewModel instance;
    final private LoginPageActivity theData;

    private static int temp = 0;

    // Below is for the login information. It uses singleton design principle
    // since only one call to get the reference from the actual database is made.
    // The variable is just used as a placeholder for that actual reference,
    // but the reference is made only once
    FirebaseAuth theAuthenticationVariable;

    public LoginPageViewModel() {
        theData = new LoginPageActivity();
        theAuthenticationVariable = firebaseAuthSingleton.getInstance().getTheInstanceFromFirebase();
    }

    public static synchronized LoginPageViewModel getInstance() {
        if (instance == null) {
            instance = new LoginPageViewModel();
        }

        return instance;
    }

    public int toHomeScreenMethodFromLogin(String username, String password) {
        // Can now login the user through firebase
        theAuthenticationVariable.signInWithEmailAndPassword(username, password)
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
