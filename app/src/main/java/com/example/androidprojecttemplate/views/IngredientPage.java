package com.example.androidprojecttemplate.views;

// Do not import android support because we are using androidx
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
//import android.widget.Toast;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
//import androidx.core.view.GravityCompat;
import com.example.androidprojecttemplate.R;

import com.example.androidprojecttemplate.viewModels.IngredientViewModel;
import com.google.android.material.navigation.NavigationView;


//implements NavigationView.OnNavigationItemSelectedListener
public class IngredientPage extends AppCompatActivity {
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private static boolean isLoggedIn = false;

    private NavigationView nav_view;

    private EditText ingredientName;
    private EditText quantity;
    private EditText calorieForIngredient;

    private EditText expirationDate;

    private Button addIngredientToFirebase;

    private IngredientViewModel viewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ingredient_page);

        viewModel = IngredientViewModel.getInstance();

        ingredientName = findViewById(R.id.nameOfIngredientInput);
        quantity = findViewById(R.id.quantityInput);
        calorieForIngredient = findViewById(R.id.ingredientCalorieInput);
        addIngredientToFirebase = findViewById(R.id.buttonToInputIngredient);
        expirationDate = findViewById(R.id.theExpirationDateInput);


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
        nav_view.setNavigationItemSelectedListener(new NavigationView
                .OnNavigationItemSelectedListener() {
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
                } else if (id == R.id.personalinfo) {
                    Intent intent = new Intent(IngredientPage.this, PersonalInfo.class);
                    startActivity(intent);
                    return true;
                }

                return false;
            }
        });


        addIngredientToFirebase.setOnClickListener(v -> {
            viewModel.getCurrentUser();

            String theName = String.valueOf(ingredientName.getText());
            String theQuantity = String.valueOf(quantity.getText());
            String theCalories = String.valueOf(calorieForIngredient.getText());
            String theExpirationDate = String.valueOf(expirationDate.getText());

            // Checks if any of the (necessary) strings are empty
            if (TextUtils.isEmpty(theName)) {
                Toast.makeText(IngredientPage.this,
                        "Please enter an ingredient name",
                        Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(theQuantity)) {
                Toast.makeText(IngredientPage.this,
                        "Please enter an ingredient name",
                        Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(theCalories)) {
                Toast.makeText(IngredientPage.this,
                        "Please enter an ingredient name",
                        Toast.LENGTH_SHORT).show();
            // Checks if expiration date is empty
            }  else if (TextUtils.isEmpty(theExpirationDate)) {
                theExpirationDate = "Not Avaliable";
            }

            int theResult = viewModel.addToFirebase(theName, theQuantity, theCalories, theExpirationDate);

            if (theResult == 1) {
                Toast.makeText(IngredientPage.this,
                        "Success",
                        Toast.LENGTH_SHORT).show();
            } else if (theResult == 2) {
                Toast.makeText(IngredientPage.this,
                        "Something went wrong",
                        Toast.LENGTH_SHORT).show();
            } else if (theResult == 3) {
                Toast.makeText(IngredientPage.this,
                        "The ingredient already exists, can't add",
                        Toast.LENGTH_SHORT).show();
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
