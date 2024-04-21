package com.example.androidprojecttemplate.viewModels;


//import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

//import com.example.androidprojecttemplate.models.DataForPantry;
//import com.example.androidprojecttemplate.models.IngredientData;
//import com.example.androidprojecttemplate.models.UserData;
import com.example.androidprojecttemplate.models.FirebaseDB;
//import com.example.androidprojecttemplate.views.IngredientPage;
import com.example.androidprojecttemplate.views.ShoppingListScrollablePage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.*;
//import java.util.logging.Handler;

public class ShoppingListScrollableViewModel {
    private static ShoppingListScrollableViewModel instance;
    private final ShoppingListScrollablePage theData;
    private FirebaseAuth theAuthenticationVariable;
    private FirebaseUser user;
    private DatabaseReference referenceForPantry;
    private DatabaseReference referenceForSpecificUser;
    private DatabaseReference referenceForIngredient;
    private String theUsersEmailFromAuthenticationDatabase;


    private String temp = "hel";


    private Timer timer;

    public ShoppingListScrollableViewModel() {
        theData = new ShoppingListScrollablePage();
    }

    public static synchronized ShoppingListScrollableViewModel getInstance() {
        if (instance == null) {
            instance = new ShoppingListScrollableViewModel();
        }

        return instance;
    }

    public void getCurrentUser() {
        theAuthenticationVariable = FirebaseDB.getInstance().getFirebaseAuth();
        user = FirebaseDB.getInstance().getUser();
        theUsersEmailFromAuthenticationDatabase = FirebaseDB.getInstance().getEmail();
    }

   // Update the quantity in firebase
    public void setTheQuantity(String theNameOfIngredient, int newQuantity) {
        referenceForPantry = FirebaseDatabase.getInstance().getReference().child("Shopping_List");

        referenceForPantry.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot theSnapshot : snapshot.getChildren()) {

                    String theEmailFromFirebase = theSnapshot.child("username")
                            .getValue().toString();
                    String theUsersName = theSnapshot.child("name").getValue().toString();

                    if (theEmailFromFirebase.equals(theUsersEmailFromAuthenticationDatabase)) {
                        referenceForSpecificUser = referenceForPantry.child(theUsersName);

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
        theReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot theSnapshot : snapshot.getChildren()) {
                    if (theSnapshot.child("name").getValue(String.class)
                            .equals(theNameOfIngredient)) {

                        // Checks if the new quantitiy is zero
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