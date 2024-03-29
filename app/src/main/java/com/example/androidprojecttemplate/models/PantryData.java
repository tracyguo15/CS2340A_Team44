package com.example.androidprojecttemplate.models;

import java.util.ArrayList;
import java.util.Set;


public class PantryData extends AbstractDatabase<String, Pair<Integer, Integer>> {
    public PantryData(ArrayList<Pair<IngredientData, Integer>> ingredients) {
        if (ingredients == null) {
            throw new IllegalArgumentException("ingredients shouldn't be null");
        }

        for (Pair<IngredientData, Integer> i : ingredients) {
            String name = i.getFirst().getName();

            //this.put(name, new Pair<>(i.getFirst().getId(), i.getFirst().get));
        }
    }

    public void add(IngredientData ingredient, int quantity) {
        String name = ingredient.getName();
        int id = ingredient.getId();

        this.put(name, new Pair<>(id, quantity));
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
            Pair<Integer, Integer> requiredItem = recipe.get(requiredItemName);
            Pair<Integer, Integer> pantryItem = this.get(requiredItemName);

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
    public ArrayList<Integer> getMissingIngredients(RecipeData recipe) {
        ArrayList<Integer> missing = new ArrayList<>();

        for (String requiredItemName : recipe.keySet()) {
            Pair<Integer, Integer> requiredItem = recipe.get(requiredItemName);
            Pair<Integer, Integer> pantryItem = this.get(requiredItemName);

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
            Pair<Integer, Integer> requiredItem = recipe.get(requiredItemName);
            Pair<Integer, Integer> pantryItem = this.get(requiredItemName);

            int pantryIngredientId = pantryItem.getFirst();

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
                    requiredItemName,new Pair<Integer, Integer>(pantryIngredientId, newPantryQuantity));
            }
        }
    }
}
