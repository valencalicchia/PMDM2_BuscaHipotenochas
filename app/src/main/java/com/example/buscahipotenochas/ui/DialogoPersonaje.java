package com.example.buscahipotenochas.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;

import com.example.buscahipotenochas.R;
import com.example.buscahipotenochas.helpers.SpinnerHelper;

public class DialogoPersonaje {
    Context context;
    String[] opciones;
    String seleccionado;
    int[] imagenes;
    int imagen_seleccionada;

    public DialogoPersonaje(Context context, String[] opciones, String seleccionado, int[] imagenes)
    {
        this.context = context;
        this.opciones = opciones;
        this.seleccionado = seleccionado;
        this.imagenes = imagenes;
    }


    public void Show(View mView, MenuItem personajeItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.personaje_titulo);

        Spinner spinner = getSpinner();

        builder.setView(spinner);

        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                changeIcon(context, personajeItem, imagen_seleccionada);

                Toast.makeText(context, "Seleccionado: " + seleccionado, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private @NonNull Spinner getSpinner() {
        Spinner spinner = new Spinner(context);
        spinner.setAdapter(new SpinnerHelper(context, opciones, imagenes));

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
                imagen_seleccionada = imagenes[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return spinner;
    }


    public static void changeIcon(Context context, MenuItem item, int seleccionado) {
        item.setIcon(seleccionado);
    }

    public String getSeleccionado(){
        return this.seleccionado;
    }

    public int getImagenSeleccionada() {return this.imagen_seleccionada;}

}
