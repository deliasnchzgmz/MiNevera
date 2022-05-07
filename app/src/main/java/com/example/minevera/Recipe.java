package com.example.minevera;

/**
 * Asignatura Aplicaciones Moviles - UC3M
 * Update: 04/03/2022.
 *
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

//creamos una clase para crear objetos posteriormente
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

    //ID y API KEY proporcionada por la web de Edamame
    public String EDAMAM_KEY = "ec4ce480d955917aee29290b5a94fcd7";
    public String EDAMAM_ID = "3d504fc1";

    //donde vamos a guardar el dato pasado desde SearchIngr a Recipe
    public String ingredient;

    private ListView m_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        m_listview = (ListView) findViewById(R.id.id_list_view);

        new EdamamRecipes().execute();

        // Recuperamos la informacion pasada en el intent
        Bundle bundle = this.getIntent().getExtras();

        ingredient = String.format(bundle.getString("ingredient"));

    }


    public class EdamamRecipes extends AsyncTask<View, Void, ArrayList<EdamamRecipe>> {

        @Override
        protected ArrayList<EdamamRecipe> doInBackground(View... urls) {
            ArrayList<EdamamRecipe> temp;
            //print en consola para comprobar que la string del url se ha creado bien
            System.out.println("https://api.edamam.com/api/recipes/v2?type=public&q="
                    + ingredient + "&app_id=" + EDAMAM_ID + "&app_key=" + EDAMAM_KEY + "&ingr=3-8");

            //llamamos al método makeCall() que devuelve un arraylist de objetos EdamameRecipe
            temp = makeCall("https://api.edamam.com/api/recipes/v2?type=public&q=" + ingredient + "&app_id=" + EDAMAM_ID + "&app_key=" + EDAMAM_KEY + "&ingr=3-8");
            return temp;
        }

        @Override
        protected void onPostExecute(ArrayList<EdamamRecipe> result) {
            // Aquí se actualiza el interfaz de usuario
            List<String> listTitle = new ArrayList<String>();

            for (int i = 0; i < result.size(); i++) {
                //hacemos una lista con las recetas
                listTitle.add(i, "\nRecipe: " + result.get(i).getName() + "\nIngredients: " + result.get(i).getIngredientLines() + "\nLink: " + result.get(i).getLink());
            }

            //adaptamos la lista para poder mostrarlo en card views y meterlo en una list view
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
            //se crea una url con el string que se pasa como parámetro
            url = new URL(stringURL);
        } catch (Exception ex) {
            System.out.println("Malformed URL");
        }

        try {
            if (url != null) {
                //se conecta con el url y se guarda el json en un buffer para poder trabajr con él
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                is = new BufferedInputStream(urlConnection.getInputStream());
            }
        } catch (IOException ioe) {
            System.out.println("IOException");
        }

        if (is != null) {
            try {
                //vamos recorriendo el fichero json que está guardado en el buffer
                //y vamos seleccionando los campos que nos interesen
                JsonReader jsonReader = new JsonReader(new InputStreamReader(is, "UTF-8"));
                jsonReader.beginObject();
                while (jsonReader.hasNext()){
                    String name = jsonReader.nextName();
                    if (name.equals("hits")){
                        jsonReader.beginArray();
                        while (jsonReader.hasNext()){
                            //creamos un objeto EdamameRecipe auxiliar en el que guardaremos los campos que nos interesen
                            EdamamRecipe receta = new EdamamRecipe();
                            jsonReader.beginObject();
                            while (jsonReader.hasNext()){
                                name = jsonReader.nextName();
                                if (name.equals("recipe")){
                                    jsonReader.beginObject();
                                    while (jsonReader.hasNext()){
                                        name = jsonReader.nextName();
                                        if (name.equals("label")){
                                            //guardamos el nombre de la receta
                                            receta.setLabel(jsonReader.nextString());
                                            System.out.println("Receta: " + receta.getName());
                                        } else if (name.equals("ingredientLines")) {
                                            jsonReader.beginArray();
                                            while (jsonReader.hasNext()){
                                                //guardamos la lista de los ingredientes
                                                receta.setIngredientLines(jsonReader.nextString());
                                            }
                                            jsonReader.endArray();
                                        } else if (name.equals("url")){
                                            //guardamos la url de la página de la que viene la receta
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
                            //se guarda cada receta en el arraylist
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
        //se devuelve el arraylist con todas las recetas
        return temp;
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
