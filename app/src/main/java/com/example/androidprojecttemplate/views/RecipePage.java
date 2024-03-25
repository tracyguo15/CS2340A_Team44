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
    //private DrawerLayout dl;
    //private ActionBarDrawerToggle abdt;
    //private NavigationView navView;
    private Nav nav;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_page);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return true;
    }
}
