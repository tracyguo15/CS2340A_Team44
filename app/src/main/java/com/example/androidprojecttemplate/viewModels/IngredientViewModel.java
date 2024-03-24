package com.example.androidprojecttemplate.viewModels;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.androidprojecttemplate.models.IngredientData;
import com.example.androidprojecttemplate.models.UserData;
import com.example.androidprojecttemplate.models.firebaseAuthSingleton;
import com.example.androidprojecttemplate.views.IngredientPage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class IngredientViewModel {
    private static IngredientViewModel instance;
    private final IngredientPage theData;
    public static int temp = 0;

    public static int valueToBeReturnedFromHelperMethod = 0;

    FirebaseAuth theAuthenticationVariable;
    FirebaseUser user;
    DatabaseReference referenceForPantry;
    DatabaseReference referenceForUsersIngredients;

    private boolean doesIngredientExists;

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
        theAuthenticationVariable = firebaseAuthSingleton.getInstance().getTheInstanceFromFirebase();
        user = firebaseAuthSingleton.getInstance().getUser();
        theUsersEmailFromAuthenticationDatabase = firebaseAuthSingleton.getInstance().getEmail();
    }

    public void addToFirebase(String name, String quantity, String calories, String expirationDate, IngredientCallback callback) {
        referenceForPantry = FirebaseDatabase.getInstance().getReference().child("Pantry");
        // get the user-specific pantry reference
        DatabaseReference userPantryRef = referenceForPantry.child(user.getDisplayName());

        userPantryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean exists = false;
                // Check if the ingredient exists
                if (snapshot.hasChild(name)) {
                    exists = true;
                }

                if (exists) {
                    // The ingredient already exists
                    callback.onCompleted(3); // already exists error message
                } else {
                    //check if quantity is invalid
                    int quantityInt;
                    try {
                        //parse from string into int
                        quantityInt = Integer.parseInt(quantity);
                    } catch (NumberFormatException e) {
                        callback.onCompleted(2); //error message
                        return; // Stop execution
                    }

                    if (quantityInt <= 0) {
                        //quantity is not positive
                        callback.onCompleted(4); //quantity not positive error message
                    } else {
                        // Add the ingredient to Firebase since the quantity is positive
                        IngredientData newIngredient = new IngredientData(name, quantity, calories, expirationDate);
                        userPantryRef.child(name).setValue(newIngredient)
                                .addOnSuccessListener(aVoid -> callback.onCompleted(1)) // Success
                                .addOnFailureListener(e -> callback.onCompleted(2)); // Error
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onCompleted(2); // Error due to Firebase operation being cancelled
            }
        });
    }


    private int checkAndThenAddIngredient(DatabaseReference theReferenceFromMethod, String Name, String Quantity, String Calories, String ExpirationDate) {
        referenceForUsersIngredients = theReferenceFromMethod.child("Ingredients");
        doesIngredientExists = false;

        // Loop to check
        referenceForUsersIngredients.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Will add the data first, then check whether it exists or not
                IngredientData theData = new IngredientData(Name, Quantity, Calories, ExpirationDate);
                referenceForUsersIngredients.child(Name).setValue(theData);

                /*
                // Loop will check whether the ingredient exists in the database
                // and if it has a value greater than zero
                for (DataSnapshot theSnapshot: snapshot.getChildren()) {
                    String theNameOfIngredient = theSnapshot.child("Name").getValue().toString();
                    String theQuantity = theSnapshot.child("Quantity").getValue().toString();

                    if (Name.equals(theNameOfIngredient) && theQuantity.contains("0")) {
                        doesIngredientExists = true;
                        break;
                    }
                }


                 */
                // If the boolean is false, it's good to add the value
                if (!doesIngredientExists) {
                    valueToBeReturnedFromHelperMethod = 1;
                } else {
                    valueToBeReturnedFromHelperMethod = 3;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                valueToBeReturnedFromHelperMethod = 2;
            }
        });

        return valueToBeReturnedFromHelperMethod;
    }
}
