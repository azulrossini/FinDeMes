package com.findemes.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.findemes.room.FechaConverter;
import com.findemes.room.FrecuenciaConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Movimiento {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    private boolean recordatorio;

    private boolean gasto; //TRUE = GASTO, FALSE = INGRESO

    private String titulo;

    private Double monto;

    private String descripcion;

    private String photoPath;

    @Embedded(prefix = "cat_")
    private Categoria categoria; //Puede ser null, implica categoria por defecto

    @TypeConverters(FechaConverter.class)
    private Date fechaInicio;

    @TypeConverters(FrecuenciaConverter.class)
    private FrecuenciaEnum frecuenciaEnum; //Puede ser null, implica un movimiento puntual

    @TypeConverters(FechaConverter.class)
    private Date fechaFinalizacion;

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

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public FrecuenciaEnum getFrecuenciaEnum() {
        return frecuenciaEnum;
    }

    public void setFrecuenciaEnum(FrecuenciaEnum frecuenciaEnum) {
        this.frecuenciaEnum = frecuenciaEnum;
    }

    public boolean isRecordatorio() {
        return recordatorio;
    }

    public void setRecordatorio(boolean recordatorio) {
        this.recordatorio = recordatorio;
    }

    public Date getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(Date fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public boolean isGasto() {
        return gasto;
    }

    public void setGasto(boolean gasto) {
        this.gasto = gasto;
    }

    //Genera una lista con las fechas correspondientes al movimiento
    public List<Date> getListaFechas() {

        List<Date> lista = new ArrayList<>();
        Date pointer = new Date(fechaInicio.getTime());

        if(frecuenciaEnum==null){
            lista.add(new Date(pointer.getTime()));
            return lista;
        }

        switch (frecuenciaEnum){
            case ANUAL:
                while(pointer.before(fechaFinalizacion)){
                    lista.add(new Date(pointer.getTime()));
                    pointer.setYear(pointer.getYear()+1);
                }
                break;

            case DIARIO:
                while(pointer.before(fechaFinalizacion)){
                    lista.add(new Date(pointer.getTime()));
                    pointer.setDate(pointer.getDate()+1);
                }
                break;

            case MENSUAL:
                while(pointer.before(fechaFinalizacion)){
                    lista.add(new Date(pointer.getTime()));
                    pointer.setMonth(pointer.getMonth()+1);
                }
                break;

            case SEMANAL:
                while(pointer.before(fechaFinalizacion)){
                    lista.add(new Date(pointer.getTime()));
                    pointer.setDate(pointer.getDate()+7);
                }
                break;

            case BIMENSUAL:
                while(pointer.before(fechaFinalizacion)){
                    lista.add(new Date(pointer.getTime()));
                    pointer.setMonth(pointer.getMonth()+2);
                }
                break;

            default: lista.add(new Date(pointer.getTime())); break;
        }

        return lista;

    }
}


