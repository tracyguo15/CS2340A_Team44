package com.example.androidprojecttemplate.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidprojecttemplate.R;

public class Splash extends AppCompatActivity {

    private static int splashTime = 2000; // 2 seconds of splash

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this, StartScreen.class);
                startActivity(intent);
                finish();
            }
        }, splashTime);
    }
}
