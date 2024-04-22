package com.example.androidprojecttemplate.models;

//import java.util.ArrayList;

public class RecipeData extends AbstractDatabase<String, Integer> {
    private String name;
    private String description;
    private int time;

    public RecipeData(String name, String description, int time) {
        this.name = name;
        this.description = description;
        this.time = time;
    }

    public RecipeData(String name, int time) {
        this(name, "", time);
    }

    public RecipeData() {
        this("", "", 0);
    }

    public void add(String ingredientName, int quantity) {
        this.put(ingredientName, quantity);
    }

    public void delete(String ingredientName) {
        this.remove(ingredientName);
    }

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

    /**
     * Given a recipe, determine whether the recipe can be cooked or not
     * Yes this is a copy of the canCook method in PantryData. I just ported
     * it over here as a temporary solution. I can deal with it after Sprint 3 demo.
     * @param pantry The pantry data
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
