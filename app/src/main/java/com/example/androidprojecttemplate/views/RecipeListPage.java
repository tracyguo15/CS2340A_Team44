package com.example.androidprojecttemplate.views;


import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;
import android.util.Log;
import android.view.View;
//import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
//import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidprojecttemplate.R;
import com.example.androidprojecttemplate.models.RecipeData;
//import com.example.androidprojecttemplate.viewModels.DataObserver;
import com.example.androidprojecttemplate.models.CustomAdapter;
import com.example.androidprojecttemplate.viewModels.RecipeListViewModel;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import android.os.Handler;


public class RecipeListPage extends AppCompatActivity {
    private RecipeListViewModel viewModel;
    private LinearLayout recipesContainer;

    private ArrayAdapter adapter;
    private Button goBackToRecipeScreen;
    private Button alphabetFilter;
    private Button timeFilter;


    private Button backToRecipePage;

    private Button missingIngredientsButton;
    private Timer timer;
    private Timer timer2;
    private String temp = "hello";

    private Handler timerHandler = new Handler();
    //private static String[] ingredientHolder = new String[1];
    private LinearLayout listViewRecipes;
    private FirebaseUser user;
    private DatabaseReference userRef;
    private DatabaseReference cookbookRef;
    private DatabaseReference pantryRef;
    private List<String[]> recipes = new ArrayList<>();
    private List<String> display = new ArrayList<>();
    private List<RecipeData> recipeDataList = new ArrayList<>();

    private ArrayList<String> recipeNames = new ArrayList<>();
    private ArrayList<Integer> recipeTimes = new ArrayList<>();
    private ArrayList<Boolean> recipeCanCooks = new ArrayList<>();
    private HashMap<String, Integer> shoppingList;

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
            displayRecipesAlpha();
        });

        //Filter by time button
        timeFilter = findViewById(R.id.btnFilterTime);
        timeFilter.setOnClickListener(v -> {
            displayRecipesTime();
        });

        // database references
        cookbookRef = FirebaseDatabase.getInstance().getReference().child("Cookbook");
        pantryRef = FirebaseDatabase.getInstance().getReference().child("Pantry");

        // container view
        recipesContainer = findViewById(R.id.listViewRecipes);

        // RecipeListViewModel instantiation
        viewModel = RecipeListViewModel.getInstance();

        // initially display recipes
        this.displayRecipes();
        Log.d("BREAK", "another break for buttons");

        missingIngredientsButton = findViewById(R.id.missingIngredientsButton);
        missingIngredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                Log.d("OUT", "click");

                shoppingList = viewModel.getAllMissingIngredients();
                if (shoppingList == null) {
                    return;
                }
                String userName = viewModel.getUserName();
                DatabaseReference shoppingListDB = FirebaseDatabase.getInstance().getReference().child("Shopping_List").child(userName);

                shoppingListDB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // for each element in the remote database that
                        for (DataSnapshot listItem : snapshot.getChildren()) {
                            Log.d("SUBMIT", listItem.getKey());
                            String keyInDB = listItem.getKey();
                            if (keyInDB.equals("name") || keyInDB.equals("username")) {
                                continue;
                            }

                            if (shoppingList.containsKey(keyInDB)) {
                                int previousQuantity = Integer.parseInt(listItem.child("quantity").getValue().toString());
                                int additionalQuantity = shoppingList.get(keyInDB);
                                String result = (previousQuantity + additionalQuantity) + "";
                                shoppingListDB.child(keyInDB).child("quantity").getRef().setValue(result);
                                shoppingList.remove(keyInDB);
                            }
                        }

                        // for each element on the user's side that isn't already in the database
                        for (String key : shoppingList.keySet()) {
                            String quantity = shoppingList.get(key) + "";
                            shoppingListDB.child(key).child("name").getRef().setValue(key);
                            shoppingListDB.child(key).child("quantity").getRef().setValue(quantity);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("ERROR", "unable to write to Shopping_List database...!");
                    }
                });

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

    /**
     * Displays each of the recipes in the database.
     */
    private void displayRecipes() {
        CustomAdapter adapter = new CustomAdapter(
                this,
                this.recipesContainer,
                this.viewModel);

        adapter.display();
    }

    /**
     * Displays each of the recipes in the database sorted alphabetically.
     */
    private void displayRecipesAlpha() {
        CustomAdapter adapter = new CustomAdapter(
                this,
                this.recipesContainer,
                this.viewModel);

        adapter.displayAlpha();
    }

    /**
     * Displays each of the recipes in the database sorted by time.
     */
    private void displayRecipesTime() {
        CustomAdapter adapter = new CustomAdapter(
                this,
                this.recipesContainer,
                this.viewModel);

        adapter.displayTime();
    }

    /*
    private void update(String recipeName) {
        display.clear();
        Log.d("RecipeList size", Integer.toString(recipes.size()));
        // Adds each String[] to a separate Arraylist with better naming conventions
        for (String[] arr : recipes) {
            display.add(arr[0] + " cook time: " + arr[1]);
        }

        // Adapter to convert the list into the ListView
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, display) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);

                // Call the canCook2 method with a callback
                viewModel.canCook2(recipes.get(position)[0], new CanCookCallback() {
                    @Override
                    public void onResult(boolean canCook) {
                        runOnUiThread(() -> {
                            // This ensures the update happens on the UI thread
                            if (canCook) {
                                text.setTextColor(Color.GREEN);
                            } else {
                                text.setTextColor(Color.RED);
                            }
                        });
                    }
                });

                text.setText(recipes.get(position)[0]);
                return view;
            }
        };

        listViewRecipes.setAdapter(adapter);
    }*/
}
