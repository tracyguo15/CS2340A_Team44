package com.example.androidprojecttemplate.models;

public class IngredientData {
    private String Name;
    private String Quantity;
    private String Calories;
    private String ExpirationDate;

    public IngredientData(String Name, String Quantity, String Calories, String ExpirationDate) {
        this.Name = Name;
        this.Quantity = Quantity;
        this.Calories = Calories;
        this.ExpirationDate = ExpirationDate;

    }

    public IngredientData(String Name) {
        this.Name = Name;
    }

    // Getter and Setter methods
    public void setTheName(String theNewName) {
        Name = theNewName;
    }

    public String getTheName() {
        return Name;
    }

    public void setTheQuantity(String theNewQuantity) {
        Quantity = theNewQuantity;
    }

    public String getTheQuantity() {
        return Quantity;
    }

    public void setTheCalories(String theNewCalories) {
        Calories = theNewCalories;
    }

    public String getTheCalories() {
        return Calories;
    }

    public void setTheExpirationDate(String theNewExpirationDate) {
        ExpirationDate = theNewExpirationDate;
    }
    public String getTheExpirationDate() {
        return ExpirationDate;
    }

}
