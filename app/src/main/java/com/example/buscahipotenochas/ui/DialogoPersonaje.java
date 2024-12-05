package com.example.buscahipotenochas.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.buscahipotenochas.R;

public class DialogoPersonaje {
    Context context;
    public DialogoPersonaje(Context context)
    {
        this.context = context;
    }

    public void Show(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.personaje_titulo);

        builder.setMessage(R.string.instrucciones_contenido);
        VIew mView

        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create();
        builder.show();
    }
}
