package com.example.androidprojecttemplate.viewModels;


//import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

//import com.example.androidprojecttemplate.models.DataForPantry;
//import com.example.androidprojecttemplate.models.IngredientData;
//import com.example.androidprojecttemplate.models.UserData;
import com.example.androidprojecttemplate.models.FirebaseDB;
import com.example.androidprojecttemplate.models.RecipeData;
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
    private DatabaseReference pantryRef;
    private DatabaseReference referenceForSpecificUser;
    private DatabaseReference referenceForRecipe;

    private String userName;


    private String theUsersEmailFromAuthenticationDatabase;
    private String theReturnQuantity = null;

    private String temp = "hel";

    private int changer = 0;

    private Timer timer;

    HashMap<String, RecipeData> cookbook;

    HashMap<String, Integer> pantry;

    public RecipeListViewModel() {
        theData = new RecipeListPage();
        getCurrentUser();
        getDatabases();
    }

    private interface InitializingPantryCallback {
        void onCallback(HashMap map);
    }

    private void getPantryDatabase(InitializingPantryCallback callback) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot ref;
                String userEmail = theUsersEmailFromAuthenticationDatabase;
                for (DataSnapshot testPantry : snapshot.getChildren()) {
                    if (userEmail.equals(testPantry.child("username").getValue().toString())) {
                        userName = testPantry.getKey();
                        ref = testPantry.child("Ingredients");
                        for (DataSnapshot item : ref.getChildren()) {
                            if (item.getKey().equals("username")) {
                                continue;
                            }
                            pantry.put(item.getKey(), Integer.parseInt(item.child("quantity").getValue().toString()));
                        }
                    }
                }

                callback.onCallback(pantry);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", "error accessing pantry database!");
            }
        };

        pantryRef.addListenerForSingleValueEvent(valueEventListener);
    }

    private void getCookbookDatabase(InitializingPantryCallback callback) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RecipeData recipe;
                for (DataSnapshot R : snapshot.getChildren()) {
                    String name = R.getKey();
                    String desc = R.child("description").getValue().toString();
                    int time = Integer.parseInt(R.child("time").getValue().toString());
                    recipe = new RecipeData(name, desc, time);
                    for (DataSnapshot item : R.child("ingredients").getChildren()) {
                        String itemName = item.getKey();
                        int quantity = Integer.parseInt(item.child("quantity").getValue().toString());
                        recipe.add(itemName, quantity);
                    }
                    cookbook.put(name, recipe);
                }

                callback.onCallback(cookbook);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", "trouble making cooking databse");
            }
        };

        cookbookRef.addListenerForSingleValueEvent(valueEventListener);
    }

    private DatabaseReference cookbookRef;
    private void getDatabases() {
        Log.d("OUT", "starting callbacks");

        pantryRef = FirebaseDatabase.getInstance().getReference().child("Pantry");
        cookbookRef = FirebaseDatabase.getInstance().getReference().child("Cookbook");
        pantry = new HashMap<>();
        cookbook = new HashMap<>();
        getPantryDatabase(new InitializingPantryCallback() {
            @Override
            public void onCallback(HashMap map) {
                Log.d("OUT", pantry.size() + "pantry");
                Log.d("OUT", userName);
            }
        });
        getCookbookDatabase(new InitializingPantryCallback() {
            @Override
            public void onCallback(HashMap map) {
                Log.d("OUT", cookbook.size() + "cookbook");
            }
        });
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

    /**
     * Returns a HashMap for ingredients needed to make the recipe. The key is the ingredient name,
     * and the value is an integer for how much is needed.
     * @param recipe RecipeData of the recipe you are trying to make
     * @return a HashMap(IngredientName, IngredientQuantity)
     */
    public HashMap<String, Integer> getRecipeIngredients(RecipeData recipe) {
        HashMap<String, Integer> needed = new HashMap<>();
        for (String item : recipe.keySet()) {
            needed.put(item, recipe.get(item));
        }
        return needed;
    }

    /**
     * This method will return a hashmap of all the ingredients and their quantities
     * that are in the user's pantry
     * @return HashMap<String, String> ingredients
     */
    public HashMap<String, Integer> getIngredients() {
        return pantry;
    }


    /**
     * This method will return a hashmap of all the ingredients and their quantities
     * that are required for a certain recipe but are missing from the user's pantry
     * @param recipeName
     * @return HashMap<String, String> missingIngredients
     */
    public HashMap<String, Integer> getMissingIngredients(String recipeName) {
        HashMap<String, Integer> recipe = new HashMap<>();
        HashMap<String, Integer> missing = new HashMap<>();

        for (String item : recipe.keySet()) {
            if (!pantry.containsKey(item)) {
                missing.put(item, recipe.get(item));
            } else {
                int recipeQuantity = recipe.get(item);
                int pantryQuantity = pantry.get(item);
                if (recipeQuantity > pantryQuantity) {
                    missing.put(item, recipeQuantity - pantryQuantity);
                }
            }
        }

        return missing;
    }

    /** Gets all of the neeeded missing ingredients in the pantry
     * and returns it as a HashMap
     * @return a HashMap(IngredientName, NeededIngredientQuantity)
    */
    public HashMap<String, Integer> getAllMissingIngredients() {
        int i = 0;
        HashMap<String, Integer> needed = new HashMap<>();
        for (RecipeData recipe : cookbook.values()) {
            if (!canCook(recipe)) {
                HashMap<String, Integer> missing = getMissingIngredients(recipe.getName());
                for (String item : missing.keySet()) {
                    if (!needed.containsKey(item)) {
                        needed.put(item, missing.get(item));
                    } else {
                        int previousQuantity = needed.get(item);
                        needed.put(item, previousQuantity + recipe.get(item));
                    }
                }
            }
        }
        return needed;
    }

    public boolean canCook(RecipeData recipe) {
        for (String item : recipe.keySet()) {
            if (!pantry.containsKey(item)) {
                return false;
            } else {
                int recipeQuantity = recipe.get(item);
                int pantryQuantity = pantry.get(item);
                if (recipeQuantity > pantryQuantity) {
                    return false;
                }
            }
        }
        return true;
    }

}
        