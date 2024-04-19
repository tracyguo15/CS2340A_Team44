package com.example.androidprojecttemplate.cats;

import com.google.firebase.database.DatabaseError;

public interface RecipeListCallback {
    boolean onCanCook(boolean canCook);
    void onError(DatabaseError databaseError);
}
