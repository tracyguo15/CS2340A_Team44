package com.example.androidprojecttemplate.models;

import java.util.ArrayList;

public class Recipe {
    private ArrayList<Ingredient> ingredients;
    private int calories;
    private int time;

    private String name;

    public Recipe(String name, int calories, int time, ArrayList<Ingredient> list) {
        if (name == null) {
            throw new IllegalArgumentException("Name is empty, please enter a valid string.");
        }
        if (time < 0) {
            throw new IllegalArgumentException("Time to cook is less than 0, please enter a nonnegative number.");
        }
        if (list == null) {
            throw new IllegalArgumentException("The list of ingredients is null, please enter a valid list.");
        } else {
            if (list.size() < 1) {
                throw new IllegalArgumentException("The Ingredient list is empty, put at least one ingredient in it.");
            } else {
                for (Ingredient i : list) {
                    if (i == null) {
                        throw new IllegalArgumentException("An ingredient in  your list is null, make sure all your Ingredients are valid.");
                    }
                }
            }
        }
        this.name = name;
        this.calories = calories;
        this.time = time;
        this.ingredients = list;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public int getCalories() {
        return calories;
    }

    public int getTime() {
        return time;
    }

    public String getName() {
        return name;
    }
}
