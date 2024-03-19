package com.example.androidprojecttemplate.viewModels;

import com.example.androidprojecttemplate.models.firebaseAuthSingleton;
import com.example.androidprojecttemplate.views.IngredientPage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class IngredientViewModel {
    private static IngredientViewModel instance;
    private final IngredientPage theData;
    public static int temp = 0;

    FirebaseAuth theAuthenticationVariable;
    FirebaseUser user;
    DatabaseReference reference;
    DatabaseReference tempReference;

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
}
