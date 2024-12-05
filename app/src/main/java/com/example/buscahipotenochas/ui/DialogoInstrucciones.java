package com.example.buscahipotenochas.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.buscahipotenochas.MainActivity;
import com.example.buscahipotenochas.R;

public class DialogoInstrucciones {

    Context context;
    public DialogoInstrucciones(Context context)
    {
        this.context = context;
    }

    public void Show(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.instrucciones_titulo);
        builder.setMessage(R.string.instrucciones_contenido);
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
