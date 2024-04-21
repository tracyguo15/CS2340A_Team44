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
import com.example.androidprojecttemplate.viewModels.ShoppingListScrollableViewModel;


import java.util.ArrayList;


public class ShoppingListScrollablePage extends AppCompatActivity {
    private ListView theListView;

    private ArrayList<String> theListOfShoppingIngredients = new ArrayList<>();
    private ArrayList<String> theQuantities = new ArrayList<>();
    private ArrayAdapter adapter;
    private Button goBacktoShoppingScreen ;

    private ShoppingListScrollableViewModel viewModel;
    private TextView theQuantity;
    private Button increase;
    private Button decrease;
    private Button goToCheckBoxScreen;
    private int theMostRecentPosition = 0;

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
                theQuantities = getIntent().getExtras().getStringArrayList("TheQuantities");

                adapter = new ArrayAdapter(ShoppingListScrollablePage.this,
                        android.R.layout.simple_list_item_1, theListOfShoppingIngredients);
                theListView.setAdapter(adapter);

                // When someone clicks on an item

                theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Since the quantities were added in the same order as the ingredient names,
                        // it'll return the correct quantity

                        theMostRecentPosition = position;
                        Log.d("thePosition", String.valueOf(theMostRecentPosition));
                        setText(position);
                    }
                });

                // Button to go back to the ingredient screen
                goBacktoShoppingScreen.setOnClickListener(v -> {
                    Intent theIntent = new Intent(ShoppingListScrollablePage.this, ShoppingList.class);
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
                        theListOfShoppingIngredients.remove(theMostRecentPosition);
                        adapter.notifyDataSetChanged();
                    } else {
                        viewModel.getCurrentUser();
                        viewModel.setTheQuantity(adapter.getItem(theMostRecentPosition).toString(), temp);
                    }
                });
            }
        };
        theThread.start();

        goToCheckBoxScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = new Intent(ShoppingListScrollablePage.this, ShoppingListTheCheckBoxPage.class);
                theIntent.putExtra("ListForShopping", theListOfShoppingIngredients);
                theIntent.putExtra("Quantities", theQuantities);
                startActivity(theIntent);
            }
        });



    }

    public void setText(int position) {
        theQuantity.setText(theQuantities.get(position));
    }

}