package com.example.androidprojecttemplate.models;

import java.util.ArrayList;

public class RecipeData extends AbstractDatabase<String, Pairs<IngredientData, Integer>> {
    private String name;
    private int time;
    private String description;

    public RecipeData(ArrayList<Pairs<IngredientData, Integer>> ingredients,
                      String name, int time, String description) {
        if (ingredients == null) {
            throw new IllegalArgumentException("ingredients shouldn't be null");
        }

        for (Pairs<IngredientData, Integer> i : ingredients) {
            String ingredientName = i.getFirst().getName();

            this.put(ingredientName, i);
        }

        this.name = name;
        this.time = time;
        this.description = description;
    }

    public void add(IngredientData ingredient, int quantity) {
        String name = ingredient.getName();

        this.put(name, new Pairs<>(ingredient, quantity));
    }

    public void delete(IngredientData ingredient, int quantity) {
        String name = ingredient.getName();

        this.remove(name);
    }

    public void setName(String name) {
        this.name = name; }
    public void setTime(int time) {
        this.time = time; }
    public void setDescription(String description) {
        this.description = description; }

    public String getName() {
        return this.name; }
    public int getTime() {
        return this.time; }
    public String getDescription() {
        return this.description; }
}
