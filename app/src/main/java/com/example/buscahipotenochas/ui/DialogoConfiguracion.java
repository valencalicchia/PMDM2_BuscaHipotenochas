package com.example.buscahipotenochas.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.example.buscahipotenochas.Configuracion;
import com.example.buscahipotenochas.R;
import com.example.buscahipotenochas.helpers.ArrayHelper;

import java.util.List;

public class DialogoConfiguracion{

    private final Context context;
    private final List<Configuracion> configuraciones; // Lista de configuraciones disponibles.
    private Configuracion configuracionSeleccionada; // Configuración seleccionada.

    public DialogoConfiguracion(Context context, List<Configuracion> configuraciones, Configuracion configuracionInicial) {
        this.context = context;
        this.configuraciones = configuraciones;
        this.configuracionSeleccionada = configuracionInicial; // Configuración inicial.
    }

    /**
     * Método para mostrar el diálogo de configuración.
     */
    public void Show() {
        String[] nombresConfiguraciones = new String[configuraciones.size()];
        for (int i = 0; i < configuraciones.size(); i++) {
            nombresConfiguraciones[i] = configuraciones.get(i).getNombre();
        }

        int seleccionInicial = configuraciones.indexOf(configuracionSeleccionada);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Selecciona una configuración");

        builder.setSingleChoiceItems(nombresConfiguraciones, seleccionInicial, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                configuracionSeleccionada = configuraciones.get(which);
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    /**
     * Método para obtener la configuración seleccionada.
     *
     * @return Configuración seleccionada por el usuario.
     */
    public Configuracion getSeleccionado() {
        return configuracionSeleccionada;
    }
}

