package com.findemes.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TabGastosFragment extends Fragment{

    public TabGastosFragment(){ }
    private MyDatabase database;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView mesActual;
    private TextView tvgastos;

    private boolean firstRun=true;

    private double gastosTotales = 0.0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = MyDatabase.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_gastos, container, false);
        mesActual = v.findViewById(R.id.title_label);
        tvgastos = v.findViewById(R.id.monto_total_gastos);

        recyclerView = v.findViewById(R.id.lista_gastos);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new BalanceRecyclerAdapter(new ArrayList()));

        firstRun=false;

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Movimiento> gastos = database.getMovimientoDAO().getGastos();
                gastos=filterThisMonth(gastos);

                gastosTotales = 0.0;
                adapter = new BalanceRecyclerAdapter(gastos);

                for(int i=0; i<gastos.size(); i++){
                    sumarGastosDelMes(gastos.get(i));
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(adapter);
                        tvgastos.setText("$ " + String.valueOf(gastosTotales));
                    }
                });

            }
        }).start();

        obtenerMes();
        //totalGastos();

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

    private void sumarGastosDelMes(Movimiento mov){
        //Verifica si el movimiento es gasto o ingreso
        //Y si es del mes actual
        //Y cuantas veces se repite en el mes

        List<Date> listaFechas = mov.getListaFechas();
        int mes = new Date().getMonth();


        for(int i = 0; i<listaFechas.size(); i++){
            //Si el movimiento corresponde al mes
            if(listaFechas.get(i).getMonth() == mes){
                if(mov.isGasto()){
                    gastosTotales += mov.getMonto();
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

                    List<Movimiento> gastos = database.getMovimientoDAO().getGastos();

                    gastos=filterThisMonth(gastos);

                    gastosTotales = 0.0;
                    adapter = new BalanceRecyclerAdapter(gastos);

                    for (int i = 0; i < gastos.size(); i++) {
                        sumarGastosDelMes(gastos.get(i));
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(adapter);
                            tvgastos.setText("$ " + String.valueOf(gastosTotales));
                        }
                    });

                }
            }).start();


            obtenerMes();
            //totalGastos();
        }
    }

    private List<Movimiento> filterThisMonth(List<Movimiento> movimientos) {

        List<Movimiento> retorno=new ArrayList<>();

        for(Movimiento mov: movimientos){
            for(Date fechaIndividual: mov.getListaFechas()){
                if(fechaIndividual.getMonth()==new Date().getMonth()){
                    retorno.add(mov);
                    break;
                }
            }
        }

        return retorno;
    }

}
