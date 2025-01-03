package com.example.androidprojecttemplate.viewModels;


import com.example.androidprojecttemplate.models.PantryData;

import java.util.Set;


public class PantryPageViewModel {
    private PantryData pantry;

    private IngredientViewModel ingredientInstance;

    private String currentIngredient;

    private int currentQuantity;

    private String currentPrice;

    private static volatile PantryPageViewModel instance;


    private PantryPageViewModel() {
        pantry = null;
        ingredientInstance = IngredientViewModel.getInstance();
        currentIngredient = null;
    }
    public static PantryPageViewModel getInstance() {
        if (instance == null) {
            synchronized (PantryPageViewModel.class) {
                if (instance == null) {
                    instance = new PantryPageViewModel();
                }
            }
        }
        return instance;
    }

    /**
     * Increments the ingredient in focus by one.
     */
    public void increment() {

    }

    /**
     * Decrements the ingredient in focus by one. If the ingredient quantity
     * reaches zero, then
     * @return a String of the button and ingredient to remove, or null if
     * nothing needs to be removed
     */
    public String decrement() {
        return null;
    }

    /**
     * When a button is clicked, it's label is sent to this function.
     * currentIngredient is set to the name. Using the current name,
     * the quantity and price is put into currentQuantity and currentPrice,
     * respectively.
     * @param name the label of the button clicked
     */
    public void focus(String name) {

    }

    public Set<String> giveIngredients() {
        //have PantryData sync to remote database
        //so when an item is added on the phone, it will be added on local machine and
        //  in remote database associated with user
        return pantry.keySet();
    }
}
