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

import com.example.buscahipotenochas.helpers.SpinnerHelper;
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

    boolean jugando = false;
    int encontradas = 0;


    private DialogoInstrucciones di;
    private DialogoPersonaje dp;
    private DialogoConfiguracion dc;
    private SpinnerHelper spinnerHelper;

    String config_seleccionado;

    String personaje_seleccionado;
    String[] personaje_opciones;
    int[] imagenes;
    TypedArray imagenesArray;

    List<Configuracion> configuraciones;
    Configuracion configuracion_seleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main = findViewById(R.id.tablero);

        di = new DialogoInstrucciones(MainActivity.this);

        String[] config_nombres = getResources().getStringArray(R.array.config_nombres);
        int[] config_casillas = getResources().getIntArray(R.array.config_casillas);
        int[] config_hipos = getResources().getIntArray(R.array.config_hipos);


        List<Configuracion> configuraciones = new ArrayList<>();
        for (int i = 0; i < config_nombres.length; i++) {
            configuraciones.add(new Configuracion(config_nombres[i], config_casillas[i], config_hipos[i]));
        }

        config_seleccionado = config_nombres[0];


        dc = new DialogoConfiguracion(MainActivity.this, config_nombres, config_seleccionado);

        personaje_opciones = getResources().getStringArray(R.array.personaje_opciones);
        personaje_seleccionado = personaje_opciones[0];
        imagenesArray = getResources().obtainTypedArray(R.array.imagenes);
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
            jugando = true;
            Configuracion confiTemp = new Configuracion("", 8, 10);
            rellenaBotones(8);
            tablero = new Tablero(confiTemp);
            tablero.jugar();
        }
        if(itemId == R.id.menu_personaje)
        {
            View mView = getLayoutInflater().inflate(R.layout.spinner_personaje, null);
            dp.Show(mView,personajeItem);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        // Obtenemos las coordenadas de la celda del texto del botón
        int x = Integer.parseInt(((Button) view).getText().toString().split(",")[0]);
        int y = Integer.parseInt(((Button) view).getText().toString().split(",")[1]);

        int resultado = tablero.compruebaCelda(x, y);

        if (resultado == -1) { // Hay hipotenocha
            // Mostrar hipotenocha muerta
            Button b = (Button) view;
            b.setPadding(0, 0, 0, 0);
            b.setTextColor(Color.BLACK);
            b.setText("X");
            b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
            int hoch = dp.getImagenSeleccionada();
            Drawable a = getResources().getDrawable(hoch);

            b.setBackground(a);
            b.setScaleY(-1);
            // Fin juego
            TableLayout tl = (TableLayout) view.getParent().getParent();
            jugando = false;
            encontradas = 0;
            deshabilitaTablero(tl);
        }
        if (resultado == 0) { // No hay hipotenochas adyacentes
            Button b = (Button) view;
            b.setPadding(0, 0, 0, 0);
            b.setBackgroundColor(Color.GRAY);
            // Despejar adyacentes con 0
            despejaAdyacentes(view, x, y);
        }
        if (resultado > 0) { // Hay hipotenochas adyacentes
            Button b = (Button) view;
            b.setPadding(0, 0, 0, 0);
            b.setText(String.valueOf(resultado));
            b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            b.setTextColor(Color.WHITE);
            b.setBackgroundColor(Color.GRAY);
            view.setEnabled(false);
        }
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
        // Recorremos los botones adyacentes y si también están a cero los despejamos
        for (int xt = -1; xt <= 1; xt++) {
            for (int yt = -1; yt <= 1; yt++) {
                if (xt != yt) {
                    if (tablero.compruebaCelda(x + xt, y + yt) == 0 && !tablero.getPulsadas(x + xt, y + yt)) {
                        Button b = (Button) traerBoton(x + xt, y + yt);
                        b.setBackgroundColor(Color.GRAY);
                        b.setClickable(false);
                        tablero.setPulsadas(x + xt, y + yt);
                        String[] coordenadas = b.getText().toString().split(",");
                        despejaAdyacentes(b, Integer.parseInt(coordenadas[0]),
                                Integer.parseInt(coordenadas[1]));
                    }
                }
            }
        }

    }

    private View traerBoton(int x, int y) {
        Button b = null;
        // Recorremos la matriz de botones hasta encontrar una coincidencia con las coordenadas
        // buscadas.
        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            TableRow tr = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0; j < tr.getChildCount(); j++) {
                b = (Button) tr.getChildAt(j);
                if (b.getText().toString().equals(x + "," + y)) {
                    return b;
                }
            }
        }
        return null;
    }


    @Override
    public boolean onLongClick(View view) {
        // Obtenemos las coordenadas de la celda del texto del botón
        int x = Integer.parseInt(((Button) view).getText().toString().split(",")[0]);
        int y = Integer.parseInt(((Button) view).getText().toString().split(",")[1]);

        int resultado = tablero.compruebaCelda(x, y);
        if (resultado == -1) { // Hay hipotenocha
            Button b = (Button) view;
            b.setPadding(0, 0, 0, 0);
            int hoch = dp.getImagenSeleccionada();
            Drawable a = getResources().getDrawable(hoch);

            b.setBackground(a);
            encontradas++;
            if (encontradas == 10) {
                TableLayout tl = (TableLayout) view.getParent().getParent();
                jugando = false;
                encontradas = 0;

                Toast.makeText(MainActivity.this, "HAS GANADO!!!", Toast.LENGTH_SHORT).show();
                deshabilitaTablero(tl);
            }
        } else { // No hay hipotenocha
            Button b = (Button) view;
            b.setText(String.valueOf(resultado));
            b.setTextSize(20);
            b.setTextColor(Color.WHITE);
            b.setBackgroundColor(Color.GRAY);
            view.setEnabled(false);
            TableLayout tl = (TableLayout) view.getParent().getParent();
            jugando = false;
            encontradas = 0;
            Toast.makeText(MainActivity.this, "HAS PERDIDO!!!", Toast.LENGTH_SHORT).show();
            deshabilitaTablero(tl);
        }

        return true;
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
                tiledBoton = new Button(this);
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
        // Recorremos la matriz de botones deshabilitando todos.
        for (int i = 0; i < tl.getChildCount(); i++) {
            TableRow tr = (TableRow) tl.getChildAt(i);
            for (int j = 0; j < tr.getChildCount(); j++) {
                Button b = (Button) tr.getChildAt(j);
                b.setEnabled(false);
            }
        }
    }
}

