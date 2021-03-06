package com.findemes.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.findemes.model.Categoria;
import com.findemes.model.Movimiento;

import java.util.List;

@Dao
public interface MovimientoDAO {

    @Query("SELECT * FROM MOVIMIENTO")
    List<Movimiento> getAll();

    @Query("SELECT * FROM MOVIMIENTO ORDER BY fechaInicio DESC LIMIT 1")
    List<Movimiento> getLast();

    @Query("SELECT * FROM MOVIMIENTO WHERE id=:id LIMIT 1")
    List<Movimiento> get(int id);

    @Query("SELECT * FROM MOVIMIENTO WHERE gasto = 1")
    List<Movimiento> getGastos();

    @Query("SELECT * FROM MOVIMIENTO WHERE gasto = 0")
    List<Movimiento> getIngresos();

    @Query("SELECT * FROM MOVIMIENTO WHERE cat_id = :cat_id")
    List<Movimiento> getAll(int cat_id);

    @Insert
    long insert (Movimiento movimiento);

    @Delete
    void delete (Movimiento movimiento);

    @Update
    void update (Movimiento movimiento);
}
