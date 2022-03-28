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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import java.text.SimpleDateFormat;
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

    public void saveProduct(View view) {
        String name = productName.getText().toString();
        String days = productDays.getText().toString();
        if (mRowId == null) {
            long id = dbAdapter.createNote(name, days);
            if (id > 0) {
                mRowId = id;
            }
        } else {
           // dbAdapter.updateNote(mRowId, name);
        }
        setResult(RESULT_OK);
        dbAdapter.close();
        Intent mainIntent = new Intent (this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }


}

