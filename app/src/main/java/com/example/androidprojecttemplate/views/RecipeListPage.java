package com.example.androidprojecttemplate.views;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
//import android.util.Log;
import android.provider.ContactsContract;
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
    private Button alphabetFilter;
    private Button timeFilter;

    private RecipeListViewModel viewModel;
    private TextView theQuantity;
    private Button backToRecipePage;
    private Timer timer;
    private Timer timer2;
    private String temp = "hello";

    private Handler timerHandler = new Handler();
    private static String[] ingredientHolder = new String[1];
    private ListView listViewRecipes;
    private DatabaseReference cookbookDatabase;
    private DatabaseReference pantryRef;
    private List<String[]> recipes = new ArrayList<>();
    private List<String> display = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list_page);

        listViewRecipes = findViewById(R.id.listViewRecipes);

        //Identify and define the button that takes you back to the recipe page
        backToRecipePage = findViewById(R.id.backToRecipePage);
        backToRecipePage.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeListPage.this, RecipePage.class);
            startActivity(intent);
        });

        //Filter alphabetically button
        alphabetFilter = findViewById(R.id.btnFilterAlpha);
        alphabetFilter.setOnClickListener(v -> {
            sortByAlphabet();
        });

        //Filter by time button
        timeFilter = findViewById(R.id.btnFilterTime);
        timeFilter.setOnClickListener(v -> {
            sortByTime();
        });

        //Define database references for use later
        cookbookDatabase = FirebaseDatabase.getInstance().getReference().child("Cookbook");
        pantryRef = FirebaseDatabase.getInstance().getReference().child("Pantry");

        //Attach listeners
        attachDatabaseReadListener();

        Log.d("DATABASE TESTING", cookbookDatabase.getParent().toString());

        //Make it so that each item in the list is clickable
        /*
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //Insert code to display recipe details here
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Implement logic to display recipe details here
                Intent intent = new Intent(RecipeListPage.this, RecipeDetailPage.class);
                startActivity(intent);
            }
        });
         */

        //Log.d("TESING", cookbookDatabase.getKey());
    }

    private void attachDatabaseReadListener() {
        cookbookDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshots : snapshot.getChildren()) {
                    //Test to check whether the data is correct
                    /*Log.d("SNAPSHOT DATA", snapshots.toString()); */

                    //Store the necessary data into their own variables
                    String name = snapshots.getKey();
                    String time = Integer.toString(snapshots.getValue(RecipeData.class).getTime());

                    //Tests to makes sure whether the variables have the correct data
                    /*Log.d("NAME", name);
                    Log.d("TIME", time); */

                    //Add the data to the String[] recipes
                    recipes.add(new String[]{name, time});
                    displayRecipes();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", "Something went wrong");
            }
        });
    }

    private void displayRecipes() {
        display.clear();
        Log.d("perhaps error?", Integer.toString(recipes.size()));
        //Adds each String[] to a separate Arraylist with better naming conventions
        for (String[] arr : recipes) {
            display.add(arr[0] + " cook time: " + arr[1]);
        }

        //Adapter to convert the list into the ListView
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, display);
        this.listViewRecipes.setAdapter(adapter);

        //Code to changed the text color based on whether the recipe can be cooked
        //this.theListView.setBackgroundColor(canCook() ? Color.GREEN : Color.RED);
        //this.theListView.textColor
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

    //canCook method from PantryData adapted for this class

    public boolean canCook() {
        //Grab an instance of the ViewModel
        viewModel = RecipeListViewModel.getInstance();
        viewModel.getCurrentUser();

        pantryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshots : snapshot.getChildren()) {
                    Log.d("PANTRY SNAPSHOT TESTING", snapshots.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return true;
    }
}
