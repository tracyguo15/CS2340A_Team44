package com.example.androidprojecttemplate.models;

public class IngredientData {
    private String name;
    private int calories;
    private double price;
    
    public IngredientData(String name, double price, int calories) {
        if (name == null) {
            throw new IllegalArgumentException("Name is null, please enter a proper value");
        }
        if (price < 0) {
            throw new IllegalArgumentException("The price is negative, please enter a positive number.");
        }
        if (calories < 0) {
            throw new IllegalArgumentException("Ingredient must have positive number of calories.");
        }

        this.name = name;
        this.price = price;
        this.calories = calories;
    }

    public void setName(String name) { this.name = name; }
    public void setCalories(int quantity) { this.quantity = quantity; }
    public void setPrice(double price) { this.price = price; }

    public String getName() { return name; }
    public int getCalories() { return calories; }
    public double getPrice() { return price; }
}