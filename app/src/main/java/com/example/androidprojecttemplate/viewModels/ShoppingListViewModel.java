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

    private String theUsersEmailFromAuthenticationDatabase;

    private boolean isItInIngredientDatabase = false;

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
                        helperMethod(referenceTemp, names, quantities);
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

    private void helperMethod(DatabaseReference theReference, ArrayList<EditText> names, ArrayList<EditText> quantities) {
        for(int i = 0; i < names.size(); i++) {
            ShoppingListData theItem = new ShoppingListData(names.get(i).getText().toString(), quantities.get(i).getText().toString());
            Log.d("theName", names.get(i).getText().toString());
            theReference.child(names.get(i).getText().toString()).setValue(theItem);
        }
    }
}