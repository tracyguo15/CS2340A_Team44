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

    public int addToFirebase(String Name, String Quantity, String Calories, String ExpirationDate) {
        referenceForPantry = FirebaseDatabase.getInstance().getReference().child("Pantry");

        referenceForPantry.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot theSnapshot: snapshot.getChildren()) {

                    String theEmailFromFirebase = theSnapshot.child("username")
                            .getValue().toString();

                    if (theEmailFromFirebase.equals(theUsersEmailFromAuthenticationDatabase)) {
                        //Found the associated email and name for the user
                        // Can now check whether the ingredient already exists or not
                        // Will do this in a helper method
                        temp = checkAndThenAddIngredient(referenceForPantry.child(theSnapshot.child("name")
                                .getValue().toString()), Name, Quantity, Calories, ExpirationDate);


                    }

                }
                //referenceForPantry.child(theData.getTheUsername()).child("Ingredients").child(theData.getTheName()).setValue(theData);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                temp = 2;
            }
        });

        return temp;
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
