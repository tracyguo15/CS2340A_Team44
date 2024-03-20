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
    String theNameOfTheUser = "FuckingHateThisShit";

    FirebaseAuth theAuthenticationVariable;
    FirebaseUser user;
    DatabaseReference referenceForPantry;
    DatabaseReference referenceForUsername;

    String theUsersEmail;

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
        theUsersEmail = firebaseAuthSingleton.getInstance().getEmail();
    }

    public int addToFirebase(String theName, String theQuantity, String theCalories, String theExpirationDate) {
        referenceForPantry = FirebaseDatabase.getInstance().getReference().child("Pantry");
        referenceForUsername = FirebaseDatabase.getInstance().getReference().child("Users");

        // First need to get the name of the user
        referenceForUsername.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot theSnapshot: snapshot.getChildren()) {

                    String theEmailFromFirebase = theSnapshot.child("username")
                            .getValue().toString();
                    if (theEmailFromFirebase.equals(theUsersEmail)) {
                        theNameOfTheUser = theSnapshot.child("name").getValue().toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                temp = 2;
            }
        });


        // Now can add the user to the pantry database
        referenceForPantry.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                IngredientData theData = new IngredientData(theName, theQuantity, theCalories, theExpirationDate, theNameOfTheUser);

                //referenceForPantry.child(theData.getTheUsername()).setValue(theData);
                referenceForPantry.child(theData.getTheUsername()).child("Ingredients").child(theData.getTheName()).setValue(theData);
                temp = 1;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                temp = 2;
            }
        });

        return temp;
    }
}
