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
    private DatabaseReference referenceForSpecifcUser;
    private DatabaseReference referenceForIngredient;

    private String theUsersEmailFromAuthenticationDatabase;
    private String theReturnQuantity = null;

    private String temp = "hel";

    private int changer = 0;

    private Timer timer;



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

    // Have to go to firebase and retrieve all of the current elements
    // * May not work if it's empty, need to test
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
    }

    private String helperMethod(DatabaseReference theReference,
                                String theNameOfIngredient, int number) {
        Log.d("testReference", theReference.toString());
        theReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot theSnapshot : snapshot.getChildren()) {
                    Log.d("test1", theSnapshot.toString());
                    if (theSnapshot.child("name").getValue(String.class)
                            .equals(theNameOfIngredient)) {
                        // Now, depending on what button was pressed,
                        // will have to increase or decrease
                        if (number == 1) {
                            //increase
                            changer = Integer.parseInt(theSnapshot.child("quantity")
                                    .getValue(String.class));
                            changer++;
                            temp = theSnapshot.child("name").getValue(String.class);
                            referenceForIngredient = theReference.child(temp);
                            referenceForIngredient.child("quantity")
                                    .setValue(String.valueOf(changer));
                        } else if (number == 2) {
                            //decrease
                            changer = Integer.parseInt(theSnapshot.child("quantity")
                                    .getValue(String.class));
                            changer--;
                            temp = theSnapshot.child("name").getValue(String.class);
                            referenceForIngredient = theReference.child(temp);
                            if (changer == 0) {
                                referenceForIngredient.removeValue();
                            } else {
                                referenceForIngredient.child("quantity")
                                        .setValue(String.valueOf(changer));
                            }
                        }

                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                temp = theSnapshot.child("quantity")
                                            .getValue(String.class);
                                Log.d("test2", temp);
                            }
                            }, 300);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", "Something went wrong 2");
            }
        });

        return temp;
    }

}