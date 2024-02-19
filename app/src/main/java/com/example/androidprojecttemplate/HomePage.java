package com.example.androidprojecttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_page);

        Button toRecipePageButton = findViewById(R.id.RecipeButton);
        toRecipePageButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, RecipePage.class);
            startActivity(intent);
        });

        Button toInputMealPageButton = findViewById(R.id.InputMealButton);
        toInputMealPageButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, InputMealPage.class);
            startActivity(intent);
        });

        Button toIngredientPageButton = findViewById(R.id.IngredientButton);
        toIngredientPageButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, IngredientPage.class);
            startActivity(intent);
        });

        Button toListPageButton = findViewById(R.id.ListButton);
        toListPageButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, ListPage.class);
            startActivity(intent);
        });
    }
}
