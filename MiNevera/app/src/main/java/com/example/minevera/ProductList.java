package com.example.minevera;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ProductList extends AppCompatActivity {

    // para indicar en un Intent si se quiere crear una nueva nota o editar una existente
    private static final int ACTIVITY_CREATE=0;
    //private static final int ACTIVITY_EDIT=1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflamos el layout
        setContentView(R.layout.activity_list);


    }


}
