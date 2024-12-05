package com.example.buscahipotenochas;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.buscahipotenochas.helpers.SpinnerHelper;
import com.example.buscahipotenochas.ui.*;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

public class MainActivity extends AppCompatActivity {

    MenuItem personajeItem;
    private DialogoInstrucciones di;
    private DialogoPersonaje dp;
    private DialogoConfiguracion dc;

    private SpinnerHelper spinnerHelper;

    String config_seleccionado;
    String personaje_seleccionado;
    String[] personaje_opciones;

    int[] imagenes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        di = new DialogoInstrucciones(MainActivity.this);

        String[] configuracion_opciones = getResources().getStringArray(R.array.config_opciones);
        config_seleccionado = configuracion_opciones[0];
        dc = new DialogoConfiguracion(MainActivity.this, configuracion_opciones, config_seleccionado);

        personaje_opciones = getResources().getStringArray(R.array.personaje_opciones);
        personaje_seleccionado = personaje_opciones[0];

        TypedArray imagenesArray = getResources().obtainTypedArray(R.array.imagenes);
        imagenes = new int[imagenesArray.length()];
        for (int i = 0; i < imagenesArray.length(); i++) {
            imagenes[i] = imagenesArray.getResourceId(i, -1); // -1 si no encuentra un recurso
        }
        imagenesArray.recycle();

        dp = new DialogoPersonaje(MainActivity.this, personaje_opciones, personaje_seleccionado, imagenes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_actionbar, menu);
        personajeItem = menu.findItem(R.id.menu_personaje);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        if(itemId == R.id.menu_instrucciones)
        {
            di.Show();
        }
        if(itemId == R.id.menu_config)
        {
            dc.Show();

        }
        if(itemId == R.id.menu_nuevo)
        {
            config_seleccionado = dc.getSeleccionado();
            Toast.makeText(MainActivity.this, "Dificultad seleccionada: " + config_seleccionado, Toast.LENGTH_SHORT).show();

            personaje_seleccionado = dp.getSeleccionado();
            Toast.makeText(MainActivity.this, "Personaje seleccionado: " + personaje_seleccionado, Toast.LENGTH_SHORT).show();
        }
        if(itemId == R.id.menu_personaje)
        {
            View mView = getLayoutInflater().inflate(R.layout.spinner_personaje, null);
            dp.Show(mView,personajeItem);
        }
        return super.onOptionsItemSelected(item);
    }




}