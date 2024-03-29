package com.example.androidprojecttemplate.views;

// Do not import android support because we are using androidx
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.androidprojecttemplate.R;
//import com.example.androidprojecttemplate.models.UserData;
import com.example.androidprojecttemplate.viewModel.PersonalInfoViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;


//implements NavigationView.OnNavigationItemSelectedListener
public class PersonalInfo extends AppCompatActivity {
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private static boolean isLoggedIn = false;

    private NavigationView navView;

    private EditText theHeightInput;

    private EditText theWeightInput;

    private EditText theGenderInput;
    private EditText theAgeInput;

    private Button theButtonToLogData;

    private PersonalInfoViewModel viewModel;

    // For firebase authentication (to get user's email)
    private FirebaseAuth auth;
    private FirebaseUser user;


    // For real-time database
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private DatabaseReference tempReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = PersonalInfoViewModel.getInstance();

        setContentView(R.layout.activity_personal_info_page);
        theHeightInput = findViewById(R.id.theHeight);
        theWeightInput = findViewById(R.id.theWeight);
        theGenderInput = findViewById(R.id.theGender);
        theAgeInput = findViewById(R.id.theAge);
        theButtonToLogData = findViewById(R.id.submitPersonalInfoData);

        Toolbar homeToolBar = (Toolbar) findViewById(R.id.nav_toolbar);
        setSupportActionBar(homeToolBar);
        dl = (DrawerLayout) findViewById(R.id.dl);
        abdt = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(abdt);
        abdt.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navView = (NavigationView) findViewById(R.id.navView);

        navView.setVisibility(View.GONE);
        navView.setNavigationItemSelectedListener(new NavigationView
                .OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.inputmeal) {
                    Intent intent = new Intent(PersonalInfo.this, InputMealPage.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.recipe) {
                    Intent intent = new Intent(PersonalInfo.this, RecipePage.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.ingredient) {
                    Intent intent = new Intent(PersonalInfo.this, IngredientPage.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.list) {
                    Intent intent = new Intent(PersonalInfo.this, ListPage.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.personalinfo) {
                    if (!(PersonalInfo.this instanceof PersonalInfo)) {
                        Intent intent = new Intent(PersonalInfo.this, PersonalInfo.class);
                        startActivity(intent);
                    }
                    return true;
                }
                return false;
            }
        });

        // Will now focus on logging the data
        theButtonToLogData.setOnClickListener(v -> {
            // Used to get the current user
            viewModel.getCurrentUser();

            // Check if the the inputs from the edit text are valid or not
            String height = String.valueOf(theHeightInput.getText());
            String weight = String.valueOf(theWeightInput.getText());
            String gender = String.valueOf(theGenderInput.getText());
            String age = String.valueOf(theAgeInput.getText());

            if (TextUtils.isEmpty(height)) {
                Toast.makeText(PersonalInfo.this,
                        "Please enter a height!",
                        Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(weight)) {
                Toast.makeText(PersonalInfo.this,
                        "Please enter a weight!",
                        Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(gender)) {
                Toast.makeText(PersonalInfo.this,
                        "Please enter a gender!",
                        Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(age)) {
                Toast.makeText(PersonalInfo.this,
                        "Please enter an age!",
                        Toast.LENGTH_SHORT).show();
                return;
            } else if (height.contains("-") || weight.contains("-")
                    || age.contains("-")) {
                throw new IllegalArgumentException("Argument contains -");
            }

            int theResult = viewModel.putTheDataIntoFirebase(height, weight, gender, age);

            if (theResult == 1) {
                Toast.makeText(PersonalInfo.this,
                        "Thank you, your information has been recorded",
                        Toast.LENGTH_SHORT).show();
            } else if (theResult == 2) {
                Toast.makeText(PersonalInfo.this,
                        "Something went wrong with firebase",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (navView.getVisibility() == View.VISIBLE) {
            navView.setVisibility(View.GONE);
        } else {
            navView.setVisibility(View.VISIBLE);
        }
        return true;
    }
}
