package com.example.minevera;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class Settings extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new MyPreferenceFragment())
                .commit();
    }


    public static class MyPreferenceFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.settings_layout, rootKey);
        }
    }

    public void AbrirAboutUs(android.view.View view) {
        Intent aboutUs_intent = new Intent(this, AboutUs.class);
        startActivity(aboutUs_intent);
    }

}
