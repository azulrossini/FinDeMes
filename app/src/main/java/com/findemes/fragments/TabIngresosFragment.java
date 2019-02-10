package com.findemes.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.findemes.R;
import com.findemes.model.Movimiento;
import com.findemes.room.MyDatabase;
import com.findemes.util.BalanceRecyclerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TabIngresosFragment extends Fragment{

    public TabIngresosFragment() { }

    private MyDatabase database;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView mesActual;
    private TextView tvingresos;

    private boolean firstRun=true;

    private double ingresosTotales = 0.0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = MyDatabase.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_ingresos, container, false);
        mesActual = v.findViewById(R.id.title_label);
        tvingresos = v.findViewById(R.id.monto_total_ingresos);

        recyclerView = v.findViewById(R.id.lista_ingresos);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new BalanceRecyclerAdapter(new ArrayList()));

        firstRun=false;

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Movimiento> ingresos = database.getMovimientoDAO().getIngresos();
                adapter = new BalanceRecyclerAdapter(ingresos);

                ingresosTotales = 0.0;

                for(int i=0; i<ingresos.size(); i++){
                    sumarIngresosDelMes(ingresos.get(i));
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(adapter);

                        tvingresos.setText("$ " + String.valueOf(ingresosTotales));

                    }
                });

            }
        }).start();

        //totalIngresos();
        obtenerMes();

        return v;
    }

    private void obtenerMes(){
        //Setea el mes actual

        int mes = new Date().getMonth();
        int anio = Calendar.getInstance().get(Calendar.YEAR);

        switch(mes){
            case 0:
                mesActual.setText("Enero " + anio);
                break;
            case 1:
                mesActual.setText("Febrero " + anio);
                break;
            case 2:
                mesActual.setText("Marzo " + anio);
                break;
            case 3:
                mesActual.setText("Abril " + anio);
                break;
            case 4:
                mesActual.setText("Mayo " + anio);
                break;
            case 5:
                mesActual.setText("Junio " + anio);
                break;
            case 6:
                mesActual.setText("Julio " + anio);
                break;
            case 7:
                mesActual.setText("Agosto " + anio);
                break;
            case 8:
                mesActual.setText("Septiembre " + anio);
                break;
            case 9:
                mesActual.setText("Octubre " + anio);
                break;
            case 10:
                mesActual.setText("Noviembre " + anio);
                break;
            case 11:
                mesActual.setText("Diciembre " + anio);
                break;

        }
    }


    private void sumarIngresosDelMes(Movimiento mov){


        List<Date> listaFechas = mov.getListaFechas();
        int mes = new Date().getMonth();


        for(int i = 0; i<listaFechas.size(); i++){
            if(listaFechas.get(i).getMonth() == mes){
                if(!mov.isGasto()){
                    ingresosTotales += mov.getMonto();
                }
            }
        }

    }



    public void update(final Context context){
        if(!firstRun) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    if (database == null) {
                        database = MyDatabase.getInstance(context);
                    }

                    List<Movimiento> ingresos = database.getMovimientoDAO().getIngresos();
                    adapter = new BalanceRecyclerAdapter(ingresos);

                    ingresosTotales = 0.0;

                    for (int i = 0; i < ingresos.size(); i++) {
                        sumarIngresosDelMes(ingresos.get(i));
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(adapter);

                            tvingresos.setText("$ " + String.valueOf(ingresosTotales));

                        }
                    });

                }
            }).start();

            //totalIngresos();
            obtenerMes();
        }
    }

}
