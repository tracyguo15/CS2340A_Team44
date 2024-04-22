package com.example.androidprojecttemplate.views;


import android.content.Intent;
import android.graphics.Color;
//import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
//import android.text.Layout;
import android.util.Log;
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

import com.example.androidprojecttemplate.models.AbstractDatabase;
import com.example.androidprojecttemplate.models.IngredientData;
//import com.example.androidprojecttemplate.models.MealData;
//import com.example.androidprojecttemplate.models.Pair;
import com.example.androidprojecttemplate.models.RecipeData;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecipePage extends AppCompatActivity {
    // ui
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private NavigationView navView;

    private LinearLayout ingredientContainer;
    private Button addIngredient;
    private Button submit;
    private final int maxIngredients = 5;
    private int ingredientCount;
    private ArrayList<EditText> ingredients;
    private ArrayList<EditText> quantities;

    private EditText timeInput;
    private EditText descriptionInput;
    private EditText nameInput;
    private Button toRecipeList;

    public void createIngredientRow() {
        // create row
        LinearLayout row = new LinearLayout(this);
        row.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        row.setOrientation(LinearLayout.HORIZONTAL);

        // row elements
        EditText input = new EditText(this);
        EditText quantityInput = new EditText(this);
        Button removeButton = new Button(this);

        row.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int width = row.getWidth();
                    }
                });

        removeButton.setLayoutParams(new ViewGroup.LayoutParams(
                205,
                ViewGroup.LayoutParams.MATCH_PARENT));

        quantityInput.setLayoutParams(new ViewGroup.LayoutParams(
                205,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        input.setLayoutParams(new ViewGroup.LayoutParams(
                959,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        removeButton.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        removeButton.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        int cornerRadius = removeButton.getHeight() / 2;

                        GradientDrawable drawable = new GradientDrawable();

                        drawable.setShape(GradientDrawable.RECTANGLE);
                        drawable.setCornerRadius(cornerRadius);

                        drawable.setColor(Color.rgb(102, 80, 164)); // purple

                        removeButton.setPadding(40, 4, 40, 4);
                        removeButton.setBackground(drawable);
                    }
                });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientContainer.removeView(row);
                ingredients.remove(input);
                quantities.remove(quantityInput);
                ingredientCount--;
            }
        });

        removeButton.setAllCaps(false);
        removeButton.setTextColor(Color.WHITE);
        removeButton.setText("-");
        removeButton.setTextSize(14);

        input.setHint("ingredient");

        quantityInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        quantityInput.setHint("0");

        row.addView(input);
        row.addView(quantityInput);
        row.addView(removeButton);

        ingredientContainer.addView(row);

        ingredients.add(input);
        quantities.add(quantityInput);
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

        navView = (NavigationView) findViewById(R.id.navView);
        navView.setVisibility(View.GONE);
        navView.setNavigationItemSelectedListener(new NavigationView
                .OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                System.out.println(R.id.inputmeal);
                int id = item.getItemId();
                if (id == R.id.inputmeal) {
                    Intent intent = new Intent(RecipePage.this, InputMealPage.class);
                    startActivity(intent);
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
                    Intent intent = new Intent(RecipePage.this, ShoppingList.class);
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

        theFunctionality();
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

    private void theFunctionality() {
        // page functionality
        ingredientContainer = findViewById(R.id.container);
        addIngredient = findViewById(R.id.addIngredientRow);
        submit = findViewById(R.id.submitRecipeData);
        toRecipeList = findViewById(R.id.toRecipeList);

        ingredients = new ArrayList<>();
        quantities = new ArrayList<>();

        timeInput = findViewById(R.id.timeInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        nameInput = findViewById(R.id.nameInput);

        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ingredientCount < maxIngredients) {
                    createIngredientRow();
                    ingredientCount++;
                } else {
                    Toast.makeText(RecipePage.this,
                            "Max 10 ingredients",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference cookbookReference = FirebaseDatabase.getInstance()
                        .getReference().child("Cookbook");
                DatabaseReference ingredientReference = FirebaseDatabase.getInstance()
                        .getReference().child("Ingredients");

                // get copy of ingredients from Ingredients database
                ingredientReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        AbstractDatabase<String, IngredientData> ingredientsDB
                                = new AbstractDatabase<>();

                        for (DataSnapshot ingredient : snapshot.getChildren()) {
                            IngredientData data = new IngredientData(
                                    ingredient.getKey().toString(),
                                    "1",
                                    0,
                                    "20240312"
                            );

                            ingredientsDB.put(ingredient.getKey().toString(), data);
                        }

                        cookbookReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String recipeName = nameInput.getText().toString();
                                String recipeDescription = descriptionInput.getText().toString();
                                int recipeTime = Integer.parseInt(timeInput.getText().toString());

                                RecipeData data = new RecipeData(recipeName, recipeDescription,
                                        recipeTime);

                                boolean allIngredientsFound = true;

                                // iterate through ingredients on this page
                                for (int i = 0; i < ingredients.size(); i++) {
                                    String ingredientName = ingredients.get(i).getText().toString();
                                    Log.d("name", ingredientName);
                                    int ingredientQuantity = Integer.parseInt(quantities.get(i)
                                            .getText().toString());

                                    if (ingredientsDB.containsKey(ingredientName)) {
                                        data.add(ingredientName, ingredientQuantity);
                                    } else {
                                        Toast.makeText(RecipePage.this,
                                                String.format("%s is not a valid ingredient.",
                                                        ingredientName),
                                                Toast.LENGTH_SHORT).show();
                                        allIngredientsFound = false;
                                        break;
                                    }
                                }

                                if (allIngredientsFound) {
                                    data.setDescription(descriptionInput.getText().toString());
                                    data.setTime(Integer.parseInt(timeInput.getText().toString()));

                                    // submit to firebase
                                    cookbookReference
                                            .child(recipeName)
                                            .child("description")
                                            .setValue(recipeDescription);
                                    cookbookReference
                                            .child(recipeName)
                                            .child("time")
                                            .setValue(recipeTime);

                                    for (String ingredientsKey : data.keySet()) {
                                        cookbookReference
                                                .child(recipeName)
                                                .child("ingredients")
                                                .child(ingredientsKey)
                                                .child("quantity")
                                                .setValue(data.get(ingredientsKey));
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(RecipePage.this,
                                        "Something went wrong in the outer portion",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // handle potential errors
                        Toast.makeText(RecipePage.this,
                                "Something went wrong in the outer portion",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        toRecipeList.setOnClickListener(v -> {
            Intent intent = new Intent(RecipePage.this, RecipeListPage.class);
            startActivity(intent);
        });
    }
}