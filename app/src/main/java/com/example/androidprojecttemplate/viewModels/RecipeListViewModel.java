package com.example.androidprojecttemplate.viewModels;


//import android.content.Intent;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;

//import com.example.androidprojecttemplate.models.DataForPantry;
//import com.example.androidprojecttemplate.models.IngredientData;
//import com.example.androidprojecttemplate.models.UserData;
import com.example.androidprojecttemplate.models.FirebaseDB;
import com.example.androidprojecttemplate.models.RecipeData;
import com.example.androidprojecttemplate.views.IngredientListPage;
import com.example.androidprojecttemplate.views.RecipeListPage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.*;
//import java.util.logging.Handler;

public class RecipeListViewModel {
    private static RecipeListViewModel instance;
    private final RecipeListPage theData;

    private FirebaseAuth theAuthenticationVariable;
    private FirebaseUser user;
    private DatabaseReference referenceForPantry;
    private DatabaseReference referenceForSpecificUser;
    private DatabaseReference referenceForRecipe;

    private String theUsersEmailFromAuthenticationDatabase;
    private String theReturnQuantity = null;

    private String temp = "hel";

    private int changer = 0;

    private Timer timer;

    public RecipeListViewModel() {
        theData = new RecipeListPage();
    }

    public static synchronized RecipeListViewModel getInstance() {
        if (instance == null) {
            instance = new RecipeListViewModel();
        }
        return instance;
    }

    public void getCurrentUser() {
        theAuthenticationVariable = FirebaseDB.getInstance().getFirebaseAuth();
        user = FirebaseDB.getInstance().getUser();
        theUsersEmailFromAuthenticationDatabase = FirebaseDB.getInstance().getEmail();
    }

    // Have to go to firebase and retrieve all of the current elements
    // * May not work if it's empty, need to test

    public String getTheQuantity(String theNameOfIngredient, int number) {
        referenceForPantry = FirebaseDatabase.getInstance().getReference().child("Pantry");


        referenceForPantry.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshots : snapshot.getChildren()) {

                    String theEmailFromFirebase = snapshots.child("username")
                            .getValue().toString();
                    String theUsersName = snapshots.child("name").getValue().toString();

                    if (theEmailFromFirebase.equals(theUsersEmailFromAuthenticationDatabase)) {
                        //This reference for specific user becomes a reference to their respective
                        // pantry
                        referenceForSpecificUser = referenceForPantry.child(theUsersName)
                                .child("Ingredients");

                        // Will use a helper method to do the rest
                        /*
                        theReturnQuantity = helperMethod(referenceForSpecificUser,
                                theNameOfIngredient, number); */
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", "Something went wrong");
            }
        });

        return theReturnQuantity;
    }

    // get hashmap of all ingredient:quantity from certain recipe
    /**
     * This method will return a hashmap of all the ingredients and their quantities
     * that are required for a certain recipe
     * @param recipeName
     * @return HashMap<String, String> ingredients
     */
    public HashMap<String, String> getRecipeIngredients(String recipeName) {
        referenceForRecipe = FirebaseDatabase.getInstance().getReference().child("Recipe");

        HashMap<String, String> ingredients = new HashMap<>();

        referenceForRecipe.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot theSnapshot : snapshot.getChildren()) {
                    if (theSnapshot.child("name").getValue().toString().equals(recipeName)) {
                        for (DataSnapshot ingredient : theSnapshot.child("ingredients").getChildren()) {
                            ingredients.put(ingredient.getKey(), ingredient.getValue().toString());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", "Something went wrong");
            }
        });

        return ingredients;

    }

    //get hashmap of all ingredient:quantity from ingredients list of current user
    /**
     * This method will return a hashmap of all the ingredients and their quantities
     * that the user has in their pantry
     * @return HashMap<String, String> ingredients
     */
    public HashMap<String, String> getIngredients() {
        referenceForPantry = FirebaseDatabase.getInstance().getReference().child("Pantry");

        HashMap<String, String> ingredients = new HashMap<>();

        referenceForPantry.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot theSnapshot : snapshot.getChildren()) {
                    String theEmailFromFirebase = theSnapshot.child("username").getValue().toString();
                    String theUsersName = theSnapshot.child("name").getValue().toString();

                    if (theEmailFromFirebase.equals(theUsersEmailFromAuthenticationDatabase)) {
                        referenceForSpecificUser = referenceForPantry.child(theUsersName).child("Ingredients");

                        referenceForSpecificUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ingredient : snapshot.getChildren()) {
                                    ingredients.put(ingredient.getKey(), ingredient.getValue().toString());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.d("Error", "Something went wrong");
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", "Something went wrong");
            }
        });

        return ingredients;
    }

    /**
     * This method will return a hashmap of all the ingredients and their quantities
     * that are required for a certain recipe but are missing from the user's pantry
     * @param recipeName
     * @return HashMap<String, String> missingIngredients
     */
    public HashMap<String, String> getMissingIngredients(String recipeName) {
        HashMap<String, String> recipeIngredients = getRecipeIngredients(recipeName);
        HashMap<String, String> pantryIngredients = getIngredients();
        HashMap<String, String> missingIngredients = new HashMap<>();

        for (String ingredient : recipeIngredients.keySet()) {
            if (!pantryIngredients.containsKey(ingredient)) {
                missingIngredients.put(ingredient, recipeIngredients.get(ingredient));
            } else {
                int requiredQuantity = Integer.parseInt(recipeIngredients.get(ingredient));
                int pantryQuantity = Integer.parseInt(pantryIngredients.get(ingredient));

                if (pantryQuantity < requiredQuantity) {
                    missingIngredients.put(ingredient, Integer.toString(requiredQuantity - pantryQuantity));
                }
            }
        }

        return missingIngredients;
    }

    /*
    public String getTheQuantity(String theNameOfRecipe, int number) {
        referenceForRecipe = FirebaseDatabase.getInstance().getReference().child("Recipe");


        referenceForRecipe.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot theSnapshot : snapshot.getChildren()) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return theReturnQuantity;
    } */
}
