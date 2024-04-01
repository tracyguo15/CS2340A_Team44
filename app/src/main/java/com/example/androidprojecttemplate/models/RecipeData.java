package com.example.androidprojecttemplate.models;

import java.util.ArrayList;

public class RecipeData extends AbstractDatabase<String, Integer> {
    private String description;
    private int time;

    public RecipeData(String description, int time) {
        this.description = description;
        this.time = time;
    }

    public void add(String ingredientName, int quantity) {
        this.put(ingredientName, quantity);
    }

    public void delete(String ingredientName) {
        this.remove(ingredientName);
    }

    public void setTime(int time) { this.time = time; }
    public void setDescription(String description) { this.description = description; }

    public int getTime() { return this.time; }
    public String getDescription() { return this.description; }
}
