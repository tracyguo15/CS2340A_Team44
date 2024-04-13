package com.example.androidprojecttemplate.models;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> items;
    private int[] textColors; // Array to hold individual text colors for each item

    public CustomAdapter(Context context, int resource, List<String> items, int[] textColors) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
        this.textColors = textColors;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText(items.get(position));

        // Set text color for the current item
        if (position < textColors.length) {
            textView.setTextColor(textColors[position]);
        } else {
            textView.setTextColor(Color.BLACK); // Default color if no color provided
        }

        return view;
    }
}
