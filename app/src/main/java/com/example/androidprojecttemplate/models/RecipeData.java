package com.example.androidprojecttemplate.models;

//import java.util.ArrayList;

import java.util.HashMap;

public class RecipeData extends AbstractDatabase<String, Integer> {
    private String name;
    private String description;
    private int time;
    HashMap<String, Integer> ingredients;

    //
    public RecipeData(String name, String description, int time, HashMap<String, Integer> ingredients) {
        this.name = name;
        this.description = description;
        this.time = time;
        this.ingredients = ingredients;
    }

    public RecipeData(String name, int time, HashMap<String, Integer> ingredients) {
        this(name, "", time, ingredients);
    }

    public RecipeData(String name, String description, int time) {
        this(name, description, time, new HashMap<String, Integer>());
    }

    public RecipeData(String name, int time) {
        this(name, "", time, new HashMap<String, Integer>());
    }

    public RecipeData() {
        this("", "", 0, new HashMap<>());
    }


    public void add(String ingredientName, int quantity) {
        this.put(ingredientName, quantity);
    }

    public void delete(String ingredientName) {
        this.remove(ingredientName);
    }

    //Setters
    public void setName(String name) {
        try {
            this.name = name;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid name. "
                    + "Please try another one.");
        }
    }
    public void setTime(int time) {
        try {
            this.time = time;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid time. "
             + "Please try a positive value.");
        }
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setIngredients(HashMap<String, Integer> ingredients) {
        this.ingredients = ingredients;
    }

    //Getters
    public String getName() {
        return this.name;
    }
    public int getTime() {
        return this.time;
    }
    public String getDescription() {
        return this.description;
    }
    public HashMap<String, Integer> getIngredients() {
        return ingredients;
    }

    /**
     * Given a recipe, determine whether the recipe can be cooked or not
     * Yes this is a copy of the canCook method in PantryData. I just ported
     * it over here as a temporary solution. I can deal with it after Sprint 3 demo.
     * @return true if the recipe can be cooked, false otherwise
     */
    public boolean canCook(PantryData pantry) {
        for (String requiredIngredient : this.keySet()) {
            int requiredQuantity = this.get(requiredIngredient);
            int pantryQuantity = pantry.get(requiredIngredient);

            if (pantryQuantity == 0 || pantryQuantity < requiredQuantity) {
                return false;
            }
        }

        return true;
    }
}
