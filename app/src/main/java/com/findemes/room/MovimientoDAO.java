package com.findemes.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.findemes.model.Movimiento;

import java.util.List;

@Dao
public interface MovimientoDAO {

    @Query("SELECT * FROM MOVIMIENTO")
    List<Movimiento> getAll();

    @Insert
    long insert (Movimiento movimiento);

    @Delete
    void delete (Movimiento movimiento);

    @Update
    void update (Movimiento movimiento);
}
