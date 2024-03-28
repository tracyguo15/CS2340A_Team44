package com.example.androidprojecttemplate.models;

import android.util.Pair;

import java.util.ArrayList;

public class PantryData extends AbstractDatabase<String, Pair<IngredientData, Integer>> {
    public PantryData(ArrayList<Pair<IngredientData, Integer>> ingredients) {
        if (ingredients == null) {
            throw new IllegalArgumentException("ingredients shouldn't be null");
        }

        for (Pair<IngredientData, Integer> i : ingredients) {
            this.put(i);
        }
    }

    public void add(IngredientData ingredient, int quantity) {
        this.put(new Pair<>(ingredient, quantity));
    }

    public void delete(IngredientData ingredient, int quantity) {
        this.remove(new Pair<>(ingredient, quantity));
    }

    /**
     * Given a recipe, determine whether the recipe can be cooked or not
     * @param recipe the recipe to be cooked
     * @return true if the recipe can be cooked, false otherwise
     */
    public boolean canCook(RecipeData recipe) {
        for (String requiredItemName : recipe.keySet()) {
            Pair<IngredientData, Integer> requiredItem = recipe.get(requiredItemName);
            Pair<IngredientData, Integer> pantryItem = this.get(requiredItemName);

            int requiredQuantity = requiredItem.getValue();
            int pantryQuantity = pantryItem.getValue();

            if (pantryItem == null || pantryQuantity < requiredQuantity) {
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
    public ArrayList<IngredientData> getMissingIngredients(RecipeData recipe) {
        ArrayList<IngredientData> missing = new ArrayList<>();

        for (String requiredItemName : recipe.keySet()) {
            Pair<IngredientData, Integer> requiredItem = recipe.get(requiredItemName);
            Pair<IngredientData, Integer> pantryItem = this.get(requiredItemName);

            int requiredQuantity = requiredItem.getValue();
            int pantryQuantity = pantryItem.getValue();

            if (pantryQuantity < requiredQuantity) {
                missing.add(pantryItem);
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
        for (String requiredItemName : recipe.keySet()) {
            Pair<IngredientData, Integer> requiredItem = recipe.get(requiredItemName);
            Pair<IngredientData, Integer> pantryItem = this.get(requiredItemName);

            Ingredient pantryIngredient = pantryItem.getKey();

            int requiredQuantity = requiredItem.getValue();
            int pantryQuantity = pantryItem.getValue();

            // if checked properly this should never be negative
            int newPantryQuantity = pantryQuantity - requiredQuantity;

            if (newPantryQuantity == 0) {
                // remove ingredient from pantry
                this.remove(requiredItemName);
            } else {
                // update ingredient quantity
                this.put(
                    requiredItemName, 
                    Pair<IngredientData, Integer>(pantryIngredient, newPantryQuantity));
            }
        }
    }
}
