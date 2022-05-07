package com.example.minevera;

/*
 * Asignatura Aplicaciones Moviles - UC3M
 * Update: 04/03/2022.
 *
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

class GooglePlace {
    private String name;
    private String latitude;
    private String longitude;

    public GooglePlace() {
        this.name = "";
        this.latitude = "";
        this.longitude = "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

}

public class Map extends AppCompatActivity implements LocationListener{

    private final int REQUEST_PERMISSION_ACCESS_FINE_LOCATION=1;

    // indicar API KEY para el API de tipo "browser" de Google Places
    final String GOOGLE_KEY = "AIzaSyDQOAsTn_Y5QIye-wV3AJIlWsVFTr1I2Z4";

    // Centrado en la UC3M Leganés
    String latitude;
    String longitude;

    // Radio de búsqueda
    final String radius = "1000";

    // Tipo de establecimiento (ver API Google Places)
    final String type = "supermarket";

    private ListView m_listview;

    ArrayList<String> supermarkets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflamos el layout
        setContentView(R.layout.activity_map);

        // Creamos un listview que va a contener los resultados de las consulta a Google Places
        m_listview = (ListView) findViewById(R.id.id_list_places);

        // Comprobamos si tenemos permiso para acceder a la localización
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            System.out.println("HELLOLOCATION: Tenemos permisos...");
        } else {
            // no tiene permiso, solicitarlo
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_ACCESS_FINE_LOCATION);
            // cuando se nos conceda el permiso se llamará a onRequestPermissionsResult()
        }

        //Accedemos al servicio de localización
        LocationManager servicioLoc = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Obtenemos la lista de proveedores disponibles (activos)
        boolean soloActivos = true;
        List<String> proveedores = servicioLoc.getProviders(soloActivos);
        // Podemos probar a cambiar en el Manifest ACCESS_FINE_LOCATION
        // por ACCESS_COARSE_LOCATION para ver qué proveedores se pueden
        // utilizar en cada caso (los mostrados en la pantalla del terminal
        // cuando ejecutamos la aplicación). También podemos probar a
        // activar y desactivar los proveedores en el teléfono para ver
        // que realmente la aplicación funciona como debe (en los ajustes
        // de Ubicación).

        //Mostramos en la pantalla el nombre de dichos proveedores
        //(lo dejamos en otro método aparte para que el código sea más claro
        //actualizarTextoProveedores(proveedores);

        if (proveedores.isEmpty()) { // No hay ninguno activo y no se puede hacer nada
            return;
        }

        // Vemos si está disponible el proveedor de localización que queremos usar
        String proveedorElegido = LocationManager.GPS_PROVIDER;
        boolean disponible = proveedores.contains(proveedorElegido);

        // Otra opción es utilizar uno cualquiera de la lista (por ejemplo, el primero)
        if (!disponible) {
            proveedorElegido = proveedores.get(0);
        }

        //Pedimos la última localización conocida por el proveedor
        Location localizacion = servicioLoc.getLastKnownLocation(proveedorElegido);

        //Mostramos la información en la pantalla del terminal
        //actualizarTextoCoordenadas(localizacion);

        // Además, vamos a pedir actualizaciones de la posición:

        //Tiempo mínimo entre escuchas de nueva posición
        int tiempo = 1000; //milisegundos
        //Distancia mínima entre escuchas de nueva posición
        int distancia = 100; //metros

        //Pedimos escuchar actualizaciones de posicion que reciba el proveedor elegido, cada
        //1000ms o 100m, que serán procesadas por el escuchador (implementado en esta misma clase)
        servicioLoc.requestLocationUpdates(proveedorElegido, tiempo, distancia, (LocationListener) this);

        latitude = String.valueOf(localizacion.getLatitude());
        longitude = String.valueOf(localizacion.getLongitude());

        new GooglePlaces().execute();

    }

    public void sendLocations(View view) {
        // Creamos el Intent que va a lanzar la segunda activity (SecondActivity)
        Intent intent = new Intent(this, MapMarkers.class);

        // Creamos la informacion a pasar entre actividades
        Bundle b = new Bundle();
        b.putString("latitude", latitude);
        b.putString("longitude", longitude);
        b.putStringArrayList("supermarkets", supermarkets);

        // Asociamos esta informacion al intent
        intent.putExtras(b);

        // Iniciamos la nueva actividad
        startActivity(intent);
    }

    public class GooglePlaces extends AsyncTask<View, Void, ArrayList<GooglePlace>> {

        @Override
        protected ArrayList<GooglePlace> doInBackground(View... urls) {
            ArrayList<GooglePlace> temp;
            //print the call in the console
            System.out.println("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                    + latitude + "," + longitude + "&radius=" + radius + "&type=" + type + "&sensor=true&key=" + GOOGLE_KEY);

            // make Call to the url
            temp = makeCall("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                    + latitude + "," + longitude + "&radius=" + radius + "&type=" + type + "&sensor=true&key=" + GOOGLE_KEY);

            return temp;
        }

        @Override
        protected void onPreExecute() {
            // we can start a progress bar here
        }

        @Override
        protected void onPostExecute(ArrayList<GooglePlace> result) {
            // Aquí se actualiza el interfaz de usuario
            List<String> listTitle = new ArrayList<String>();

            for (int i = 0; i < result.size(); i++) {
                // make a list of the venus that are loaded in the list.
                // show the name, the category and the city
                listTitle.add(i, "Place name: " +result.get(i).getName() + "\nLatitude: " + result.get(i).getLatitude() + "\nLongitude:" + result.get(i).getLongitude());
            }

            supermarkets = new ArrayList<String>(listTitle);
            // set the results to the list
            // and show them in the xml
            ArrayAdapter<String> myAdapter;
            myAdapter = new ArrayAdapter<String>(Map.this, R.layout.row_places, R.id.listplaces, listTitle);
            m_listview.setAdapter(myAdapter);
        }
    }

    public static ArrayList<GooglePlace> makeCall(String stringURL) {

        URL url = null;
        BufferedInputStream is = null;
        JsonReader jsonReader;
        ArrayList<GooglePlace> temp = new ArrayList<GooglePlace>();

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
                    // Busca la cadena "results"
                    if (name.equals("results")) {
                        // comienza un array de objetos
                        jsonReader.beginArray();
                        while (jsonReader.hasNext()) {
                            GooglePlace poi = new GooglePlace();
                            jsonReader.beginObject();
                            // comienza un objeto
                            while (jsonReader.hasNext()) {
                                name = jsonReader.nextName();
                                if (name.equals("name")) {
                                    // si clave "name" guarda el valor
                                    poi.setName(jsonReader.nextString());
                                    System.out.println("PLACE NAME:" + poi.getName());
                                } else if (name.equals("geometry")) {
                                    // Si clave "geometry" empieza un objeto
                                    jsonReader.beginObject();
                                    while (jsonReader.hasNext()) {
                                        name = jsonReader.nextName();
                                        if (name.equals("location")) {
                                            // dentro de "geometry", si clave "location" empieza un objeto
                                            jsonReader.beginObject();
                                            while (jsonReader.hasNext()) {
                                                name = jsonReader.nextName();
                                                // se queda con los valores de "lat" y "long" de ese objeto
                                                if (name.equals("lat")) {
                                                    poi.setLatitude(jsonReader.nextString());
                                                    System.out.println("PLACE LATITUDE:" + poi.getLatitude());
                                                } else if (name.equals("lng")) {
                                                    poi.setLongitude(jsonReader.nextString());
                                                    System.out.println("PLACE LONGITUDE:" + poi.getLongitude());
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
                                } else{
                                    jsonReader.skipValue();
                                }
                            }
                            jsonReader.endObject();
                            temp.add(poi);
                        }
                        jsonReader.endArray();
                    } else {
                        jsonReader.skipValue();
                    }
                }
                jsonReader.endObject();
            } catch (Exception e) {
                System.out.println("Exception");
                return new ArrayList<GooglePlace>();
            }
        }

        return temp;}


    @Override
    public void onDestroy(){
        super.onDestroy();

        //Puesto que la aplicación va a dejar de correr en el sistema, nos desuscribimos de las
        //actualizaciones
        LocationManager servicioLoc = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        servicioLoc.removeUpdates(this);
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

    /* A continuación, métodos que debemos implementar de la interfaz LocationListener*/

    @Override
    public void onLocationChanged(Location location) {
        //Cada vez que el "escuchador" detecte una nueva localización,
        //actualizará la interfaz gráfica mostrando las coordenadas
        //de la nueva localización
        //actualizarTextoCoordenadas(location);
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Lo dejamos vacío porque no queremos realizar ninguna acción
        //cuando el proveedor provider se desactiva
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Lo dejamos vacío porque no queremos realizar ninguna acción
        //cuando el proveedor provider se activa
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Lo dejamos vacío porque no queremos realizar ninguna acción
        //cuando el proveedor provider cambia de estado
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    finish();
                    startActivity(new Intent(this, this.getClass()));

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

}