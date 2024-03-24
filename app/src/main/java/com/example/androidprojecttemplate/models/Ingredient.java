package com.example.androidprojecttemplate.models;

public class Ingredient {
    private double price;
    private int calories;
    private int quantity;

    private String name;

    public Ingredient(String name, double price, int calories, int quantity) {
        if (name == null) {
            throw new IllegalArgumentException("Name is null, please enter a proper value");
        }
        if (price < 0) {
            throw new IllegalArgumentException("The price is negative, please enter a nonnegative number.");
        }
        if (calories < 0) {
            throw new IllegalArgumentException("Ingredient must have nonnegative number of calories.");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("The quantity must be nonnegative integer.");
        }
        this.name = name;
        this.price = price;
        this.calories = calories;
        this.quantity = quantity;
    }
    public void consume(int quantity) { }

    public void replenish(int quantity) { }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public int getCalories() {
        return calories;
    }
}
