package com.example.androidprojecttemplate;

// Do not import android support because we are using androidx
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;
import com.example.androidprojecttemplate.R;

import com.google.android.material.navigation.NavigationView;
import com.example.androidprojecttemplate.IngredientPage;
import com.example.androidprojecttemplate.InputMealPage;
import com.example.androidprojecttemplate.ListPage;
import com.example.androidprojecttemplate.RecipePage;


//implements NavigationView.OnNavigationItemSelectedListener
public class HomePage extends AppCompatActivity {
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private static boolean isLoggedIn = false;

    private NavigationView navView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Check if user is logged in before going on home page
        if (isLoggedIn) {
            // If not logged in, stay on the login activity
            Intent intent = new Intent(this, loginPageActivity.class);
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

        navView = (NavigationView) findViewById(R.id.nav_view);

        navView.setVisibility(View.GONE);
        navView.setNavigationItemSelectedListener(new
            NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.inputmeal) {
                    // Toast.makeText(HomePage.this, "InputMeal", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomePage.this, InputMealPage.class);
                    startActivity(intent);
                } else if (id == R.id.recipe) {
                    //Toast.makeText(HomePage.this, "Recipe", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomePage.this, RecipePage.class);
                    startActivity(intent);
                } else if (id == R.id.ingredient) {
                    //Toast.makeText(HomePage.this, "Ingredient", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomePage.this, IngredientPage.class);
                    startActivity(intent);
                } else if (id == R.id.list) {
                    // Toast.makeText(HomePage.this, "ShoppingList", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomePage.this, ListPage.class);
                    startActivity(intent);
                }

                return true;
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
        //|| abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    // Method to set the login state. remember to set login after login page is implemented
    public static void setLoggedIn(boolean value) {
        isLoggedIn = value;
    }
}