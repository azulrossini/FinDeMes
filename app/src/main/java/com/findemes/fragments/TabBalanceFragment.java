package com.findemes.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.findemes.R;
import com.findemes.model.Movimiento;
import com.findemes.room.MyDatabase;
import com.findemes.util.BalanceRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TabBalanceFragment extends Fragment{

    public TabBalanceFragment() { }
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    //ROOM
    private MyDatabase database;

    //private List<Movimiento> database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Movimiento mov = new Movimiento();
        mov.setTitulo("Hola");
        mov.setMonto(500.0);

        //database = new ArrayList<Movimiento>();
        //database.add(mov);

        database = MyDatabase.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_balance, container, false);
        ListView listaMovimientos = v.findViewById(R.id.lista_movimientos);
        TextView mesActual = v.findViewById(R.id.title_label);
        TextView ingresos = v.findViewById(R.id.monto_total_ingresos);
        TextView gastos = v.findViewById(R.id.monto_total_gastos);


        recyclerView = v.findViewById(R.id.lista_movimientos);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("adapter");
                adapter = new BalanceRecyclerAdapter(database.getMovimientoDAO().getAll());
                recyclerView.setAdapter(adapter);
            }
        }).start();


        //adapter = new BalanceRecyclerAdapter(database.getMovimientoDAO().getAll());
        recyclerView.setAdapter(adapter);

        return v;
    }

}
