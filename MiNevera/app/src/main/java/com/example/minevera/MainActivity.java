package com.example.minevera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

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

    //Botón de Recetas
    public void Recetas(android.view.View view) {
          Snackbar.make(findViewById(R.id.mainact), R.string.b_lista_desc,
                Snackbar.LENGTH_SHORT).show();
          Intent listIntent = new Intent(this, SearchIngr.class);
          startActivity(listIntent);

    }

    public void AñadirNevera(android.view.View view){
        ImageButton boton_camara =  findViewById(R.id.boton_camara);
        ImageButton boton_bbdd = findViewById(R.id.boton_bbdd);

        boton_camara.setVisibility(View.VISIBLE);
        boton_bbdd.setVisibility(View.VISIBLE);
    }

    public void AbrirCamara(View view) {
        Intent addIntent = new Intent(this, Camara.class);
        startActivity(addIntent);
        finish();
    }

    //Botón de añadir a la lista de la compra manualmente
    public void AñadirManualmente(View view) {
        Intent listIntent = new Intent(this, AddProducts.class);
        startActivity(listIntent);
        finish();
    }

    //Botón de abrir el mapa
    public void AbrirMapa(android.view.View view) {
        //Snackbar.make(findViewById(R.id.mainact), R.string.b_mapa_desc,
        //Snackbar.LENGTH_SHORT).show();
        Intent mapIntent = new Intent(this, Map.class);
        startActivity(mapIntent);
    }

    private void fillData() {
        ArrayList<ProductObject> productList = new ArrayList<ProductObject>();
        Cursor notesCursor = dbAdapter.fetchAllNotes();
        int count  = notesCursor.getCount(); //número de elementos en la base de datos
        ArrayList<SimpleCursorAdapter> mArray;
        for (int i = 1;i<=count;i++){
            Cursor singleCursor = dbAdapter.fetchNote(i);
            Cursor aux = singleCursor;
            aux.moveToFirst();
            String n = aux.getString(1);
            String d = aux.getString(2);
            String diff_days = aux.getString(3);
            ProductObject product = new ProductObject(Integer.toString(i),n,d,diff_days);
            productList.add(product);
            }
        CardAdapter adapter = new CardAdapter(this,productList);

        p_listview.setAdapter(adapter);
    }

    private int setLayout(String difference){
        int diff = Integer.parseInt(difference);
        if (diff<=2){
            return R.layout.cards_row_red;
        }else if(diff>2&&diff<5){
            return R.layout.cards_row_amber;
        }else{
            return R.layout.cards_row_green;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }

}