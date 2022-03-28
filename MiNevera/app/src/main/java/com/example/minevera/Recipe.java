package com.example.minevera;

/*
 * Asignatura Aplicaciones Moviles - UC3M
 * Update: 04/03/2022.
 *
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

class EdamamRecipe {
    String label;
    ArrayList<String> ingredientLines;
    URL link;

    public EdamamRecipe() { //String name, String ingredients, String link
        this.label = "";
        this.ingredientLines = new ArrayList<String>();
        this.link = null;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setIngredientLines(String ingredient) {
        ingredientLines.add(ingredient);
    }

    public void setLink(String longitude) {
        try {
            this.link = new URL(longitude);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return label;
    }

    public String getIngredientLines() {
        String aux = "";
        for (int i=0; i<ingredientLines.size();i++) {
            aux += "\n  -  "+ingredientLines.get(i) +"\n";
        }
        return aux;
    }

    public String getLink() {
        return link.toString();
    }

    @Override
    public String toString(){
        return "Recipe: " + label + "\nIngredients: " + ingredientLines + "\nLink: " + link;
    }
}

public class Recipe extends AppCompatActivity {

    // indicar API KEY para el API de tipo "browser" de Google Places
    public String EDAMAM_KEY = "ec4ce480d955917aee29290b5a94fcd7";
    public String EDAMAM_ID = "3d504fc1";

    public String q = "";

    private ListView m_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        m_listview = (ListView) findViewById(R.id.id_list_view);

        new EdamamRecipes().execute();
        // Obtener referencia al TextView que visualizara el saludo
        TextView text_hello = (TextView)findViewById(R.id.text_hello_name);

        // Recuperamos la informacion pasada en el intent
        Bundle bundle = this.getIntent().getExtras();

        // Construimos el saludo a partir del nombre que le pasa la actividad principal
        q = String.format(bundle.getString("NAME"));

    }


    public class EdamamRecipes extends AsyncTask<View, Void, ArrayList<EdamamRecipe>> {

        @Override
        protected ArrayList<EdamamRecipe> doInBackground(View... urls) {
            ArrayList<EdamamRecipe> temp;
            //print the call in the console
            System.out.println("https://api.edamam.com/api/recipes/v2?type=public&q="
                    + q + "&app_id=" + EDAMAM_ID + "&app_key=" + EDAMAM_KEY + "&ingr=3-8");

            // make Call to the url
            temp = makeCall("https://api.edamam.com/api/recipes/v2?type=public&q=" + q + "&app_id=" + EDAMAM_ID + "&app_key=" + EDAMAM_KEY + "&ingr=3-8");
            //temp = makeCall("https://api.edamam.com/api/recipes/v2?type=public&q=chicken&app_id=3d504fc1&app_key=ec4ce480d955917aee29290b5a94fcd7&ingr=3-8&field=url");
            return temp;
        }

        @Override
        protected void onPreExecute() {
            // we can start a progress bar here
        }

        @Override
        protected void onPostExecute(ArrayList<EdamamRecipe> result) {
            // Aquí se actualiza el interfaz de usuario
            List<String> listTitle = new ArrayList<String>();

            for (int i = 0; i < result.size(); i++) {
                // make a list of the venus that are loaded in the list.
                // show the name, the category and the city
                listTitle.add(i, "\nRecipe: " + result.get(i).getName() + "\nIngredients: " + result.get(i).getIngredientLines() + "\nLink: " + result.get(i).getLink());
            }

            // set the results to the list
            // and show them in the xml
            ArrayAdapter<String> myAdapter;
            myAdapter = new ArrayAdapter<String>(Recipe.this, R.layout.row_layout, R.id.listText, listTitle);
            m_listview.setAdapter(myAdapter);
        }

    }

    public ArrayList<EdamamRecipe> makeCall(String stringURL) { //

        URL url = null;
        BufferedInputStream is = null;
        ArrayList<EdamamRecipe> temp = new ArrayList<EdamamRecipe>();

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
                JsonReader jsonReader = new JsonReader(new InputStreamReader(is, "UTF-8"));
                jsonReader.beginObject();
                while (jsonReader.hasNext()){
                    String name = jsonReader.nextName();
                    if (name.equals("hits")){
                        jsonReader.beginArray();
                        while (jsonReader.hasNext()){
                            EdamamRecipe receta = new EdamamRecipe();
                            jsonReader.beginObject();
                            while (jsonReader.hasNext()){
                                name = jsonReader.nextName();
                                if (name.equals("recipe")){
                                    jsonReader.beginObject();
                                    while (jsonReader.hasNext()){
                                        name = jsonReader.nextName();
                                        if (name.equals("label")){
                                            receta.setLabel(jsonReader.nextString());
                                            System.out.println("Receta: " + receta.getName());
                                        } else if (name.equals("ingredientLines")) {
                                            //ArrayList<String> ingrlines = new ArrayList<>();
                                            jsonReader.beginArray();
                                            while (jsonReader.hasNext()){
                                                receta.setIngredientLines(jsonReader.nextString());
                                            }
                                            jsonReader.endArray();
                                        } else if (name.equals("url")){
                                            receta.setLink(jsonReader.nextString());
                                            System.out.println("Link: " + receta.getLink());
                                        }else {
                                            jsonReader.skipValue();
                                        }
                                    }
                                    jsonReader.endObject();
                                } else {
                                    jsonReader.skipValue();
                                }
                            }
                            jsonReader.endObject();
                            temp.add(receta);
                        }
                        jsonReader.endArray();
                    } else {
                        jsonReader.skipValue();
                    }
                }
            } catch (Exception e) {
                System.out.println("Exception");
                return new ArrayList<EdamamRecipe>();
            }
        }
        return temp;
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
}