package com.example.androidprojecttemplate;

// Do not import android support because we are using androidx
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
//import android.widget.Toast;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
//import androidx.core.view.GravityCompat;
//import com.example.androidprojecttemplate.R;

import com.google.android.material.navigation.NavigationView;


//implements NavigationView.OnNavigationItemSelectedListener
public class IngredientPage extends AppCompatActivity {
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private static boolean isLoggedIn = false;

    private NavigationView navView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ingredient_page);

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
        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.inputmeal) {
                        Intent intent = new Intent(IngredientPage.this, InputMealPage.class);
                        startActivity(intent);
                        return true;
                    } else if (id == R.id.recipe) {
                        Intent intent = new Intent(IngredientPage.this, RecipePage.class);
                        startActivity(intent);
                        return true;
                    } else if (id == R.id.ingredient) {
                        if (!(IngredientPage.this instanceof IngredientPage)) {
                            Intent intent = new Intent(IngredientPage.this, IngredientPage.class);
                            startActivity(intent);
                        }
                        return true;
                    } else if (id == R.id.list) {
                        Intent intent = new Intent(IngredientPage.this, ListPage.class);
                        startActivity(intent);
                        return true;
                    }

                    return false;
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