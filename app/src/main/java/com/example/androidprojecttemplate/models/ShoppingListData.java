package com.example.androidprojecttemplate.models;

public class ShoppingListData {
    private String name;
    private String quantity;

    public ShoppingListData(String name, String quantity) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name is null or empty, please enter a proper value");
        }

        if (quantity == null || quantity.isEmpty() || Integer.parseInt(quantity) < 0){
            throw new IllegalArgumentException("Quantity is null, empty, or negative please enter a proper value");
        }

        this.name = name;
        this.quantity = quantity;
    }


    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name is null or empty, please enter a proper value");
        }
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setQuantity(String quantity) {
        if (quantity == null || quantity.isEmpty() || Integer.parseInt(quantity) < 0) {
            throw new IllegalArgumentException("Quantity is null, empty, or negative please enter a proper value");
        }
        this.quantity = quantity;
    }
    public String getQuantity() {return quantity; }

}