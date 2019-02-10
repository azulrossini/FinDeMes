package com.findemes.room;


import android.arch.persistence.room.RoomDatabase;

import com.findemes.model.Categoria;
import com.findemes.model.Movimiento;

@android.arch.persistence.room.Database(entities =  {Movimiento.class, Categoria.class}, version = 11)
public abstract class Database extends RoomDatabase {
    public abstract MovimientoDAO movimientoDAO();
    public abstract CategoriaDAO categoriaDAO();
}
