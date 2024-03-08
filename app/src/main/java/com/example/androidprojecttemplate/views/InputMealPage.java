package com.example.androidprojecttemplate.views;

// Do not import android support because we are using androidx
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.androidprojecttemplate.R;

import com.google.android.material.navigation.NavigationView;


//implements NavigationView.OnNavigationItemSelectedListener
public class InputMealPage extends AppCompatActivity {
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private EditText mealInput;
    private EditText calorieInput;
    private static boolean isLoggedIn = false;

    private NavigationView nav_view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_input_meal_page);

        mealInput = findViewById(R.id.mealInput);
        calorieInput = findViewById(R.id.calorieInput);

        Toolbar homeToolBar = (Toolbar) findViewById(R.id.nav_toolbar);
        setSupportActionBar(homeToolBar);
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
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (nav_view.getVisibility() == View.VISIBLE) {
            nav_view.setVisibility(View.GONE);
        } else {
            nav_view.setVisibility(View.VISIBLE);
        }
        return true;
    }
}
