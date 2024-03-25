package com.example.androidprojecttemplate.models;

import java.util.ArrayList;

public class RecipeData {
    private ArrayList<Ingredient> ingredients;
    private int time;

    private String name;

    public RecipeData(String name, int time, ArrayList<Ingredient> ingredients) {
        if (name == null) {
            throw new IllegalArgumentException("Name is empty, please enter a valid string.");
        }
        if (time < 0) {
            throw new IllegalArgumentException("Time to cook is less than 0, please enter a positive number.");
        }
        if (ingredients == null) {
            throw new IllegalArgumentException("The list of ingredients is null, please enter a valid list.");
        } else {
            if (ingredients.size() < 1) {
                throw new IllegalArgumentException("The Ingredient list is empty, put at least one ingredient in it.");
            } else {
                for (Ingredient i : ingredients) {
                    if (i == null) {
                        throw new IllegalArgumentException("An ingredient in  your list is null, make sure all your Ingredients are valid.");
                    }
                }
            }
        }
        this.name = name;
        this.time = time;
        this.ingredients = ingredients;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public int getCalories() {
        int totalCalories = 0;

        for (Ingredient i : this.ingredients) {
            totalCalories += i.getCalories();
        }

        return totalCalories;
    }

    public int getTime() {
        return time;
    }

    public String getName() {
        return name;
    }
}
