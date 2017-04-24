package com.minerales;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.apache.cordova.CordovaActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class NuevoMineralActivity extends CordovaActivity {

    String URL_ADD_MINERAL = "https://minerales.herokuapp.com/mineral/";
    String URL_LISTA_MINERALES = "https://minerales.herokuapp.com/minerales/";

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
    private String codigoMineral;

    //
    private CoordinatorLayout coordinatorLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_mineral);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.layoutAñadirMineral);

        usuarioLogueado = (String) getIntent().getExtras().getSerializable("usuarioLogueado");

        ListadosUtil.crearAdapterList(this, ListadosUtil.HABITO_LIST, R.id.spinnerHabito);
        ListadosUtil.crearAdapterList(this, ListadosUtil.CLASIFICACION_LIST, R.id.spinnerClasificacion);
        ListadosUtil.crearAdapterList(this, ListadosUtil.DUREZA_LIST, R.id.spinnerDureza);
        ListadosUtil.crearAdapterList(this, ListadosUtil.TENACIDAD_LIST, R.id.spinnerTenacidad);
        ListadosUtil.crearAdapterList(this, ListadosUtil.ROTURA_LIST, R.id.spinnerRotura);
        ListadosUtil.crearAdapterList(this, ListadosUtil.BRILLO_LIST, R.id.spinnerBrillo);

        //Inputs
        textNombre = (EditText) findViewById(R.id.nombre);
        textDensidad = (EditText) findViewById(R.id.densidad);

        //Spinners
        spinnerHabito = (MaterialBetterSpinner) findViewById(R.id.spinnerHabito);
        spinnerHabito.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                textSpinnerHabito = adapterView.getItemAtPosition(position).toString();
            }
        });

        spinnerClasificacion = (MaterialBetterSpinner) findViewById(R.id.spinnerClasificacion);
        spinnerClasificacion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                textSpinnerClasificacion = adapterView.getItemAtPosition(position).toString();
            }
        });

        spinnerDureza = (MaterialBetterSpinner) findViewById(R.id.spinnerDureza);
        spinnerDureza.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                textSpinnerDureza = adapterView.getItemAtPosition(position).toString();
            }
        });

        spinnerTenacidad = (MaterialBetterSpinner) findViewById(R.id.spinnerTenacidad);
        spinnerTenacidad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                textSpinnerTenacidad = adapterView.getItemAtPosition(position).toString();
            }
        });

        spinnerRotura = (MaterialBetterSpinner) findViewById(R.id.spinnerRotura);
        spinnerRotura.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                textSpinnerRotura = adapterView.getItemAtPosition(position).toString();
            }
        });

        spinnerBrillo = (MaterialBetterSpinner) findViewById(R.id.spinnerBrillo);
        spinnerBrillo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                textSpinnerBrillo = adapterView.getItemAtPosition(position).toString();
            }
        });

        textColor = (EditText) findViewById(R.id.color);
        textColorRaya = (EditText) findViewById(R.id.colorRaya);

        //Ultimo codigo del mineral
        codigoMineral();

        //Boton
        btnAñadir = (Button) findViewById(R.id.buttonAñadirMineral);

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
        Este método se conecta con la API rest y añade un mineral a la base de datos Mongodb a través de
        AsyncHttpClient
     */
    private void añadirMineral() throws JSONException, UnsupportedEncodingException {

        AsyncHttpClient client = new AsyncHttpClient();

        //Se crea el json con los datos del mineral a introducir
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("codigo", codigoMineral);
        jsonObj.put("nombre", textNombre.getText().toString());
        if (textSpinnerHabito == null)
            textSpinnerHabito = "Isometricos o cubicos";
        jsonObj.put("habito", textSpinnerHabito);
        if (textSpinnerClasificacion == null)
            textSpinnerClasificacion = "Elementos";
        jsonObj.put("clasificacion", textSpinnerClasificacion);
        jsonObj.put("densidad", textDensidad.getText().toString());
        if (textSpinnerDureza == null)
            textSpinnerDureza = "Talco";
        jsonObj.put("dureza", textSpinnerDureza);
        if (textSpinnerTenacidad == null)
            textSpinnerTenacidad = "Fragil";
        jsonObj.put("tenacidad", textSpinnerTenacidad);
        if (textSpinnerRotura == null)
            textSpinnerRotura = "Fractura";
        jsonObj.put("rotura", textSpinnerRotura);
        if (textSpinnerBrillo == null)
            textSpinnerBrillo = "Metalico";
        jsonObj.put("brillo", textSpinnerBrillo);
        jsonObj.put("color", textColor.getText().toString());
        jsonObj.put("colorRaya", textColorRaya.getText().toString());

        StringEntity entity = new StringEntity(jsonObj.toString());
        client.post(this, URL_ADD_MINERAL + usuarioLogueado, entity, "application/json", new JsonHttpResponseHandler() {
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
                    intent.putExtra("usuarioLogueado", usuarioLogueado);
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

    private void codigoMineral() {
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(URL_LISTA_MINERALES + usuarioLogueado, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if (response.length() >= 1) {
                    try {
                        JSONObject objeto = (JSONObject) response.get(0);
                        if (objeto.has("minerales") && objeto.getJSONArray("minerales").length() > 0) {
                            JSONArray lista = objeto.getJSONArray("minerales");
                            Integer codigo = Integer.parseInt(lista.getJSONObject(lista.length() - 1).getString("codigo"));
                            codigoMineral = String.valueOf(codigo + 1);
                        } else {
                            codigoMineral = "0";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            ;

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
