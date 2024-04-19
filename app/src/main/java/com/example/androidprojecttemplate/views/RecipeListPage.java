package com.example.androidprojecttemplate.views;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
//import android.util.Log;
import android.provider.ContactsContract;
import android.text.Html;
import android.util.Log;
import android.view.View;
//import android.widget.Adapter;
import android.view.ViewGroup;
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
import com.example.androidprojecttemplate.models.Pair;
import com.example.androidprojecttemplate.models.PantryData;
import com.example.androidprojecttemplate.models.RecipeData;
//import com.example.androidprojecttemplate.viewModels.DataObserver;
import com.example.androidprojecttemplate.viewModels.RecipeListCallback;
import com.example.androidprojecttemplate.viewModels.RecipeListViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private Button backToRecipePage;
    private Timer timer;
    private Timer timer2;
    private String temp = "hello";

    private Handler timerHandler = new Handler();
    private static String[] ingredientHolder = new String[1];
    private ListView listViewRecipes;
    private FirebaseUser user;
    private DatabaseReference userRef;
    private DatabaseReference cookbookDatabase;
    private DatabaseReference pantryRef;
    private List<String[]> recipes = new ArrayList<>();
    private List<String> display = new ArrayList<>();
    private List<RecipeData> recipeDataList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list_page);

        listViewRecipes = findViewById(R.id.listViewRecipes);
        viewModel = RecipeListViewModel.getInstance();
        viewModel.getCurrentUser();

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
        Log.d("Break before database listener", "Yuh");
        attachDatabaseReadListener();

        //Log.d("CAN COOK METHOD TESTING", canCook());

        //Make it so that each item in the list is clickable
        /*
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Implement logic to display recipe details here
                String recipeName = theListView.get(position); //This should get the value at the position in which it was clicked
                Intent intent = new Intent(RecipeListPage.this, RecipeDetailPage.class);
                intent.putExtra("recipe", recipeName);
                startActivity(intent);
            }
        }); */

        //Log.d("TESTING", cookbookDatabase.getKey());
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
                    RecipeData recipe = snapshots.getValue(RecipeData.class);

                    //Tests to makes sure whether the variables have the correct data
                    Log.d("NAME", name);
                    //Log.d("TIME", time);
                    //Log.d("RECIPEDATA", recipe.toString());

                    //Add the data to the String[] recipes
                    recipes.add(new String[]{name, time});
                    recipeDataList.add(recipe);

                    //Update the views
                    update(name);
                    //displayRecipes();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", "Something went wrong");
            }
        });
    }

    private void displayRecipes() {


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

    //canCook method from PantryData not yet adapted for this class
    /*
    public boolean canCook(RecipeData recipe) {
        //Boolean variable to be returned
        boolean cooked = false;

        //Grab an instance of the ViewModel
        viewModel = RecipeListViewModel.getInstance();
        viewModel.getCurrentUser();

        //Get String[] of all the ingredients necessary and available
        ArrayList<String[]> recipeIngredients = viewModel.getRecipeIngredients(recipe);
        ArrayList<String[]> pantryIngredients = viewModel.getPantryIngredients();
        Log.d("recipeIngredients", recipeIngredients.toString());
        Log.d("pantryIngredients", pantryIngredients.toString());

        //Go through each ingredient necessary for recipe
        for (String[] r : recipeIngredients) {
            //Go through every ingredient in the pantry
            for (String[] p : pantryIngredients) {
                //Compare the names to see if they match
                if (r[0].equals(p[0])) {
                    //If the pantry amount < recipe amount
                    if (Integer.parseInt(p[1]) < Integer.parseInt(r[1])) {
                        break;
                    } else if (Integer.parseInt(p[1]) > Integer.parseInt(r[1])) {
                        cooked = true;
                    }
                }
            }

            //If cooked != true, then that means either the quantity was too low
            //or it wasn't in the pantry
            if (cooked != true) {
                break;
            }
        }

        return cooked;
    } */

    private void update(String recipeName) {

        //for (RecipeData recipe : recipeList) {

        //}
        //Log.d("canCook Called", String.valueOf(viewModel.canCook(recipe)));
        display.clear();
        Log.d("RecipeList size", Integer.toString(recipes.size()));
        //Adds each String[] to a separate Arraylist with better naming conventions
        for (String[] arr : recipes) {
            display.add(arr[0] + " cook time: " + arr[1]);
        }

        //Adapter to convert the list into the ListView
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, display) {
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                String availabilityText;
                if (viewModel.canCook(recipeName)) {
                    availabilityText = "<font color=\"#32CD32\">" + recipeName + "</font>";
                } else {
                    availabilityText = "<font color=\"#DC143C\">" + recipeName + "</font>";
                }
                text.setText(Html.fromHtml(availabilityText));
                return view;
            }

            @Override
            public void notifyDataSetChanged() {
                TextView text = (TextView) theListView.findViewById(android.R.id.text1);
                text.setTextColor(Color.GREEN);
            }
        };
        this.listViewRecipes.setAdapter(adapter);
    }
}
