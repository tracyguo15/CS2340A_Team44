package com.example.androidprojecttemplate.models;

public class UserLoginData {

    private String username;
    private String password;

    public UserLoginData(String username, String password) {
        this.username = username;
        this.password = password;
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
}
