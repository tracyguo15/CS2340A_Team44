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
import com.google.firebase.auth.FirebaseAuthException;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText usernameInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;
    private Button toHomeScreen;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        toHomeScreen = findViewById(R.id.toHomeScreen);

        firebaseAuth = FirebaseAuth.getInstance();

        toHomeScreen.setOnClickListener(v -> {
            String username = String.valueOf(usernameInput.getText());
            String password = String.valueOf(passwordInput.getText());
            String confirmPassword = String.valueOf(confirmPasswordInput.getText());

            // check validity of username and password
            if (TextUtils.isEmpty(username)) {
                Toast.makeText(CreateAccountActivity.this, "Please enter an username!", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(CreateAccountActivity.this, "Please enter a password!", Toast.LENGTH_SHORT).show();
                return;
            }

            // check that password and password confirmation match
            if (!TextUtils.equals(password, confirmPassword)) {
                Toast.makeText(CreateAccountActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                return;
            }

            // check that the username has an "@"
            if (!username.contains("@")) {
                Toast.makeText(CreateAccountActivity.this, "Not a valid email", Toast.LENGTH_SHORT).show();
                return;
            }

            // check that the password is six or more characters
            if (password.length() < 6) {
                Toast.makeText(CreateAccountActivity.this, "Passwords isn't long enough", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if there are spaces within the username or password
            if (username.contains(" ") || password.contains(" ")) {
                Toast.makeText(CreateAccountActivity.this, "No spaces allowed", Toast.LENGTH_SHORT).show();
                return;
            }

                // create user with firebase
                firebaseAuth.createUserWithEmailAndPassword(username, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(CreateAccountActivity.this, "successful", Toast.LENGTH_SHORT).show();
                                    // switch to home page
                                    Intent theIntent = new Intent(CreateAccountActivity.this, LoginPageActivity.class);
                                    startActivity(theIntent);
                                    //finish();
                                } else {
                                    Toast.makeText(CreateAccountActivity.this, "An email has already been registered, please login", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
        });
    }
}
