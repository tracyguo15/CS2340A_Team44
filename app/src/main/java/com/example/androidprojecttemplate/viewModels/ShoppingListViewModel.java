package com.example.androidprojecttemplate.viewModels;

import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.androidprojecttemplate.models.DataForPantry;
import com.example.androidprojecttemplate.models.FirebaseDB;
import com.example.androidprojecttemplate.models.IngredientData;
import com.example.androidprojecttemplate.models.ShoppingListData;
import com.example.androidprojecttemplate.models.UserData;
import com.example.androidprojecttemplate.views.IngredientPage;
import com.example.androidprojecttemplate.views.PersonalInfo;
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
import java.util.TimerTask;

public class ShoppingListViewModel {
    private static ShoppingListViewModel instance;
    private ShoppingList theData;

    private FirebaseAuth theAuthenticationVariable;
    private FirebaseUser user;
    private DatabaseReference referenceForShoppingList;
    private DatabaseReference referenceTemp;
    private DatabaseReference referenceForPantry;
    private boolean doesItExist = false;

    private String theUsersEmailFromAuthenticationDatabase;

    private boolean isItInIngredientDatabase;

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

    public void addToFirebase(ArrayList<EditText> names, ArrayList<EditText> quantities, TheCallback callback) {
        referenceForShoppingList = FirebaseDatabase.getInstance().getReference().child("Shopping_List");

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
                        helperMethod(referenceTemp, names, quantities, theUsersName);
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

    private void helperMethod(DatabaseReference theReference, ArrayList<EditText> names, ArrayList<EditText> quantities, String theUsersName) {
        for(int i = 0; i < names.size(); i++) {
            // Will have to check the pantry database
            if(!isInPantryDatabase(theUsersName, names.get(i).getText().toString(), quantities.get(i).getText().toString())) {
                ShoppingListData theItem = new ShoppingListData(names.get(i).getText().toString(), quantities.get(i).getText().toString());
                theReference.child(names.get(i).getText().toString()).setValue(theItem);
            }
        }
    }

    private boolean isInPantryDatabase(String theUsersName, String nameOfIngredient, String theQuantity) {
        doesItExist = false;
        referenceForPantry = FirebaseDatabase.getInstance().getReference().child("Pantry").child(theUsersName).child("Ingredients");
        Log.d("Reference1:", referenceForPantry.toString());

        referenceForPantry.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot theSnapshot : snapshot.getChildren()) {
                    if(nameOfIngredient.equals(theSnapshot.getKey())) {
                        // The ingredient already exists in the Pantry database
                        // Simply update the quantity
                        referenceForPantry.child(nameOfIngredient).child("quantity").setValue(theQuantity);
                        doesItExist = true;
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", "Something went wrong");
            }
        });

        Log.d("TheBool", String.valueOf(doesItExist));
        return doesItExist;
    }
}