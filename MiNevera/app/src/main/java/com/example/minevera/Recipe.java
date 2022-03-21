package com.example.minevera;

/*
 * Asignatura Aplicaciones Moviles - UC3M
 * Update: 04/03/2022.
 *
 */

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

class EdamamRecipe {
    String label;
    String ingredientLines;
    String link;

    public EdamamRecipe(String name, String ingredients, String link) {
        this.label = "";
        this.ingredientLines = "";
        this.link = "";
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setIngredientLines(String ingredientLines) {
        this.ingredientLines = ingredientLines;
    }

    public void setLink(String longitude) {
        this.link = link;
    }

    public String getName() {
        return label;
    }

    public String getIngredientLines() {
        return ingredientLines;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString(){
        return "Recipe: " + label + "\nIngredients: " + ingredientLines + "\nLink: " + link;
    }
}

public class Recipe<temp> extends AppCompatActivity {

    // indicar API KEY para el API de tipo "browser" de Google Places
    final String EDAMAM_KEY = "ec4ce480d955917aee29290b5a94fcd7\t";
    final String EDAMAM_ID = "3d504fc1";

    public String q = "chicken";
    public String ingr = "3-8";

    private ListView m_listview;
    String temp = makeCall("https://api.edamam.com/api/recipes/v2/json?type=public&q="
            + q + "&app_id=" + EDAMAM_ID + "&app_key=" + EDAMAM_KEY + "&ingr=" + ingr);;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
    }

    //CLASE PARA LEER FICHERO JSON VIDEO DEL SEÑOR
    /*public void readJson(View v){
        //String json_string = "{'label':'patatas fritas','ingredientLines':'patatas\nsal','link':'link'}";
        //Gson gson = new Gson();

        //EdamamRecipe receta = gson.fromJson(json_string, EdamamRecipe.class);

        //Log.d("prueba", receta.toString());
        try {
            InputStream inputstream = getAssets().open("recetas.json");
            int size = inputstream.available();
            byte[] buffer = new byte[size];
            inputstream.read(buffer);
            inputstream.close();

            String json_string = new String(buffer, "UTF-8");



             Gson gson = new Gson();

            EdamamRecipe receta = gson.fromJson(json_string, EdamamRecipe.class);

            Log.d("prueba", receta.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    public String makeCall(String stringURL) { //

        URL url = null;
        BufferedInputStream is = null;
        Gson gson = new Gson();
        ArrayList<EdamamRecipe> temp = new ArrayList<EdamamRecipe>();
        String str ;

        try {
            url = new URL(stringURL);
        } catch (Exception ex) {
            System.out.println("Malformed URL");
        }

        try {
            if (url != null) {
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();

                while((str = br.readLine()) != null){
                    sb.append(str + "\n");
                }
                br.close();
                Log.d("prueba", sb.toString());
                return sb.toString();

                //is = new BufferedInputStream(urlConnection.getInputStream());
            }
        } catch (IOException ioe) {
            System.out.println("IOException");
        }
        return null;
    }


    /*public class EdamamRecipes extends AsyncTask<View, Void, ArrayList<EdamamRecipe>> {

        @Override
        protected ArrayList<EdamamRecipe> doInBackground(View... urls) {
            ArrayList<EdamamRecipe> temp;
            //print the call in the console
            System.out.println("https://api.edamam.com/api/recipes/v2/json?type=public&q="
                    + q + "&app_id=" + EDAMAM_ID + "&app_key=" + EDAMAM_KEY + "&ingr=" + ingr);

            // make Call to the url
            temp = makeCall("https://api.edamam.com/api/recipes/v2/json?type=public&q="
                    + q + "&app_id=" + EDAMAM_ID + "&app_key=" + EDAMAM_KEY + "&ingr=" + ingr);

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
                listTitle.add(i, result.get(i).toString());
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
        Gson gson = new Gson();
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
                JsonParser jp = new JsonParser();
                JsonElement root = jp.parse(new InputStreamReader((InputStream) is ));
                JsonObject rootobj = root.getAsJsonObject();
                String rootstr = new String(rootobj.getAsString());
                Log.d("prueba", rootstr);


            } catch (Exception e) {
                System.out.println("Exception");
                return new ArrayList<EdamamRecipe>();
            }
        }

        return temp;
    }*/

}