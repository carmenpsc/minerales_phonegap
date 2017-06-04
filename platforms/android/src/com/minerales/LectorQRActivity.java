package com.minerales;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import android.support.v7.widget.Toolbar;
import android.view.View;

import android.widget.TextView;


public class LectorQRActivity extends MenuApp {

    private CoordinatorLayout coordinatorLayout;
    private String data = "";

    @Override
    protected void onRestart() {
        if(data.equals("")){
            finish();
        }
        super.onRestart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lector_qr);
        Intent intentPG = this.getIntent();

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.qrLayout);

        // Se abre barcode scanner si está instalada en el dispositivo
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException exception) {
            Snackbar bar = Snackbar.make(coordinatorLayout, "Instale Barcode Scanner.", Snackbar.LENGTH_LONG)
                    .setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
            bar.show();
        }
        setResult(RESULT_OK, intentPG);
    }

    /*Método que se encarga de escanear el QR y mostrar el resultado por pantalla*/
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode == 0) && (resultCode == -1)) {
            this.data = data.getStringExtra("SCAN_RESULT_FORMAT");
            updateUITextViews(data.getStringExtra("SCAN_RESULT"), data.getStringExtra("SCAN_RESULT_FORMAT"));
        } else {
            Snackbar bar = Snackbar.make(coordinatorLayout, "Instale Barcode Scanner.", Snackbar.LENGTH_LONG)
                    .setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
            bar.show();
        }
    }


    /*Método que añade a la activity lector QR la cadena de texto correspondiente al mineral escaneado*/
    private void updateUITextViews(String scan_result, String scan_result_format) {
        ((TextView)findViewById(R.id.tvFormat)).setText(scan_result_format);
        final TextView tvResult = (TextView)findViewById(R.id.tvResult);
        tvResult.setText(scan_result);
    }

}
