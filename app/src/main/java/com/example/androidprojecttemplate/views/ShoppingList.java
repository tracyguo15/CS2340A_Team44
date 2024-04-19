package com.example.androidprojecttemplate.views;

// Do not import android support because we are using androidx
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.androidprojecttemplate.R;

import com.example.androidprojecttemplate.cats.ShoppingListViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


//implements NavigationView.OnNavigationItemSelectedListener
public class ShoppingList extends AppCompatActivity {
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;

    private NavigationView navView;

    private LinearLayout ingredientContainer;
    private Button addShoppingIngredient;
    private Button submit;
    private ShoppingListViewModel viewModel;
    private final int maxIngredients = 5;
    private int ingredientCount;
    private boolean isThereInvalidEntry = false;
    private ArrayList<EditText> ingredients;
    private ArrayList<EditText> quantities;
    private ArrayList<String> theList = new ArrayList<>();
    private Timer timer;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shopping_list_page);
        ingredientContainer = findViewById(R.id.container);
        addShoppingIngredient = findViewById(R.id.addShoppingIngredientRow);
        submit = findViewById(R.id.submitShoppingListData);

        ingredients = new ArrayList<>();
        quantities = new ArrayList<>();

        viewModel = ShoppingListViewModel.getInstance();



        // Navbar
        Toolbar homeToolBar = (Toolbar) findViewById(R.id.nav_toolbar);
        setSupportActionBar(homeToolBar);
        dl = (DrawerLayout) findViewById(R.id.dl);
        abdt = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(abdt);
        abdt.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navView = (NavigationView) findViewById(R.id.navView);

        navView.setVisibility(View.GONE);
        navView.setNavigationItemSelectedListener(new NavigationView
                .OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.inputmeal) {
                    Intent intent = new Intent(ShoppingList.this, InputMealPage.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.recipe) {
                    Intent intent = new Intent(ShoppingList.this, RecipePage.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.ingredient) {
                    Intent intent = new Intent(ShoppingList.this, IngredientPage.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.list) {
                    if (!(ShoppingList.this instanceof ShoppingList)) {
                        Intent intent = new Intent(ShoppingList.this, ShoppingList.class);
                        startActivity(intent);
                    }
                    return true;
                } else if (id == R.id.personalinfo) {
                    Intent intent = new Intent(ShoppingList.this, PersonalInfo.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        addShoppingIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ingredientCount < maxIngredients) {
                    createIngredientRow();
                    ingredientCount++;
                } else {
                    Toast.makeText(ShoppingList.this,
                            "Max 5 ingredients",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        submit.setOnClickListener(v -> {
            viewModel.getCurrentUser();


            // First check if anything was inputed in the first place
            if (ingredients.size() == 0 || quantities.size() == 0) {
                Toast.makeText(ShoppingList.this,
                        "Please input something!",
                        Toast.LENGTH_SHORT).show();
                isThereInvalidEntry = true;
            } else {
                // Checks for null
                // Need to traverse through the ingredients and quantities to determine if null
                for (int i = 0; i < ingredients.size(); i++) {
                    if(ingredients.get(i).getText().toString().isEmpty()) {
                        // Should display a toast message
                        Toast.makeText(ShoppingList.this,
                                "Please input an ingredient!",
                                Toast.LENGTH_SHORT).show();
                        isThereInvalidEntry = true;
                        break;
                    }

                    if (quantities.get(i).getText().toString().isEmpty()) {
                        Toast.makeText(ShoppingList.this,
                                "Please input a quantity!",
                                Toast.LENGTH_SHORT).show();
                        isThereInvalidEntry = true;
                        break;
                    }
                }
            }


            if (!isThereInvalidEntry) {
                viewModel.addToFirebase(ingredients, quantities, result -> runOnUiThread(() -> {
                    if (result == 1) {
                        Toast.makeText(ShoppingList.this,
                                "Success",
                                Toast.LENGTH_SHORT).show();
                        switchScreen();
                    } else if (result == 2) {
                        Toast.makeText(ShoppingList.this,
                                "Something went wrong with the firebase connection",
                                Toast.LENGTH_SHORT).show();
                    }
                }));


            }

            // Will change the value of the boolean variable for the next time
            isThereInvalidEntry = false;
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (navView.getVisibility() == View.VISIBLE) {
            navView.setVisibility(View.GONE);
        } else {
            navView.setVisibility(View.VISIBLE);
        }
        return true;
    }

    public void switchScreen() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                theList = viewModel.getTheArrayList();
                //Switch to other screen
                Intent theIntent = new Intent(ShoppingList.this, ShoppingListScrollablePage.class);
                theIntent.putExtra("TheList", theList);
                startActivity(theIntent);
            }
        }, 2000);
    }

    public void createIngredientRow() {
        // create row
        LinearLayout row = new LinearLayout(this);
        row.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        row.setOrientation(LinearLayout.HORIZONTAL);

        // row elements
        EditText input = new EditText(this);
        EditText quantityInput = new EditText(this);
        Button removeButton = new Button(this);

        row.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int width = row.getWidth();
                    }
                });

        removeButton.setLayoutParams(new ViewGroup.LayoutParams(
                205,
                ViewGroup.LayoutParams.MATCH_PARENT));

        quantityInput.setLayoutParams(new ViewGroup.LayoutParams(
                205,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        input.setLayoutParams(new ViewGroup.LayoutParams(
                500,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        removeButton.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        removeButton.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        int cornerRadius = removeButton.getHeight() / 2;

                        GradientDrawable drawable = new GradientDrawable();

                        drawable.setShape(GradientDrawable.RECTANGLE);
                        drawable.setCornerRadius(cornerRadius);

                        drawable.setColor(Color.rgb(102, 80, 164)); // purple

                        removeButton.setPadding(40, 4, 40, 4);
                        removeButton.setBackground(drawable);
                    }
                });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientContainer.removeView(row);
                ingredients.remove(input);
                quantities.remove(quantityInput);
                ingredientCount--;
            }
        });

        removeButton.setAllCaps(false);
        removeButton.setTextColor(Color.WHITE);
        removeButton.setText("-");
        removeButton.setTextSize(14);

        input.setHint("ingredient");

        quantityInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        quantityInput.setHint("0");

        row.addView(input);
        row.addView(quantityInput);
        row.addView(removeButton);

        ingredientContainer.addView(row);

        //ingredients.add(String.valueOf(input.getText()));
        //quantities.add(String.valueOf(quantityInput.getText()));
        ingredients.add(input);
        quantities.add(quantityInput);

        Log.d("Shit", ingredients.get(0).getText().toString());
    }
}
