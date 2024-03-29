package com.example.androidprojecttemplate.views;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.androidprojecttemplate.R;
import com.example.androidprojecttemplate.viewModel.PantryPageViewModel;
import com.google.android.material.navigation.NavigationView;

public class PantryPage extends AppCompatActivity {

    private ActionBarDrawerToggle abdt;
    private DrawerLayout dl;
    private static boolean isLoggedIn = false;

    private NavigationView navView;

    private PantryPageViewModel viewmodel = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ingredient_pantry_page);

        // Check if user is logged in before going on home page
        if (isLoggedIn) {
            // If not logged in, stay on the login activity
            Intent intent = new Intent(this, LoginPageActivity.class);
            startActivity(intent);
            // Finish the current activity to prevent going back to it when pressing back button
            finish();
            return;
        }
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
                    //Toast.makeText(HomePage.this, "InputMeal", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PantryPage.this, InputMealPage.class);
                    startActivity(intent);
                } else if (id == R.id.recipe) {
                    //Toast.makeText(HomePage.this, "Recipe", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PantryPage.this, RecipePage.class);
                    startActivity(intent);
                } else if (id == R.id.ingredient) {
                    //Toast.makeText(HomePage.this, "Ingredient", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PantryPage.this, IngredientPage.class);
                    startActivity(intent);
                } else if (id == R.id.list) {
                    //Toast.makeText(HomePage.this, "ShoppingList", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PantryPage.this, ListPage.class);
                    startActivity(intent);
                } else if (id == R.id.personalinfo) {
                    Intent intent = new Intent(PantryPage.this, PersonalInfo.class);
                    startActivity(intent);
                }
                return true;
            }
        });

        viewmodel = new PantryPageViewModel();

        LinearLayout group = findViewById(R.id.pantry_layout_group);


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

    /*
    Method to set the login state. remember to set login after login page is implemented
    public static void setLoggedIn(boolean value) {
        isLoggedIn = value;
    }
     */
}