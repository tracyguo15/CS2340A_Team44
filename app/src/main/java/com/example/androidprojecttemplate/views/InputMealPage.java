package com.example.androidprojecttemplate.views;

// Do not import android support because we are using androidx
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
import com.example.androidprojecttemplate.viewModels.UserDataViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.androidprojecttemplate.R;

import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;


// implements NavigationView.OnNavigationItemSelectedListener
public class InputMealPage extends AppCompatActivity {
    // ui
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private TextView userHeight;
    private TextView userWeight;
    private TextView userGender;
    private TextView userCalorieGoal;
    private TextView userDailyCalorieIntake;
    private EditText mealInput;
    private EditText calorieInput;
    private Button submitMealData;
    private NavigationView nav_view;

    // state
    private String userId;
    private static boolean isLoggedIn = false;
    private UserDataViewModel viewModel;
    FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_meal_page);

        // ui
        userHeight = findViewById(R.id.userHeight);
        userWeight = findViewById(R.id.userWeight);
        userGender = findViewById(R.id.userGender);
        userCalorieGoal = findViewById(R.id.userCalorieGoal);
        userDailyCalorieIntake = findViewById(R.id.userDailyCalorieIntake);
        mealInput = findViewById(R.id.mealInput);
        calorieInput = findViewById(R.id.calorieInput);
        submitMealData = findViewById(R.id.submitMealData);

        // navbar
        Toolbar homeToolBar = (Toolbar) findViewById(R.id.nav_toolbar);
        setSupportActionBar(homeToolBar);

        // nav menu
        dl = (DrawerLayout) findViewById(R.id.dl);
        abdt = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(abdt);
        abdt.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nav_view = (NavigationView) findViewById(R.id.nav_view);

        nav_view.setVisibility(View.GONE);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                System.out.println(R.id.inputmeal);
                int id = item.getItemId();
                if (id == R.id.inputmeal) {
                    if (!(InputMealPage.this instanceof InputMealPage)) {
                        Intent intent = new Intent(InputMealPage.this, InputMealPage.class);
                        startActivity(intent);
                    }
                    return true;
                } else if (id == R.id.recipe) {
                    Intent intent = new Intent(InputMealPage.this, RecipePage.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.ingredient) {
                    Intent intent = new Intent(InputMealPage.this, IngredientPage.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.list) {
                    Intent intent = new Intent(InputMealPage.this, ListPage.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.personalinfo) {
                    Intent intent = new Intent(InputMealPage.this, PersonalInfo.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        userId = savedInstanceState.getString("USER_ID");

        Log.d("YourTag", userId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (nav_view.getVisibility() == View.VISIBLE) {
            nav_view.setVisibility(View.GONE);
        } else {
            nav_view.setVisibility(View.VISIBLE);
        }
        return true || abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}
