package com.example.androidprojecttemplate.views;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
//import android.util.Log;
import android.util.Log;
import android.view.View;
//import android.widget.Adapter;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
//import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidprojecttemplate.R;
import com.example.androidprojecttemplate.viewModels.ShoppingListViewModel;
import com.example.androidprojecttemplate.models.RecipeData;
//import com.example.androidprojecttemplate.viewModels.DataObserver;
import com.example.androidprojecttemplate.viewModels.RecipeListCallback;
import com.example.androidprojecttemplate.viewModels.RecipeListViewModel;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.HashMap;

import android.os.Handler;
import android.widget.Toast;

public class RecipeListPage extends AppCompatActivity {
    private ListView theListView;

    private ArrayAdapter adapter;
    private Button goBackToRecipeScreen;
    private Button alphabetFilter;
    private Button timeFilter;

    private RecipeListViewModel viewModel;
    private Button backToRecipePage;

    private Button missingIngredientsButton;
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

        Log.d("BREAK", "another break for buttons");

        missingIngredientsButton = findViewById(R.id.missingIngredientsButton);
        missingIngredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Integer> missing = viewModel.getAllMissingIngredients(recipes);
                Log.d("SIZE OF SHOPLIST", missing.size() + "");
                ShoppingListViewModel shop = ShoppingListViewModel.getInstance();
                Log.d("EVENT", "switching classes");
                ArrayList<EditText> ingredientEditTexts = new ArrayList<>();
                ArrayList<EditText> quantitiesEditTexts = new ArrayList<>();
                for (String ingredient : missing.keySet()) {
                    EditText i = new EditText(RecipeListPage.this);
                    EditText q = new EditText(RecipeListPage.this);
                    i.setText(ingredient);
                    q.setText(missing.get(ingredient).toString());
                    ingredientEditTexts.add(i);
                    quantitiesEditTexts.add(q);
                }
                shop.addToFirebase(ingredientEditTexts, quantitiesEditTexts, result -> runOnUiThread(() -> {
                    if (result == 1) {
                        Toast.makeText(RecipeListPage.this,
                                "Success",
                                Toast.LENGTH_SHORT).show();
                    } else if (result == 2) {
                        Toast.makeText(RecipeListPage.this,
                                "Something went wrong with the firebase connection",
                                Toast.LENGTH_SHORT).show();
                    }
                }));
                v.setEnabled(false);
                Toast.makeText(RecipeListPage.this,"Sending you to Shopping List", Toast.LENGTH_SHORT);
                Log.d("EVENT", "done");
                v.setEnabled(true);
            }
        });

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
                    Log.d("TIME", time);
                    Log.d("RECIPEDATA", recipe.toString());

                    //Add the data to the String[] recipes
                    recipes.add(new String[]{name, time});
                    recipeDataList.add(recipe);
                    Log.d("RLIST SIZE", recipes.size() + "");
                    Log.d("RDLIST SIZE", recipeDataList.size() + "");
                    //Update the views
                    update(recipeDataList);
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
                android.R.layout.simple_list_item_1, display) {
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.RED);
                return view;
            }

            @Override
            public void notifyDataSetChanged() {
                TextView text = (TextView) theListView.findViewById(android.R.id.text1);
                text.setTextColor(Color.GREEN);
            }
        };
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

    //canCook method from PantryData not yet adapted for this class

    public boolean canCook(RecipeData recipe, RecipeListCallback callback) {
        return false;
    }

    private void update(List<RecipeData> recipes) {

        for (RecipeData recipe : recipes) {
            //if (recipe.canCook(pantryRef.getValue(PantryData.class))) {
                //TextView text = (TextView) theListView.findViewById(android.R.id.text1);
                //text.setTextColor(Color.GREEN);
            //} else {
                //TextView text = (TextView) theListView.findViewById(android.R.id.text1);
                //text.setTextColor(Color.RED);
            //}
            Log.d("canCook Called", String.valueOf(canCook(recipe, new RecipeListCallback() {
                @Override
                public boolean onCanCook(boolean canCook) {
                    return canCook;
                }

                @Override
                public void onError(DatabaseError databaseError) {
                    Log.d("CALLBACK ERROR", databaseError.toString());
                }
            })));
        }
    }
}
