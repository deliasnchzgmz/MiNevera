package com.example.minevera;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class AddMenu extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);
    }
    //Botón de añadir a la lista de la compra manualmente
    public void AñadirManualmente(View view) {
        Intent listIntent = new Intent(this, AddProducts.class);
        startActivity(listIntent);
        finish();
    }

    //Botón de abrir cámara
    public void AbrirCamara(View view) {
        Intent addIntent = new Intent(this, Camara.class);
        startActivity(addIntent);
        finish();
    }
}