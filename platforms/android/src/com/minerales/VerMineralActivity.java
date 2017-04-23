package com.minerales;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.apache.cordova.CordovaActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class VerMineralActivity extends CordovaActivity {

    String URL_SELECT_MINERAL = "https://minerales.herokuapp.com/mineral/";

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
    private EditText textVerNombre;
    private EditText textVerDensidad;
    private MaterialBetterSpinner spinnerVerHabito;
    private MaterialBetterSpinner spinnerVerClasificacion;
    private MaterialBetterSpinner spinnerVerDureza;
    private MaterialBetterSpinner spinnerVerTenacidad;
    private MaterialBetterSpinner spinnerVerRotura;
    private MaterialBetterSpinner spinnerVerBrillo;
    private EditText textVerColor;
    private EditText textColorVerRaya;

    //Layout
    private CoordinatorLayout coordinatorLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_mineral);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.layoutVerMineral);

        usuarioLogueado = (String) getIntent().getExtras().getSerializable("usuarioLogueado");
        codigo = (String) getIntent().getExtras().getSerializable("codigoMineral");

        //Relleno los spinner con las propiedades de los minerales
        ListadosUtil.crearAdapterList(this, ListadosUtil.HABITO_LIST, R.id.spinnerVerHabito);
        ListadosUtil.crearAdapterList(this, ListadosUtil.CLASIFICACION_LIST, R.id.spinnerVerClasificacion);
        ListadosUtil.crearAdapterList(this, ListadosUtil.DUREZA_LIST, R.id.spinnerVerDureza);
        ListadosUtil.crearAdapterList(this, ListadosUtil.TENACIDAD_LIST, R.id.spinnerVerTenacidad);
        ListadosUtil.crearAdapterList(this, ListadosUtil.ROTURA_LIST, R.id.spinnerVerRotura);
        ListadosUtil.crearAdapterList(this, ListadosUtil.BRILLO_LIST, R.id.spinnerVerBrillo);


        //Cojo todos los campos del formulario
        textVerNombre = (EditText) findViewById(R.id.verNombre);
        spinnerVerHabito = (MaterialBetterSpinner) findViewById(R.id.spinnerVerHabito);
        spinnerVerClasificacion = (MaterialBetterSpinner) findViewById(R.id.spinnerVerClasificacion);
        textVerDensidad = (EditText) findViewById(R.id.verDensidad);
        spinnerVerDureza = (MaterialBetterSpinner) findViewById(R.id.spinnerVerDureza);
        spinnerVerTenacidad = (MaterialBetterSpinner) findViewById(R.id.spinnerVerTenacidad);
        spinnerVerRotura = (MaterialBetterSpinner) findViewById(R.id.spinnerVerRotura);
        spinnerVerBrillo = (MaterialBetterSpinner) findViewById(R.id.spinnerVerBrillo);
        textVerColor = (EditText) findViewById(R.id.verColor);
        textColorVerRaya = (EditText) findViewById(R.id.verColorRaya);

        buscarMineral();
    }

    private void rellenarCampos() {
        //Relleno los campos con los datos actuales del mineral a modificar
        textVerNombre.setText(nombreActual);
        spinnerVerHabito.setText(habitoActual);
        spinnerVerClasificacion.setText(clasificacionActual);
        textVerDensidad.setText(densidadActual);
        spinnerVerDureza.setText(durezaActual);
        spinnerVerTenacidad.setText(tenacidadActual);
        spinnerVerRotura.setText(roturaActual);
        spinnerVerBrillo.setText(brilloActual);
        textVerColor.setText(colorActual);
        textColorVerRaya.setText(colorRayaActual);
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

}
