package com.example.androidprojecttemplate.models;

public class IngredientData {
    private int calories;
    private double price;
    
    public IngredientData(double price, int calories) {
        if (price < 0) {
            throw new IllegalArgumentException("The price is negative, please enter a positive number.");
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
