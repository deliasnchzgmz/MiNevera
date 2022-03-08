package com.example.minevera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    //Botón de añadir a la lista de la compra
    public void ListaCompra(android.view.View view) {
          Snackbar.make(findViewById(R.id.mainact), R.string.b_lista_desc,
                Snackbar.LENGTH_SHORT).show();
          Intent listIntent = new Intent(this, ProductList.class);
          startActivity(listIntent);

    }
    //Botón de añadir productos a la nevera
    public void AñadirNevera(android.view.View view) {
        Snackbar.make(findViewById(R.id.mainact), R.string.b_mas_desc,
                Snackbar.LENGTH_SHORT).show();
        Intent addIntent = new Intent(this, AddProducts.class);
        startActivity(addIntent);
    }
    //Botón de abrir el mapa
    public void AbrirMapa(android.view.View view) {
        Snackbar.make(findViewById(R.id.mainact), R.string.b_mapa_desc,
                Snackbar.LENGTH_SHORT).show();
        Intent mapIntent = new Intent(this, Map.class);
        startActivity(mapIntent);
    }


}