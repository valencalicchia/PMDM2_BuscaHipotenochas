package com.example.buscahipotenochas.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.buscahipotenochas.Personaje;
import com.example.buscahipotenochas.R;
import com.example.buscahipotenochas.helpers.SpinnerHelper;

import java.util.List;


public class DialogoPersonaje {
    private final Context context; // Contexto donde se mostrará el diálogo.
    private final List<Personaje> personajes; // Lista de personajes disponibles.
    private Personaje personajeSeleccionado; // Personaje seleccionado.
    String[] nombresPersonajes;
    int[] imagenes;


    public DialogoPersonaje(Context context, List<Personaje> personajes, Personaje personajeInicial) {
        this.context = context;
        this.personajes = personajes;
        this.personajeSeleccionado = personajeInicial; // Personaje inicial.
    }

    /**
     * Método para mostrar el diálogo de selección de personaje.
     */
    public void Show(MenuItem personajeItem){

        nombresPersonajes = new String[personajes.size()];
        imagenes = new int[personajes.size()];

        for (int i = 0; i < personajes.size(); i++) {
            Personaje personaje = personajes.get(i);
            nombresPersonajes[i] = personaje.getNombre();
            imagenes[i] = personaje.getImagen();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.personaje_titulo);

        Spinner spinner = getSpinner();

        builder.setView(spinner);

        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeIcon(context, personajeItem, personajeSeleccionado.getImagen());
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private @NonNull Spinner getSpinner() {
        Spinner spinner = new Spinner(context);
        spinner.setAdapter(new SpinnerHelper(context, nombresPersonajes, imagenes));

        int selectedIndex = 0;
        for (int i = 0; i < personajes.size(); i++) {
            if (personajes.get(i).equals(personajeSeleccionado)) {
                selectedIndex = i;
                break;
            }
        }
        spinner.setSelection(selectedIndex);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                personajeSeleccionado = personajes.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return spinner;
    }

    /**
     * Método para obtener el personaje seleccionado.
     *
     * @return Personaje seleccionado por el usuario.
     */
    public Personaje getSeleccionado() {
        return personajeSeleccionado;
    }

    public static void changeIcon(Context context, MenuItem item, int seleccionado) {
        item.setIcon(seleccionado);
    }

}