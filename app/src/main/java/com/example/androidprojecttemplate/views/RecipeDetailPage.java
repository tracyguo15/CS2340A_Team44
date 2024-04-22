package com.example.androidprojecttemplate.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidprojecttemplate.R;
import com.example.androidprojecttemplate.models.FirebaseDB;
import com.example.androidprojecttemplate.models.MealData;
import com.example.androidprojecttemplate.viewModels.ArrayListIngredientCallback;
import com.example.androidprojecttemplate.viewModels.FirebaseCallback;
import com.example.androidprojecttemplate.viewModels.RecipeListViewModel;
import com.example.androidprojecttemplate.viewModels.StringCallback;
import com.example.androidprojecttemplate.viewModels.TheCallback;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class RecipeDetailPage extends AppCompatActivity {
    //Code to display the data
    private TextView recipeName;
    private TextView recipeDescription;
    private TextView recipeTime;
    private RecipeListViewModel viewModel;
    private FirebaseUser user;
    private String username;
    private ListView ingredients;
    private Button backbtn;
    private Button cookBtn;
    private DatabaseReference recipeDatabase;
    private DatabaseReference referenceForCookBookIngredients;
    private DatabaseReference referenceForMeal;
    private DatabaseReference pantryRef;
    private ArrayList<String> theListOfIngredients = new ArrayList<>();
    private int totalCalories = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        // ui text
        recipeName = findViewById(R.id.textViewRecipeName);
        recipeDescription = findViewById(R.id.textViewRecipeDescription);
        recipeTime = findViewById(R.id.textViewRecipeTime);
        //ingredients = findViewById(R.id.listViewIngredients);

        //Retrieve the name of the recipe that was selected
        viewModel = RecipeListViewModel.getInstance();
        viewModel.getCurrentUser();
        Intent intent = getIntent();
        String recipeKey = (String) intent.getSerializableExtra("recipeName");

        //Creates a reference to the recipe and pantry in the Firebase Database
        recipeDatabase = FirebaseDatabase.getInstance().getReference().child("Cookbook")
                .child(recipeKey);
        pantryRef = FirebaseDatabase.getInstance().getReference().child("Pantry");

        //Define the button to go back to the recipe list page
        backbtn = findViewById(R.id.btnBack);
        backbtn.setOnClickListener(v -> {
            Intent intent2 = new Intent(RecipeDetailPage.this, RecipeListPage.class);
            startActivity(intent2);
        });

        //Define the button to cook the recipe
        cookBtn = findViewById(R.id.cookBtn);
        cookBtn.setOnClickListener(v -> {
            // Gets a list of the ingredients for this meal
            referenceForCookBookIngredients = FirebaseDatabase.getInstance().getReference()
                    .child("Cookbook")
                    .child(recipeKey)
                    .child("ingredients");

            getListOfIngredients(new ArrayListIngredientCallback() {
                @Override
                public void onCallback(ArrayList<String> list) {
                    // Adds to the meals database
                    addToMealDatabase(recipeKey, list);
                }
            });

            //Cook recipe logic here
            removeFromPantry(recipeKey);

            // Adds to the meals database
            //addToMealDatabase(recipeKey, theListOfIngredients);
        });

        //Displays all the relevant information
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
    }

    /**
     * Yes this is copied over from the RecipeListViewModel.
     * When I called viewModel.getCurrentUser it wasn't setting
     * any values so I tried this instead.
     * Do not remove, uses a callback which differentiates it
     * @param callback The callback
     */
    public void getCurrentUser(StringCallback callback) {
        user = FirebaseDB.getInstance().getUser();

        String email = user.getEmail();
        DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshots : snapshot.getChildren()) {
                    // check if email from db == user email
                    String tempEmail = snapshots.child("username").getValue(String.class);

                    if (tempEmail.equals(email)) {
                        username = snapshots.getKey();
                        callback.onCallback(username);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("GET CURRENT USER", error.toString());
            }
        });
    }

    /**
     * Will get a HashMap of all of the ingredients in the recipe and the user's pantry
     * then remove the ingredients from the pantry.
     * Since this is inside the RecipeDetailPage, this means that the recipe should be
     * able to be cooked and therefore can be automatically subtracted from
     * @param recipeName the name of the recipe to cook
     */
    private void removeFromPantry(String recipeName) {
        //Makes sure that the current user is up to date
        getCurrentUser(new StringCallback() {
            @Override
            public void onCallback(String string) {
                //Log.d("USERNAME", string);

                //Sets a database reference to the user's pantry ingredients
                DatabaseReference userPantryRef = pantryRef.child(string)
                        .child("Ingredients");

                //Calls on the getRecipeIngredients method
                // to get a HashMap of the recipe's ingredients
                viewModel.getRecipeIngredients(recipeName, new FirebaseCallback() {
                    @Override
                    public void onCallback(HashMap<String, String> recipeIngredients) {
                        //Call on the getIngredients method to get a HashMap of the user's pantry
                        viewModel.getIngredients(new FirebaseCallback() {
                            @Override
                            public void onCallback(HashMap<String, String> pantryIngredients) {
                                //For each ingredient in the recipe
                                for (String ingredient : recipeIngredients.keySet()) {

                                    //Logic here
                                    getQuantity(userPantryRef, ingredient, new TheCallback() {
                                        @Override
                                        public void onCompleted(int result) {
                                            //Log.d("RESULT", String.valueOf(result));

                                            //Set the value in the database
                                            userPantryRef.child(ingredient)
                                                    .child("quantity")
                                                    .setValue(String.valueOf(result));

                                            if (result == 0) {
                                                userPantryRef.child(ingredient)
                                                        .removeValue();
                                            }

                                            //Toast message notifying that the recipe
                                            // was successfully cooked
                                            Toast.makeText(RecipeDetailPage.this,
                                                    "Recipe cooked",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * A method made to add up the total amount of calories
     * in the ingredients and add it to the meal database
     * @param theRecipeName the name of the recipe to be added to the meal database
     * @param theListOfIngredients a list of the ingredients
     */
    private void addToMealDatabase(String theRecipeName, ArrayList<String> theListOfIngredients) {
        // Gets the current time
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String date = currentDate.format(formatter);

        getTotalCalories(new TheCallback() {
            @Override
            public void onCompleted(int result) {
                Log.d("TOTAL CALORIES", String.valueOf(totalCalories));

                // Now that we have the appropriate data,
                // can now add it to the meal database
                // The code for the meals page will automatically
                // accommodate the addition of the meal
                referenceForMeal = FirebaseDatabase.getInstance().getReference().child("Meals");
                referenceForMeal.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        MealData theData = new MealData();
                        theData.setCalories(result);
                        theData.setUsername(FirebaseDB.getInstance().getUser().getEmail());
                        theData.setDate(date);

                        referenceForMeal.child(theRecipeName).setValue(theData);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("ADD TO MEAL ERROR", error.toString());
                    }
                });
            }
        });
    }

    /**
     * Helper method for the addToMealDatabase method
     * Used to get the total amount of calories in a meal
     * @param callback callback used for getting back an int
     */
    private void getTotalCalories(TheCallback callback) {
        //Sets the reference to the ingredients database
        referenceForCookBookIngredients =  FirebaseDatabase.getInstance().getReference()
                .child("Ingredients");



        // Goes to the ingredient database to get the quantities
        referenceForCookBookIngredients.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Snapshot will be pointing to each individual ingredient in the database
                //Log.d("CALORIE SNAPSHOT", snapshot.toString());
                //Log.d("SIZE OF INGREDIENT DATABASE", String.valueOf(snapshot.getChildrenCount()));

                //For each item in the list of ingredients
                for (int i = 0; i < theListOfIngredients.size(); i++) {
                    //Log.d("LIST OF INGREDIENT", theListOfIngredients.get(i));

                    //Compares the name of the ingredient in the list to the name of the ingredient
                    //currently hovered
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        //Log.d("SNAPSHOT INGREDIENT", snapshots.getKey());

                        if (theListOfIngredients.get(i).equals(snapshots.getKey())) {
                            Log.d("Ingredient Calories", snapshots.child("calories").toString());

                            // Found a matching ingredient
                            totalCalories += Integer.parseInt(
                                    String.valueOf(snapshots.child("calories").getValue()));
                            break;
                        }
                    }
                }
                callback.onCompleted(totalCalories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TOTAL CALORIES ERROR", error.toString());
            }
        });
    }

    /**
     * Helper method for cookBtn
     * Gets an ArrayList(String) for use in the addToMealDatabase method
     * @param callback callback that return an ArrayList(String)
     */
    private void getListOfIngredients(ArrayListIngredientCallback callback) {
        // Go through a loop
        referenceForCookBookIngredients.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot theSnapshot : snapshot.getChildren()) {
                    if (theSnapshot.exists()) {
                        theListOfIngredients.add(theSnapshot.getKey());
                    }
                }
                callback.onCallback(theListOfIngredients);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("LIST OF INGREDIENTS ERROR", error.toString());
            }
        });
    }

    /**
     * Helper method that gets the final quantity after subtraction
     * @param ref Database Reference to a user's pantry
     * @param ingredient the name of the ingredient
     * @param callback callback that returns an integer
     */
    private void getQuantity(DatabaseReference ref, String ingredient, TheCallback callback) {
        //The quantity of the ingredient in the user's pantry
        DatabaseReference pantryQuantityRef = ref
                .child(ingredient)
                .child("quantity");

        pantryQuantityRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Log.d("Pantry Snapshot", String.valueOf(snapshot.getValue()));

                //Should get the correct quantity of pantry ingredients
                int pantryQuantity = Integer.parseInt(String.valueOf(snapshot.getValue()));

                //The quantity of ingredient required for the recipe
                DatabaseReference recipeQuantityRef = recipeDatabase.child("ingredients")
                        .child(ingredient);

                recipeQuantityRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //Log.d("Recipe Snapshot", String.valueOf(snapshot.getValue()));

                        int recipeQuantity = Integer.parseInt(String.valueOf(snapshot
                                .child("quantity").getValue()));

                        callback.onCompleted(
                                pantryQuantity - recipeQuantity);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("Recipe quantity error", error.toString());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("PANTRY QUANTITY ERROR", error.toString());
            }
        });
    }
}
