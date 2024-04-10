package com.example.androidprojecttemplate.models;

public class UserLoginData {

    private String username;
    private String password;

    private String name;

    public UserLoginData(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public UserLoginData(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public UserLoginData(String username) {
        this.username = username;
    }

    // Setter and Getter methods
    public void setUsername(String newUsername) {
        username = newUsername;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String newPassword) {
        password = newPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String newName) {
        name = newName;
    }

    public String getName() {
        return name;
    }
}
