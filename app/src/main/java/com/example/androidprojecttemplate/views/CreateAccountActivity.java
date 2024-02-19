package com.example.androidprojecttemplate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class createAccountActivity extends AppCompatActivity {
    EditText theUsername;
    EditText thePassword;
    EditText confirmThePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // DO NOT MODIFY
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        theUsername = findViewById(R.id.usernameCreate);
        thePassword = findViewById(R.id.passwordCreate);
        confirmThePassword = findViewById(R.id.confirmPasswordCreate);

    }


}