package com.example.androidprojecttemplate.views;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidprojecttemplate.R;



import java.util.ArrayList;


public class IngredientListPage extends AppCompatActivity {
    private ListView theListView;

    private ArrayList<String> theListOfIngredients = new ArrayList<>();
    private ArrayAdapter adapter;
    private Button goBackToIngredientScreen;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ingredient_list_page);
        goBackToIngredientScreen = findViewById(R.id.goBack);
        theListView = findViewById(R.id.theListViewForIngredients);

        theListOfIngredients = getIntent().getExtras().getStringArrayList("TheList");

        adapter = new ArrayAdapter(IngredientListPage.this, android.R.layout.simple_list_item_1, theListOfIngredients);
        theListView.setAdapter(adapter);

        goBackToIngredientScreen.setOnClickListener(v -> {
            Intent theIntent = new Intent(IngredientListPage.this, IngredientPage.class);
            startActivity(theIntent);
        });

    }

}
