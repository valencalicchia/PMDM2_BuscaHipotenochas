package com.example.buscahipotenochas.ui;

import android.app.AlertDialog;
import android.content.Context;
import com.example.buscahipotenochas.R;

public class DialogoInstrucciones {
    Context context;

    public DialogoInstrucciones(Context context) {
        this.context = context;
    }

    // Muestra el cuadro de diálogo con las instrucciones del juego.
    public void Show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.instrucciones_titulo);
        builder.setMessage(R.string.instrucciones_contenido);
        builder.setNeutralButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());

        builder.create().show();
    }
}