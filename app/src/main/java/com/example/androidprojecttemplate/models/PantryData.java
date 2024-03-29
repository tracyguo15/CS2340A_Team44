package com.example.androidprojecttemplate.models;

import java.util.ArrayList;
import java.util.Set;

public class PantryData extends AbstractDatabase<String, Integer> {
    public PantryData(ArrayList<Pair<String, Integer>> ingredients) {
        if (ingredients == null) {
            throw new IllegalArgumentException("ingredients shouldn't be null");
        }

        for (Pair<String, Integer> i : ingredients) {
            this.add(i.getFirst(), i.getSecond());
        }
    }

    public void add(String ingredientName, int quantity) {
        this.put(ingredientName, quantity);
    }

    public void delete(String ingredientName) {
        this.remove(ingredientName);
    }

    /**
     * Given a recipe, determine whether the recipe can be cooked or not
     * @param recipe the recipe to be cooked
     * @return true if the recipe can be cooked, false otherwise
     */
    public boolean canCook(RecipeData recipe) {
        for (String requiredIngredient : recipe.keySet()) {
            int requiredQuantity = recipe.get(requiredIngredient);
            int pantryQuantity = this.get(requiredIngredient);

            if (pantryQuantity == 0 || pantryQuantity < requiredQuantity) {
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
    public ArrayList<String> getMissingIngredients(RecipeData recipe) {
        ArrayList<String> missing = new ArrayList<>();

        for (String requiredIngredient : recipe.keySet()) {
            int requiredQuantity = recipe.get(requiredIngredient);
            int pantryQuantity = this.get(requiredIngredient);

            if (pantryQuantity == 0 || pantryQuantity < requiredQuantity) {
                missing.add(requiredIngredient);
            }
        }

        return missing;
    }

    /**
     * Given a recipe, finds all the items needed to cook the recipe,
     * and decrease the item count for all the items used to cook it.
     * Before you cook, please see if you can cook the recipe, and
     * you have the required ingredients in your pantry.
     * @param recipe the recipe to cook
     */
    public void cook(RecipeData recipe) {
        for (String requiredIngredient : recipe.keySet()) {
            int requiredQuantity = recipe.get(requiredIngredient);
            int pantryQuantity = this.get(requiredIngredient);

            // if checked properly this should never be negative
            int newPantryQuantity = pantryQuantity - requiredQuantity;

            if (newPantryQuantity == 0) {
                // remove ingredient from pantry
                this.remove(requiredIngredient);
            } else {
                // update ingredient quantity
                this.put(requiredIngredient, newPantryQuantity);
            }
        }
    }
}
