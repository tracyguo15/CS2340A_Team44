package com.example.androidprojecttemplate.models;

public class IngredientData {
    private String Name;
    private String Quantity;
    private String Calories;
    private String ExpirationDate;
    private String Username;

    public IngredientData(String Name, String Quantity, String Calories, String ExpirationDate, String Username) {
        this.Name = Name;
        this.Quantity = Quantity;
        this.Calories = Calories;
        this.ExpirationDate = ExpirationDate;

        // The username is the person's email, which is found in a different class
        this.Username = Username;
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

    public void setTheUsername(String theNewUsername) {
        Username = theNewUsername;
    }

    public String getTheUsername() {
        return Username;
    }
}
