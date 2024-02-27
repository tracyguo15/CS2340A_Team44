package com.example.androidprojecttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

<<<<<<< Updated upstream:app/src/main/java/com/example/androidprojecttemplate/Splash.java
    private static int splash = 2000; // 2 seconds of splash
=======
    private static int splashTime = 2000; // 2 seconds of splash
>>>>>>> Stashed changes:app/src/main/java/com/example/androidprojecttemplate/views/Splash.java

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
<<<<<<< Updated upstream:app/src/main/java/com/example/androidprojecttemplate/Splash.java
        }, splash);
=======
        }, splashTime);
>>>>>>> Stashed changes:app/src/main/java/com/example/androidprojecttemplate/views/Splash.java
    }
}
