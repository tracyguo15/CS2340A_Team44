package com.example.androidprojecttemplate.models;

public class DataForPantry {
    private String name;

    private String quantity;

    private String expirationDate;

    public DataForPantry(String name, String quantity, String expirationDate) {
        if (name == null) {
            throw new IllegalArgumentException("Name is null, please enter a proper value");
        }

        if (quantity == null) {
            throw new IllegalArgumentException("quantity is null, please enter a proper value");
        }

        this.name = name;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
    }



    //Setters
    public void setName(String name) {
        this.name = name; }

    public void setQuantity(String quantity) {
        this.quantity = quantity; }

    public void setExpirationDate(String theExpirationDate) {
        expirationDate = theExpirationDate;
    }


    //Getters
    public String getName() {
        return name; }

    public String getQuantity() {
        return quantity;
    }

    public String getExpirationDate() {
        return expirationDate;
    }
}
