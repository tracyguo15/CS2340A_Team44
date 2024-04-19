package com.example.androidprojecttemplate.cats;

import android.text.TextUtils;

import com.example.androidprojecttemplate.models.FirebaseDB;

import androidx.annotation.NonNull;

import com.example.androidprojecttemplate.models.UserLoginData;
import com.example.androidprojecttemplate.views.CreateAccountActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccountViewModel {
    private static CreateAccountViewModel instance;
    private final CreateAccountActivity data;

    private FirebaseAuth theAuthenticationVariable;

    private DatabaseReference reference;
    DatabaseReference referenceForPantry;
    DatabaseReference childForReferenceForPantry;
    DatabaseReference referenceForShoppingList;

    private boolean isItValid = true;

    public CreateAccountViewModel() {
        data = new CreateAccountActivity();
        theAuthenticationVariable = FirebaseDB.getInstance().getFirebaseAuth();

    }

    public static synchronized CreateAccountViewModel getInstance() {
        if (instance == null) {
            instance = new CreateAccountViewModel();
        }

        return instance;
    }

    public void toLoginScreenFromCreate(String username, String password,
                                       String confirmPassword, String name, TheCallback callback) {
        // check validity of username and password
        // password and confirmPassword don't equal
        if (!TextUtils.equals(password, confirmPassword)) {
            callback.onCompleted(1);
            isItValid = false;
            // username is not a valid email
        } else if (!username.contains("@") || !username.contains(".com")) {
            callback.onCompleted(2);
            isItValid = false;
            // password is less than length 6
        } else if (password.length() < 6) {
            callback.onCompleted(3);
            isItValid = false;
            // Inputs have spaces
        } else if (username.contains(" ") || password.contains(" ")
                || confirmPassword.contains(" ")) {
            callback.onCompleted(4);
            isItValid = false;
        }

        // Checks are done, can now create the username and password
        if (isItValid) {
            // create user with firebase
            theAuthenticationVariable.createUserWithEmailAndPassword(username, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Add entires to the real-time database
                                reference = FirebaseDatabase.getInstance()
                                        .getReference()
                                        .child("Users");

                                UserLoginData theUser = new UserLoginData(username, password, name);

                                // Will track different usernames in the database by taking the first
                                // letter of their username (since you can't use the full username since
                                // it has special characters)
                                reference.child(name).setValue(theUser);


                                // Furthermore, will also add the user's name to the pantry database
                                // This is just for organizational purposes
                                referenceForPantry = FirebaseDatabase.getInstance()
                                        .getReference()
                                        .child("Pantry");

                                UserLoginData theName = new UserLoginData(name, username);
                                referenceForPantry.child(name).setValue(theName);

                                childForReferenceForPantry = referenceForPantry.child(name);
                                UserLoginData tempValue = new UserLoginData(name);
                                childForReferenceForPantry.child("Ingredients").setValue(tempValue);

                                // Lastly, will add the user's name to the shopping list database
                                referenceForShoppingList = FirebaseDatabase.getInstance()
                                        .getReference()
                                        .child("Shopping_List");

                                referenceForShoppingList.child(name).setValue(theName);

                                callback.onCompleted(5);
                            } else {
                                callback.onCompleted(6);
                            }
                        }
                    });
        }
        isItValid = true;
    }

}