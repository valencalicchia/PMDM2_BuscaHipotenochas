package com.example.buscahipotenochas;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;


import com.example.buscahipotenochas.ui.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    Tablero tablero;
    LinearLayout main;
    MenuItem personajeItem;
    Button tiledBoton;
    TableLayout tableLayout;
    Drawable imagen;

    boolean jugando = false;
    int encontradas = 0;

    private DialogoInstrucciones di;
    private DialogoPersonaje dp;
    private DialogoConfiguracion dc;

    List<Configuracion> configuraciones;
    Configuracion configuracion_seleccionada;

    List<Personaje> personajes;
    Personaje personaje_seleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main = findViewById(R.id.tablero);

        di = new DialogoInstrucciones(MainActivity.this);

        //Carga de configuraciones
        String[] config_nombres = getResources().getStringArray(R.array.config_nombres);
        int[] config_casillas = getResources().getIntArray(R.array.config_casillas);
        int[] config_hipos = getResources().getIntArray(R.array.config_hipos);

        List<Configuracion> configuraciones = new ArrayList<>();
        for (int i = 0; i < config_nombres.length; i++) {
            configuraciones.add(new Configuracion(config_nombres[i], config_casillas[i], config_hipos[i]));
        }

        configuracion_seleccionada = configuraciones.get(0);
        dc = new DialogoConfiguracion(MainActivity.this, configuraciones, configuracion_seleccionada);

        //Carga de personajes
        String[] personajeNombres = getResources().getStringArray(R.array.personaje_nombre);
        TypedArray personajeImagenesArray = getResources().obtainTypedArray(R.array.personajes_imagenes);

        List<Personaje> personajes = new ArrayList<>();
        for (int i = 0; i < personajeNombres.length; i++) {
            int imagenId = personajeImagenesArray.getResourceId(i, -1); // Obtiene el ID de la imagen.
            personajes.add(new Personaje(personajeNombres[i], imagenId)); // Crea un objeto Personaje.
        }

        personajeImagenesArray.recycle();
        personaje_seleccionado = personajes.get(0);
        dp = new DialogoPersonaje(MainActivity.this, personajes, personaje_seleccionado);

        rellenaBotones(configuracion_seleccionada.getCasillas());
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

        if (itemId == R.id.menu_instrucciones) {
            di.Show();
        }
        if (itemId == R.id.menu_config) {
            dc.Show();
        }
        if (itemId == R.id.menu_nuevo) {
            iniciarNuevoJuego();
        }
        if (itemId == R.id.menu_personaje) {
            dp.Show(personajeItem);
        }
        return super.onOptionsItemSelected(item);
    }

    // **Método para iniciar un nuevo juego**
    private void iniciarNuevoJuego() {
        jugando = true;

        configuracion_seleccionada = dc.getSeleccionado();
        personaje_seleccionado = dp.getSeleccionado();

        rellenaBotones(configuracion_seleccionada.getCasillas());
        imagen = getResources().getDrawable(personaje_seleccionado.getImagen());
        tablero = new Tablero(configuracion_seleccionada);
        tablero.jugar();
    }

    @Override
    public void onClick(View view) {
        // Obtenemos las coordenadas de la celda del texto del botón
        String[] coordenadas = ((Button) view).getText().toString().split(",");
        int x = Integer.parseInt(coordenadas[0]);
        int y = Integer.parseInt(coordenadas[1]);

        int resultado = tablero.compruebaCelda(x, y);
        Button b = (Button) view;
        b.setPadding(0, 0, 0, 0);

        switch (resultado) {
            case -1: // Hay hipotenocha
                b.setText("X");
                b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
                b.setTextColor(Color.BLACK);
                b.setBackground(imagen);
                b.setScaleY(-1);
                finalizarJuegoConDerrota(view);
                break;

            case 0: // No hay hipotenochas adyacentes
                b.setBackgroundColor(Color.GRAY);
                despejaAdyacentes(view, x, y);
                break;

            default: // Hay hipotenochas adyacentes
                b.setText(String.valueOf(resultado));
                b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                b.setTextColor(Color.WHITE);
                b.setBackgroundColor(Color.GRAY);
                view.setEnabled(false);
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        // Obtenemos las coordenadas de la celda del texto del botón
        String[] coordenadas = ((Button) view).getText().toString().split(",");
        int x = Integer.parseInt(coordenadas[0]);
        int y = Integer.parseInt(coordenadas[1]);

        int resultado = tablero.compruebaCelda(x, y);
        Button b = (Button) view;
        b.setPadding(0, 0, 0, 0);

        if (resultado == -1) { // Hay hipotenocha
            b.setBackground(imagen);
            encontradas++;
            if (encontradas == configuracion_seleccionada.getHipos()) {
                finalizarJuegoConVictoria(view);
            }
        } else { // No hay hipotenocha
            b.setText(String.valueOf(resultado));
            b.setTextSize(20);
            b.setTextColor(Color.WHITE);
            b.setBackgroundColor(Color.GRAY);
            view.setEnabled(false);
            finalizarJuegoConDerrota(view);
        }

        return true;
    }

    /**
     * Método que descubre automáticamente todas las casillas adyacentes que no tienen hipotenochas
     * alrededor.
     *
     * @param view El botón a descubrir.
     * @param x    Fila.
     * @param y    Columna.
     */
    private void despejaAdyacentes(View view, int x, int y) {
        for (int xt = -1; xt <= 1; xt++) {
            for (int yt = -1; yt <= 1; yt++) {
                if (xt != yt) {
                    if (tablero.compruebaCelda(x + xt, y + yt) == 0 && !tablero.getPulsadas(x + xt, y + yt)) {
                        Button b = (Button) traerBoton(x + xt, y + yt);
                        b.setBackgroundColor(Color.GRAY);
                        b.setClickable(false);
                        tablero.setPulsadas(x + xt, y + yt);
                        String[] coordenadas = b.getText().toString().split(",");
                        despejaAdyacentes(b, Integer.parseInt(coordenadas[0]), Integer.parseInt(coordenadas[1]));
                    }
                }
            }
        }
    }

    private View traerBoton(int x, int y) {
        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            TableRow tr = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0; j < tr.getChildCount(); j++) {
                Button b = (Button) tr.getChildAt(j);
                if (b.getText().toString().equals(x + "," + y)) {
                    return b;
                }
            }
        }
        return null;
    }

    private void rellenaBotones(int botones) {
        tableLayout = new TableLayout(this);
        tableLayout.setStretchAllColumns(true);
        tableLayout.setShrinkAllColumns(true);
        tableLayout.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
        tableLayout.setWeightSum(botones);

        for (int i = 0; i < botones; i++) {
            TableRow tr = new TableRow(this);
            tr.setGravity(Gravity.CENTER);
            tr.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT, 1.0f));

            for (int j = 0; j < botones; j++) {
                Button tiledBoton = new Button(this);
                tiledBoton.setWidth(20);
                tiledBoton.setHeight(20);
                tiledBoton.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                tiledBoton.setId(View.generateViewId());
                tiledBoton.setText(i + "," + j);
                tiledBoton.setTextSize(0);
                tiledBoton.setOnClickListener(this);
                tiledBoton.setOnLongClickListener(this);
                tr.addView(tiledBoton);
            }
            tableLayout.addView(tr);
        }

        main.removeAllViews();
        main.addView(tableLayout);

        if (!jugando) deshabilitaTablero(tableLayout);
    }

    private void deshabilitaTablero(View view) {
        TableLayout tl = (TableLayout) view;
        for (int i = 0; i < tl.getChildCount(); i++) {
            TableRow tr = (TableRow) tl.getChildAt(i);
            for (int j = 0; j < tr.getChildCount(); j++) {
                Button b = (Button) tr.getChildAt(j);
                b.setEnabled(false);
            }
        }
    }


    private void finalizarJuegoConVictoria(View view) {
        TableLayout tl = (TableLayout) view.getParent().getParent();
        jugando = false;
        encontradas = 0;
        Toast.makeText(MainActivity.this, "HAS GANADO!!!", Toast.LENGTH_SHORT).show();
        deshabilitaTablero(tl);
    }

    private void finalizarJuegoConDerrota(View view) {
        TableLayout tl = (TableLayout) view.getParent().getParent();
        jugando = false;
        encontradas = 0;
        Toast.makeText(MainActivity.this, "HAS PERDIDO!!!", Toast.LENGTH_SHORT).show();
        deshabilitaTablero(tl);
    }
}

