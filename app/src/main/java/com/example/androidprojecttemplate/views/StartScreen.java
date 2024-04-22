package com.example.androidprojecttemplate.views;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import com.example.androidprojecttemplate.R;
import com.google.firebase.database.DatabaseReference;


public class StartScreen extends AppCompatActivity {

    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Button startButton = findViewById(R.id.startButton);
        Button quitButton = findViewById(R.id.quitButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // to start the app and go to the login screen
                Intent intent = new Intent(StartScreen.this, WelcomeScreenActivity.class);
                startActivity(intent);
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //quits the app when pressed
                finish();
            }
        });
    }
}
