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
            ShoppingListData shoppingListData = new ShoppingListData("apple", "-1");
        } catch (IllegalArgumentException e) {
            assertEquals("Quantity is null, empty, or negative please enter a proper value", e.getMessage());
        }
    }

    //Tracy Guo
    //check if shopping list quantity is null
    @Test
public void testShoppingListDataQuantityNull() {
        try {
            ShoppingListData shoppingListData = new ShoppingListData("apple", null);
        } catch (IllegalArgumentException e) {
            assertEquals("Quantity is null, empty, or negative please enter a proper value", e.getMessage());
        }
    }



}
