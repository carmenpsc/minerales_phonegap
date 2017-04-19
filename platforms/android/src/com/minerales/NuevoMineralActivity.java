package com.minerales;

import android.os.Bundle;

import org.apache.cordova.CordovaActivity;

public class NuevoMineralActivity extends CordovaActivity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nuevo_mineral);
    }

}
