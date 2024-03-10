package com.example.androidprojecttemplate.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidprojecttemplate.models.UserLoginData;
import com.example.androidprojecttemplate.views.HomePage;
import com.example.androidprojecttemplate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText usernameInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;

    private EditText nameInput;

    private Button toHomeScreen;


    // For authentication
    FirebaseAuth firebaseAuth;
    private String userId;

    // For real-time database
    DatabaseReference reference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        toHomeScreen = findViewById(R.id.toHomeScreen);
        nameInput = findViewById(R.id.theName);

        firebaseAuth = FirebaseAuth.getInstance();

        toHomeScreen.setOnClickListener(v -> {
            String username = String.valueOf(usernameInput.getText());
            String password = String.valueOf(passwordInput.getText());
            String confirmPassword = String.valueOf(confirmPasswordInput.getText());
            String name = String.valueOf(nameInput.getText());

            // check validity of username and password
            if (TextUtils.isEmpty(username)) {
                Toast.makeText(CreateAccountActivity.this, "Please enter an username!", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(CreateAccountActivity.this, "Please enter a password!", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(name)) {
                Toast.makeText(CreateAccountActivity.this, "Please enter a name!", Toast.LENGTH_SHORT).show();
                return;
            }

            // check that password and password confirmation match
            if (!TextUtils.equals(password, confirmPassword)) {
                Toast.makeText(CreateAccountActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                return;
            }

            // create user with firebase
            firebaseAuth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        
                        // login with firebase
                        firebaseAuth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // update user id
                                    // save user UID
                                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                                    userId = currentUser.getUid();

                                    Toast.makeText(CreateAccountActivity.this, "successful", Toast.LENGTH_SHORT).show();

                                    // Add entires to the real-time database
                                    reference = FirebaseDatabase.getInstance().getReference().child("Users");

                                    UserLoginData theUser = new UserLoginData(username, password, name);

                                    // Will track different usernames in the database by taking the first letter of their username (since you can't use the full username since it has special characters)
                                    reference.child(name).setValue(theUser);

                                    // switch to home page
                                    Intent intent = new Intent(CreateAccountActivity.this, HomePage.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(CreateAccountActivity.this, "user login failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(CreateAccountActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("USER_ID", userId);
    }
}
