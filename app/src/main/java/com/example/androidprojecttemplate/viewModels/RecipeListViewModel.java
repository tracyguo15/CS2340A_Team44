package com.example.androidprojecttemplate.viewModels;


//import android.content.Intent;
import android.util.AtomicFile;
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

    // later
    private FirebaseAuth theAuthenticationVariable;
    private FirebaseUser user;

    // maybe need
    private DatabaseReference pantryRef;
    private DatabaseReference referenceForSpecificUser;
    private DatabaseReference recipeReference;
    private DatabaseReference referenceForPantry;

    private String theUsersEmailFromAuthenticationDatabase;
    private String theReturnQuantity = null;

    private String temp = "hel";

    private int changer = 0;

    private Timer timer;

    private RecipeListPage theData;

    public RecipeListViewModel() {
        theData = new RecipeListPage();
        recipeReference = FirebaseDatabase.getInstance().getReference().child("Cookbook");
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

        recipeReference = FirebaseDatabase.getInstance().getReference().child("Cookbook");
    }

    /**
     * Compiles all recipes in the database.
     *
     * @return the recipes
     */
    public ArrayList<RecipeData> getRecipes() {
        ArrayList<RecipeData> recipes = new ArrayList<>();

        recipeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshots : snapshot.getChildren()) {
                    String name = snapshots.getKey();
                    int time = snapshots.child("time").getValue(Integer.class);

                    RecipeData data = new RecipeData();
                    data.setName(name);
                    data.setTime(time);

                    recipes.add(data);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("RECIPE INGREDIENTS ERROR", error.toString());
            }
        });

        return recipes;
    }

    public ArrayList<String[]> getRecipeIngredients(RecipeData recipe) {
        return null;
    }

    public ArrayList<String[]> getPantryIngredients() {
        return null;
    }

    /**
     * Determines if a recipe can be cooked.
     *
     * @return true if a recipe can be cooked, false otherwise
     */
    public boolean canCook() {
        return true;
    }
}