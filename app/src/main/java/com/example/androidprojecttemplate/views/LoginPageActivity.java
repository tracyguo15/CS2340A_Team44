package com.example.androidprojecttemplate.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidprojecttemplate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPageActivity extends AppCompatActivity {
    private EditText usernameInput;
    private EditText passwordInput;
    private Button toHomeScreen;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);
        toHomeScreen = findViewById(R.id.toHomeScreen);

        firebaseAuth = FirebaseAuth.getInstance();

        toHomeScreen.setOnClickListener(v -> {
            String username = String.valueOf(usernameInput.getText());
            String password = String.valueOf(passwordInput.getText());

            // check validity of username and password
            if (TextUtils.isEmpty(username)) {
                Toast.makeText(LoginPageActivity.this, "Please enter an username!", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(LoginPageActivity.this, "Please enter a password!", Toast.LENGTH_SHORT).show();
                return;
            }

            // login with firebase
            firebaseAuth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginPageActivity.this, "Successfull OMG!", Toast.LENGTH_SHORT).show();
                        // switch to home page
                        Intent theIntent = new Intent(LoginPageActivity.this, HomePage.class);
                        startActivity(theIntent);
                        finish();
                    } else {
                        Toast.makeText(LoginPageActivity.this, "Terrible OMG!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }
}
