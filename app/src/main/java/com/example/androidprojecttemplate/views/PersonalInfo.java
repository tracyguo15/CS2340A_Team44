package com.example.androidprojecttemplate.views;

// Do not import android support because we are using androidx
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.androidprojecttemplate.R;
import com.example.androidprojecttemplate.models.UserData;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


//implements NavigationView.OnNavigationItemSelectedListener
public class PersonalInfo extends AppCompatActivity {
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private static boolean isLoggedIn = false;

    private NavigationView nav_view;

    private EditText theHeightInput;

    private EditText theWeightInput;

    private EditText theGenderInput;

    private Button theButtonToLogData;

    // For firebase authentication (to get user's email)
    FirebaseAuth auth;
    FirebaseUser user;


    // For real-time database
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    DatabaseReference tempReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_personal_info_page);
        theHeightInput = findViewById(R.id.theHeight);
        theWeightInput = findViewById(R.id.theWeight);
        theGenderInput = findViewById(R.id.theGener);
        theButtonToLogData = findViewById(R.id.buttonToEnnterData);


        Toolbar homeToolBar = (Toolbar) findViewById(R.id.nav_toolbar);
        setSupportActionBar(homeToolBar);
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
                int id = item.getItemId();
                if (id == R.id.inputmeal) {
                    Intent intent = new Intent(PersonalInfo.this, InputMealPage.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.recipe) {
                    Intent intent = new Intent(PersonalInfo.this, RecipePage.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.ingredient) {
                    Intent intent = new Intent(PersonalInfo.this, IngredientPage.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.list) {
                    Intent intent = new Intent(PersonalInfo.this, ListPage.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.personalinfo) {
                    if (!(PersonalInfo.this instanceof PersonalInfo)) {
                        Intent intent = new Intent(PersonalInfo.this, PersonalInfo.class);
                        startActivity(intent);
                    }
                    return true;
                }
                return false;
            }
        });

        // Get the current user's email, which will be used further down the code
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        String theUsersEmail = user.getEmail();

        //String[] theNames = new String[15];

        // Will now focus on logging the data
        theButtonToLogData.setOnClickListener(v -> {
            // Check if the the inputs from the edit text are valid or not
            String height = String.valueOf(theHeightInput.getText());
            String weight = String.valueOf(theWeightInput.getText());
            String gender = String.valueOf(theGenderInput.getText());

            if (TextUtils.isEmpty(height)) {
                Toast.makeText(PersonalInfo.this, "Please enter a height!", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(weight)) {
                Toast.makeText(PersonalInfo.this, "Please enter a weight!", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(gender)) {
                Toast.makeText(PersonalInfo.this, "Please enter a gender!", Toast.LENGTH_SHORT).show();
                return;
            }

            reference = FirebaseDatabase.getInstance().getReference().child("Users");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot theSnapshot: snapshot.getChildren()) {

                        String theEmailFromFirebase = theSnapshot.child("username").getValue().toString();
                        if (theEmailFromFirebase.equals(theUsersEmail)) {
                           //Found the email, can now add the data for that specific user
                            //UserData theInfo = new personalInfo(height, weight, gender);
                            UserData data = new UserData();
                            data.setHeight(Integer.parseInt(height));
                            data.setWeight(Integer.parseInt(weight));
                            data.setGender(gender);

                            tempReference = reference.child(theSnapshot.child("name").getValue().toString());
                            tempReference.child("Personal Info").setValue(data);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(PersonalInfo.this, "Something went wrong in the outer portion", Toast.LENGTH_SHORT).show();
                }
            });

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
