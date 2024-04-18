package com.example.androidprojecttemplate.models;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.androidprojecttemplate.viewModels.RecipeListViewModel;
import com.example.androidprojecttemplate.views.RecipeListPage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CustomAdapter {
    private RecipeListPage recipeListPage;
    private LinearLayout container;
    private RecipeListViewModel viewModel;


    public CustomAdapter(
            RecipeListPage recipeListPage,
            LinearLayout container,
            RecipeListViewModel viewModel) {
        this.recipeListPage = recipeListPage;
        this.container = container;
        this.viewModel = viewModel;
    }

    /**
     * Creates a view to be used in the recipes container.
     *
     * @return the formatted view
     */
    public TextView createView(String text, boolean canCook) {
        TextView view = new TextView(this.recipeListPage);
        view.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        view.setPadding(0, 16, 16, 0);
        view.setAllCaps(false);
        view.setTextSize(24);

        // set color and listener if can cook.
        if (canCook) {
            view.setTextColor(Color.GREEN);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(
                            recipeListPage,
                            "Can cook this recipe!",
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            view.setTextColor(Color.RED);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(
                            recipeListPage,
                            "Cannot cook this recipe!",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        view.setText(text);

        return view;
    }

    /**
     * Displays the views in the container.
     */
    public void display() {
        // clear view before
        container.removeAllViews();

        viewModel.updateRecipes(new RecipeListViewModel.UpdateRecipesCallback() {
            @Override
            public void onRecipesUpdated(ArrayList<RecipeData> recipesData) {
                for (RecipeData recipeData : recipesData) {
                    Log.d("container added", "added");
                    String name = recipeData.getName();
                    int time = recipeData.getTime();

                    String text = name + " Time: " + time;
                    boolean canCook = true;

                    TextView newView = createView(text, canCook);
                    container.addView(newView);
                }
            }
        });
    }

    /**
     * Displays the views in the container sorted alphabetically.
     */
    public void displayAlpha() {
        // clear view before
        container.removeAllViews();

        viewModel.updateRecipes(new RecipeListViewModel.UpdateRecipesCallback() {
            @Override
            public void onRecipesUpdated(ArrayList<RecipeData> recipesData) {
                // sort alphabetically
                Collections.sort(recipesData, new Comparator<RecipeData>() {
                    @Override
                    public int compare(RecipeData recipe1, RecipeData recipe2) {
                        String name1 = recipe1.getName().toUpperCase();
                        String name2 = recipe2.getName().toUpperCase();

                        return name1.compareTo(name2);
                    }
                });

                for (RecipeData recipeData : recipesData) {
                    Log.d("container added", "added");
                    String name = recipeData.getName();
                    int time = recipeData.getTime();

                    String text = name + " Time: " + time;
                    boolean canCook = true;

                    TextView newView = createView(text, canCook);
                    container.addView(newView);
                }
            }
        });
    }

    /**
     * Displays the views in the container sorted by time.
     */
    public void displayTime() {
        // clear view before
        container.removeAllViews();

        viewModel.updateRecipes(new RecipeListViewModel.UpdateRecipesCallback() {
            @Override
            public void onRecipesUpdated(ArrayList<RecipeData> recipesData) {
                // sort by time
                Collections.sort(recipesData, new Comparator<RecipeData>() {
                    @Override
                    public int compare(RecipeData recipe1, RecipeData recipe2) {
                        return Integer.compare(recipe1.getTime(), recipe2.getTime());
                    }
                });

                for (RecipeData recipeData : recipesData) {
                    Log.d("container added", "added");
                    String name = recipeData.getName();
                    int time = recipeData.getTime();

                    String text = name + " Time: " + time;
                    boolean canCook = true;

                    TextView newView = createView(text, canCook);
                    container.addView(newView);
                }
            }
        });
    }
}