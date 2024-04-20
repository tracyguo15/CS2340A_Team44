package com.example.androidprojecttemplate.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidprojecttemplate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RecipeDetailPage extends AppCompatActivity {
    //Code to display the data
    private TextView recipeName;
    private TextView recipeDescription;
    private TextView recipeTime;
    private ListView ingredients;
    private Button backbtn;
    private Button cookBtn;
    private DatabaseReference recipeDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        // ui text
        recipeName = findViewById(R.id.textViewRecipeName);
        recipeDescription = findViewById(R.id.textViewRecipeDescription);
        recipeTime = findViewById(R.id.textViewRecipeTime);
        //ingredients = findViewById(R.id.listViewIngredients);

        //Define the button to go back to the recipe list page
        backbtn = findViewById(R.id.btnBack);
        backbtn.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeDetailPage.this, RecipeListPage.class);
            startActivity(intent);
        });

        //Define the button to cook the recipe
        cookBtn = findViewById(R.id.cookBtn);
        cookBtn.setOnClickListener(v -> {
            //Cook recipe logic here
        });

        //Retrieve the name of the recipe that was selected
        Intent intent = getIntent();
        String recipeKey = (String) intent.getSerializableExtra("recipeName");

        Log.d("RECIPE KEY", recipeKey);


        //Creates a reference to the recipe in the Firebase Database
        recipeDatabase = FirebaseDatabase.getInstance().getReference().child("Cookbook")
                .child(recipeKey);

        recipeDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Displays the recipe's name
                String recipeNameText = snapshot.getKey();
                recipeName.setText(recipeNameText);

                //Displays the recipe's description
                String recipeDescText = "Description: " + snapshot.child("description").getValue();
                recipeDescription.setText(recipeDescText);

                //Displays the recipe's cook time
                String recipeTimeText = "Total time: " + snapshot.child("time").getValue();
                recipeTime.setText(recipeTimeText);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("RECIPE ERROR", error.toString());
            }
        });
        /*
        //Displays the recipe's name
        String recipeNameText = recipeDatabase.getKey();
        recipeName.setText(recipeNameText);

        //Displays the recipe's description
        String recipeDescText = "Description: " + recipeDatabase.chi;
        recipeDescription.setText(recipeDescText);

        //Displays the recipe's cook time
        String recipeTimeText = "Total time: " + recipeDatabase.child("time").get().toString();
        recipeTime.setText(recipeTimeText); */
    }
}
