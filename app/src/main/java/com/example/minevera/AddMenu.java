package com.example.minevera;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class AddMenu extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);
    }
    //Botón de añadir a la lista de la compra manualmente
    public void AñadirManualmente(View view) {
        Intent listIntent = new Intent(this, AddProducts.class);
        startActivity(listIntent);

    }

    //Botón de abrir cámara
    public void AbrirCamara(View view) {
        Intent addIntent = new Intent(this, Camara.class);
        startActivity(addIntent);
    }
}