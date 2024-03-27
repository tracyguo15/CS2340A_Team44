package com.example.androidprojecttemplate.models;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Set;


public class PantryData extends AbstractDatabase<String, Pair<IngredientData, Integer>> {
    public PantryData(ArrayList<Pair<IngredientData, Integer>> ingredients) {
        if (ingredients == null) {
            throw new IllegalArgumentException("ingredients shouldn't be null");
        }

        for (Pair<IngredientData, Integer> i : ingredients) {
            String name = i.getFirst().getName();

            this.put(name, i);

        }
    }

    public void add(IngredientData ingredient, int quantity) {
        String name = ingredient.getName();

        this.put(name, new Pair<>(ingredient, quantity));
    }


    public void remove(IngredientData ingredient, int quantity) {

    }

    public void delete(IngredientData ingredient) {
        String name = ingredient.getName();

        this.remove(name);
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

            int requiredQuantity = requiredItem.getSecond();
            int pantryQuantity = pantryItem.getSecond();

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

            int requiredQuantity = requiredItem.getSecond();
            int pantryQuantity = pantryItem.getSecond();

            if (pantryQuantity < requiredQuantity) {

                missing.add(pantryItem.getFirst());
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

            IngredientData pantryIngredient = pantryItem.getFirst();

            int requiredQuantity = requiredItem.getSecond();
            int pantryQuantity = pantryItem.getSecond();

            // if checked properly this should never be negative
            int newPantryQuantity = pantryQuantity - requiredQuantity;

            if (newPantryQuantity == 0) {
                // remove ingredient from pantry
                this.remove(requiredItemName);
            } else {
                // update ingredient quantity
                this.put(
                    requiredItemName,new Pair<IngredientData, Integer>(pantryIngredient, newPantryQuantity));
            }
        }
    }
}
