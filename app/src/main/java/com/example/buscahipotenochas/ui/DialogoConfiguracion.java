package com.example.buscahipotenochas.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.example.buscahipotenochas.R;
import com.example.buscahipotenochas.helpers.ArrayHelper;

public class DialogoConfiguracion{

    Context context;
    String[] opciones;
    String seleccionado;

    public DialogoConfiguracion(Context context, String[] opciones, String seleccionado)
    {
        this.context = context;
        this.opciones = opciones;
        this.seleccionado = seleccionado;
    }

    public void Show(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.config_titulo);

        ArrayHelper arrayHelper = new ArrayHelper();
        int index = arrayHelper.indexOf(opciones, seleccionado);

        builder.setSingleChoiceItems(opciones, index, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                seleccionado = opciones[i];
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Toast.makeText(context, "Dificultad seleccionada: " + seleccionado, Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();
    }

    public String getSeleccionado(){
        return this.seleccionado;
    }
}

