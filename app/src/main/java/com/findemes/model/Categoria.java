package com.findemes.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.findemes.util.RandomColorGenerator;

@Entity
public class Categoria {

    public Categoria() {}

    public Categoria(String nombre){
        Categoria categoria = new Categoria();
        RandomColorGenerator generador = new RandomColorGenerator();
        categoria.setNombre(nombre);
        categoria.setColor(generador.generar());
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nombre;

    private boolean gasto;

    private int color;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isGasto() {
        return gasto;
    }

    public void setGasto(boolean gasto) {
        this.gasto = gasto;
    }

    public int getColor() { return color; }

    public void setColor(int color) { this.color = color; }

    @Override
    public String toString() {
        return nombre;
    }
}
