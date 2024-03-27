package com.example.androidprojecttemplate.views;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.androidprojecttemplate.R;

import com.google.android.material.navigation.NavigationView;

public class RecipePage extends AppCompatActivity {
    // ui
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;

    private NavigationView navView;

    private LinearLayout ingredientContainer;
    private Button addIngredient;
    private final int MAX_INGREDIENTS = 10;
    private int ingredientCount;

    public void createIngredientRow() {
        // remove button
        Button removeButton = new Button(this);

        removeButton.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        // dynamically sets drawable attributes based on render
        removeButton.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                removeButton.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int cornerRadius = removeButton.getHeight() / 2;

                GradientDrawable drawable = new GradientDrawable();

                drawable.setShape(GradientDrawable.RECTANGLE);
                drawable.setCornerRadius(cornerRadius);

                drawable.setColor(Color.rgb(102, 80, 164)); // purple

                removeButton.setPadding(80, 4, 80, 4);
                removeButton.setBackground(drawable);
            }
        });

        removeButton.setAllCaps(false);
        removeButton.setTextColor(Color.WHITE);
        removeButton.setText("-");
        removeButton.setTextSize(14);

        // ingredient input
        EditText input = new EditText(this);

        input.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        // row layout
        LinearLayout row = new LinearLayout(this);
        row.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        row.setOrientation(LinearLayout.HORIZONTAL);

        // remove button functionality
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientContainer.removeView(row);
                ingredientCount--;
            }
        });

        // row composition
        row.addView(input);
        row.addView(removeButton);

        // ingredients container composition
        ingredientContainer.addView(row);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_page);

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

        navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setVisibility(View.GONE);
        navView.setNavigationItemSelectedListener(new NavigationView
                .OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                System.out.println(R.id.inputmeal);
                int id = item.getItemId();
                if (id == R.id.inputmeal) {
                    if (!(RecipePage.this instanceof RecipePage)) {
                        Intent intent = new Intent(RecipePage.this, InputMealPage.class);
                        startActivity(intent);
                    }
                    return true;
                } else if (id == R.id.recipe) {
                    Intent intent = new Intent(RecipePage.this, RecipePage.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.ingredient) {
                    Intent intent = new Intent(RecipePage.this, IngredientPage.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.list) {
                    Intent intent = new Intent(RecipePage.this, ListPage.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.personalinfo) {
                    Intent intent = new Intent(RecipePage.this, PersonalInfo.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        // page functionality
        ingredientContainer = findViewById(R.id.container);
        addIngredient = findViewById(R.id.addIngredientRow);

        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ingredientCount < MAX_INGREDIENTS) {
                    createIngredientRow();
                    ingredientCount++;
                } else {
                    Toast.makeText(RecipePage.this, "Max 10 ingredients", Toast.LENGTH_SHORT).show();
                }
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