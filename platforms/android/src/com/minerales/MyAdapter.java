package com.minerales;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.ResponseHandler;

/**
 * Created by carmenpenalver on 23/4/17.
 */

public class MyAdapter extends BaseAdapter implements ListAdapter {
    String URL_ELIMINAR_MINERAL="https://minerales.herokuapp.com/mineral/";

    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    private String usuarioLogueado;
    private CoordinatorLayout coordinatorLayout;

    public MyAdapter(ArrayList<String> list, Context context, String usuarioLogueado, CoordinatorLayout coordinatorLayout) {
        this.list = list;
        this.context = context;
        this.usuarioLogueado = usuarioLogueado;
        this.coordinatorLayout = coordinatorLayout;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return  0;
        //return list.get(pos).getCodigo();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.lista_minerales, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.textList);
        listItemText.setText(list.get(position));

        //Handle buttons and add onClickListeners
        Button botonBorrarMineral = (Button)view.findViewById(R.id.botonBorrarMineral);
        Button botonModificarMineral = (Button)view.findViewById(R.id.botonModificarMineral);
        Button botonVerMineral = (Button)view.findViewById(R.id.botonVerMineral);


        botonBorrarMineral.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Elimina el mineral de la listView
                list.remove(position);
                notifyDataSetChanged();

                //Cojo el codigo del mineral a borrar
                RelativeLayout row = (RelativeLayout) v.getParent();
                TextView idTextView = (TextView)row.getChildAt(0);
                String codigo = idTextView.getText().toString().split("-")[0];

                //ELiminar de la base de datos el mineral
                AsyncHttpClient client = new AsyncHttpClient();
                client.delete(URL_ELIMINAR_MINERAL+usuarioLogueado+"/"+codigo, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Snackbar bar = Snackbar.make(coordinatorLayout, "El mineral se ha eliminado con éxito.", Snackbar.LENGTH_LONG)
                                .setAction("CLOSE", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                });
                        bar.show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Snackbar bar = Snackbar.make(coordinatorLayout, "Error al eliminar el mineral.", Snackbar.LENGTH_LONG)
                                .setAction("CLOSE", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                });
                        bar.show();
                    }
                });

            }
        });
        botonModificarMineral.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Cojo el codigo del mineral a borrar
                RelativeLayout row = (RelativeLayout) v.getParent();
                TextView idTextView = (TextView)row.getChildAt(0);
                String codigo = idTextView.getText().toString().split("-")[0];

                Intent modifciarMineralActivity = new Intent(context, ModificarMineralActivity.class);
                modifciarMineralActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                modifciarMineralActivity.putExtra("usuarioLogueado", usuarioLogueado);
                modifciarMineralActivity.putExtra("codigoMineral",codigo);
                context.startActivity(modifciarMineralActivity);
            }
        });

        botonVerMineral.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Cojo el codigo del mineral a borrar
                RelativeLayout row = (RelativeLayout) v.getParent();
                TextView idTextView = (TextView)row.getChildAt(0);
                String codigo = idTextView.getText().toString().split("-")[0];

                Intent verMineralActivity = new Intent(context, VerMineralActivity.class);
                verMineralActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                verMineralActivity.putExtra("usuarioLogueado", usuarioLogueado);
                verMineralActivity.putExtra("codigoMineral",codigo);
                context.startActivity(verMineralActivity);

            }
        });

        return view;
    }
}
