package com.minerales;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.apache.cordova.CordovaActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class NuevoMineralActivity extends CordovaActivity{

    String URL_ADD_MINERAL = "https://minerales.herokuapp.com/mineral/";

    String[] HABITO_LIST = {"Isómetricos o cúbicos", "Alargados en una dirección", "Alargados en dos direcciones","Formas intermedias", "Granulares", "Lamelares o laminares", "Oolíticos", "Concrecciones", "Dendrítico o arborescente", "Estalactitas"};
    String[] CLASIFICACION_LIST = {"Elementos", "Sulfuros", "Sulfosales", "Óxidos", "Haluros", "Carbonatos", "Fofatos", "Sulfatos", "Silicatos"};
    String[] DUREZA_LIST = {"Talco", "Yeso", "Calcita", "Fluorita", "Apatito", "Ortosa", "Cuarzo", "Topacio", "Carindon", "Diamante"};
    String[] TENACIDAD_LIST = {"Frágil", "Maleable", "Séctil", "Dúctil", "Flexible", "Elástico"};
    String[] ROTURA_LIST = {"Fractura", "Exfoliación"};
    String[] BRILLO_LIST = {"Metálico", "Semimetálico", "No metálico"};

    //Varibales
    private EditText textNombre;
    private EditText textDensidad;
    private MaterialBetterSpinner spinnerHabito;
    private String textSpinnerHabito;
    private MaterialBetterSpinner spinnerClasificacion;
    private String textSpinnerClasificacion;
    private MaterialBetterSpinner spinnerDureza;
    private String textSpinnerDureza;
    private MaterialBetterSpinner spinnerTenacidad;
    private String textSpinnerTenacidad;
    private MaterialBetterSpinner spinnerRotura;
    private String textSpinnerRotura;
    private MaterialBetterSpinner spinnerBrillo;
    private String textSpinnerBrillo;
    private EditText textColor;
    private EditText textColorRaya;
    private Button btnAñadir;

    //UsuarioLogueado
    private String usuarioLogueado;

    //
    private CoordinatorLayout coordinatorLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_mineral);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.layoutAñadirMineral);

        usuarioLogueado = (String) getIntent().getExtras().getSerializable("usuarioLogueado");

        crearAdapterList(HABITO_LIST, R.id.spinnerHabito);
        crearAdapterList(CLASIFICACION_LIST, R.id.spinnerClasificacion);
        crearAdapterList(DUREZA_LIST, R.id.spinnerDureza);
        crearAdapterList(TENACIDAD_LIST, R.id.spinnerTenacidad);
        crearAdapterList(ROTURA_LIST, R.id.spinnerRotura);
        crearAdapterList(BRILLO_LIST, R.id.spinnerBrillo);

        //Inputs
        textNombre = (EditText)findViewById(R.id.nombre);
        textDensidad = (EditText) findViewById(R.id.densidad);

        //Spinners
        spinnerHabito = (MaterialBetterSpinner)findViewById(R.id.spinnerHabito);
        spinnerHabito.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                textSpinnerHabito = adapterView.getItemAtPosition(position).toString();
            }
        });

        spinnerClasificacion = (MaterialBetterSpinner)findViewById(R.id.spinnerClasificacion);
        spinnerClasificacion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                textSpinnerClasificacion = adapterView.getItemAtPosition(position).toString();
            }
        });

        spinnerDureza = (MaterialBetterSpinner)findViewById(R.id.spinnerDureza);
        spinnerDureza.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                textSpinnerDureza = adapterView.getItemAtPosition(position).toString();
            }
        });

        spinnerTenacidad = (MaterialBetterSpinner)findViewById(R.id.spinnerTenacidad);
        spinnerTenacidad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                textSpinnerTenacidad = adapterView.getItemAtPosition(position).toString();
            }
        });

        spinnerRotura = (MaterialBetterSpinner)findViewById(R.id.spinnerRotura);
        spinnerRotura.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                textSpinnerRotura = adapterView.getItemAtPosition(position).toString();
            }
        });

        spinnerBrillo = (MaterialBetterSpinner)findViewById(R.id.spinnerBrillo);
        spinnerBrillo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                textSpinnerBrillo = adapterView.getItemAtPosition(position).toString();
            }
        });

        textColor = (EditText)findViewById(R.id.color);
        textColorRaya = (EditText) findViewById(R.id.colorRaya);


        //Boton
        btnAñadir = (Button)findViewById(R.id.buttonAñadirMineral);

        //Implementamos el evento click del botón para añadir un nuevo mineral conectandonos con la API rest
        btnAñadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    añadirMineral();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*
        Método que añade a los spinner sus items correspondientes
     */
    private void crearAdapterList(String[] nombreLista, int idLayout){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.lista_spinner, nombreLista);

        MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner)
                findViewById(idLayout);
        materialDesignSpinner.setAdapter(arrayAdapter);
    }

    /*
        Este método se conecta con la API rest y añade un mineral a la base de datos Mongodb a través de
        AsyncHttpClient
     */
    private void añadirMineral() throws JSONException, UnsupportedEncodingException {

        AsyncHttpClient client = new AsyncHttpClient();

        //Se crea el json con los datos del mineral a introducir
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("nombre", textNombre.getText().toString());
        jsonObj.put("habito", textSpinnerHabito);
        jsonObj.put("clasificacion", textSpinnerClasificacion);
        jsonObj.put("densidad", textDensidad.getText().toString());
        jsonObj.put("dureza", textSpinnerDureza);
        jsonObj.put("tenacidad", textSpinnerTenacidad);
        jsonObj.put("rotura", textSpinnerRotura);
        jsonObj.put("brillo", textSpinnerBrillo);
        jsonObj.put("color", textColor.getText().toString());
        jsonObj.put("colorRaya", textColorRaya.getText().toString());

        StringEntity entity = new StringEntity(jsonObj.toString());

        client.post(this,URL_ADD_MINERAL+usuarioLogueado,entity,"application/json",new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    //Notificación
                    Snackbar bar = Snackbar.make(coordinatorLayout, "El mineral se ha añadido correctamente.", Snackbar.LENGTH_LONG)
                            .setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            });
                    bar.show();
                    Intent intent = new Intent(getApplicationContext(), ListaMineralesActivity.class);
                    finish();
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                //Notificación
                Snackbar bar = Snackbar.make(coordinatorLayout, "Error al añadir el mineral.", Snackbar.LENGTH_LONG)
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
