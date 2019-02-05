package com.findemes.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.findemes.room.FechaConverter;
import com.findemes.room.FrecuenciaConverter;

import java.util.Date;
import java.util.List;

@Entity
public class Movimiento {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private boolean gasto; //TRUE = GASTO, FALSE = INGRESO

    private String titulo;

    private Double monto;

    private String descripcion;

    @Embedded(prefix = "cat_")
    private Categoria categoria;

    @TypeConverters(FechaConverter.class)
    private Date fecha;

    @TypeConverters(FrecuenciaConverter.class)
    private FrecuenciaEnum frecuenciaEnum;

    @TypeConverters(FechaConverter.class)
    private Date fechaFinalizacion;

    @Ignore
    private List<Date> listaFechas;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public FrecuenciaEnum getFrecuenciaEnum() {
        return frecuenciaEnum;
    }

    public void setFrecuenciaEnum(FrecuenciaEnum frecuenciaEnum) {
        this.frecuenciaEnum = frecuenciaEnum;
    }

    public Date getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(Date fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public List<Date> getListaFechas() {
        return listaFechas;
    }

    public void setListaFechas(List<Date> listaFechas) {
        this.listaFechas = listaFechas;
    }

    public boolean isGasto() {
        return gasto;
    }

    public void setGasto(boolean gasto) {
        this.gasto = gasto;
    }
}


