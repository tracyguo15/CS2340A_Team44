package com.example.androidprojecttemplate.views;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;

import com.example.androidprojecttemplate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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


                //Experimenting with firebase
                databaseReference = FirebaseDatabase.getInstance().getReference("this is the path");

                //change value
                databaseReference.setValue("Hello this is a test")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(StartScreen.this,
                                            "success", Toast.LENGTH_SHORT).show();
                                    }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(StartScreen.this,
                                        e.getMessage().toString(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

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
