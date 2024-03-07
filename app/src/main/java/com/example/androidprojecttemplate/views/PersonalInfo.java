package com.example.androidprojecttemplate.views;

// Do not import android support because we are using androidx
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.androidprojecttemplate.R;
import com.google.android.material.navigation.NavigationView;


//implements NavigationView.OnNavigationItemSelectedListener
public class PersonalInfo extends AppCompatActivity {
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private static boolean isLoggedIn = false;

    private NavigationView nav_view;

    private EditText heightField;
    private EditText weightField;
    private RadioGroup genderButtons;
    private String genderChosen = null;

    private Button saveButton;

    // plan -- add object here later to store demographic data

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_personal_info_page);

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

        heightField = findViewById(R.id.heightNumberField);
        weightField = findViewById(R.id.weightNumberField);
        genderButtons = findViewById(R.id.genderRadioGroup);
        saveButton = findViewById(R.id.piiSaveButton);

        //get user data and reflect the values previously defined in the text field
        //so get user height and prefill it in heightField as an empty field text
        //similarly get user weight and prefill it in weightField as an empty number
        //finally, set up gender -- try checking RadioButton.setChecked(boolean checked) for more details

        //now set up listener for gender buttons so that when they are clicked they update genderChosen
        genderButtons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton b = findViewById(checkedId);

                genderChosen = getGenderChosen(b);
            }
        });

        //and set up save button listener so it:
        // 1) deactivate the input widgets
        // 2) save to external database
        //   2b) gather data from input and
        //   2a) if it takes too long to connect, reactivate the widget and exit this function
        // 3) clear all the inputs and prefill with new values
        // 4) reactivate widgets
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heightField.setEnabled(false);
                weightField.setEnabled(false);
                genderButtons.setEnabled(false);

                //gather data and send it to external database
                saveToExternalDatabase();

                //prefill with new values, try looking at EditText.setHint(String s) for more details
                prefillValues();

                //re-enable UI components
                heightField.setEnabled(true);
                weightField.setEnabled(true);
                genderButtons.setEnabled(true);
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

    /**
     * A helper method to determine what to set genderChosen to after a radioButton is clicked.
     *
     * @param b the RadioButton chosen
     * @return what gender was selected: "Male", "Female", "Non-Binary", "Other"
     * */
    private String getGenderChosen(RadioButton b) {
        String label = b.getText().toString();
        if (label.equals("Other/Not Specified")) {
            return "Other";
        } else {
            return label;
        }
    }

    /**
     * This gets the data in the text field and sends it off to the database.
     * The plan is to update an object in here, probably a ViewModel of some sort,
     * and then send the results off to the database. If a text field is blank or
     * no changes have occurred, then exit this function.
     */
    private void saveToExternalDatabase() {

    }

    /**
     * Gets data from the planned Demographic object, and then
     * prefills it into empty text fields.
     */
    private void prefillValues() {

    }
}
