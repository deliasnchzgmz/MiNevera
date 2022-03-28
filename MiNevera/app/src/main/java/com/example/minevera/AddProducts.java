package com.example.minevera;
/*
 * Asignatura Aplicaciones Moviles - UC3M
 * Update: 13/02/2018.
 *
 * Based in code by Google with Apache License, Version 2.0
 *
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class AddProducts extends AppCompatActivity {

    private int[] daysOfMonth = new int[]{31,28,31,30,31,30,31,31,30,31,30,31};

    private EditText productName;
    private Long mRowId;
    private dbProducts dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // infla el layout
        setContentView(R.layout.activity_add);

        // obtiene referencia a los tres views que componen el layout
        productName = (EditText) findViewById(R.id.name);
        Button confirmButton = (Button) findViewById(R.id.save);

        //creamos el adaptador de la BD y la abrimos
        dbAdapter = new dbProducts(this);
        dbAdapter.open();

        // obtiene id de fila de la tabla si se le ha pasado (hemos pulsado una nota para editarla)
        mRowId = (savedInstanceState == null) ? null :
                (Long) savedInstanceState.getSerializable(dbProducts.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(dbProducts.KEY_ROWID) : null;
        }
        // Si se le ha pasado un id (no era null) rellena el título y el cuerpo con los campos guardados en la BD
        // en caso contrario se dejan en blanco (editamos una nota nueva)
        if (mRowId != null) {
            Cursor note = dbAdapter.fetchNote(mRowId);
            productName.setText(note.getString(
                    note.getColumnIndexOrThrow(dbProducts.KEY_TITLE)));

        }
    }
    /*@Override
    public boolean onOptionsItemSelected(Button b) {
        // Gestiona la seleccion de opciones en el menú
        int id = b.getId();
        if (id == R.id.action_delete) {
            if (mRowId != null) {
                dbAdapter.deleteNote(mRowId);
            }
            setResult(RESULT_OK);
            dbAdapter.close();
            finish();
        }

        if (id == R.id.action_about) {
            System.out.println("APPMOV: About action...");
        }

        return super.onOptionsItemSelected(item);
    }*/

    public void saveProduct(View view) {
        String name = productName.getText().toString();
        String num_days = productDays.getText().toString();
        String days = getDate(num_days);
        if (mRowId == null) {
            long id = dbAdapter.createNote(name);
            if (id > 0) {
                mRowId = id;
            }
        } else {
           // dbAdapter.updateNote(mRowId, name);
        }
        setResult(RESULT_OK);
        dbAdapter.close();
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
        finish();
    }

    public String getDate(String num_days){
        String exp_date;
        int input_days = Integer.parseInt(num_days);
        int[] new_date_int = new int[3];
        int[] current_date_int = new int[3];
        int[] int_separate = new int[3];
        String[] current_separate = (DateFormat.format("dd-MM-yyyy", new java.util.Date()).toString()).split("-");

        for (int i=0; i< current_separate.length; i++){
            current_date_int[i] = Integer.parseInt(current_separate[i]); //array de ints fecha actual
        }//int[0]=dia; int[1]=mes; int[2]=año
        if((current_date_int[0]+input_days)>daysOfMonth[current_date_int[1]]){//compruebo si se pasa de mes
            if(current_date_int[1]==12){ //compruebo si se pasa de año
                new_date_int[2] = current_date_int[2]+1;
            }else{//solo pasa de mes
                if((current_date_int[1]==2)&&(current_date_int[2]%4==0)&&((current_date_int[2]%100==0)||(current_date_int[2]%400==0))){ //es bisiesto?
                    new_date_int[0] = (current_date_int[0]+input_days)-(daysOfMonth[current_date_int[1]]+1);
                    //new_day = current_day+input_days-num_days_current_month
                    new_date_int[1] = current_date_int[1]+1; //paso de mes
                }else {
                    new_date_int[0] = (current_date_int[0] + input_days) - (daysOfMonth[current_date_int[1]]+1);
                    //new_day = current_day+input_days-num_days_current_month
                    new_date_int[1] = current_date_int[1] + 1; //paso de mes
                }
                new_date_int[2] = current_date_int[2];
            }

        }else{
            //si no paso ni de mes ni de año
            new_date_int[0] = current_date_int[0]+input_days;
            new_date_int[1] = current_date_int[1];
            new_date_int[2] =current_date_int[2];
        }
        //ahora lo paso a string!!!
        exp_date = Integer.toString(new_date_int[0])+"-"+Integer.toString(new_date_int[1])+"-"+Integer.toString(new_date_int[2]);

        return exp_date;
    }

}

