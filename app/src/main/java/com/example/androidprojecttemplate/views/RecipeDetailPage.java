package com.example.androidprojecttemplate.views;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidprojecttemplate.R;
import com.example.androidprojecttemplate.models.FirebaseDB;
import com.example.androidprojecttemplate.models.MealData;
import com.example.androidprojecttemplate.viewModels.FirebaseCallback;
import com.example.androidprojecttemplate.viewModels.RecipeListViewModel;
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
            referenceForCookBookIngredients = FirebaseDatabase.getInstance().getReference().child("Cookbook").child("theRecipeName").child("ingredients");
            // Go through a loop
            referenceForCookBookIngredients.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot theSnapshot : snapshot.getChildren()) {
                        if (theSnapshot.exists()) {
                            theListOfIngredients.add(theSnapshot.getKey());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("Error", "Firebase error");
                }
            });


            //Cook recipe logic here
            removeFromPantry(recipeKey);

            // Adds to the meals database
            addToMealDatabase(recipeKey, theListOfIngredients);
        });

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
     * Can be removed
     */
    public void getCurrentUser() {
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
                    String tempEmail = snapshots.child("username").getValue().toString();

                    if (tempEmail.equals(email)) {
                        username = snapshots.getKey();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("RECIPE INGREDIENTS ERROR", error.toString());
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
        viewModel.getCurrentUser();

        //These are returning null for some reason
        Log.d("USER", user.toString()); //Attempt to invoke virtual method 'java.lang.String java.lang.Object.toString()' on a null object reference
        Log.d("username", username);

        //Sets a database reference to the user's pantry ingredients
        DatabaseReference userPantryRef = pantryRef.child(username).child("Ingredients");

        //Calls on the getRecipeIngredients method to get a HashMap of the recipe's ingredients
        viewModel.getRecipeIngredients(recipeName, new FirebaseCallback() {
            @Override
            public void onCallback(HashMap<String, String> recipeIngredients) {
                //Call on the getIngredients method to get a HashMap of the user's pantry
                viewModel.getIngredients(new FirebaseCallback() {
                    @Override
                    public void onCallback(HashMap<String, String> pantryIngredients) {
                        //For each ingredient in the recipe
                        for (String ingredient : recipeIngredients.keySet()) {

                            //The quantity of the ingredient in the user's pantry
                            int pantryQuantity = Integer.parseInt(pantryRef.child(ingredient)
                                    .child("quantity").toString());
                            Log.d("pantryQuantity", String.valueOf(pantryQuantity));

                            //The quantity of ingredient required for the recipe
                            int recipeQuantity = Integer.parseInt(recipeDatabase.child("ingredients")
                                    .child(ingredient).toString());
                            Log.d("pantryQuantity", String.valueOf(recipeQuantity));

                            //Subtract the recipe from the pantry and set the value in the database
                            pantryRef.child(ingredient).child("quantity")
                                    .setValue(pantryQuantity - recipeQuantity);
                        }
                    }
                });
            }
        });
    }

    private void addToMealDatabase(String theRecipeName, ArrayList<String> theListOfIngredients) {
        // Gets the current time
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String date = currentDate.format(formatter);

        referenceForCookBookIngredients =  FirebaseDatabase.getInstance().getReference().child("Ingredients");



        // Goes to the ingredient database to get the quantities
        referenceForCookBookIngredients.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(int i = 0; i < theListOfIngredients.size(); i++) {
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        if (theListOfIngredients.get(i).equals(snapshots.toString())) {
                            // Found a matching ingredient
                            totalCalories += Integer.parseInt(snapshots.child("calories").getValue(String.class));
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", "Firebase error");
            }
        });


        // Now that we have the appropriate data, can now add it to the meal database
        // The code for the meals page will automatically accomodate the addition of the meal
        referenceForMeal = FirebaseDatabase.getInstance().getReference().child("Meals");
        referenceForMeal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MealData theData = new MealData();
                theData.setCalories(totalCalories);
                theData.setUsername(FirebaseDB.getInstance().getUser().getEmail());
                theData.setDate(date);

                referenceForMeal.child(theRecipeName).setValue(theData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", "Firebase issue");
            }
        });

    }
}
