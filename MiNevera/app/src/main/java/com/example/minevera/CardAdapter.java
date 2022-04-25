package com.example.minevera;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class CardAdapter extends ArrayAdapter<ProductObject> {

    public CardAdapter(Context context, ArrayList<ProductObject> product) {
        super(context, 0, product);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ProductObject p = getItem(position);

        int diff = Integer.parseInt(p.difference);
        if (diff<=2){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cards_row_red, parent, false);
        }else if(diff>2&&diff<5){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cards_row_amber, parent, false);
        }else{
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cards_row_green, parent, false);
        }
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cards_row_green, parent, false);
        }
        // Lookup view for data population
        TextView text1 = (TextView) convertView.findViewById(R.id.text1);
        TextView text2 = (TextView) convertView.findViewById(R.id.text2);
        // Populate the data into the template view using the data object
        text1.setText(p.name);
        text2.setText(p.date);
        // Return the completed view to render on screen
        return convertView;
    }
}
