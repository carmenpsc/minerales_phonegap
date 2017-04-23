package com.minerales;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class ModificarMineralActivity extends AppCompatActivity {

    String URL_SELECT_MINERAL = "https://minerales.herokuapp.com/mineral/";
    String URL_MODIFICAR_MINERAL = "https://minerales.herokuapp.com/modificar/";


    //Datos usuario y mineral
    private String usuarioLogueado;
    private String codigo;

    //Datos actuales del formulario mineral
    private String nombreActual;
    private String habitoActual;
    private String clasificacionActual;
    private String densidadActual;
    private String durezaActual;
    private String tenacidadActual;
    private String roturaActual;
    private String brilloActual;
    private String colorActual;
    private String colorRayaActual;

    //Varibales
    private EditText textEditarNombre;
    private EditText textEditarDensidad;
    private MaterialBetterSpinner spinnerEditarHabito;
    private String textSpinnerEditarHabito;
    private MaterialBetterSpinner spinnerEditarClasificacion;
    private String textSpinnerEditarClasificacion;
    private MaterialBetterSpinner spinnerEditarDureza;
    private String textSpinnerEditarDureza;
    private MaterialBetterSpinner spinnerEditarTenacidad;
    private String textSpinnerEditarTenacidad;
    private MaterialBetterSpinner spinnerEditarRotura;
    private String textSpinnerEditarRotura;
    private MaterialBetterSpinner spinnerEditarBrillo;
    private String textSpinnerEditarBrillo;
    private EditText textEditarColor;
    private EditText textColorEditarRaya;
    private Button botonModificarMineral;

    //Layout
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_mineral);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.layoutModificarMineral);

        usuarioLogueado = (String) getIntent().getExtras().getSerializable("usuarioLogueado");
        codigo = (String) getIntent().getExtras().getSerializable("codigoMineral");

        //Relleno los spinner con las propiedades de los minerales
        ListadosUtil.crearAdapterList(this, ListadosUtil.HABITO_LIST, R.id.spinnerEditarHabito);
        ListadosUtil.crearAdapterList(this, ListadosUtil.CLASIFICACION_LIST, R.id.spinnerEditarClasificacion);
        ListadosUtil.crearAdapterList(this, ListadosUtil.DUREZA_LIST, R.id.spinnerEditarDureza);
        ListadosUtil.crearAdapterList(this, ListadosUtil.TENACIDAD_LIST, R.id.spinnerEditarTenacidad);
        ListadosUtil.crearAdapterList(this, ListadosUtil.ROTURA_LIST, R.id.spinnerEditarRotura);
        ListadosUtil.crearAdapterList(this, ListadosUtil.BRILLO_LIST, R.id.spinnerEditarBrillo);

        //Cojo todos los campos del formulario
        textEditarNombre = (EditText) findViewById(R.id.editarNombre);
        spinnerEditarHabito = (MaterialBetterSpinner) findViewById(R.id.spinnerEditarHabito);
        spinnerEditarHabito.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                textSpinnerEditarHabito = adapterView.getItemAtPosition(position).toString();
            }
        });
        spinnerEditarClasificacion = (MaterialBetterSpinner) findViewById(R.id.spinnerEditarClasificacion);
        spinnerEditarClasificacion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                textSpinnerEditarClasificacion = adapterView.getItemAtPosition(position).toString();
            }
        });
        textEditarDensidad = (EditText) findViewById(R.id.editarDensidad);
        spinnerEditarDureza = (MaterialBetterSpinner) findViewById(R.id.spinnerEditarDureza);
        spinnerEditarDureza.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                textSpinnerEditarDureza = adapterView.getItemAtPosition(position).toString();
            }
        });
        spinnerEditarTenacidad = (MaterialBetterSpinner) findViewById(R.id.spinnerEditarTenacidad);
        spinnerEditarTenacidad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                textSpinnerEditarTenacidad = adapterView.getItemAtPosition(position).toString();
            }
        });
        spinnerEditarRotura = (MaterialBetterSpinner) findViewById(R.id.spinnerEditarRotura);
        spinnerEditarRotura.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                textSpinnerEditarRotura = adapterView.getItemAtPosition(position).toString();
            }
        });
        spinnerEditarBrillo = (MaterialBetterSpinner) findViewById(R.id.spinnerEditarBrillo);
        spinnerEditarBrillo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                textSpinnerEditarBrillo = adapterView.getItemAtPosition(position).toString();
            }
        });
        textEditarColor = (EditText) findViewById(R.id.editarColor);
        textColorEditarRaya = (EditText) findViewById(R.id.editarColorRaya);

        buscarMineral();

        //Boton
        botonModificarMineral = (Button) findViewById(R.id.botonModificarMineralBD);

        //Implementamos el evento click del botón para añadir un nuevo mineral conectandonos con la API rest
        botonModificarMineral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    modificarMineral();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void rellenarCampos() {
        //Relleno los campos con los datos actuales del mineral a modificar
        textEditarNombre.setText(nombreActual);
        spinnerEditarHabito.setText(habitoActual);
        spinnerEditarClasificacion.setText(clasificacionActual);
        textEditarDensidad.setText(densidadActual);
        spinnerEditarDureza.setText(durezaActual);
        spinnerEditarTenacidad.setText(tenacidadActual);
        spinnerEditarRotura.setText(roturaActual);
        spinnerEditarBrillo.setText(brilloActual);
        textEditarColor.setText(colorActual);
        textColorEditarRaya.setText(colorRayaActual);
    }

    /*
        Método que busca el mineral a modificar para mostrar por pantalla sus datos. Se comunica con la API
     */
    public void buscarMineral() {
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(URL_SELECT_MINERAL + usuarioLogueado + "/" + codigo, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (responseBody.length >= 1) {

                    try {
                        JSONObject result = new JSONObject(new String(responseBody));
                        JSONArray array = result.getJSONArray("minerales");
                        JSONObject objeto = (JSONObject) array.get(0);

                        //JSONObject objeto = (JSONObject) array.get(0);
                        nombreActual = objeto.getString("nombre");
                        habitoActual = objeto.getString("habito");
                        clasificacionActual = objeto.getString("clasificacion");
                        densidadActual = objeto.getString("densidad");
                        durezaActual = objeto.getString("dureza");
                        tenacidadActual = objeto.getString("tenacidad");
                        roturaActual = objeto.getString("rotura");
                        brilloActual = objeto.getString("brillo");
                        colorActual = objeto.getString("color");
                        colorRayaActual = objeto.getString("colorRaya");
                        rellenarCampos();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Snackbar bar = Snackbar.make(coordinatorLayout, "No existe un mineral con este codigo.", Snackbar.LENGTH_LONG)
                            .setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            });
                    bar.show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
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

    /*
       Este método se conecta con la API rest y modifica un mineral en la base de datos Mongodb a través de
       AsyncHttpClient
    */
    private void modificarMineral() throws JSONException, UnsupportedEncodingException {

        AsyncHttpClient client = new AsyncHttpClient();

        //Se crea el json con los datos del mineral a introducir
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("codigo", codigo);
        jsonObj.put("nombre", textEditarNombre.getText().toString());
        if (textSpinnerEditarHabito == null)
            textSpinnerEditarHabito = habitoActual;
        jsonObj.put("habito", textSpinnerEditarHabito);
        if (textSpinnerEditarClasificacion == null)
            textSpinnerEditarClasificacion = clasificacionActual;
        jsonObj.put("clasificacion", textSpinnerEditarClasificacion);
        jsonObj.put("densidad", textEditarDensidad.getText().toString());
        if (textSpinnerEditarDureza == null)
            textSpinnerEditarDureza = durezaActual;
        jsonObj.put("dureza", textSpinnerEditarDureza);
        if (textSpinnerEditarTenacidad == null)
            textSpinnerEditarTenacidad = tenacidadActual;
        jsonObj.put("tenacidad", textSpinnerEditarTenacidad);
        if (textSpinnerEditarRotura == null)
            textSpinnerEditarRotura = roturaActual;
        jsonObj.put("rotura", textSpinnerEditarRotura);
        if (textSpinnerEditarBrillo == null)
            textSpinnerEditarBrillo = brilloActual;
        jsonObj.put("brillo", textSpinnerEditarBrillo);
        jsonObj.put("color", textEditarColor.getText().toString());
        jsonObj.put("colorRaya", textColorEditarRaya.getText().toString());

        StringEntity entity = new StringEntity(jsonObj.toString());

        client.post(this, URL_MODIFICAR_MINERAL + usuarioLogueado+"/"+codigo, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Intent intent = new Intent(getApplicationContext(), ListaMineralesActivity.class);
                    intent.putExtra("usuarioLogueado", usuarioLogueado);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
