package com.example.minevera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            System.out.println("APPMOV: About settings...");

            Intent intent = new Intent(this, Settings.class);

            startActivity(intent);
            return true;
        }
        if (id == R.id.action_about) {
            System.out.println("APPMOV: About action...");

            Intent intent = new Intent(this, AboutUs.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        Intent addIntent = new Intent(this, AddMenu.class);
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