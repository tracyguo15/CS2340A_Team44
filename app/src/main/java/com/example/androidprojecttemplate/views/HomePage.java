package com.example.androidprojecttemplate.views;

import com.example.androidprojecttemplate.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//implements NavigationView.OnNavigationItemSelectedListener
public class HomePage extends AppCompatActivity {
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private static boolean isLoggedIn = false;

    private NavigationView navView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Check if user is logged in before going on home page
        if (isLoggedIn) {
            // If not logged in, stay on the login activity
            Intent intent = new Intent(this, LoginPageActivity.class);
            startActivity(intent);
            // Finish the current activity to prevent going back to it when pressing back button
            finish();
            return;
        }
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
                    //Toast.makeText(HomePage.this, "InputMeal", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomePage.this, InputMealPage.class);
                    startActivity(intent);
                } else if (id == R.id.recipe) {
                    //Toast.makeText(HomePage.this, "Recipe", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomePage.this, RecipePage.class);
                    startActivity(intent);
                } else if (id == R.id.ingredient) {
                    //Toast.makeText(HomePage.this, "Ingredient", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomePage.this, IngredientPage.class);
                    startActivity(intent);
                } else if (id == R.id.list) {
                    //Toast.makeText(HomePage.this, "ShoppingList", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomePage.this, ShoppingList.class);
                    startActivity(intent);
                } else if (id == R.id.personalinfo) {
                    Intent intent = new Intent(HomePage.this, PersonalInfo.class);
                    startActivity(intent);
                }
                return true;
            }
        });


        testAccess();
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

    /*
    Method to set the login state. remember to set login after login page is implemented
    public static void setLoggedIn(boolean value) {
        isLoggedIn = value;
    }
     */
    private DatabaseReference recipeDB;
    private ArrayList<String> superList = new ArrayList<>();
    private interface FirebaseCallback {
        void onCallback(List<String> list);
    }

    private void accessRecipes(FirebaseCallback callback) {
        Log.d("CALLBACK", "starting callback");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    superList.add(child.getKey());
                }
                callback.onCallback(superList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("CALLBACK", "Error executing callback");
            }
        };

        recipeDB.addListenerForSingleValueEvent(valueEventListener);
    }

    private void testAccess() {
        recipeDB = FirebaseDatabase.getInstance().getReference().child("Cookbook");
        accessRecipes(new FirebaseCallback() {
            @Override
            public void onCallback(List<String> list) {
                Log.d("CALLBACKWITHIN", superList.toString());
                Log.d("CALLBACK", "good jooobbbb :333");
            }
        });
        Log.d("TEST ACCESS", superList.toString());
    }
}
