package com.example.androidprojecttemplate;

import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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

        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);
        confirmPasswordInput = findViewById(R.id.confirmPassword);
        toHomeScreen = findViewById(R.id.toHomeScreen);

        firebaseAuth = FirebaseAuth.getInstance();

        toHomeScreen.setOnClickListener(v -> {
            String username = String.valueOf(usernameInput.getText());
            String password = String.valueOf(passwordInput.getText());
            String confirmPassword = String.valueOf(confirmPasswordInput.getText());

            // check validity of username and password
            if (TextUtils.isEmpty(theActualUsername)) {
                Toast.makeText(LoginPageActivity.this, "Please enter an username!", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(theActualPassword)) {
                Toast.makeText(LoginPageActivity.this, "Please enter a password!", Toast.LENGTH_SHORT).show();
                return;
            }

            // check that password and password confirmation match
            if (!TextUtils.equals(password, confirmPassword)) {
                Toast.makeText(LoginPageActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(LoginPageActivity.this, "successful", Toast.LENGTH_SHORT).show();
                                    // switch to home page
                                    Intent theIntent = new Intent(loginPageActivity.this, HomePage.class);
                                    startActivity(theIntent);
                                    finish();
                                } else {
                                    Toast.makeText(LoginPageActivity.this, "user login failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(LoginPageActivity.this, "user creation failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }
}
