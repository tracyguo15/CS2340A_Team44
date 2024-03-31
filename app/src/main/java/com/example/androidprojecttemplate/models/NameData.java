package com.example.androidprojecttemplate.models;

public class NameData {
    // This class just adds the user's name to the database
    // This is only for organizational purposes for the pantry database
    private String name;
    private String username;

    public NameData(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public void setName(String theNewName) {
        name = theNewName;
    }

    public void setUsername(String theNewUsername) {
        username = theNewUsername;
    }

}
