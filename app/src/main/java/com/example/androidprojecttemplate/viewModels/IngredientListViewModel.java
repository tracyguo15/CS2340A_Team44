package com.example.androidprojecttemplate.viewModels;


//import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

//import com.example.androidprojecttemplate.models.DataForPantry;
//import com.example.androidprojecttemplate.models.IngredientData;
//import com.example.androidprojecttemplate.models.UserData;
import com.example.androidprojecttemplate.models.FirebaseDB;
import com.example.androidprojecttemplate.views.IngredientListPage;
//import com.example.androidprojecttemplate.views.IngredientPage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.*;
//import java.util.logging.Handler;

public class IngredientListViewModel {
    private static IngredientListViewModel instance;
    private final IngredientListPage theData;

    private FirebaseAuth theAuthenticationVariable;
    private FirebaseUser user;
    private DatabaseReference referenceForPantry;
    private DatabaseReference referenceForSpecificUser;
    private DatabaseReference referenceForIngredient;

    private String theUsersEmailFromAuthenticationDatabase;
    private String theReturnQuantity = null;

    private String temp = "hel";

    public IngredientListViewModel() {
        theData = new IngredientListPage();
    }

    public static synchronized IngredientListViewModel getInstance() {
        if (instance == null) {
            instance = new IngredientListViewModel();
        }

        return instance;
    }

    public void getCurrentUser() {
        theAuthenticationVariable = FirebaseDB.getInstance().getFirebaseAuth();
        user = FirebaseDB.getInstance().getUser();
        theUsersEmailFromAuthenticationDatabase = FirebaseDB.getInstance().getEmail();
    }


    // Updates the quantity in firebase
    public void setTheQuantity(String theNameOfIngredient, int newQuantity) {
        referenceForPantry = FirebaseDatabase.getInstance().getReference().child("Pantry");
        referenceForPantry.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot theSnapshot : snapshot.getChildren()) {

                    String theEmailFromFirebase = theSnapshot.child("username")
                            .getValue().toString();
                    String theUsersName = theSnapshot.child("name").getValue().toString();

                    if (theEmailFromFirebase.equals(theUsersEmailFromAuthenticationDatabase)) {
                        referenceForSpecificUser = referenceForPantry.child(theUsersName)
                                .child("Ingredients");

                        // Will use a helper method to do the rest
                        helperMethod(referenceForSpecificUser,
                                theNameOfIngredient, newQuantity);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", "Something went wrong");
            }
        });
    }

    private void helperMethod(DatabaseReference theReference,
                                String theNameOfIngredient, int newQuantity) {
        Log.d("testReference", theReference.toString());
        theReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot theSnapshot : snapshot.getChildren()) {
                    Log.d("name", theNameOfIngredient);
                    Log.d("theSnapshot", String.valueOf(theSnapshot.child("name")));
                    if (theSnapshot.child("name").getValue(String.class)
                            .equals(theNameOfIngredient)) {

                        // Check if the new quantity is zero
                        if (newQuantity == 0) {
                            temp = theSnapshot.child("name").getValue(String.class);
                            theReference.child(temp).removeValue();
                        } else {
                            temp = theSnapshot.child("name").getValue(String.class);
                            referenceForIngredient = theReference.child(temp);
                            referenceForIngredient.child("quantity")
                                    .setValue(String.valueOf(newQuantity));
                        }
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", "Something went wrong 2");
            }
        });
    }

}