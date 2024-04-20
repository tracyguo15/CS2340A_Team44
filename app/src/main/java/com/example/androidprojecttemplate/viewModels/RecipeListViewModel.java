package com.example.androidprojecttemplate.viewModels;


//import android.content.Intent;
import android.util.Log;
//import android.widget.ListView;

import androidx.annotation.NonNull;

//import com.example.androidprojecttemplate.models.DataForPantry;
//import com.example.androidprojecttemplate.models.IngredientData;
//import com.example.androidprojecttemplate.models.UserData;
//import com.example.androidprojecttemplate.models.CookbookData;
import com.example.androidprojecttemplate.models.FirebaseDB;
//import com.example.androidprojecttemplate.models.Pair;
//import com.example.androidprojecttemplate.models.RecipeData;
//import com.example.androidprojecttemplate.views.IngredientListPage;
import com.example.androidprojecttemplate.views.RecipeListPage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//import java.sql.Array;
import java.util.*;
//import java.util.logging.Handler;

public class RecipeListViewModel {
    private static RecipeListViewModel instance;
    private static RecipeListPage theData;
    private Timer timer;
    private Timer timer2;

    private FirebaseAuth theAuthenticationVariable;
    private FirebaseUser user;
    private String userName;
    private DatabaseReference pantryRef;
    private DatabaseReference referenceForSpecificUser;
    private DatabaseReference referenceForRecipe;

    private String theUsersEmailFromAuthenticationDatabase;
    private final ArrayList<String[]> pantryQuantities;
    private final ArrayList<String[]> recipeQuantities;

    public RecipeListViewModel() {
        theData = new RecipeListPage();
        pantryQuantities = new ArrayList<>();
        recipeQuantities = new ArrayList<>();
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
//
//    //
//    public void getRecipeIngredients(String recipeName) {
//        //Clear the Arraylist before adding everything otherwise they'll stack
//        recipeQuantities.clear();
//        //String recipeName = recipe.getName();
//        //Log.d("RECIPEEEEE", recipe.toString());
//        Log.d("RECIPE NAME", recipeName);
//
//        //Should have a reference pointing directly at a recipe's ingredient list
//        referenceForRecipe = FirebaseDatabase.getInstance().getReference()
//                .child("Cookbook").child(recipeName).child("ingredients");
//        Log.d("RECIPE REFERENCE", referenceForRecipe.toString());
//
//
//        referenceForRecipe.addListenerForSingleValueEvent(new ValueEventListener() {
//            //snapshot should be pointing the value inside of a recipe
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                //This for each loop will iterate through each ingredient in the list
//                for (DataSnapshot snapshots : snapshot.getChildren()) {
//                    //Get the name and quantity
//                    Log.d("RLVM USER", String.valueOf(snapshot.getChildrenCount()));
//                    Log.d("RLVM USER", snapshots.toString());
//                    String name = snapshots.getKey();
//                    String quantity = String.valueOf(snapshots.child("quantity").getValue());
//
//                    //Store the name and quantity in the arraylist
//                    String[] recipeItem = new String[]{name, quantity};
//                    recipeQuantities.add(recipeItem);
//                    Log.d("RECIPE QUANTITIES", recipeQuantities.get(0)[0] + " " + recipeQuantities.get(0)[1]);
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d("RECIPE INGREDIENTS ERROR", error.toString());
//            }
//        });
//    }
//
//    public void getPantryIngredients() {
//        //Clear the Arraylist before adding everything otherwise they'll stack
//        pantryQuantities.clear();
//
//        //Sets up the variables needed to authenticate user's data
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        user = firebaseAuth.getCurrentUser();
//        String email = user.getEmail();
//        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
//                .child("Users");
//
//        userRef.addValueEventListener(new ValueEventListener() {
//            //The snapshot should be pointed to Users in the database
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                //Each child should be pointing to a specific user
//                for (DataSnapshot snapshots : snapshot.getChildren()) {
//                    //Grabs the email of the user in the snapshot
//                    String theEmailFromFirebase = snapshots.child("username")
//                            .getValue().toString();
//
//                    //Checks if the email from the snapshot matches the email of the current user
//                    if (theEmailFromFirebase.equals(email)) {
//                        /*
//                        Only runs the code the the emails matches, which means we are checking the
//                        correct pantry
//                        Sets the reference to a user's Pantry Ingredients
//                        Since the snapshot should be pointing at the correct user's pantry,
//                        snapshots.child("Name").getValue().toString should just be the user's name
//                        */
//                        pantryRef = FirebaseDatabase.getInstance().getReference()
//                                .child("Pantry").child(snapshots.child("name")
//                                        .getValue().toString()).child("Ingredients");
//
//                        //I don't like the idea of a nested event listener,
//                        // but idk what else to do rn
//                        pantryRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                //Each snapshot should be point at an individual ingredient in a
//                                //user's pantry
//                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                                    //Get the pantry ingredient's name and quantity
//                                    if (!snapshot1.getKey().equals("username")) {
//                                        String name = snapshot1.getKey();
//                                        String quantity = (String) snapshot1
//                                                .child("quantity").getValue();
//
//                                        //Add the name and quantity to the Arraylist of String[]
//                                        String[] pantryItem = new String[]{name, quantity};
//                                        Log.d("PANTRY ITEM", pantryItem[0] + " " + pantryItem[1]);
//                                        pantryQuantities.add(pantryItem);
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                                Log.d("PANTRY NESTED ERROR", error.toString());
//                            }
//                        });
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d("USER ERROR", error.toString());
//            }
//        });
//
//        Log.d("pantryQuantities", Integer.toString(pantryQuantities.size()));
//    }
//
//    public boolean canCook(String recipeName) {
//        //Boolean variable to be returned
//        double random = Math.random() * 100;
//        boolean cooked = false;
//
//        //Grab an instance of the current user
//        getCurrentUser();
//
//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                //Get String[] of all the ingredients necessary and available
//                getRecipeIngredients(recipeName);
//                //Log.d("canCook recipe name", recipe.toString());
//                getPantryIngredients();
//            }
//        }, 1000);
//
//        Log.d("recipeIngredients", recipeQuantities.toString());
//        Log.d("pantryIngredients", pantryQuantities.toString());
//
//        //Go through each ingredient necessary for recipe
//        for (String[] r : recipeQuantities) {
//            //Go through every ingredient in the pantry
//            for (String[] p : pantryQuantities) {
//                //Compare the names to see if they match
//                if (r[0].equals(p[0])) {
//                    //If the pantry amount < recipe amount
//                    if (Integer.parseInt(p[1]) < Integer.parseInt(r[1])) {
//                        break;
//                    } else if (Integer.parseInt(p[1]) > Integer.parseInt(r[1])) {
//                        cooked = true;
//                    }
//                }
//            }
//
//            //If cooked != true, then that means either the quantity was too low
//            //or it wasn't in the pantry
//            if (cooked != true) {
//                break;
//            }
//        }
//
//        return cooked;
//    }

    public void getRecipeIngredients2(String recipeName, FirebaseCallback callback) {
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
                Log.d("Error", "Something went wrong");
            }
        });
    }

    public void getIngredients2(FirebaseCallback callback) {
        DatabaseReference pantryRef = FirebaseDatabase.getInstance().getReference().child("Pantry");
                //.child(userName).child("Ingredients");
        HashMap<String, String> ingredients = new HashMap<>();

        //Sets up the variables needed to authenticate user's data
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        String email = user.getEmail();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("Users");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Each child should be pointing to a specific
                for (DataSnapshot snapshots : snapshot.getChildren()) {
                    //Grabs the email of the user in the snapshot
                    String theEmailFromFirebase = snapshots.child("username")
                            .getValue().toString();

                    //Checks if the email from the snapshot matches the
                    if (theEmailFromFirebase.equals(email)) {
                        String userName = String.valueOf(snapshots.child("name").getValue());
                        DatabaseReference userPantry = pantryRef.child(userName).child("Ingredients");
                        userPantry.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ingredient : snapshot.getChildren()) {
                                    if (!"username".equals(ingredient.getKey())) {
                                        Log.d("INGREDIENT", ingredient.toString());
                                        String key = ingredient.getKey();
                                        String value = ingredient.child("quantity").getValue(String.class);
                                        ingredients.put(key, value);
                                    }
                                }
                                callback.onCallback(ingredients); // invoke the callback method with the fetched data
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.d("PANTRY REF", "Something went wrong");
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
    }


    /**
     * This method will return a hashmap of all the ingredients and their quantities
     * that are required for a certain recipe but are missing from the user's pantry
     *
     * @param recipeName
     * @return HashMap<String, String> missingIngredients
     */
    public void getMissingIngredients(String recipeName, MissingIngredientsCallback missingIngredientsCallback) {
        getCurrentUser(); // Ensures current user data is up to date

        getRecipeIngredients2(recipeName, new FirebaseCallback() {
            @Override
            public void onCallback(HashMap<String, String> recipeIngredients) {
                getIngredients2(new FirebaseCallback() {
                    @Override
                    public void onCallback(HashMap<String, String> pantryIngredients) {
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

                        missingIngredientsCallback.onCallback(missingIngredients);
                    }
                });
            }
        });
    }


    //canCook methods using Tracy's hashmaps
    public void canCook2(String recipeName, CanCookCallback canCookCallback) {
        getCurrentUser(); // Ensures current user data is up to date

        getRecipeIngredients2(recipeName, new FirebaseCallback() {
            @Override
            public void onCallback(HashMap<String, String> recipeIngredients) {
                getIngredients2(new FirebaseCallback() {
                    @Override
                    public void onCallback(HashMap<String, String> pantryIngredients) {
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
}

