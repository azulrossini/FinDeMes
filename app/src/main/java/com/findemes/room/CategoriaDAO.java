package com.findemes.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.findemes.model.Categoria;

import java.util.List;

@Dao
public interface CategoriaDAO {

    @Query("SELECT * FROM CATEGORIA")
    List<Categoria> getAll();

    @Query("SELECT * FROM CATEGORIA WHERE gasto=:gasto")
    List<Categoria> getAll(boolean gasto);

    @Query("SELECT * FROM CATEGORIA WHERE gasto=:gasto AND nombre=:nombre")
    List<Categoria> getAll(boolean gasto, String nombre);

    @Insert
    long insert (Categoria categoria);

    @Delete
    void delete (Categoria categoria);

    @Update
    void update (Categoria categoria);

}
