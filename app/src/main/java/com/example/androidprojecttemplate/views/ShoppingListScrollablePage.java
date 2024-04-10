package com.example.androidprojecttemplate.views;
import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;
import android.util.Log;
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
import com.example.androidprojecttemplate.viewModels.ShoppingListScrollableViewModel;
import com.example.androidprojecttemplate.viewModels.ShoppingListViewModel;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;


public class ShoppingListScrollablePage extends AppCompatActivity {
    private ListView theListView;

    private ArrayList<String> theListOfShoppingIngredients = new ArrayList<>();
    private ArrayAdapter adapter;
    private Button goBacktoShoppingScreen ;

    private ShoppingListScrollableViewModel viewModel;
    private TextView theQuantity;
    private Button increase;
    private Button decrease;
    private Button goToCheckBoxScreen;
    private Timer timer;

    private String temp = "hello";

    private Handler timerHandler = new Handler();
    private static String[] ShoppingHolder = new String[1];


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shopping_list_scrollable_page);
        goBacktoShoppingScreen = findViewById(R.id.buttonToGoBack);
        theListView = findViewById(R.id.scrollableListForShopping);
        theQuantity = findViewById(R.id.quantityTextViewShopping);
        increase = findViewById(R.id.increaseShopping);
        decrease = findViewById(R.id.decreaseShopping);
        goToCheckBoxScreen = findViewById(R.id.buttonToCheckBox);

        viewModel = ShoppingListScrollableViewModel.getInstance();
        Thread theThread = new Thread() {
            public void run() {
                theListOfShoppingIngredients = getIntent().getExtras().getStringArrayList("TheList");

                adapter = new ArrayAdapter(ShoppingListScrollablePage.this,
                        android.R.layout.simple_list_item_1, theListOfShoppingIngredients);
                theListView.setAdapter(adapter);

                // When someone clicks on an item

                theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        viewModel.getCurrentUser();
                        ShoppingHolder[0] =  adapter.getItem(position).toString();
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                temp = viewModel.getTheQuantity(adapter.getItem(position).toString(), 0);
                            }
                        }, 1000);
                        theQuantity.setText(temp);
                    }
                });

                // Button to go back to the ingredient screen
                goBacktoShoppingScreen.setOnClickListener(v -> {
                    Intent theIntent = new Intent(ShoppingListScrollablePage.this, ShoppingList.class);
                    startActivity(theIntent);
                });


                increase.setOnClickListener(v -> {
                    viewModel.getCurrentUser();
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            temp = viewModel.getTheQuantity(ShoppingHolder[0], 1);
                        }
                    }, 1000);
                    theQuantity.setText(temp);
                });

                decrease.setOnClickListener(v -> {
                    viewModel.getCurrentUser();
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            temp = viewModel.getTheQuantity(ShoppingHolder[0], 2);
                        }
                    }, 1000);
                    theQuantity.setText(temp);
                });
            }
        };
        theThread.start();

        goToCheckBoxScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = new Intent(ShoppingListScrollablePage.this, ShoppingListTheCheckBoxPage.class);
                theIntent.putExtra("ListForShopping", theListOfShoppingIngredients);
                startActivity(theIntent);
            }
        });

    }

}