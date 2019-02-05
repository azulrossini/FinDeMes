package com.findemes.room;

import android.arch.persistence.room.Room;
import android.content.Context;

public class MyDatabase {
    private static MyDatabase _INSTANCIA_UNICA=null;

    public static MyDatabase getInstance(Context ctx){
        if(_INSTANCIA_UNICA==null) _INSTANCIA_UNICA = new MyDatabase(ctx);
        return _INSTANCIA_UNICA;
    }

    private Database db;
    private MovimientoDAO movimientoDAO;
    private CategoriaDAO categoriaDAO;
    private MyDatabase(Context ctx) {
        db = Room.databaseBuilder(ctx,
                Database.class, "findemes")
                .fallbackToDestructiveMigration()
                .build();
        categoriaDAO = db.categoriaDAO();
        movimientoDAO = db.movimientoDAO();
    }

    public void borrarTodo(){
        this.db.clearAllTables();
    }


    public MovimientoDAO getMovimientoDAO() {
        return movimientoDAO;
    }

    public void setMovimientoDAO(MovimientoDAO movimientoDAO) {
        this.movimientoDAO = movimientoDAO;
    }

    public CategoriaDAO getCategoriaDAO() {
        return categoriaDAO;
    }

    public void setCategoriaDAO(CategoriaDAO categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }
}
