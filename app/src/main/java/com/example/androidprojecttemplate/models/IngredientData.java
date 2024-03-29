package com.example.androidprojecttemplate.models;

public class IngredientData {
    private int calories;
    private double price;

    private String quantity;

    private String expirationDate;

    public IngredientData(String name, String quantity, int calories, String expirationDate) {
        if (name == null) {
            throw new IllegalArgumentException("Name is null, please enter a proper value");
        }
        if (calories < 0) {
            throw new IllegalArgumentException("Ingredient must have positive number of calories.");
        }

        this.name = name;
        this.calories = calories;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
    }
    
    public IngredientData(double price, int calories) {
        if (price < 0) {
            throw new IllegalArgumentException("The price is negative, "
                    + "please enter a positive number.");
        }
        if (calories < 0) {
            throw new IllegalArgumentException("Ingredient must have positive number of calories.");
        }

        this.price = price;
        this.calories = calories;
    }

    public void setCalories(int calories) { this.calories = calories; }
    public void setPrice(double price) { this.price = price; }

    public int getCalories() { return calories; }
    public double getPrice() { return price; }
}