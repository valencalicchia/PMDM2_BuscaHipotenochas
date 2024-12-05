package com.example.buscahipotenochas;

public class Configuracion {
    private String nombre;
    private int casillas;
    private int hipos;

    public Configuracion(String nombre, int casillas, int hipos) {
        this.nombre = nombre;
        this.casillas = casillas;
        this.hipos = hipos;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCasillas() {
        return casillas;
    }

    public int getHipos() {
        return hipos;
    }
}