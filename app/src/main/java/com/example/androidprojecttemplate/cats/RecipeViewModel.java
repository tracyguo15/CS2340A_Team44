package com.example.androidprojecttemplate.cats;

import androidx.annotation.NonNull;

import com.example.androidprojecttemplate.models.FirebaseDB;
import com.example.androidprojecttemplate.views.RecipePage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;

public class RecipeViewModel {
    private static RecipeViewModel instance;
    private final RecipePage theData;

    private FirebaseAuth theAuthenticationVariable;
    private FirebaseUser user;
    private DatabaseReference referenceForPantry;
    private DatabaseReference cookbookDatabase;
    private DatabaseReference referenceForSpecificUser;
    private DatabaseReference referenceForIngredientDatabase;

    private String theUsersEmailFromAuthenticationDatabase;

    private String temp;
    private Timer timer;

    private Timer timer2;

    private boolean isItInIngredientDatabase = false;

    public RecipeViewModel() {
        theData = new RecipePage();
    }

    public static synchronized RecipeViewModel getInstance() {
        if (instance == null) {
            instance = new RecipeViewModel();
        }

        return instance;
    }

    public void getCurrentUser() {
        theAuthenticationVariable = FirebaseDB.getInstance().getFirebaseAuth();
        user = FirebaseDB.getInstance().getUser();
        theUsersEmailFromAuthenticationDatabase = FirebaseDB.getInstance().getEmail();
    }

    private static ArrayList<String> addedRecipeNames = new ArrayList<>();

    private void addTheRecipesFromFirebase() {
        cookbookDatabase = FirebaseDatabase.getInstance().getReference().child("Cookbook");

        cookbookDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshots : snapshot.getChildren()) {
                    String theEmailFromFirebase = snapshots.child("username")
                            .getValue().toString();
                    String theUsersName = snapshots.child("name").getValue().toString();

                    if (theEmailFromFirebase.equals(theUsersEmailFromAuthenticationDatabase)) {
                        //referenceForSpecificUser = cookbookDatabase.child
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
