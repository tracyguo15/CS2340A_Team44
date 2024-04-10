package com.example.androidprojecttemplate.views;
import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
//import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
//import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidprojecttemplate.R;
import com.example.androidprojecttemplate.viewModels.ShoppingListScrollableViewModel;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
import android.widget.Toast;


public class ShoppingListTheCheckBoxPage extends AppCompatActivity {
    private Button buttonToGoBackToScrollablePage;
    private Button buttonToSubmit;
    private ListView theListView;
    private ArrayList<String> theShoppingList = new ArrayList<>();
    ArrayAdapter<String> adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shopping_list_check_box_pge);
        buttonToGoBackToScrollablePage = findViewById(R.id.goBackToScrollable);
        buttonToSubmit = findViewById(R.id.submitFromCheckBox);
        theListView = findViewById(R.id.TheListViewCheckBox);

        theShoppingList = getIntent().getExtras().getStringArrayList("ListForShopping");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, theShoppingList);
        theListView.setAdapter(adapter);

        theListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        buttonToGoBackToScrollablePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = new Intent(ShoppingListTheCheckBoxPage.this, ShoppingListScrollablePage.class);
                startActivity(theIntent);
            }
        });


        buttonToSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Will analyze the listview with the different check boxes
                String itemSelected = "Selected items:";
                for(int i = 0; i < theListView.getCount(); i++) {
                    if(theListView.isItemChecked(i)) {
                        itemSelected += theListView.getItemAtPosition(i);
                    }
                }
                Toast.makeText(ShoppingListTheCheckBoxPage.this, itemSelected, Toast.LENGTH_SHORT).show();
            }
        });


    }


}