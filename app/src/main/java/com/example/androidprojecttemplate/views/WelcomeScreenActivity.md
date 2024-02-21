package com.example.androidprojecttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class welcomeScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // DO NOT MODIFY
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
        Button goToLogin = findViewById(R.id.goToLoginScreen);
        Button goToCreateAccountScreen = findViewById(R.id.goToCreateAccount);

        // The button that goes to the login page
        goToLogin.setOnClickListener(v -> {
            Intent theIntent = new Intent(welcomeScreenActivity.this, loginPageActivity.class);
            startActivity(theIntent);
        });

        // The button that goes to the create account page
        goToCreateAccountScreen.setOnClickListener(v -> {
            Intent theIntent = new Intent(welcomeScreenActivity.this, createAccountActivity.class);
            startActivity(theIntent);
        });
    }
}