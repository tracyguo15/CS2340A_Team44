package com.example.androidprojecttemplate.viewModel;


import android.widget.Button;

import com.example.androidprojecttemplate.models.IngredientData;
import com.example.androidprojecttemplate.models.PantryData;


public class PantryPageViewModel {
    private PantryData pantry;

    private IngredientViewModel ingredientInstance;

    private String currentIngredient;

    private int currentQuantity;

    private String currentPrice;

    private static volatile PantryPageViewModel instance;


    private PantryPageViewModel() {
        pantry = PantryData.getInstance();
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
}
