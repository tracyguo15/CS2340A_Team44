package com.example.androidprojecttemplate.models;

import java.util.ArrayList;

public class Pantry extends AbstractDatabase<String, Ingredient> {
    private static volatile Pantry instance;

    private Pantry() {
        super();
    }

    public static Pantry getInstance() {
        if (instance == null) {
            synchronized (Pantry.class) {
                if (instance == null) {
                    instance = new Pantry();
                }
            }
        }
        return instance;
    }

    /**
     * Given a recipe, determine whether the recipe can be cooked or not
     * @param recipe the recipe to be cooked
     * @return true if the recipe can be cooked, false otherwise
     */
    public boolean canCook(Recipe recipe) {
        if (this.size() == 0) {
            return false;
        }
        ArrayList<Ingredient> ingredientsNeeded = recipe.getIngredients();
        if (ingredientsNeeded.size() > this.size()) {
            return false;
        }
        for (Ingredient item : ingredientsNeeded) {
            Ingredient available = this.get(item.getName());
            if (available == null) {
                return false;
            } else if (available.getQuantity() < item.getQuantity()) {
                return false;
            }
        }
        return true;
    }

    /**
     * If a recipe cannot be cooked, make a list of the ingredients needed and return it to
     * the user. If everything exists in the pantry, return an empty list
     * @param recipe the recipe to find the missing ingredients for
     * @return an ArrayList containing the missing ingredients, or an empty list if all
     * the ingredients exist
     */
    public ArrayList<Ingredient> getMissingIngredients(Recipe recipe) {
        ArrayList<Ingredient> ingredientsNeeded = new ArrayList<>();
        ArrayList<Ingredient> recipeIngredients = recipe.getIngredients();
        if (this.size() > 0 || recipeIngredients.size() <= this.size()) {
            for (Ingredient item : recipeIngredients) {
                Ingredient available = this.get(item.getName());
                if (available == null) {
                    ingredientsNeeded.add(item);
                } else if (available.getQuantity() < item.getQuantity()) {
                    Ingredient i = new Ingredient(
                            item.getName(),
                            item.getPrice(),
                            item.getCalories(),
                            item.getQuantity() - available.getQuantity()
                    );
                    ingredientsNeeded.add(i);
                }
            }
            return ingredientsNeeded;
        }
        return recipeIngredients;
    }
}
