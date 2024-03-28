package com.example.androidprojecttemplate.models;

public class IngredientData {
    private int id;
    private String name;
    private int calories;
    private double price;
    
    public IngredientData(int id, String name, double price, int calories) {
        if (name == null) {
            throw new IllegalArgumentException("Name is null, please enter a proper value");
        }
        if (price < 0) {
            throw new IllegalArgumentException("The price is negative, please enter a positive number.");
        }
        if (calories < 0) {
            throw new IllegalArgumentException("Ingredient must have positive number of calories.");
        }

        this.id = id;
        this.name = name;
        this.price = price;
        this.calories = calories;
    }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCalories(int calories) { this.calories = calories; }
    public void setPrice(double price) { this.price = price; }

    public int getId() { return this.id; }
    public String getName() { return name; }
    public int getCalories() { return calories; }
    public double getPrice() { return price; }
}
