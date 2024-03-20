package com.example.androidprojecttemplate.models;

public class Cookbook extends AbstractDatabase<String, Recipe> {
    private static volatile Cookbook instance;

    private Cookbook() {
        super();
    }

    public static Cookbook getInstance() {
        if (instance == null) {
            synchronized (Cookbook.class) {
                if (instance == null) {
                    instance = new Cookbook();
                }
            }
        }
        return instance;
    }
}

