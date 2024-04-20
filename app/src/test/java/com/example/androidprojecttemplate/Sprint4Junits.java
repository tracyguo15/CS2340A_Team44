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

    //Michael Leonick
    @Test
    //test shopping list quantity empty
    public void testShoppingListDataQuantityEmpty() {
        try {
            ShoppingListData shoppingListData = new ShoppingListData("apple", "");
        } catch (IllegalArgumentException e) {
            assertEquals("Quantity is null, empty, or negative please enter a proper value", e.getMessage());
        }
    }
    //Michael Leonick
    @Test
    //test shopping list name empty
    public void testShoppingListDataNameEmpty() {
        try {
            ShoppingListData shoppingListData = new ShoppingListData("", "1");
        } catch (IllegalArgumentException e) {
            assertEquals("Name is null or empty, please enter a proper value", e.getMessage());
        }
    }

    //Daniel Deller
    @Test
    //test shopping list name null
    public void testShoppingListDataNameNull() {
        try {
            ShoppingListData shoppingListData = new ShoppingListData(null, "1");
        } catch (IllegalArgumentException e) {
            assertEquals("Name is null or empty, please enter a proper value", e.getMessage());
        }
    }

    //Daniel Deller



}
