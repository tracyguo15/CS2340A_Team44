package com.example.sprint_1_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText theUsername;
    EditText thePassword;

    Button theButtonToHomeScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.the_login_screen);

         theUsername = findViewById(R.id.username);
         thePassword = findViewById(R.id.password);
         theButtonToHomeScreen = findViewById(R.id.buttonToHomeScreen);


         // Function to change the screens
        theButtonToHomeScreen.setOnClickListener(v -> {

        });
    }
}