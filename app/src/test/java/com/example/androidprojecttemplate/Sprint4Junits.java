package com.example.androidprojecttemplate;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.androidprojecttemplate.models.ShoppingListData;



public class Sprint4Junits {
    //Tracy Guo
    //check if shopping list quantity is negative
    @Test
    public void testShoppingListDataQuantityNegative() {
        try {
            ShoppingListData shoppingListData = new ShoppingListData("apple", "-1", "50");
        } catch (IllegalArgumentException e) {
            assertEquals("Quantity is null, empty, or negative please enter a proper value", e.getMessage());
        }
    }

    //Tracy Guo
    //check if shopping list quantity is null
    @Test
    public void testShoppingListDataQuantityNull() {
        try {
            ShoppingListData shoppingListData = new ShoppingListData("apple", null, "50");
        } catch (IllegalArgumentException e) {
            assertEquals("Quantity is null, empty, or negative please enter a proper value", e.getMessage());
        }
    }

    //Michael Leonick
    @Test
    //test shopping list quantity empty
    public void testShoppingListDataQuantityEmpty() {
        try {
            ShoppingListData shoppingListData = new ShoppingListData("apple", "", "30");
        } catch (IllegalArgumentException e) {
            assertEquals("Quantity is null, empty, or negative please enter a proper value", e.getMessage());
        }
    }
    //Michael Leonick
    @Test
    //test shopping list name empty
    public void testShoppingListDataNameEmpty() {
        try {
            ShoppingListData shoppingListData = new ShoppingListData("", "1", "100");
        } catch (IllegalArgumentException e) {
            assertEquals("Name is null or empty, please enter a proper value", e.getMessage());
        }
    }

    //Daniel Deller
    @Test
    //test shopping list name null
    public void testShoppingListDataNameNull() {
        try {
            ShoppingListData shoppingListData = new ShoppingListData(null, "1", "40");
        } catch (IllegalArgumentException e) {
            assertEquals("Name is null or empty, please enter a proper value", e.getMessage());
        }
    }

    //Daniel Deller
    @Test
    public void changeShoppingListNameNull() {
        try {
            ShoppingListData theData = new ShoppingListData("Lox", "5", "90");
            theData.setName("Bagel");
            assertEquals("Bagel", theData.getName());
            theData.setName(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Name is null or empty, please enter a proper value", e.getMessage());
        }


    }


    //RecipeListPage rp;
    //Daniel Deller
    //test whether RecipeListViewModel calculated the missing ingredients correctly
   /* public void testCalculateMissingIngredientseProperly() {
        rp = new RecipeListPage();
        RecipeListViewModel vm = rp.getViewModel();
        HashMap<String, RecipeData> cookbook = vm.getCookbook();
        HashMap<String, Integer> pantry = vm.getPantry();

        HashMap<String, Integer> missing = vm.getAllMissingIngredients();

        HashMap<String, Integer> needed = new HashMap<>();
        for (RecipeData recipe : cookbook.values()) {
            if (!vm.canCook(recipe)) {
                HashMap<String, Integer> recipeItem = vm.getMissingIngredients(recipe.getName());
                for (String item : recipeItem.keySet()) {
                    if (!needed.containsKey(item)) {
                        needed.put(item, recipeItem.get(item));
                    } else {
                        int previousQuantity = needed.get(item);
                        needed.put(item, previousQuantity + recipe.get(item));
                    }
                }
            }
        }

        for (String key : needed.keySet()) {
            boolean keyInMachien = missing.containsKey(key);
            boolean keyInDB = needed.containsKey(key);
            assertEquals(keyInDB, keyInMachien);
            int q1 = missing.get(key);
            int q2 = needed.get(key);
            assertEquals(q1, q2);
        }
    }

    private interface Callback {
        void onCallback(HashMap<String, Integer> map);
    }
    //push the changes, and then compare to see if the shopping list was properly updated
    public void testUpdateShoppingListProperly() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Shopping_List");
        rp = new RecipeListPage();
        RecipeListViewModel vm = rp.getViewModel();
        ref = ref.child(vm.getUserName());
        Button b = rp.getMissingIngredientsButton();

        HashMap<String, Integer> missing = vm.getAllMissingIngredients();
        b.callOnClick();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    if (item.getKey().equals("username") || (item.getKey().equals("name"))) {
                        continue;
                    }
                    String val = item.child("quantity").getValue().toString();
                    assertEquals(val, missing.get(item.getKey()) + "");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    } */

    //Rohan Bhole
    public void testSomething1() {

    }

    //Rohan Bhole

    //Michael Vaden
    //Micahel Vaden


    //Adam Vaughn


    //Adam Vaughn

}
