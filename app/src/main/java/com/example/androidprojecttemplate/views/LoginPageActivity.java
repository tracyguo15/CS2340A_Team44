package com.example.androidprojecttemplate;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPageActivity extends AppCompatActivity {
private EditText usernameInput;
private EditText passwordInput;

FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);
        Button toHomeScreen = findViewById(R.id.toHomeScreen);
        // Firebase authentication
        mAuth = FirebaseAuth.getInstance();

        // The button to login
        toHomeScreen.setOnClickListener(v -> {
            String theActualUsername;
            String theActualPassword;

            theActualUsername = String.valueOf(usernameInput.getText());
            theActualPassword = String.valueOf(passwordInput.getText());

            // Statement to check whether the username or password is empty or not
            if (TextUtils.isEmpty(theActualUsername)) {
                // Toast message to display to the user
                Toast.makeText(LoginPageActivity.this, "Please enter an username!", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(theActualPassword)) {
                Toast.makeText(LoginPageActivity.this, "Please enter a password!", Toast.LENGTH_SHORT).show();
                return;
            }


            // Code to login user from firebase
            mAuth.signInWithEmailAndPassword(theActualUsername, theActualPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginPageActivity.this, "Successfull OMG!", Toast.LENGTH_SHORT).show();
                                // Switch screen to home page
                                //Intent theIntent = new Intent(loginPageActivity.this, HomePage.class);
                                //startActivity(theIntent);
                            } else {
                                Toast.makeText(LoginPageActivity.this, "Terrible OMG!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        });

    }


}

