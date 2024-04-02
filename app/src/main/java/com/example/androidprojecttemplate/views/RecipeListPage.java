package com.example.androidprojecttemplate.views;
import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;
import android.util.Log;
import android.view.View;
//import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
//import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidprojecttemplate.R;
import com.example.androidprojecttemplate.models.FirebaseDB;
import com.example.androidprojecttemplate.models.PantryData;
import com.example.androidprojecttemplate.models.RecipeData;
import com.example.androidprojecttemplate.viewModels.RecipeListViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
public class RecipeListPage extends AppCompatActivity {
    private ListView theListView;

    private ArrayAdapter adapter;
    private Button goBackToRecipeScreen;

    private RecipeListViewModel viewModel;
    private TextView theQuantity;
    private Button backToRecipePage;
    private Timer timer;
    private Timer timer2;
    private String temp = "hello";

    private Handler timerHandler = new Handler();
    private static String[] ingredientHolder = new String[1];
    private ListView listViewRecipes;
    private DatabaseReference recipeRef;
    private DatabaseReference pantryRef;
    private List<String[]> recipes = new ArrayList<>();
    List<String> display = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list_page);

        backToRecipePage = findViewById(R.id.backToRecipePage);

        backToRecipePage.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeListPage.this, RecipePage.class);
            startActivity(intent);
        });

        listViewRecipes = findViewById(R.id.listViewRecipes);
        recipeRef = FirebaseDatabase.getInstance().getReference().child("CookBook")
                .child("Recipe");
        pantryRef = FirebaseDatabase.getInstance().getReference().child("Pantry");

        //Attach listeners
        attachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        recipeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshots : snapshot.getChildren()) {
                    RecipeData recipe = snapshots.getValue(RecipeData.class);
                    if (recipe.canCook(recipe)) {
                        recipes.add(new String[]{recipe.getName(), Integer.toString(recipe
                                .getTime())});
                    }
                    RecipeListPage.this.displayRecipes();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", "Something went wrong");
            }
        });
    }

    private void displayRecipes() {
        for (String[] arr : recipes) {
            display.add(arr[0] + " cook time: " + arr[1]);
        }

        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, display);
        listViewRecipes.setAdapter(adapter);

        listViewRecipes.setOnItemClickListener((parent, view, position, id) -> {
            //On click event to show the recipe details
            String[] selectedRecipe = recipes.get(position);

            //Implement logic to display recipe details here
            //Intent intent = new Intent(RecipeListPage.this, RecipeDetailsPage.class);
            //startActivity(intent);
        });
    }

    public void sortByAlphabet() {
        Collections.sort(recipes, (recipe1, recipe2) ->
                recipe1[0].compareToIgnoreCase(recipe2[0]));
        displayRecipes();
    }

    public void sortByTime() {
        Collections.sort(recipes, new Comparator<String[]>() {
            @Override
            public int compare(String[] time1, String[] time2) {
                int quantity1 = Integer.parseInt(time1[1]);
                int quantity2 = Integer.parseInt(time2[1]);
                return Integer.compare(quantity1, quantity2);
            }
        });
        displayRecipes();
    }
}
