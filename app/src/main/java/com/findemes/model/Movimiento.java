package com.findemes.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.findemes.room.FechaConverter;

import java.util.Date;
import java.util.List;

@Entity
public class Movimiento {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String titulo;

    private Double monto;

    private String descripcion;

    @Embedded(prefix = "cat_")
    private Categoria categoria;

    @TypeConverters(FechaConverter.class)
    private Date fecha;

    private FrecuenciaEnum frecuenciaEnum;

    @TypeConverters(FechaConverter.class)
    private Date fechaFinalizacion;

    private List<Date> listaFechas;

}
