package com.example.minevera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;


import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private dbProducts dbAdapter;
    private ListView p_listview;
    private ArrayList<ProductObject> productList;


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
        p_listview.setOnItemClickListener(
                new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view, int position, long id)
                    {
                        Intent i = new Intent(view.getContext(),AddProducts.class);
                        //int real_id= Integer.parseInt (productList.get(position).getId());
                        int real_id= 0;
                        try {
                            real_id = findRealId(position);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        i.putExtra(dbProducts.KEY_ROWID, real_id);
                        startActivityForResult(i, 1);
                    }
                }
        );

        // rellenamos el listview con los títulos de todas las notas en la BD
        try {
            fillData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            checkData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
    }

    //Botón de añadir a la lista de la compra manualmente
    public void AñadirManualmente(View view) {
        Intent listIntent = new Intent(this, AddProducts.class);
        startActivity(listIntent);

    }

    //Botón de abrir el mapa
    public void AbrirMapa(android.view.View view) {
        //Snackbar.make(findViewById(R.id.mainact), R.string.b_mapa_desc,
        //Snackbar.LENGTH_SHORT).show();
        Intent mapIntent = new Intent(this, Map.class);
        startActivity(mapIntent);
    }

    private void fillData() throws ParseException {
        productList = new ArrayList<ProductObject>();
        Cursor notesCursor = dbAdapter.fetchAllNotes();
        //int count  = notesCursor.getCount(); //número de elementos en la base de datos
        ArrayList<SimpleCursorAdapter> mArray;
        ArrayList<Integer> ids = new ArrayList<Integer>();
        Cursor aux=notesCursor;
        if(aux.getCount()!=0) {
            aux.moveToFirst();
            while (notesCursor.isLast() != true) {
                Integer i = aux.getInt(0);
                String n = aux.getString(1);
                String d = aux.getString(2);
                dbAdapter.updateDifference(i, n, d);
                String diff_days = aux.getString(3);
                ProductObject product = new ProductObject(Integer.toString(i), n, d, diff_days);
                productList.add(product);
                aux.moveToNext();
            }
            Integer i = aux.getInt(0);
            String n = aux.getString(1);
            String d = aux.getString(2);
            dbAdapter.updateDifference(i, n, d);
            String diff_days = aux.getString(3);
            ProductObject product = new ProductObject(Integer.toString(i), n, d, diff_days);
            productList.add(product);
            aux.close();
        }
/*
            for (int i = 1;i<=count;i++){
                //fetchnote the mira el id, asi que si borro el id=3 se para y no funciona
                Cursor aux = dbAdapter.fetchNote(i);
                if(aux.getCount()!=0){

                    aux.moveToFirst();
                    String a=aux.getString(0);
                    String n = aux.getString(1);
                    String d = aux.getString(2);
                    dbAdapter.updateDifference(i, n, d);
                    String diff_days = aux.getString(3);
                    ProductObject product = new ProductObject(Integer.toString(i),n,d,diff_days);
                    productList.add(product);
                }

            }
*/
        CardAdapter adapter = new CardAdapter(this,productList);
        //int i es el id del producto que va en cada card

        p_listview.setAdapter(adapter);
    }

    private int findRealId(int position) throws ParseException {
        productList = new ArrayList<ProductObject>();
        Cursor notesCursor = dbAdapter.fetchAllNotes();
        ArrayList<SimpleCursorAdapter> mArray;
        ArrayList<Integer> ids = new ArrayList<Integer>();
        Cursor aux = notesCursor;
        if (aux.getCount() != 0) {
            aux.moveToFirst();
            while (notesCursor.isLast() != true) {
                Integer i = aux.getInt(0);
                String n = aux.getString(1);
                String d = aux.getString(2);
                dbAdapter.updateDifference(i, n, d);
                String diff_days = aux.getString(3);
                ProductObject product = new ProductObject(Integer.toString(i), n, d, diff_days);
                productList.add(product);
                aux.moveToNext();
            }
            Integer i = aux.getInt(0);
            String n = aux.getString(1);
            String d = aux.getString(2);
            dbAdapter.updateDifference(i, n, d);
            String diff_days = aux.getString(3);
            ProductObject product = new ProductObject(Integer.toString(i), n, d, diff_days);
            productList.add(product);

            aux.close();
        }


        int id=Integer.parseInt(productList.get(position).getId());
        return id;
    }

    //check data to see if we need to send notifications
    private void checkData() throws ParseException {
        productList = new ArrayList<ProductObject>();
        Cursor notesCursor = dbAdapter.fetchAllNotes();
        //int count  = notesCursor.getCount(); //número de elementos en la base de datos
        ArrayList<SimpleCursorAdapter> mArray;
        ArrayList<Integer> ids = new ArrayList<Integer>();
        Cursor aux=notesCursor;
        if(aux.getCount()!=0) {
            aux.moveToFirst();
            while (notesCursor.isLast() != true) {
                Integer i = aux.getInt(0);
                String n = aux.getString(1);
                String d = aux.getString(2);
                dbAdapter.updateDifference(i, n, d);
                String diff_days = aux.getString(3);
                if(Integer. parseInt(diff_days)<=2){
                    startNotifications(n,diff_days);
                }
                ProductObject product = new ProductObject(Integer.toString(i), n, d, diff_days);
                productList.add(product);
                aux.moveToNext();
            }
            Integer i = aux.getInt(0);
            String n = aux.getString(1);
            String d = aux.getString(2);
            dbAdapter.updateDifference(i, n, d);
            String diff_days = aux.getString(3);
            if(Integer. parseInt(diff_days)<=2){
                startNotifications(n,diff_days);
            }
            ProductObject product = new ProductObject(Integer.toString(i), n, d, diff_days);
            productList.add(product);
            aux.close();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        try {
            fillData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //las notificaciones

    public void startNotifications(String name, String diff) {
        // Creamos el Intent que va a lanzar la activity
        Intent intent = new Intent(this, NotificationService.class);

        // Creamos la informacion a pasar entre actividades
        Bundle b = new Bundle();
        //b.putString("NAME", nameText.getText().toString());
        b.putString("NAME", name);
        b.putString("DAYS", diff);

        // Asociamos esta informacion al intent
        intent.putExtras(b);

        // Iniciamos el servicio
        startService(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, NotificationService.class);
        stopService(intent);
    }



}