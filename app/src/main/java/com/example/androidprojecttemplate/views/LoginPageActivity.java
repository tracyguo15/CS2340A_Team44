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
import com.example.androidprojecttemplate.viewmodels.LoginPageViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPageActivity extends AppCompatActivity {
    private EditText usernameInput;
    private EditText passwordInput;
    private Button toHomePage;

    private Button toCreateAccount;

    private LoginPageViewModel viewModel;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        viewModel = LoginPageViewModel.getInstance();

        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);
        toHomePage = findViewById(R.id.toHomeScreen);
        toCreateAccount = findViewById(R.id.clickToCreateAccount);

        firebaseAuth = FirebaseAuth.getInstance();

        toHomePage.setOnClickListener(v -> {
            String username = String.valueOf(usernameInput.getText());
            String password = String.valueOf(passwordInput.getText());

            int theResult = viewModel.toHomeScreenMethodFromLogin(username, password);
            if (theResult == 1) {
                Toast.makeText(LoginPageActivity.this, "Please enter an username!", Toast.LENGTH_SHORT).show();
            } else if (theResult == 2) {
                Toast.makeText(LoginPageActivity.this, "Please enter a password!", Toast.LENGTH_SHORT).show();
            } else if (theResult == 3) {
                Toast.makeText(LoginPageActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                // switch to home page
                Intent theIntent = new Intent(LoginPageActivity.this, HomePage.class);
                startActivity(theIntent);
                finish();
            } else if (theResult == 4) {
                Toast.makeText(LoginPageActivity.this, "The username or password is wrong", Toast.LENGTH_SHORT).show();
            }
        });

        toCreateAccount.setOnClickListener(v -> {
            Intent theIntent = new Intent(LoginPageActivity.this, CreateAccountActivity.class);
            startActivity(theIntent);
        });
    }
}
