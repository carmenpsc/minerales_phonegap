package com.minerales;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.apache.cordova.CordovaActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ListaMineralesActivity extends CordovaActivity {

    String URL_LISTA_MINERALES = "https://minerales.herokuapp.com/minerales/";

    //UsuarioLogueado
    private String usuarioLogueado;

    //Adapter lista de minerales
    private ArrayList<String> arrayListMinerales;

    //Layout para añadir notificaciones
    private CoordinatorLayout coordinatorLayout;

    //Boton que va al formulario de añadir minerl
    private FloatingActionButton añadirMineral;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_minerales);


        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.layoutListaMinerales);
        añadirMineral = (FloatingActionButton) findViewById(R.id.nuevoMineralBoton);

        Intent intent= this.getIntent();
        boolean pedo = intent.hasExtra("items");
        if (intent.hasExtra("items")){
            usuarioLogueado = intent.getExtras().getStringArrayList("items").get(0);
        }

        crearAdapterList();

        añadirMineral.setOnClickListener(new View.OnClickListener(){
            public void onClick (View v){
                Intent intent = new Intent(getApplicationContext(), NuevoMineralActivity.class);
                intent.putExtra("usuarioLogueado", usuarioLogueado);
                startActivity(intent);
                finish();
            }
        });

    }

    /*
        Método que añade a los spinner sus items correspondientes
    */
    private void crearAdapterList(){
        arrayListMinerales = new ArrayList<String>();

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(URL_LISTA_MINERALES+usuarioLogueado, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if(response.length() >= 1){
                    try {
                        JSONObject objeto = (JSONObject) response.get(0);
                        JSONArray lista = objeto.getJSONArray("minerales");
                        for (int i=0; i<lista.length();i++){
                            String codigo = lista.getJSONObject(i).getString("codigo");
                            String nombre = lista.getJSONObject(i).getString("nombre");

                            arrayListMinerales.add(codigo+"-"+nombre);
                        }
                        MyAdapter adapter = new MyAdapter(arrayListMinerales, getApplicationContext(), usuarioLogueado, coordinatorLayout);

                        ListView listViewMinerales = (ListView) findViewById(R.id.listViewMinerales);
                        listViewMinerales.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Snackbar bar = Snackbar.make(coordinatorLayout, "En este momento no hay minerales en lalista.", Snackbar.LENGTH_LONG)
                            .setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            });
                    bar.show();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Snackbar bar = Snackbar.make(coordinatorLayout, "Error al conectarse con la base de datos.", Snackbar.LENGTH_LONG)
                        .setAction("CLOSE", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        });
                bar.show();
            }
        });

    }

}
