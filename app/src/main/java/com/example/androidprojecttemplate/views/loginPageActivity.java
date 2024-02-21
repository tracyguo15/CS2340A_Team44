package com.example.androidprojecttemplate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class loginPageActivity extends AppCompatActivity {
private EditText theUsername;
private EditText thePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        theUsername = findViewById(R.id.usernameExisting);
        thePassword = findViewById(R.id.passwordExisting);

        Button theButtonToHomeScreen = findViewById(R.id.buttonToHomeScreenExisting);

    }

}

