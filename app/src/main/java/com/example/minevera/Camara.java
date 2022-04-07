
package com.example.minevera;
/*
 * Asignatura Aplicaciones Moviles - UC3M
 * Update: 26/02/2018.
 *
 */
/*
Añadir licencia de https://opendatacommons.org/licenses/dbcl/1-0/
*/

/*
 * Copyright 2009 ZXing authors
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
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.DatePicker;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import javax.net.ssl.HttpsURLConnection;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;
import com.google.zxing.*;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Camara extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private Button boton_escaner;
    private TextView cod_barras;
    private TextView product_name;
    private String barcode= null;

    private TextView datetext;
    private Button datebutton;

    private Long mRowId;
    private dbProducts dbAdapter;

    String name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara);
        //Botón escaner
        boton_escaner=findViewById(R.id.boton_escaner);
        cod_barras=findViewById(R.id.cod_barras);
        // Creamos un textview y el editview que van a contener la fecha
        datetext= (TextView) findViewById(R.id.info_introducefecha);
        datebutton = (Button) findViewById(R.id.introducir_fecha);

        // Creamos un textview que va a contener los resultados de la consulta
        product_name = (TextView) findViewById(R.id.product_name);
        //creamos el adaptador de la BD y la abrimos
        dbAdapter = new dbProducts(this);
        dbAdapter.open();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null)
            if (result.getContents() != null){
                cod_barras.setText(result.getContents());
                new DownloadBarcodeTask().execute();

            }else{
                cod_barras.setText("Error al escanear el código de barras");
            }
    }
    //Botón de escanear código de barras
    public void Escanear(View view) {
         new IntentIntegrator(Camara.this).initiateScan();
        }


   private class DownloadBarcodeTask extends AsyncTask<String, Void, String> {

       @Override
       protected String doInBackground(String... urls) {

           barcode=cod_barras.getText().toString();
           // make Call to the url
           String temp;
           String url="https://world.openfoodfacts.org/api/v0/product/["+barcode+"].json";
           temp= makeCall("https://world.openfoodfacts.org/api/v0/product/["+barcode+"].json");
           return temp;
       }
       @Override
       protected void onPreExecute() {
           // Podemos por ejemplo poner una barra de progreso en el interfaz
       }

       @Override
       protected void onPostExecute(String result) {

           // Aquí se actualiza el interfaz de usuario. Mostramos la página en un TextView
           product_name.setText(result);
           name = product_name.getText().toString();
           datetext.setVisibility(View.VISIBLE);
           datebutton.setVisibility(View.VISIBLE);
           //boton para seleccionar fecha
           datebutton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   DialogFragment datePicker = new DatePickerFragment();
                   datePicker.show(getSupportFragmentManager(), "date picker");
               }
           });

       }
   }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = Integer.toString(dayOfMonth)+"-"+Integer.toString(month)+"-"+Integer.toString(year);

        TextView fecha = (TextView) findViewById(R.id.mostrar_fecha);
        fecha.setText(currentDateString);
        String date= fecha.getText().toString();
        saveProduct(name,date);
    }




       public static String makeCall(String stringURL) {

           URL url = null;
           BufferedInputStream is = null;
           String temp = null;
           JsonReader jsonReader;

           try {
               url = new URL(stringURL);
           } catch (Exception ex) {
               System.out.println("Malformed URL");
           }

           try {
               if (url != null) {
                   HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                   is = new BufferedInputStream(urlConnection.getInputStream());
               }
           } catch (IOException ioe) {
               System.out.println("IOException");
           }

           if (is != null) {
               try {
                   jsonReader = new JsonReader(new InputStreamReader(is, "UTF-8"));
                   jsonReader.beginObject();
                   while (jsonReader.hasNext()) {
                       String name = jsonReader.nextName();
                       // Busca la cadena product_name_es
                       if (name.equals("product")) {
                           jsonReader.beginObject();
                           while (jsonReader.hasNext()) {
                               name = jsonReader.nextName();
                               // Busca la cadena product_name_es
                               if (name.equals("product_name_es")) {
                                   temp = jsonReader.nextString();
                               } else {
                                   jsonReader.skipValue();
                               }
                           }
                           jsonReader.endObject();
                       } else {
                           jsonReader.skipValue();
                       }
                   }
                   jsonReader.endObject();

               } catch (Exception ex) {
                   System.out.println("Error reading URL");
               }
           }

           return temp;
       }

    public void saveProduct(String name, String days) {

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
        finish();
        Intent mainIntent = new Intent (this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }










}











