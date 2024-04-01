package com.example.androidprojecttemplate.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidprojecttemplate.viewModels.CreateAccountViewModel;
import com.example.androidprojecttemplate.R;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText usernameInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;

    private EditText nameInput;

    private Button toHomeScreen;
    private Button toWelcomePage;

    private CreateAccountViewModel viewModel;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        viewModel = CreateAccountViewModel.getInstance();

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        toHomeScreen = findViewById(R.id.toHomeScreen);
        nameInput = findViewById(R.id.theName);
        toWelcomePage = findViewById(R.id.backButton);

        toHomeScreen.setOnClickListener(v -> {
            String username = String.valueOf(usernameInput.getText());
            String password = String.valueOf(passwordInput.getText());
            String confirmPassword = String.valueOf(confirmPasswordInput.getText());
            String name = String.valueOf(nameInput.getText());

            // Check if anything is empty
            if (TextUtils.isEmpty(username)) {
                Toast.makeText(CreateAccountActivity.this,
                        "Please enter an username!",
                        Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(CreateAccountActivity.this,
                        "Please enter a password!",
                        Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(CreateAccountActivity.this,
                        "Please enter a confirm password!",
                        Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(name)) {
                Toast.makeText(CreateAccountActivity.this,
                        "Please enter a name!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            int theResult = viewModel.toLoginScreenFromCreate(username, password,
                    confirmPassword, name);

            if (theResult == 1) {
                Toast.makeText(CreateAccountActivity.this,
                        "Passwords do not match!",
                        Toast.LENGTH_SHORT).show();
            } else if (theResult == 2) {
                Toast.makeText(CreateAccountActivity.this,
                        "Not a valid email",
                        Toast.LENGTH_SHORT).show();
            } else if (theResult == 3) {
                Toast.makeText(CreateAccountActivity.this,
                        "Passwords isn't long enough",
                        Toast.LENGTH_SHORT).show();
            } else if (theResult == 4) {
                Toast.makeText(CreateAccountActivity.this,
                        "No spaces allowed",
                        Toast.LENGTH_SHORT).show();
            } else if (theResult == 5) {
                Toast.makeText(CreateAccountActivity.this,
                        "Your account has been created, please login",
                        Toast.LENGTH_SHORT).show();
                // switch to home page
                Intent theIntent = new Intent(CreateAccountActivity.this, LoginPageActivity.class);
                startActivity(theIntent);
                //finish();
            } else if (theResult == 6) {
                Toast.makeText(CreateAccountActivity.this,
                        "An email has already been registered, please login",
                        Toast.LENGTH_SHORT).show();
            }
        });

        toWelcomePage.setOnClickListener(f -> {
            Intent intent = new Intent(CreateAccountActivity.this, WelcomeScreenActivity.class);
            startActivity(intent);
        });
    }
}
