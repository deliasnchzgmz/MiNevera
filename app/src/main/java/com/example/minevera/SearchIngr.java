package com.example.minevera;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

/*
 * Asignatura Aplicaciones Moviles - UC3M
 * Update: 04/03/2022.
 *
 */

public class SearchIngr extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ingr);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //creamos los intents que van a llevar desde el menu desplegable del app bar hasta esas dos actividades
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

    public void sendName(View view) {
        // Creamos el Intent que va a lanzar la activity Recipe
        Intent intent = new Intent(this, Recipe.class);
        // Obtenemos referencias a los elementos del interfaz grafico
        EditText nameText = (EditText) findViewById(R.id.edit_message);
        Button searchButton = (Button) findViewById(R.id.button_search);

        if(nameText.getText().toString().equals("")||nameText.getText().toString().equals(" ")){
            Snackbar.make(findViewById(R.id.search_ingr), R.string.message_dialog_ingr, Snackbar.LENGTH_SHORT).show();
        }else{
        // Creamos la informacion a pasar entre actividades
        Bundle b = new Bundle();
        b.putString("ingredient", nameText.getText().toString());

        // Asociamos esta informacion al intent creado
        intent.putExtras(b);

        // Iniciamos la nueva actividad
        startActivity(intent);
        }

    }
}