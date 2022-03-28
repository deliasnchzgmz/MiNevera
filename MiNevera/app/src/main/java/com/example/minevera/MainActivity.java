package com.example.minevera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.database.Cursor;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private dbProducts dbAdapter;
    private ListView p_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  //inflar layout

        dbAdapter = new dbProducts(this);
        dbAdapter.open();

        // Creamos un listview que va a contener el título de todas las notas y
        // en el que cuando pulsemos sobre un título lancemos una actividad de editar
        // la nota con el id correspondiente
        p_listview = (ListView) findViewById(R.id.id_list_view);

        // rellenamos el listview con los títulos de todas las notas en la BD
        fillData();

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
          Intent listIntent = new Intent(this, SearchIngr.class);
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
        //Snackbar.make(findViewById(R.id.mainact), R.string.b_mapa_desc,
        //Snackbar.LENGTH_SHORT).show();
        Intent mapIntent = new Intent(this, Map.class);
        startActivity(mapIntent);
    }

    private void fillData() {
        Cursor notesCursor = dbAdapter.fetchAllNotes();

        // Creamos un array con los campos que queremos mostrar en el listview (sólo el título de la nota)
        String[] from = new String[]{dbProducts.KEY_TITLE,dbProducts.KEY_DATE};

        // array con los campos que queremos ligar a los campos del array de la línea anterior (en este caso sólo text1)
        int[] to = new int[]{R.id.text1, R.id.text2};

        // Creamos un SimpleCursorAdapter y lo asignamos al listview para mostrarlo
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.notes_row, notesCursor, from, to, 0);
        p_listview.setAdapter(notes);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }

}