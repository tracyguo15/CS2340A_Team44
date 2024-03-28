package com.example.androidprojecttemplate.models;

import java.util.ArrayList;

public class RecipeData extends AbstractDatabase<String, Pair<Integer, Integer>> {
    private String name;
    private int time;
    private String description;

    public RecipeData() {
        this.name = "";
        this.time = 0;
        this.description = "";
    }

    public void add(int id, int quantity) {
        this.put(name, new Pair<>(id, quantity));
    }

    public void delete(IngredientData ingredient) {
        String name = ingredient.getName();

        this.remove(name);
    }

    public void setName(String name) { this.name = name; }
    public void setTime(int time) { this.time = time; }
    public void setDescription(String description) { this.description = description; }

    public String getName() { return this.name; }
    public int getTime() { return this.time; }
    public String getDescription() { return this.description; }
}
