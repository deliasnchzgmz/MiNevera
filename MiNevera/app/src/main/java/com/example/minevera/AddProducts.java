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
import com.google.android.material.snackbar.Snackbar;

import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddProducts extends AppCompatActivity {

    private EditText productName;
    private EditText productDays;

    private Long mRowId;
    private dbProducts dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // infla el layout
        setContentView(R.layout.activity_add);

        // obtiene referencia a los tres views que componen el layout
        productName = (EditText) findViewById(R.id.name);
        productDays = (EditText) findViewById(R.id.days);
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

    public void saveProduct(View view) throws ParseException {

        String name = productName.getText().toString();
        String num_days = productDays.getText().toString();

        if(name.equals("")||name.equals(" ")||num_days.equals("")||num_days.equals(" ")){
            Snackbar.make(findViewById(R.id.add_act), R.string.null_name,
                    Snackbar.LENGTH_SHORT).show();
            return;
        }else if(Integer.parseInt(num_days)<1){
            Snackbar.make(findViewById(R.id.add_act), R.string.null_days,
                    Snackbar.LENGTH_SHORT).show();
            return;
        }else if(Integer.parseInt(num_days)>25){
            Snackbar.make(findViewById(R.id.add_act), R.string.max_days,
                    Snackbar.LENGTH_SHORT).show();
            return;
        }

        String days = getDate(num_days);
        String diff = getDiff(days);
        if (mRowId == null) {
            if(name==null||name.equals(" ")){
                Snackbar.make(findViewById(R.id.mainact), R.string.null_name,
                        Snackbar.LENGTH_SHORT).show();
                return;
            }else if(Integer.parseInt(num_days)<1){
                Snackbar.make(findViewById(R.id.mainact), R.string.null_days,
                        Snackbar.LENGTH_SHORT).show();
                return;
            }else if(Integer.parseInt(num_days)>25){
                Snackbar.make(findViewById(R.id.mainact), R.string.max_days,
                        Snackbar.LENGTH_SHORT).show();
                return;
            }else{
                long id = dbAdapter.createNote(name, days, diff);
                if (id > 0) {
                    mRowId = id;
            }

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

    public String getDate(String num_days) throws ParseException {
        Calendar c = Calendar.getInstance();
        Date cDate = c.getTime();
        c.add(Calendar.DATE, Integer.parseInt(num_days));
        String exp_date = DateFormat.format("dd-MM-yyyy", c).toString();
        Date nDate = new SimpleDateFormat("dd-MM-yyyy").parse(exp_date);
        return exp_date;
    }

    public String getDiff(String exp) throws ParseException {
        Calendar c = Calendar.getInstance();
        Date cDate = c.getTime(); // fecha actual
        Date expDate = new SimpleDateFormat("dd-MM-yyyy").parse(exp);;
        long diff = expDate.getTime() - cDate.getTime(); // tiempo en milisegundos
        diff = ((diff/1000)/3600)/24;
        return Long.toString(diff);
    }

}

