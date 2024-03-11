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

    FirebaseAuth firebaseAuth;

    public LoginPageViewModel() {
        theData = new LoginPageActivity();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static synchronized LoginPageViewModel getInstance() {
        if (instance == null) {
            instance = new LoginPageViewModel();
        }

        return instance;
    }

    public int toHomeScreenMethodFromLogin(String username, String password) {
        // check validity of username and password
        if (TextUtils.isEmpty(username)) {
            temp = 1;
        } else if (TextUtils.isEmpty(password)) {
            temp = 2;
        }

        // Can now login the user through firebase
        firebaseAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            temp = 3;
                        } else {
                            temp = 4;
                        }
                    }
                });

        return temp;
    }
}
