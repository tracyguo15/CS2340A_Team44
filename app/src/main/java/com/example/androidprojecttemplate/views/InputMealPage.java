package com.example.androidprojecttemplate.views;

// Do not import android support because we are using androidx
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
import com.example.androidprojecttemplate.models.UserData;
import com.example.androidprojecttemplate.models.MealData;
import com.example.androidprojecttemplate.viewModels.UserDataViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
    private Button visual1;
    private Button visual2;
    private NavigationView nav_view;

    // state
    private static boolean isLoggedIn = false;
    List<MealData> meals;
    int totalCalories;
    private UserDataViewModel viewModel;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference userReference;
    DatabaseReference mealReference;

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
        visual1 = findViewById(R.id.displayVisualization1);
        visual2 = findViewById(R.id.displayVisualization2);

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

        userReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mealReference = FirebaseDatabase.getInstance().getReference().child("Meals");

        // displaying user data
        userReference.addValueEventListener(new ValueEventListener() {
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
                            String age = userDataSnapshot.child("age").getValue().toString();

                            viewModel.updateData(
                                Integer.parseInt(height),
                                Integer.parseInt(weight),
                                gender,
                                Integer.parseInt(age));

                            userHeight.setText(viewModel.heightText());
                            userWeight.setText(viewModel.weightText());
                            userGender.setText(viewModel.genderText());
                            userCalorieGoal.setText(viewModel.calorieGoalText());
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

        // meal submission
        submitMealData.setOnClickListener(v -> {
            // validate inputs
            String meal = String.valueOf(mealInput.getText());
            String calories = String.valueOf(calorieInput.getText());

            if (TextUtils.isEmpty(meal)) {
                Toast.makeText(InputMealPage.this, "Please enter a meal!", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(calories)) {
                Toast.makeText(InputMealPage.this, "Please enter calories!", Toast.LENGTH_SHORT).show();
                return;
            } else if (Integer.parseInt(calories) <= 0) {
                Toast.makeText(InputMealPage.this, "Calories cannot be zero or negative!", Toast.LENGTH_SHORT).show();
                return;
            }

            // get date
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String date = currentDate.format(formatter);

            mealReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // update the meal data
                    MealData data = new MealData();
                    data.setCalories(Integer.parseInt(calories));
                    data.setUsername(email);
                    data.setDate(date);
                   
                    mealReference.child(meal).setValue(data);

                    // reset input text
                    mealInput.setText("");
                    calorieInput.setText("");

                    // display toast
                    Toast.makeText(InputMealPage.this, "Meal submitted!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(InputMealPage.this, "Something went wrong in the outer portion", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // displaying daily calories and collecting meal data
        mealReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // current date
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                String currentDate = LocalDate.now().format(formatter);

                for (DataSnapshot theSnapshot : snapshot.getChildren()) {
                    String firebaseUsername = theSnapshot.child("username").getValue().toString();
                    String date = theSnapshot.child("date").getValue().toString();

                    // query by username and date
                    if (firebaseUsername.equals(email) && date.equals(currentDate)) {
                        String calories = theSnapshot.child("calories").getValue().toString();
                        totalCalories += Integer.parseInt(calories);
                    }

                    // query by username
                    if (firebaseUsername.equals(email)) {
                        String calories = theSnapshot.child("calories").getValue().toString();

                        // update meals
                        MealData data = new MealData();
                        data.setCalories(Integer.parseInt(calories));
                        data.setUsername(email);
                        data.setDate(date);

                        meals.add(data);
                    }
                }

                // update daily calories label
                userDailyCalorieIntake.setText(String.format("Daily Calories: %d", totalCalories));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(InputMealPage.this, "Something went wrong in the outer portion", Toast.LENGTH_SHORT).show();
            }
        });

        // displaying visuals
        visual1.setOnClickListener(v -> {
            
        });

        visual2.setOnClickListener(v -> {
            
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
