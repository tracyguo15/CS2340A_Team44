package com.example.androidprojecttemplate.viewModels;


//import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

//import com.example.androidprojecttemplate.models.DataForPantry;
//import com.example.androidprojecttemplate.models.IngredientData;
//import com.example.androidprojecttemplate.models.UserData;
import com.example.androidprojecttemplate.models.DataForPantry;
import com.example.androidprojecttemplate.models.FirebaseDB;
//import com.example.androidprojecttemplate.views.IngredientPage;
import com.example.androidprojecttemplate.views.ShoppingListTheCheckBoxPage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.*;
//import java.util.logging.Handler;

public class ShoppingListTheCheckBoxViewModel {
    private static ShoppingListTheCheckBoxViewModel instance;
    private final ShoppingListTheCheckBoxPage theData;

    private FirebaseAuth theAuthenticationVariable;
    private FirebaseUser user;
    private DatabaseReference theReference;
    private DatabaseReference referenceForShoppingList;
    private DatabaseReference referenceForPantry;

    private String theUsersEmailFromAuthenticationDatabase;
    private String theNameOfTheUser;

    private String theReturned;
    private boolean isInPantryDatabase;


    public ShoppingListTheCheckBoxViewModel() {
        theData = new ShoppingListTheCheckBoxPage();
    }

    public static synchronized ShoppingListTheCheckBoxViewModel getInstance() {
        if (instance == null) {
            instance = new ShoppingListTheCheckBoxViewModel();
        }

        return instance;
    }

    public void getCurrentUser() {
        theAuthenticationVariable = FirebaseDB.getInstance().getFirebaseAuth();
        user = FirebaseDB.getInstance().getUser();
        theUsersEmailFromAuthenticationDatabase = FirebaseDB.getInstance().getEmail();
    }

    public void sendToFirebase(ArrayList<String> itemsToBeRemovedFromShoppingDatabase,
                               ArrayList<String> theQuantities) {
        theReference = FirebaseDatabase.getInstance().getReference().child("Users");
        theReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot theSnapshot : snapshot.getChildren()) {

                    String theEmailFromFirebase = theSnapshot.child("username")
                            .getValue().toString();
                    String theUsersName = theSnapshot.child("name").getValue().toString();

                    if (theEmailFromFirebase.equals(theUsersEmailFromAuthenticationDatabase)) {
                        theReturned = theUsersName;
                        Log.d("TheName2", theReturned);
                        deleteFromShoppingList(
                                itemsToBeRemovedFromShoppingDatabase, theQuantities, theReturned);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", "Something went wrong");
            }
        });
    }

    private void deleteFromShoppingList(ArrayList<String> itemsToBeRemovedFromShoppingDatabase,
                                        ArrayList<String> theQuantities,
                                        String theNameOfTheUser) {

        referenceForShoppingList = FirebaseDatabase.getInstance()
                .getReference()
                .child("Shopping_List")
                .child(theNameOfTheUser);

        Log.d("TheReference", referenceForShoppingList.toString());

        //loop to remove, have to do linear search
        for (String theNameOfIngredient: itemsToBeRemovedFromShoppingDatabase) {
            referenceForShoppingList.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot theSnapshot : snapshot.getChildren()) {
                        Log.d("fuck", theSnapshot.toString());
                        if (theNameOfIngredient.equals(theSnapshot.getKey())) {
                            referenceForShoppingList.child(theNameOfIngredient).removeValue();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("Error", "Something wen't wrong ");
                }
            });
        }

        // Now, add those ingredients to the pantry (watch out for duplicates)
        addToPantry(itemsToBeRemovedFromShoppingDatabase, theQuantities, theNameOfTheUser);
    }

    private void addToPantry(ArrayList<String> itemsToAddToPantry,
                             ArrayList<String> theQuantities,
                             String name) {
        referenceForPantry = FirebaseDatabase.getInstance()
                .getReference().child("Pantry")
                .child(name).child("Ingredients");

        // Need to check for duplicates
        //for(String theNameOfIngredient: ItemsToAddToPantry) {
        for (int i = 0; i < itemsToAddToPantry.size(); i++) {
            String theNewName = itemsToAddToPantry.get(i);
            String theNewQuantity = theQuantities.get(i);

            isInPantryDatabase = false;
            referenceForPantry.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot theSnapshot : snapshot.getChildren()) {
                        if (theNewName.equals(theSnapshot.getKey())) {
                            // The ingredient is already in the pantry database, break
                            isInPantryDatabase = false;
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("Error", "Something went wrong");
                }
            });

            if (!isInPantryDatabase) {
                // Can now add it
                DataForPantry theItem = new DataForPantry(theNewName, theNewQuantity, "0");
                referenceForPantry.child(theNewName).setValue(theItem);
            }
        }
    }
}