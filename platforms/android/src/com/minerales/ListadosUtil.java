package com.minerales;

import android.app.Activity;
import android.widget.ArrayAdapter;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.apache.cordova.CordovaActivity;

/**
 * Created by carmenpenalver on 23/4/17.
 */

public class ListadosUtil extends CordovaActivity{

    static final String[] HABITO_LIST = {"Isometricos o cubicos", "Alargados en una direccion", "Alargados en dos direcciones","Formas intermedias", "Granulares", "Lamelares o laminares", "Oliticos", "Concrecciones", "Dendritico o arborescente", "Estalactitas"};
    static final String[] CLASIFICACION_LIST = {"Elementos", "Sulfuros", "Sulfosales", "Oxidos", "Haluros", "Carbonatos", "Fofatos", "Sulfatos", "Silicatos"};
    static final String[] DUREZA_LIST = {"Talco", "Yeso", "Calcita", "Fluorita", "Apatito", "Ortosa", "Cuarzo", "Topacio", "Carindon", "Diamante"};
    static final String[] TENACIDAD_LIST = {"Fragil", "Maleable", "Sectil", "Ductil", "Flexible", "Elastico"};
    static final String[] ROTURA_LIST = {"Fractura", "Exfoliacion"};
    static final String[] BRILLO_LIST = {"Metalico", "Semimetálico", "No metalico"};

    /*
    Método que añade a los spinner sus items correspondientes
 */
    static void crearAdapterList(Activity activity, String[] nombreLista, int idLayout){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity,
                R.layout.lista_spinner, nombreLista);

        MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner) activity.findViewById(idLayout);
        if(activity instanceof NuevoMineralActivity){
            materialDesignSpinner.setText(nombreLista[0]);
        }
        materialDesignSpinner.setAdapter(arrayAdapter);
    }

}
