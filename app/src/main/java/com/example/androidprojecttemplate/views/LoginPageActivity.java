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
import com.example.androidprojecttemplate.viewModels.LoginPageViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPageActivity extends AppCompatActivity {
    private EditText usernameInput;
    private EditText passwordInput;
    private Button toHomeScreen;

    private LoginPageViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        viewModel = LoginPageViewModel.getInstance();

        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);
        toHomeScreen = findViewById(R.id.toHomeScreen);


        toHomeScreen.setOnClickListener(v -> {
            String username = String.valueOf(usernameInput.getText());
            String password = String.valueOf(passwordInput.getText());

            // Check if the strings are empty
            if (TextUtils.isEmpty(username)) {
                Toast.makeText(LoginPageActivity.this, "Please enter an username!", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(LoginPageActivity.this, "Please enter a password!", Toast.LENGTH_SHORT).show();
                return;
            }

            int theResult = viewModel.toHomeScreenMethodFromLogin(username, password);

            if  (theResult == 1) {
                Toast.makeText(LoginPageActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                // switch to home page
                Intent theIntent = new Intent(LoginPageActivity.this, HomePage.class);
                startActivity(theIntent);
                finish();
            } else if (theResult == 2) {
                Toast.makeText(LoginPageActivity.this, "The username or password is wrong", Toast.LENGTH_SHORT).show();
            }

        });
    }

    //setters and getters for username and password input used for testing
    public String getUsername() {
        return String.valueOf(usernameInput.getText());
    }

    public void setUsername(String username) {
        usernameInput.setText(username);
    }

    public String getPassword() {
        return String.valueOf(passwordInput.getText());
    }

    public void setPassword(String password) {
        passwordInput.setText(password);
    }
}
