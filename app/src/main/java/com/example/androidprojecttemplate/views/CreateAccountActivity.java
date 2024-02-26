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
import com.example.androidprojecttemplate.viewmodels.CreateAccountViewModel;
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

    private CreateAccountViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        viewModel = CreateAccountViewModel.getInstance();

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        toHomeScreen = findViewById(R.id.toHomeScreen);

        toHomeScreen.setOnClickListener(v -> {
            String username = String.valueOf(usernameInput.getText());
            String password = String.valueOf(passwordInput.getText());
            String confirmPassword = String.valueOf(confirmPasswordInput.getText());

            int theResult = viewModel.toLoginScreenFromCreate(username, password, confirmPassword);

            if (theResult == 1) {
                Toast.makeText(CreateAccountActivity.this, "Please enter an username!", Toast.LENGTH_SHORT).show();
            } else if (theResult == 2) {
                Toast.makeText(CreateAccountActivity.this, "Please enter a password!", Toast.LENGTH_SHORT).show();
            } else if (theResult == 3) {
                Toast.makeText(CreateAccountActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            }else if (theResult == 4) {
                Toast.makeText(CreateAccountActivity.this, "Not a valid email", Toast.LENGTH_SHORT).show();
            } else if (theResult == 5) {
                Toast.makeText(CreateAccountActivity.this, "Passwords isn't long enough", Toast.LENGTH_SHORT).show();
            } else if (theResult == 6) {
                Toast.makeText(CreateAccountActivity.this, "No spaces allowed", Toast.LENGTH_SHORT).show();
            } else if (theResult == 7) {
                Toast.makeText(CreateAccountActivity.this, "Please put your password in confirmPassword!", Toast.LENGTH_SHORT).show();
            } else if (theResult == 8) {
                Toast.makeText(CreateAccountActivity.this, "successful", Toast.LENGTH_SHORT).show();
                // switch to home page
                Intent theIntent = new Intent(CreateAccountActivity.this, LoginPageActivity.class);
                startActivity(theIntent);
                //finish();
            } else if (theResult == 9) {
                Toast.makeText(CreateAccountActivity.this, "An email has already been registered, please login", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
