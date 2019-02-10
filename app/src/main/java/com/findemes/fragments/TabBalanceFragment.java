package com.findemes.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.findemes.R;
import com.findemes.model.Movimiento;
import com.findemes.room.MyDatabase;
import com.findemes.util.BalanceRecyclerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TabBalanceFragment extends Fragment {

    public TabBalanceFragment() {
    }

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    //ROOM
    private MyDatabase database;


    private ListView listaMovimientos;
    private TextView mesActual;
    private TextView ingresos;
    private TextView gastos;
    private RoundCornerProgressBar progressBar;

    private double totalGastos = 0.0;
    private double totalIngresos = 0.0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = MyDatabase.getInstance(getContext());

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tab_balance, container, false);
        mesActual = v.findViewById(R.id.title_label);
        ingresos = v.findViewById(R.id.monto_total_ingresos);
        gastos = v.findViewById(R.id.monto_total_gastos);
        progressBar = v.findViewById(R.id.progressbar);

        recyclerView = v.findViewById(R.id.lista_movimientos);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new BalanceRecyclerAdapter(new ArrayList()));

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("adapter");
                adapter = new BalanceRecyclerAdapter(database.getMovimientoDAO().getAll());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(adapter);
                    }
                });

            }
        }).start();


        totalMovimientos();
        obtenerMes();

        return v;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void totalMovimientos() {
        //Setea la progress bar y el total de gastos e ingresos
        final List<Movimiento> movimientos = new ArrayList<>();
        totalGastos = 0.0;
        totalIngresos = 0.0;

        new Thread(new Runnable() {
            @Override
            public void run() {
                movimientos.addAll(database.getMovimientoDAO().getAll());

                for (int i = 0; i < movimientos.size(); i++) {
                    sumarMovimientosDelMes(movimientos.get(i));
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ingresos.setText("$ " + String.valueOf(totalIngresos));
                        gastos.setText("$ " + String.valueOf(totalGastos));
                        if (totalGastos == 0 && totalIngresos == 0) {
                            progressBar.setProgress(50);
                        } else {
                            float total = Float.valueOf(String.valueOf((totalIngresos * 100) / (totalIngresos + totalGastos)));
                            System.out.println(total);
                            progressBar.setProgress(total);
                        }

                    }
                });
            }
        }).start();


    }


    private void obtenerMes() {
        //Setea el mes actual

        int mes = new Date().getMonth();
        int anio = Calendar.getInstance().get(Calendar.YEAR);

        switch (mes) {
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

    private void sumarMovimientosDelMes(Movimiento mov) {
        //Verifica si el movimiento es gasto o ingreso
        //Y si es del mes actual
        //Y cuantas veces se repite en el mes


        List<Date> listaFechas = mov.getListaFechas();
        int mes = new Date().getMonth();


        for (int i = 0; i < listaFechas.size(); i++) {
            //Si el movimiento corresponde al mes
            if (listaFechas.get(i).getMonth() == mes) {
                if (mov.isGasto()) {
                    totalGastos += mov.getMonto();
                } else {
                    totalIngresos += mov.getMonto();
                }
            }
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(
                isVisibleToUser);

        // Refresh tab data:

        if (getFragmentManager() != null) {
            getFragmentManager()
                    .beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit();
        }
    }

}

