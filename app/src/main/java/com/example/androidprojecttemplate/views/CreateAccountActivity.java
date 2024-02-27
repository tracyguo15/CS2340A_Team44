package com.example.androidprojecttemplate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class createAccountActivity extends AppCompatActivity {
    EditText theUsername;
    EditText thePassword;
    EditText confirmThePassword;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // DO NOT MODIFY
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        theUsername = findViewById(R.id.usernameCreate);
        thePassword = findViewById(R.id.passwordCreate);
        confirmThePassword = findViewById(R.id.confirmPasswordCreate);
        Button theButtonToHomeScreen = findViewById(R.id.buttonToHomeScreenFromCreate);
        // Firebase initialization
        mAuth = FirebaseAuth.getInstance();

        // Button to create an account
        theButtonToHomeScreen.setOnClickListener(v -> {
            String theActualUsername;
            String theActualPassword;
            String theActualPasswordConfirmation;

            theActualUsername = String.valueOf(theUsername.getText());
            theActualPassword = String.valueOf(thePassword.getText());
            theActualPasswordConfirmation = String.valueOf(confirmThePassword.getText());

            // Checks whether any of the editTexts are empty or not
            if (TextUtils.isEmpty(theActualUsername)) {
                Toast.makeText(createAccountActivity.this, "Please enter a username!", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(theActualPassword)) {
                Toast.makeText(createAccountActivity.this, "Please enter a password!", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(theActualPasswordConfirmation)) {
                Toast.makeText(createAccountActivity.this, "Please enter a password confirmation!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Checks to make sure that the password matches the confirmation password
            if (theActualPassword.compareTo(theActualPasswordConfirmation) != 0) {
                Toast.makeText(createAccountActivity.this, "Password and Password Confirmation don't match!", Toast.LENGTH_SHORT).show();
                return;
            }

            // check to make sure that the password is more than 6 characters
            if(theActualPassword.length() < 6) {
                Toast.makeText(createAccountActivity.this, "Password needs to be 6 or more letters", Toast.LENGTH_SHORT).show();
                return;
            }

            // check if email is valid
            if(!theActualUsername.contains("@")) {
                Toast.makeText(createAccountActivity.this, "Username has to be a valid email", Toast.LENGTH_SHORT).show();
                return;
            }

                // Now we will register the user using firebase
                mAuth.createUserWithEmailAndPassword(theActualUsername, theActualPassword)
                        .addOnCompleteListener(createAccountActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //Toast.makeText(createAccountActivity.this, "Successfull!", Toast.LENGTH_SHORT).show();
                                    // Switch to new screen
                                    Intent theIntent = new Intent(createAccountActivity.this, loginPageActivity.class);
                                    startActivity(theIntent);
                                    finish();
                                } else {
                                    Toast.makeText(createAccountActivity.this, "Not Good", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });
        });
    }

   /* @Override
    public void onStart() {
        super.onStart();
        FirebaseUser theUser = mAuth.getCurrentUser();
        if (theUser != null) {
            Intent theIntent = new Intent(createAccountActivity.this, HomePage.class);
            startActivity(theIntent);
        }
    } */
}
