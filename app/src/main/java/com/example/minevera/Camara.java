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

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.DatePicker;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.net.ssl.HttpsURLConnection;

import androidx.fragment.app.DialogFragment;
import android.app.DatePickerDialog;
import java.util.Calendar;
import java.util.Date;

import com.google.android.material.snackbar.Snackbar;
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
        private AddProducts addProductsClass;

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
                if(name.equals("")||name.equals(" ")){
                    Snackbar.make(findViewById(R.id.camara_act), R.string.null_name_camara, Snackbar.LENGTH_SHORT).show();
                }else{
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
        }

        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar c = Calendar.getInstance(); // current date
            month = month+1;
            String m = Integer.toString(month);
            String d = Integer.toString(dayOfMonth);
            String y = Integer.toString(year);
            if(Integer.toString(dayOfMonth).length()<2){
                d = "0" + Integer.toString(dayOfMonth);
            }
            if(Integer.toString(month).length()<2){
                m = "0" + Integer.toString(month);
            }
            String expDate = d+"-"+m+"-"+y;
            try {
                saveProduct(name,expDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
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

        public void saveProduct(String name, String days) throws ParseException {
            String diff = getDiff(days);

            if (mRowId == null) {
                if(!name.equals("")&&!name.equals(" ")){

                long id = dbAdapter.createCard(name, days, diff);
                if (id > 0) {
                    mRowId = id;
                }
            }
            setResult(RESULT_OK);
            dbAdapter.close();
            }
            Intent mainIntent = new Intent (this, MainActivity.class);
            startActivity(mainIntent);
            finish();
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