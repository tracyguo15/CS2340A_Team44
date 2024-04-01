package com.example.androidprojecttemplate.models;

public class CookbookData extends AbstractDatabase<String, RecipeData> {
    private static volatile CookbookData instance;

    private CookbookData() {
        super();
    }

    public static CookbookData getInstance() {
        if (instance == null) {
            synchronized (CookbookData.class) {
                if (instance == null) {
                    instance = new CookbookData();
                }
            }
        }
        return instance;
    }
}

