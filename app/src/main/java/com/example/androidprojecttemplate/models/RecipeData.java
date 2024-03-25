package com.example.androidprojecttemplate.models;

import java.util.ArrayList;

public class RecipeData extends AbstractDatabase<String, Pair<IngredientData, int>> {
    private String name;
    private int time;
    private String description;
}
