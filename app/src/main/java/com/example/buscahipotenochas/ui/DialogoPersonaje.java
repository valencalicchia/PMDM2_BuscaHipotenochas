package com.example.buscahipotenochas.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.buscahipotenochas.R;
import com.example.buscahipotenochas.helpers.SpinnerHelper;

public class DialogoPersonaje {
    Context context;
    String[] opciones;
    String seleccionado;

    public DialogoPersonaje(Context context, String[] opciones, String seleccionado, SpinnerHelper spinnerHelper)
    {
        this.context = context;
        this.opciones = opciones;
        this.seleccionado = seleccionado;
    }


    public void Show(View mView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.personaje_titulo);

        Spinner spinner = new Spinner(context);
        spinner.setAdapter(new SpinnerHelper(context, opciones, null));

        int selectedIndex = 0;
        for (int i = 0; i < opciones.length; i++) {
            if (opciones[i].equals(seleccionado)) {
                selectedIndex = i;
                break;
            }
        }
        spinner.setSelection(selectedIndex);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seleccionado = opciones[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        builder.setView(spinner);

        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Seleccionado: " + seleccionado, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        builder.create().show();
    }


    public String getSeleccionado(){
        return this.seleccionado;
    }
}
