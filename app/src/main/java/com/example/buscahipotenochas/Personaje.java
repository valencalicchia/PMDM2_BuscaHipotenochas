package com.example.buscahipotenochas;

public class Personaje {
    private String nombre;
    private int imagen;

    public Personaje(String nombre, int imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public int getImagen() {
        return imagen;
    }
}
