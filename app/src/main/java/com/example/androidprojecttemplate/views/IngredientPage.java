package com.example.androidprojecttemplate.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.androidprojecttemplate.R;

import com.example.androidprojecttemplate.viewModel.IngredientViewModel;
import com.google.android.material.navigation.NavigationView;


public class IngredientPage extends AppCompatActivity {
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private static boolean isLoggedIn = false;

    private NavigationView navView;

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

        navView = (NavigationView) findViewById(R.id.navView);

        navView.setVisibility(View.GONE);
        navView.setNavigationItemSelectedListener(new NavigationView
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

        Button toPantry = findViewById(R.id.toPantryButton);
        toPantry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IngredientPage.this, PantryPage.class);
                startActivity(intent);
            }
        });
        addIngredientToFirebase.setOnClickListener(v -> {
            viewModel.getCurrentUser();
            String theName = ingredientName.getText().toString();
            String theQuantity = quantity.getText().toString();
            String theCalories = calorieForIngredient.getText().toString();
            String theExpirationDate = expirationDate.getText().toString();

            // Validation checks
            if (TextUtils.isEmpty(theName)) {
                Toast.makeText(IngredientPage.this,
                        "Please enter an ingredient name",
                        Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(theQuantity)) {
                Toast.makeText(IngredientPage.this,
                        "Please enter the quantity",
                        Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(theCalories)) {
                Toast.makeText(IngredientPage.this,
                        "Please enter the calories",
                        Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(theExpirationDate)) {
                theExpirationDate = "Not Available";
            }

            viewModel.addToFirebase(theName, theQuantity,
                    theCalories, theExpirationDate,
                    result -> runOnUiThread(() -> {
                        if (result == 1) {
                            Toast.makeText(IngredientPage.this,
                                    "Success",
                                    Toast.LENGTH_SHORT).show();
                        } else if (result == 2) {
                            Toast.makeText(IngredientPage.this,
                                    "Something went wrong",
                                    Toast.LENGTH_SHORT).show();
                        } else if (result == 3) {
                            Toast.makeText(IngredientPage.this,
                                    "The ingredient already exists, can't add",
                                    Toast.LENGTH_SHORT).show();
                        } else if (result == 4) {
                            Toast.makeText(IngredientPage.this,
                                    "Quantity is not positive, can't add",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }));
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
