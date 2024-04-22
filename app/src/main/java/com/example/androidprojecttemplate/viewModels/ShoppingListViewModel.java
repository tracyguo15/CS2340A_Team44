package com.example.androidprojecttemplate.viewModels;

import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.androidprojecttemplate.models.FirebaseDB;
import com.example.androidprojecttemplate.models.ShoppingListData;
import com.example.androidprojecttemplate.views.ShoppingList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;

public class ShoppingListViewModel {
    private static ShoppingListViewModel instance;
    private ShoppingList theData;

    private FirebaseAuth theAuthenticationVariable;
    private FirebaseUser user;
    private DatabaseReference referenceForShoppingList;
    private DatabaseReference referenceTemp;
    private DatabaseReference referenceForPantry;
    private DatabaseReference referenceForSpecifcUser;
    private boolean doesItExist = false;

    private String theUsersEmailFromAuthenticationDatabase;

    private static int timesCalled = 0;

    private Timer timer;
    private String temp;
    private String temp2;
    private boolean doesItAlreadyExists;

    public ShoppingListViewModel() {
        theData = new ShoppingList();
    }

    public static synchronized ShoppingListViewModel getInstance() {
        if (instance == null) {
            instance = new ShoppingListViewModel();
        }

        return instance;
    }

    public void getCurrentUser() {
        theAuthenticationVariable = FirebaseDB.getInstance().getFirebaseAuth();
        user = FirebaseDB.getInstance().getUser();
        theUsersEmailFromAuthenticationDatabase = FirebaseDB.getInstance().getEmail();
    }

    private ArrayList<String> addedShoppingListItems = new ArrayList<>();
    private ArrayList<String> addedQuantities = new ArrayList<>();

    // Have to go to firebase and retrieve all of the current elements
    // * May not work if it's empty, need to test
    private void addTheElementsFromFirebaseToTheList() {
        referenceForShoppingList = FirebaseDatabase.getInstance().getReference().child("Shopping_List");

        referenceForShoppingList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot theSnapshot : snapshot.getChildren()) {

                    String theEmailFromFirebase = theSnapshot.child("username").getValue(String.class);

                    String theUsersName = theSnapshot.child("name").getValue(String.class);

                    if (theEmailFromFirebase.equals(theUsersEmailFromAuthenticationDatabase)) {
                        referenceForSpecifcUser = referenceForShoppingList.child(theUsersName);
                        // Will use a helper method to do the rest
                        helperMethod(referenceForSpecifcUser);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", "Something went wrong");
            }
        });
    }

    private void helperMethod(DatabaseReference theReference) {
        theReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot theSnapshot : snapshot.getChildren()) {

                    if (theSnapshot.child("name").getValue(String.class) != null) {
                        temp = theSnapshot.child("name").getValue(String.class).toString();
                        addedShoppingListItems.add(temp);
                        temp2 = theSnapshot.child("quantity").getValue(String.class).toString();
                        addedQuantities.add(temp2);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", "Something went wrong 2");
            }
        });
    }


    public void addToFirebase(ArrayList<EditText> names, ArrayList<EditText> quantities, ArrayList<EditText> calories, TheCallback callback) {
        referenceForShoppingList = FirebaseDatabase.getInstance().getReference().child("Shopping_List");

        if (timesCalled == 0) {
            addTheElementsFromFirebaseToTheList();
        }

        timesCalled++;
        referenceForShoppingList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot theSnapshot : snapshot.getChildren()) {

                    String theEmailFromFirebase = theSnapshot.child("username")
                            .getValue().toString();

                    String theUsersName = theSnapshot.child("name").getValue().toString();

                    if (theEmailFromFirebase.equals(theUsersEmailFromAuthenticationDatabase)) {
                        referenceTemp = referenceForShoppingList.child(theUsersName);
                        // Will use a helper method to do the rest
                        helperMethod2(referenceTemp, names, quantities, calories, theUsersName);
                        callback.onCompleted(1);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", "Something went wrong");
                callback.onCompleted(2);
            }
        });
    }

    private void helperMethod2(DatabaseReference theReference, ArrayList<EditText> names, ArrayList<EditText> quantities1, ArrayList<EditText> calories1, String theUsersName) {

        // Will have to check firebase to see if the ingredient already exists
        for(int i = 0; i < names.size(); i++) {
            String theCurrName = names.get(i).getText().toString();
            String theCurrQuantity = quantities1.get(i).getText().toString();
            String theCurrCalories = calories1.get(i).getText().toString();
            doesItAlreadyExists = false;
            theReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot theSnapshot : snapshot.getChildren()) {
                        if(theSnapshot.exists()) {
                            // Now check the names
                            if (theCurrName.equals(theSnapshot.getKey())) {
                                // Name already exists, simply update the quantity
                                theReference.child(theCurrName).child("quantity").setValue(theCurrQuantity);

                                // Will need to call another method to update that quantity's value in the arrayList
                                updateArrayListsWithNewValues(theCurrName, theCurrQuantity);
                                doesItAlreadyExists = true;
                            }
                        }
                    }

                    if (!doesItAlreadyExists) {
                        // Can now add it to the new list
                        ShoppingListData theItem = new ShoppingListData(theCurrName, theCurrQuantity, theCurrCalories);
                        theReference.child(theCurrName).setValue(theItem);
                        addedShoppingListItems.add(theCurrName);
                        addedQuantities.add(theCurrQuantity);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("Error", "Something went wrong");
                }
            });
        }
    }

    private void updateArrayListsWithNewValues(String theName, String theNewQuantity) {
        for (int i = 0; i < addedShoppingListItems.size(); i++) {
            if (theName.equals(addedShoppingListItems.get(i))) {
                addedQuantities.set(i, theNewQuantity);
                break;
            }
        }
    }

    public ArrayList<String> getTheArrayList() {
        return addedShoppingListItems;
    }

    public ArrayList<String> getTheQuantities() { return addedQuantities;}
}