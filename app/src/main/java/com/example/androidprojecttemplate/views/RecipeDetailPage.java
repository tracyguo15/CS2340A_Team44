package com.example.androidprojecttemplate.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidprojecttemplate.R;

public class RecipeDetailPage extends AppCompatActivity {
    //Code to display the data
    private TextView recipeName;
    private TextView description;
    private TextView time;
    private ListView ingredients;
    private Button backbtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        recipeName = findViewById(R.id.textViewRecipeName);
        description = findViewById(R.id.textViewRecipeDescription);
        time = findViewById(R.id.textViewRecipeTime);
        ingredients = findViewById(R.id.listViewIngredients);

        //Define the button to go back to the recipe list page
        backbtn = findViewById(R.id.btnBack);
        backbtn.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeDetailPage.this, RecipeListPage.class);
            startActivity(intent);
        });
    }
}
