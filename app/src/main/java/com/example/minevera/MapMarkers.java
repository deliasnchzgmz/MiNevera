package com.example.minevera;

/**
 * Asignatura Aplicaciones Moviles - UC3M
 * Update: 04/03/2022.
 * Código original: Alicia Rodríguez Carrión
 */


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapMarkers extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    ArrayList<String> supermarkets;

    Double latd;
    Double lond;

    String latitude;
    String longitude;
    String lat_market;
    String lon_market;
    String place_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_markers);

        // Recuperamos la informacion pasada en el intent
        Bundle bundle = this.getIntent().getExtras();

        latitude = bundle.getString("latitude");
        longitude = bundle.getString("longitude");
        supermarkets = bundle.getStringArrayList("supermarkets");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        UiSettings settings = mMap.getUiSettings();

        //ponemos el boton de zoom a un lado del mapa
        settings.setZoomControlsEnabled(true);
        //ponemos la brújula
        settings.isCompassEnabled();

        //ponemos el punto azul con la localización actual
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled(true);


        for(int i=0; i<supermarkets.size();i++){
            //empezamos a sacar solo los datos que necesitamos de cada string de cada supermercado
            lat_market = supermarkets.get(i).substring(supermarkets.get(i).indexOf("Latitude:"), supermarkets.get(i).indexOf("\nLongitude"));
            lon_market = supermarkets.get(i).substring(supermarkets.get(i).indexOf("Longitude:"));
            place_name = supermarkets.get(i).substring(supermarkets.get(i).indexOf("name:"), supermarkets.get(i).indexOf("\nLatitude"));

            //continuamos sacando solo los datos que necesitamos
            lat_market = lat_market.substring(lat_market.indexOf(":"));
            lon_market = lon_market.substring(lon_market.indexOf(":"));
            place_name = place_name.substring(place_name.indexOf(":"));

            lat_market = lat_market.replace(":", "");
            lon_market = lon_market.replace(":", "");
            place_name= place_name.replace(":", "");

            //pasamos la latitud y longitud a Double para poder hacer un objeto LatLng con ellos
            latd = Double.parseDouble(lat_market);
            lond = Double.parseDouble(lon_market);

            //creamos el marcados del supermercado
            LatLng market = new LatLng(latd, lond);
            MarkerOptions markerOpts = new MarkerOptions();
            markerOpts.position(market);
            //si se hace click en el marcador muestra el nombre del supermercado
            mMap.addMarker(markerOpts).setTitle(place_name);
        }

        //centramos el mapa en nuestra localizacion
        centerMap(Double.parseDouble(latitude), Double.parseDouble(longitude));

    }

    public void centerMap(double latitude, double longitude){

        // A partir de una pareja de coordenadas (tipo double) creamos un objeto LatLng,
        // que es el tipo de dato que debemos usar al tratar con mapas
        LatLng position = new LatLng(latitude, longitude);

        // Obtenemos un objeto CameraUpdate que indique el movimiento de cámara que queremos;
        // en este caso, centrar el mapa en unas coordenadas con el método newLatLng()
        CameraUpdate update = CameraUpdateFactory.newLatLng(position);

        float zoom = 16;
        update = CameraUpdateFactory.newLatLngZoom(position, zoom);

        // Pasamos el tipo de actualización configurada al método del mapa que mueve la cámara
        mMap.moveCamera(update);

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
}