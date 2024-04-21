package com.example.androidprojecttemplate.views;
import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
//import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
//import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.androidprojecttemplate.R;
import com.example.androidprojecttemplate.viewModels.IngredientListViewModel;


import java.util.ArrayList;
import java.util.Timer;

import android.os.Handler;


public class IngredientListPage extends AppCompatActivity {
    private ListView theListView;

    private ArrayList<String> theListOfIngredients = new ArrayList<>();
    private ArrayList<String> theQuantities = new ArrayList<>();
    private ArrayAdapter adapter;
    private Button goBackToIngredientScreen;

    private IngredientListViewModel viewModel;
    private TextView theQuantity;
    private Button increase;
    private Button decrease;
    private Timer timer;
    private Timer timer2;
    private String temp = "hello";
    private int theMostRecentPosition = 0;

    private Handler timerHandler = new Handler();
    private static String[] ingredientHolder = new String[1];


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ingredient_list_page);
        goBackToIngredientScreen = findViewById(R.id.goBack);
        theListView = findViewById(R.id.theListViewForIngredients);
        theQuantity = findViewById(R.id.QuantityTextView);
        increase = findViewById(R.id.IncreaseQuantity);
        decrease = findViewById(R.id.DecreaseQuantity);

        viewModel = IngredientListViewModel.getInstance();
        Thread theThread = new Thread() {
            public void run() {
                theListOfIngredients = getIntent().getExtras().getStringArrayList("TheList");
                theQuantities = getIntent().getExtras().getStringArrayList("quantities");

                adapter = new ArrayAdapter(IngredientListPage.this,
                        android.R.layout.simple_list_item_1, theListOfIngredients);
                theListView.setAdapter(adapter);

                // When someone clicks on an item
                theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                      // Since the quantities were added in the same order as the ingredient names
                        // it'll return the correct quantity

                        theMostRecentPosition = position;
                        setText(position);
                    }
                });

                // Button to go back to the ingredient screen
                goBackToIngredientScreen.setOnClickListener(v -> {
                    Intent theIntent = new Intent(IngredientListPage.this, IngredientPage.class);
                    startActivity(theIntent);
                });


                increase.setOnClickListener(v -> {
                   // Changes quantity
                    int temp = Integer.parseInt(theQuantities.get(theMostRecentPosition));
                    temp += 1;
                    theQuantities.set(theMostRecentPosition, String.valueOf(temp));
                    setText(theMostRecentPosition);

                    // Method to update firebase
                    viewModel.getCurrentUser();
                    viewModel.setTheQuantity(adapter.getItem(theMostRecentPosition).toString(), temp);
                });

                decrease.setOnClickListener(v -> {
                    // Needed to update firebase
                    String theName = adapter.getItem(theMostRecentPosition).toString();

                    // Changes quantity
                    int temp = Integer.parseInt(theQuantities.get(theMostRecentPosition));
                    temp -= 1;
                    theQuantities.set(theMostRecentPosition, String.valueOf(temp));
                    setText(theMostRecentPosition);

                    // If the quantity is zero, then delete
                    if (temp == 0) {
                        // Calls method to remove from firebase
                        viewModel.getCurrentUser();
                        viewModel.setTheQuantity(adapter.getItem(theMostRecentPosition).toString(), temp);

                        // Remove from listview
                        theQuantities.remove(theMostRecentPosition);
                        theListOfIngredients.remove(theMostRecentPosition);
                        adapter.notifyDataSetChanged();
                    } else {
                        viewModel.getCurrentUser();
                        viewModel.setTheQuantity(adapter.getItem(theMostRecentPosition).toString(), temp);
                    }
                });
            }
        };
        theThread.start();
    }

    public void setText(int position) {
        theQuantity.setText(theQuantities.get(position));
    }

}
