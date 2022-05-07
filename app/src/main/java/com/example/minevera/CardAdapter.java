package com.example.minevera;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


//Creación de un adaptador, para elegir qué layout colocar dependiendo de los días que queden hasta la fecha de caducidad del producto
//Realizado siguiendo las directrices de la página recomendada: Developer Android
public class CardAdapter extends ArrayAdapter<ProductObject> {


    public CardAdapter(Context context, ArrayList<ProductObject> productList) {
        super(context, 0,productList);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProductObject p = getItem(position);
        int diff = Integer.parseInt(p.difference);
        if (diff <= 2) {
            //Card roja si quedan 2 días o menos
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cards_row_red, parent, false);
        } else if (diff > 2 && diff < 5) {
            //Card ámbar si quedan entre 5 y 2 días
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cards_row_amber, parent, false);
        } else {
            //Card verde si quedan más de 5 días
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
