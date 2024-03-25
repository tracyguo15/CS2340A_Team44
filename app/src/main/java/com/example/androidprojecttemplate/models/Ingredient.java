package com.example.androidprojecttemplate.models;

public class Ingredient {
    private double price;
    private int calories;

    private String name;

    public Ingredient(String name, double price, int calories) {
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
    public void consume(int quantity) { }

    public void replenish(int quantity) { }

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
