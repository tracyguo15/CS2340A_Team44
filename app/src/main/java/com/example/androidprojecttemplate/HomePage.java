package com.example.androidprojecttemplate;

// Do not import android support because we are using androidx
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import com.google.android.material.navigation.NavigationView;

public class HomePage extends AppCompatActivity {
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private static boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Check if user is logged in before going on home page
        if (!isLoggedIn) {
            // If not logged in, stay on the login activity
            Intent intent = new Intent(this, loginPageActivity.class);
            startActivity(intent);
            // Finish the current activity to prevent going back to it when pressing back button
            finish();
            return;
        }
        dl = (DrawerLayout) findViewById(R.id.dl);
        abdt = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(abdt);
        abdt.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.inputmeal) {
                    Toast.makeText(HomePage.this, "InputMeal", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.recipe) {
                    Toast.makeText(HomePage.this, "Recipe", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.ingredient) {
                    Toast.makeText(HomePage.this, "Ingredient", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.shoppinglist) {
                    Toast.makeText(HomePage.this, "ShoppingList", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

//        Button toRecipePageButton = findViewById(R.id.RecipeButton);
//        toRecipePageButton.setOnClickListener(v -> {
//            Intent intent = new Intent(HomePage.this, RecipePage.class);
//            startActivity(intent);
//        });
//
//        Button toInputMealPageButton = findViewById(R.id.InputMealButton);
//        toInputMealPageButton.setOnClickListener(v -> {
//            Intent intent = new Intent(HomePage.this, InputMealPage.class);
//            startActivity(intent);
//        });
//
//        Button toIngredientPageButton = findViewById(R.id.IngredientButton);
//        toIngredientPageButton.setOnClickListener(v -> {
//            Intent intent = new Intent(HomePage.this, IngredientPage.class);
//            startActivity(intent);
//        });
//
//        Button toListPageButton = findViewById(R.id.ListButton);
//        toListPageButton.setOnClickListener(v -> {
//            Intent intent = new Intent(HomePage.this, ListPage.class);
//            startActivity(intent);
//        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    // Method to set the login state. remember to do this after login page is implemented
    public static void setLoggedIn(boolean value) {
        isLoggedIn = value;
    }
}
