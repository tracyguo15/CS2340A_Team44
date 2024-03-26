package com.example.androidprojecttemplate.models;

import java.util.ArrayList;

public class RecipeData extends AbstractDatabase<String, Pair<IngredientData, Integer>> {
    private String name;
    private int time;
    private String description;

    public RecipeData(ArrayList<Pair<IngredientData, Integer>> ingredients, Strings name, int time, String description) {
        if (ingredients == null) {
            throw new IllegalArgumentException("ingredients shouldn't be null");
        }

        for (Pair<IngredientData, Integer> i : ingredients) {
            this.put(i);
        }

        this.name = name;
        this.time = time;
        
        this.description = description;
    }

    public void add(IngredientData ingredient, int quantity) {
        this.put(new Pair<>(ingredient, quantity));
    }

    public void delete(IngredientData ingredient, int quantity) {
        this.remove(new Pair<>(ingredient, quantity));
    }

    public void setName(String name) { this.name = name; }
    public void setTime(int time) { this.time = time; }
    public void setDescription(String description) { this.description = description};

    public String getName() { return this.name; }
    public int getTime() { return this.time; }
    public String getDescription() { return this.description; }
}
