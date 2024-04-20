package com.example.androidprojecttemplate.viewModels;

import android.util.Log;
import androidx.annotation.NonNull;
import com.example.androidprojecttemplate.models.FirebaseDB;
import com.example.androidprojecttemplate.models.RecipeData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.*;


public class RecipeListViewModel {
    /**
     * Callback for updateRecipes().
     */
    public interface UpdateRecipesCallback {
        void onRecipesUpdated(ArrayList<RecipeData> recipesData);
    }

    private static RecipeListViewModel instance;
    private FirebaseUser user;
    private String username;
    private DatabaseReference recipeReference;

    /**
     * Initializes the viewModel with a reference to the recipes database.
     */
    public RecipeListViewModel() {
        recipeReference = FirebaseDatabase.getInstance().getReference().child("Cookbook");
    }

    public static synchronized RecipeListViewModel getInstance() {
        if (instance == null) {
            instance = new RecipeListViewModel();
        }
        return instance;
    }

    /**
     * Updates current user information.
     */
    public void getCurrentUser() {
        user = FirebaseDB.getInstance().getUser();
        Log.d("DETAIL PAGE USER", user.toString());

        String email = user.getEmail();
        Log.d("DETAIL PAGE MAIL", email);
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
     * Queries the database for the ingredients of a certain recipe and executes a callback.
     * @param recipeName - the name of the recipe to query
     * @param callback - the callback to execute
     */
    public void getRecipeIngredients(String recipeName, FirebaseCallback callback) {
        DatabaseReference referenceForRecipe = FirebaseDatabase.getInstance().getReference().child("Cookbook").child(recipeName).child("ingredients");
        HashMap<String, String> ingredients = new HashMap<>();

        referenceForRecipe.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ingredient : snapshot.getChildren()) {
                    String key = ingredient.getKey();
                    String value = String.valueOf(ingredient.child("quantity").getValue());
                    //Log.d("QUANTITY", value);
                    ingredients.put(key, value);
                }
                callback.onCallback(ingredients); // invoke the callback method with the fetched data
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Compiles all recipes in the database and executes a callback.
     * @param callback - the callback to execute while updating
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
                Log.d("error", "something went wrong");
            }
        });
    }

    /**
     * Queries pantry ingredients from database and executes a callback.
     * @param callback - callback to be called with queried data
     */
    public void getIngredients(FirebaseCallback callback) {
        // ensure user data updated
        getCurrentUser();

        DatabaseReference recipeRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Pantry")
                .child(username)
                .child("Ingredients");

        HashMap<String, String> ingredients = new HashMap<>();

        recipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ingredient : snapshot.getChildren()) {
                    String key = ingredient.getKey();
                    String value = String.valueOf(ingredient.child("quantity").getValue());

                    ingredients.put(key, value);
                }

                callback.onCallback(ingredients); // invoke the callback method with the fetched data
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", "something went wrong");
            }
        });
    }

    // canCook methods using Tracy's hashmaps
    public void canCook(String recipeName, CanCookCallback canCookCallback) {
        getCurrentUser(); // Ensures current user data is up to date

        getRecipeIngredients(recipeName, new FirebaseCallback() {

            @Override
            public void onCallback(HashMap<String, String> recipeIngredients) {
                getIngredients(new FirebaseCallback() {
                    @Override
                    public void onCallback(HashMap<String, String> pantryIngredients) {
                        //Log.d("test", "2");
                        boolean cooked = true;
                        for (String ingredient : recipeIngredients.keySet()) {
                            //Log.d("pantry quant", String.valueOf(pantryIngredients.containsKey("Bacon")));
                            if (pantryIngredients.containsKey(ingredient)) {
                                if (Integer.parseInt(pantryIngredients.get(ingredient)) < Integer.parseInt(recipeIngredients.get(ingredient))) {
                                    cooked = false;
                                    break;
                                }
                            } else if (!pantryIngredients.containsKey(ingredient)) {
                                cooked = false;
                                break;
                            }
                        }
                        canCookCallback.onResult(cooked);
                    }
                });
            }
        });
    }
}
