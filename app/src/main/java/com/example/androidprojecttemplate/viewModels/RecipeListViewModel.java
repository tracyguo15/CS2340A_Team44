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

    /**
     * Initializes the viewModel with a reference to the recipes database.
     */

    public RecipeListViewModel() {
        getCurrentUser();
        theUsersEmailFromAuthenticationDatabase = user.getEmail();
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
                getCookbookDatabase(new InitializingPantryCallback() {
                    @Override
                    public void onCallback(HashMap map) {
                        Log.d("OUT", cookbook.size() + "cookbook");
                    }
                });
            }
        });
    }

    public String getUserName() {
        return userName;
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

    public ArrayList<String[]> getRecipeIngredients(RecipeData recipe) {
        return null;
    }

    public ArrayList<String[]> getPantryIngredients() {
        return null;
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

    public HashMap<String, Integer> getIngredients() {
        return pantry;
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
                        Log.d("test", "2");
                        boolean cooked = true;
                        for (String ingredient : recipeIngredients.keySet()) {
                            Log.d("pantry quant", String.valueOf(pantryIngredients.containsKey("Bacon")));
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
    /**
     * This method will return a hashmap of all the ingredients and their quantities
     * that are required for a certain recipe but are missing from the user's pantry
     * @param recipeName
     * @return HashMap<String, String> missingIngredients
     */
    public HashMap<String, Integer> getMissingIngredients(String recipeName) {
        HashMap<String, Integer> recipe = getRecipeIngredients(cookbook.get(recipeName));
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

