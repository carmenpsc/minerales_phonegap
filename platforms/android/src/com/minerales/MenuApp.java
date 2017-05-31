package com.minerales;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.cordova.CordovaActivity;

/**
 * Created by carmenpenalver on 31/5/17.
 */

public class MenuApp extends AppCompatActivity{

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.cerrarSesion :
                cerrarSesion();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void cerrarSesion(){
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setTitle("¡Hasta luego!");
        builder.setMessage("¿Está seguro de que desea cerrar sesión y salir?");

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Cancelar", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
