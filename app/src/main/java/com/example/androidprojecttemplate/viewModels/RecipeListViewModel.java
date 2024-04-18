package com.example.androidprojecttemplate.viewModels;

import android.util.Log;
import androidx.annotation.NonNull;

import com.example.androidprojecttemplate.models.DataForPantry;
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

import java.sql.Array;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import java.util.*;


public class RecipeListViewModel {
    public interface UpdateRecipesCallback {
        void onRecipesUpdated(ArrayList<RecipeData> recipesData);
    }

    private static RecipeListViewModel instance;

    // later
    private FirebaseAuth theAuthenticationVariable;
    private FirebaseUser user;

    // maybe need
    //private DatabaseReference pantryRef;
    //private DatabaseReference referenceForSpecificUser;
    private DatabaseReference recipeReference;
    //private DatabaseReference referenceForPantry;

    //private String theUsersEmailFromAuthenticationDatabase;
    //private String theReturnQuantity = null;


    public RecipeListViewModel() {
        //theData = new RecipeListPage();
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
        //theUsersEmailFromAuthenticationDatabase = FirebaseDB.getInstance().getEmail();

        recipeReference = FirebaseDatabase.getInstance().getReference().child("Cookbook");
    }

    public interface RecipeDataListener {
        void onRecipeDataReceived(ArrayList<RecipeData> recipesData);
    }

    /**
     * Compiles all recipes in the database.
     *
     * @return the recipes
     */
    public void updateRecipes(UpdateRecipesCallback callback) {
        ArrayList<RecipeData> recipesData = new ArrayList<>();

        recipeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshots : snapshot.getChildren()) {
                    String name = snapshots.getKey();
                    int time = snapshots.child("time").getValue(Integer.class);

                    RecipeData data = new RecipeData();
                    data.setName(name);
                    data.setTime(time);

                    recipesData.add(data);
                }

                callback.onRecipesUpdated(recipesData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("RECIPE INGREDIENTS ERROR", error.toString());
            }
        });
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

    /*
    public ArrayList<String[]> getRecipeIngredients(RecipeData recipe) {
        //Arraylist of String arrays to hold each ingredient and its quantities
        // ArrayList<String[]> recipeQuantities = new ArrayList<>();

        //Should have a reference pointing directly at a recipe's ingredient list


        referenceForRecipe.addValueEventListener(new ValueEventListener() {
            //snapshot should be pointing the value inside of a recipe
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //This for each loop will iterate through each ingredient in the list
                for (DataSnapshot snapshots : snapshot.getChildren()) {
                    //Get the name and quantity
                    String name = snapshots.getKey();
                    String quantity = Integer.toString((int) snapshots.getValue());

                    //Store the name and quantity in the arraylist
                    recipeQuantities.add(new String[]{name, quantity});
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("RECIPE INGREDIENTS ERROR", error.toString());
            }
        });

        return recipeQuantities;
    }*/

    /*
    public ArrayList<String[]> getPantryIngredients() {
        //Arraylist of String arrays to hold each ingredient and its quantities
        ArrayList<String[]> pantryQuantities = new ArrayList<>();

        //Sets up the variables needed to authenticate user's data
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        String email = user.getEmail();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("Users");

        userRef.addValueEventListener(new ValueEventListener() {
            //The snapshot should be pointed to Users in the database
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Each child should be pointing to a specific user
                for (DataSnapshot snapshots : snapshot.getChildren()) {
                    //Grabs the email of the user in the snapshot
                    String theEmailFromFirebase = snapshots.child("username")
                            .getValue().toString();

                    //Checks if the email from the snapshot matches the email of the current user
                    if (theEmailFromFirebase.equals(email)) {

                        Only runs the code the the emails matches, which means we are checking the
                        correct pantry



                        Sets the reference to a user's Pantry Ingredients
                        Since the snapshot should be pointing at the correct user's pantry,
                        snapshots.child("Name").getValue().toString should just be the user's name

                        pantryRef = FirebaseDatabase.getInstance().getReference()
                                .child("Pantry").child(snapshots.child("name")
                                        .getValue().toString()).child("Ingredients");

                        //I don't like the idea of a nested event listener, but idk what else to do
                        pantryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //Each snapshot should be point at an individual ingredient in a
                                //user's pantry
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    //Get the pantry ingredient's name and quantity
                                    String name = snapshot1.getKey();
                                    String quantity = Integer.toString((int) snapshot1
                                            .child("quantity").getValue());

                                    //Add the name and quantity to the Arraylist of String[]
                                    pantryQuantities.add(new String[]{name, quantity});
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.d("PANTRY NESTED ERROR", error.toString());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("USER ERROR", error.toString());
            }
        });

        return pantryQuantities;
    }*/
