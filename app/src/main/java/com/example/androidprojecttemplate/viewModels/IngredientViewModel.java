package com.example.androidprojecttemplate.viewModels;


import androidx.annotation.NonNull;

import com.example.androidprojecttemplate.models.IngredientData;
import com.example.androidprojecttemplate.models.UserData;
import com.example.androidprojecttemplate.models.FirebaseDB;
import com.example.androidprojecttemplate.views.IngredientPage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.*;

public class IngredientViewModel {
    private static IngredientViewModel instance;
    private final IngredientPage theData;

    FirebaseAuth theAuthenticationVariable;
    FirebaseUser user;
    DatabaseReference referenceForPantry;

    String theUsersEmailFromAuthenticationDatabase;

    public IngredientViewModel() {
        theData = new IngredientPage();
    }

    public static synchronized IngredientViewModel getInstance() {
        if (instance == null) {
            instance = new IngredientViewModel();
        }

        return instance;
    }

    public void getCurrentUser() {
        theAuthenticationVariable = FirebaseDB.getInstance().getFirebaseAuth();
        user = FirebaseDB.getInstance().getUser();
        theUsersEmailFromAuthenticationDatabase = FirebaseDB.getInstance().getEmail();
    }

    //used to check for duplicate ingredients
    Set<String> ingredients = new HashSet<String> ();

    public void addToFirebase(String name, String quantity, String calories, String expirationDate, IngredientCallback callback) {
        referenceForPantry = FirebaseDatabase.getInstance().getReference().child("Pantry");

        referenceForPantry.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot theSnapshot : snapshot.getChildren()) {

                    // Check if the ingredient exists in set
                    if (ingredients.contains(name)) {
                        callback.onCompleted(3); // already exists error message
                        return;
                    }

                    //check if quantity is invalid
                    int quantityInt;
                    try {
                        quantityInt = Integer.parseInt(quantity);
                    } catch (NumberFormatException e) {
                        callback.onCompleted(2); //error message
                        return;
                    }

                    if (quantityInt <= 0) {
                        //quantity is not positive
                        callback.onCompleted(4); //quantity not positive error message
                        return;
                    }

                    // Add the ingredient to Firebase
                    IngredientData newIngredient = new IngredientData(name, quantity, Integer.parseInt(calories), expirationDate);
                    referenceForPantry.child(theSnapshot.child("name").getValue().toString()).child("Ingredients").child(name).setValue(newIngredient)
                            .addOnSuccessListener(aVoid -> {
                                ingredients.add(name);
                                callback.onCompleted(1); // Success
                            })
                            .addOnFailureListener(e -> callback.onCompleted(2)); // Error

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onCompleted(2); // Error due to Firebase operation being cancelled
            }

        });
    }
}
