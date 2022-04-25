package com.example.minevera;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class CardAdapter extends ArrayAdapter<ProductObject> {


    public CardAdapter(Context context, ArrayList<ProductObject> product) {
        super(context, 0, product);
    }
    /*
    public int getItemPosition(long id)
    {
        for (int position=0; position<lista.size(); position++)
            if (lista.get(position).getId() == id)
                return position;
        return 0;
    }
    */


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // si pillo la pos del product list funcionaría !!! algo rollo productList(pos) pero bien escrito
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
