package com.example.androidprojecttemplate.viewmodels;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.androidprojecttemplate.views.CreateAccountActivity;
import com.example.androidprojecttemplate.views.LoginPageActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountViewModel {
    private static CreateAccountViewModel instance;
    final private CreateAccountActivity theData;

    FirebaseAuth firebaseAuth;

    private static int temp = 0;

    public CreateAccountViewModel() {
        theData = new CreateAccountActivity();
        firebaseAuth = FirebaseAuth.getInstance();

    }

    public static synchronized CreateAccountViewModel getInstance() {
        if (instance == null) {
            instance = new CreateAccountViewModel();
        }

        return instance;
    }

    public int toLoginScreenFromCreate(String username, String password, String confirmPassword) {
        // check validity of username and password

        // Username is empty
        if (TextUtils.isEmpty(username)) {
            temp = 1;
        // Password is empty
        } else if (TextUtils.isEmpty(password)) {
            temp = 2;
        // password and confirmPassword don't equal
        } else if (!TextUtils.equals(password, confirmPassword)) {
            temp = 3;
        // username is not a valid email
        } else if (!username.contains("@") || !username.contains(".com")) {
            temp = 4;
        // password is less than length 6
        } else if (password.length() < 6) {
            temp = 5;
        // Inputs have spaces
        } else if (username.contains(" ") || password.contains(" ") || confirmPassword.contains(" ")) {
            temp = 6;
        // confirmPassword is empty
        } else if (TextUtils.isEmpty(confirmPassword)) {
            temp = 7;
        }

        // Checks are done, can now create the username and password

        // create user with firebase
        firebaseAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            temp = 8;
                        } else {
                            temp = 9;
                        }
                    }
                });

    return temp;

    }

}
