package com.example.androidprojecttemplate;

import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class CreateAccountActivity extends AppCompatActivity {
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
