package com.example.androidprojecttemplate.views;

// Do not import android support because we are using androidx
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
import com.example.androidprojecttemplate.models.UserData;
import com.example.androidprojecttemplate.viewModels.UserDataViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.androidprojecttemplate.R;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


// implements NavigationView.OnNavigationItemSelectedListener
public class InputMealPage extends AppCompatActivity {
    // ui
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private TextView userHeight;
    private TextView userWeight;
    private TextView userGender;
    private TextView userCalorieGoal;
    private TextView userDailyCalorieIntake;
    private EditText mealInput;
    private EditText calorieInput;
    private Button submitMealData;
    private NavigationView nav_view;

    // state
    private static boolean isLoggedIn = false;
    private UserDataViewModel viewModel;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference reference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_meal_page);

        // ui
        userHeight = findViewById(R.id.userHeight);
        userWeight = findViewById(R.id.userWeight);
        userGender = findViewById(R.id.userGender);
        userCalorieGoal = findViewById(R.id.userCalorieGoal);
        userDailyCalorieIntake = findViewById(R.id.userDailyCalorieIntake);
        mealInput = findViewById(R.id.mealInput);
        calorieInput = findViewById(R.id.calorieInput);
        submitMealData = findViewById(R.id.submitMealData);

        // navbar
        Toolbar homeToolBar = (Toolbar) findViewById(R.id.nav_toolbar);
        setSupportActionBar(homeToolBar);

        // nav menu
        dl = (DrawerLayout) findViewById(R.id.dl);
        abdt = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(abdt);
        abdt.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nav_view = (NavigationView) findViewById(R.id.nav_view);

        nav_view.setVisibility(View.GONE);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                System.out.println(R.id.inputmeal);
                int id = item.getItemId();
                if (id == R.id.inputmeal) {
                    if (!(InputMealPage.this instanceof InputMealPage)) {
                        Intent intent = new Intent(InputMealPage.this, InputMealPage.class);
                        startActivity(intent);
                    }
                    return true;
                } else if (id == R.id.recipe) {
                    Intent intent = new Intent(InputMealPage.this, RecipePage.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.ingredient) {
                    Intent intent = new Intent(InputMealPage.this, IngredientPage.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.list) {
                    Intent intent = new Intent(InputMealPage.this, ListPage.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.personalinfo) {
                    Intent intent = new Intent(InputMealPage.this, PersonalInfo.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        String email = user.getEmail();

        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot theSnapshot: snapshot.getChildren()) {

                    String theEmailFromFirebase = theSnapshot.child("username").getValue().toString();
                    if (theEmailFromFirebase.equals(email)) {
                        viewModel = UserDataViewModel.getInstance();

                        DataSnapshot userDataSnapshot = theSnapshot.child("Personal Info");

                        if (userDataSnapshot != null) {
                            String height = userDataSnapshot.child("height").getValue().toString();
                            String weight = userDataSnapshot.child("weight").getValue().toString();
                            String gender = userDataSnapshot.child("gender").getValue().toString();

                            viewModel.updateData(
                                Integer.parseInt(height),
                                Integer.parseInt(weight),
                                gender);

                            userHeight.setText(viewModel.heightText());
                            userWeight.setText(viewModel.weightText());
                            userGender.setText(viewModel.genderText());
                        } else {
                            userHeight.setText("no user data available");
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(InputMealPage.this, "Something went wrong in the outer portion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (nav_view.getVisibility() == View.VISIBLE) {
            nav_view.setVisibility(View.GONE);
        } else {
            nav_view.setVisibility(View.VISIBLE);
        }
        return true || abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}
