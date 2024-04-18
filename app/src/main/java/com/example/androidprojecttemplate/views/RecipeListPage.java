package com.example.androidprojecttemplate.views;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
//import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidprojecttemplate.R;
import com.example.androidprojecttemplate.models.CustomAdapter;
import com.example.androidprojecttemplate.models.RecipeData;
import com.example.androidprojecttemplate.viewModels.RecipeListCallback;
import com.example.androidprojecttemplate.viewModels.RecipeListViewModel;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class RecipeListPage extends AppCompatActivity {
    private RecipeListViewModel viewModel;
    private LinearLayout recipesContainer;

    private ArrayAdapter adapter;
    private Button alphabetFilter;
    private Button timeFilter;


    private Button backToRecipePage;

    private FirebaseUser user;
    private DatabaseReference userRef;
    private DatabaseReference cookbookRef;
    private DatabaseReference pantryRef;
    private List<String[]> recipes = new ArrayList<>();

    private ArrayList<String> recipeNames = new ArrayList<>();
    private ArrayList<Integer> recipeTimes = new ArrayList<>();
    private ArrayList<Boolean> recipeCanCooks = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list_page);

        viewModel = RecipeListViewModel.getInstance();

        // back button
        backToRecipePage = findViewById(R.id.backToRecipePage);
        backToRecipePage.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeListPage.this, RecipePage.class);
            startActivity(intent);
        });

        // filter alphabetically button
        alphabetFilter = findViewById(R.id.btnFilterAlpha);
        alphabetFilter.setOnClickListener(v -> {
            displayRecipesAlpha();
        });

        // filter by time button
        timeFilter = findViewById(R.id.btnFilterTime);
        timeFilter.setOnClickListener(v -> {
            displayRecipesTime();
        });

        // database references
        cookbookRef = FirebaseDatabase.getInstance().getReference().child("Cookbook");
        pantryRef = FirebaseDatabase.getInstance().getReference().child("Pantry");

        // container view
        recipesContainer = findViewById(R.id.listViewRecipes);

        // RecipeListViewModel instantiation
        viewModel = RecipeListViewModel.getInstance();

        // initially display recipes
        this.displayRecipes();
    }

    /**
     * Displays each of the recipes in the database.
     */
    private void displayRecipes() {
        CustomAdapter adapter = new CustomAdapter(
                this,
                this.recipesContainer,
                this.viewModel);

        adapter.display();
    }

    /**
     * Displays each of the recipes in the database sorted alphabetically.
     */
    private void displayRecipesAlpha() {
        CustomAdapter adapter = new CustomAdapter(
                this,
                this.recipesContainer,
                this.viewModel);

        adapter.displayAlpha();
    }

    /**
     * Displays each of the recipes in the database sorted by time.
     */
    private void displayRecipesTime() {
        CustomAdapter adapter = new CustomAdapter(
                this,
                this.recipesContainer,
                this.viewModel);

        adapter.displayTime();
    }


    //canCook method from PantryData not yet adapted for this class

    /*
    public boolean canCook(RecipeData recipe, RecipeListCallback callback) {
        //Boolean variable to be returned
        boolean cooked = false;

        //Grab an instance of the ViewModel
        viewModel = RecipeListViewModel.getInstance();
        viewModel.getCurrentUser();

        //Get String[] of all the ingredients necessary and available
        ArrayList<String[]> recipeIngredients = viewModel.getRecipeIngredients(recipe);
        ArrayList<String[]> pantryIngredients = viewModel.getPantryIngredients();

        //Go through each ingredient necessary for recipe
        for (String[] r : recipeIngredients) {
            //Go through every ingredient in the pantry
            for (String[] p : pantryIngredients) {
                //Compare the names to see if they match
                if (r[0].equals(p[0])) {
                    //If the pantry amount < recipe amount
                    if (Integer.parseInt(p[1]) < Integer.parseInt(r[1])) {
                        break;
                    } else if (Integer.parseInt(p[1]) > Integer.parseInt(r[1])) {
                        cooked = true;
                    }
                }
            }

            //If cooked != true, then that means either the quantity was too low
            //or it wasn't in the pantry
            if (cooked != true) {
                break;
            }
        }

        return cooked;
    }*/

    /*
    private void update(List<RecipeData> recipes) {

        for (RecipeData recipe : recipes) {
            //if (recipe.canCook(pantryRef.getValue(PantryData.class))) {
                //TextView text = (TextView) theListView.findViewById(android.R.id.text1);
                //text.setTextColor(Color.GREEN);
            //} else {
                //TextView text = (TextView) theListView.findViewById(android.R.id.text1);
                //text.setTextColor(Color.RED);
            //}
            Log.d("canCook Called", String.valueOf(canCook(recipe, new RecipeListCallback() {
                @Override
                public boolean onCanCook(boolean canCook) {
                    return canCook;
                }

                @Override
                public void onError(DatabaseError databaseError) {
                    Log.d("CALLBACK ERROR", databaseError.toString());
                }
            })));
        }
    }*/
}
