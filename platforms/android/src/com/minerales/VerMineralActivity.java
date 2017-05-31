package com.minerales;

import android.Manifest;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.support.v7.widget.Toolbar;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class VerMineralActivity extends MenuApp{

    private static final int PERMISO_EXTERNAL = 1 ;


    String URL_SELECT_MINERAL = "https://minerales.herokuapp.com/mineral/";

    //Datos usuario y mineral
    private String usuarioLogueado;
    private String codigo;

    //Datos actuales del formulario mineral
    private String codigoActual;
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
    private ImageView imageView;

    //Layout
    private CoordinatorLayout coordinatorLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_mineral);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.layoutVerMineral);

        //Se añade el menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarVer);
        setSupportActionBar(toolbar);

        //Se piden los permisos necesarios para escribir en el telefono y guardar la imagen
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISO_EXTERNAL);

            }
        }

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
                        String cadena = generarCadena();
                        generarQR(cadena);
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

    /*
        Método que pide permisos al usuario para usar la sdCard
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISO_EXTERNAL: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
            }
        }
    }

    /*
        Método para crear una string bonita en el QR
    */
    private String generarCadena(){
        String cadena = "Nombre del mineral: "+nombreActual+"\n"+
                "Habito del mineral: "+habitoActual+"\n"+
                "Clasificación del mineral: "+clasificacionActual+"\n"+
                "Densidad del mineral: "+densidadActual+"\n"+
                "Dureza del mineral: "+durezaActual+"\n"+
                "Tenacidad del mineral: "+tenacidadActual+"\n"+
                "Rotura del mineral: "+roturaActual+"\n"+
                "Brillo del mineral: "+brilloActual+"\n"+
                "Color del mineral: "+colorActual+"\n"+
                "Color de la raya del mineral: "+colorRayaActual;

        return  cadena;
    }
    /*
        Metodo que genera el codigo qr del mineral
     */
    private void generarQR(String text) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 256, 256);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            imageView = (ImageView) findViewById(R.id.qrImagen);
            imageView.setImageBitmap(bmp);
            guardarImagen(bmp);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    /*
        Método que guarda la imagen en el movil
     */
    public void guardarImagen(Bitmap ImageToSave) {
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/minerales";
        File dir = new File(file_path);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, codigo+"-"+nombreActual + ".jpg");

        try {
            FileOutputStream fOut = new FileOutputStream(file);

            ImageToSave.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            MakeSureFileWasCreatedThenMakeAvabile(file);
            Snackbar bar = Snackbar.make(coordinatorLayout, "QR guardado con exito en "+file_path, Snackbar.LENGTH_LONG)
                    .setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
            bar.show();
        }

        catch(FileNotFoundException e) {
            Snackbar bar = Snackbar.make(coordinatorLayout, "Error al guardar el QR.", Snackbar.LENGTH_LONG)
                    .setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
            bar.show();
        }
        catch(IOException e) {
            Snackbar bar = Snackbar.make(coordinatorLayout, "Error al guardar el QR.", Snackbar.LENGTH_LONG)
                    .setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
            bar.show();        }

    }
    private void MakeSureFileWasCreatedThenMakeAvabile(File file){
        MediaScannerConnection.scanFile(getApplicationContext(),
                new String[] { file.toString() } , null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }
}
