package com.example.minevera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


// Basado en código de Alicia Rodríguez Carrión

// Implementamos la interfaz LocationListener, del framework de localización
// propio de Android: android.location

// NOTA: Desde Android nos recomiendan encarecidamente que utilicemos la API
// de Google Play en lugar de este framework, por su simplicidad y eficiencia.
// Nosotros encuentramos más sencillo utilizar el framework nativo y
// el framework sigue estando soportado, por lo que hemos hecho el ejemplo usando
// este y no la API de Google Play.

public class Map extends FragmentActivity implements OnMapReadyCallback {

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }
}
