package com.example.androidprojecttemplate.viewModel;

/**
 * This interface is used as a callback for when we're doing something with ingredients,
 * like adding them to our database. It's like a promise that when we're done doing the
 * background work (which takes some time), we'll use this to tell our app what happened
 * - whether it worked, something went wrong, or if the ingredient was already there.
 * By using this interface, we can update our app's interface accordingly, based on the
 * result of our background task.
 */
public interface IngredientCallback {
    //this is used for
    void onCompleted(int result);
}
