package com.findemes.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Categoria {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nombre;

    private boolean esGasto;

}
