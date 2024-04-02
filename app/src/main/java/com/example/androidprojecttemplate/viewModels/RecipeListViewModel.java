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
    /*
    public String getTheQuantity(String theNameOfIngredient, int number) {
        referenceForPantry = FirebaseDatabase.getInstance().getReference().child("Pantry");


        referenceForPantry.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot theSnapshot : snapshot.getChildren()) {

                    String theEmailFromFirebase = theSnapshot.child("username")
                            .getValue().toString();
                    String theUsersName = theSnapshot.child("name").getValue().toString();

                    if (theEmailFromFirebase.equals(theUsersEmailFromAuthenticationDatabase)) {
                        referenceForSpecifcUser = referenceForPantry.child(theUsersName)
                                .child("Ingredients");

                        // Will use a helper method to do the rest
                        theReturnQuantity = helperMethod(referenceForSpecifcUser,
                                theNameOfIngredient, number);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", "Something went wrong");
            }
        });

        return theReturnQuantity;
    } */

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
