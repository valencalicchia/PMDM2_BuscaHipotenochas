package com.example.buscahipotenochas;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.buscahipotenochas.helpers.SpinnerHelper;
import com.example.buscahipotenochas.ui.DialogoConfiguracion;
import com.example.buscahipotenochas.ui.DialogoInstrucciones;
import com.example.buscahipotenochas.ui.DialogoPersonaje;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.buscahipotenochas.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private DialogoInstrucciones di;
    private DialogoPersonaje dp;
    private DialogoConfiguracion dc;

    String[] opciones;
    String seleccionado;

    String[] spinnerTitles;
    int[] spinnerImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        opciones = getResources().getStringArray(R.array.config_opciones);
        seleccionado = opciones[0];
        di = new DialogoInstrucciones(MainActivity.this);
        dc = new DialogoConfiguracion(MainActivity.this, opciones, seleccionado);
        dp = new DialogoPersonaje(MainActivity.this);

        spinnerTitles = new String[]{"Australia", "Brazil", "China", "France", "Germany", "India", "Ireland", "Italy", "Mexico", "Poland"};

        SpinnerHelper mCustomAdapter = new SpinnerHelper(MainActivity.this, spinnerTitles, spinnerImages);
        mSpinner.setAdapter(mCustomAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_desplegable, menu);
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
            seleccionado = dc.getSeleccionado();
            Toast.makeText(MainActivity.this, "Dificultad seleccionada: " + seleccionado, Toast.LENGTH_SHORT).show();
        }
        if(itemId == R.id.menu_personaje)
        {

        }
        return super.onOptionsItemSelected(item);
    }
}