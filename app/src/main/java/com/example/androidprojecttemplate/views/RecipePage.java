package com.example.androidprojecttemplate.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.androidprojecttemplate.R;

import com.google.android.material.navigation.NavigationView;

public class RecipePage extends AppCompatActivity {
    // navbar
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private NavigationView navView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // navbar
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_page);

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
                    Intent intent = new Intent(RecipePage.this, IngredientPage.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.recipe) {
                    if (!(RecipePage.this instanceof RecipePage)) {
                        Intent intent = new Intent(RecipePage.this, RecipePage.class);
                        startActivity(intent);
                    }
                    return true;
                } else if (id == R.id.ingredient) {
                    Intent intent = new Intent(RecipePage.this, IngredientPage.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.list) {
                    Intent intent = new Intent(RecipePage.this, ListPage.class);
                    startActivity(intent);
                    return true;
                }

                return false;
            }
        })
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // navbar
        if (navView.getVisibility() == View.VISIBLE) {
            navView.setVisibility(View.GONE);
        } else {
            navView.setVisibility(View.VISIBLE);
        }
        return true;
    }
}
