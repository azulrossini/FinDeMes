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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.findemes.R;
import com.findemes.model.Movimiento;
import com.findemes.room.MyDatabase;
import com.findemes.util.BalanceRecyclerAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TabBalanceFragment extends Fragment{

    public TabBalanceFragment() { }
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    //ROOM
    private MyDatabase database;
    private ListView listaMovimientos;
    private TextView mesActual;
    private TextView ingresos;
    private TextView gastos ;
    private RoundCornerProgressBar progressBar;

    private double totalGastos = 0.0;
    private double totalIngresos = 0.0;

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


        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("adapter");
                adapter = new BalanceRecyclerAdapter(database.getMovimientoDAO().getAll());
                recyclerView.setAdapter(adapter);
            }
        }).start();


        totalMovimientos();
        obtenerMes();

        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void totalMovimientos(){
        //Setea la progress bar y el total de gastos e ingresos
        final List<Movimiento> movimientos=new ArrayList<>();


        new Thread(new Runnable() {
            @Override
            public void run() {
                movimientos.addAll(database.getMovimientoDAO().getAll());
                System.out.println(movimientos.size());
                for(int i=0; i<movimientos.size(); i++){
                    if(movimientos.get(i).isGasto()){
                        //si es gasto
                        totalGastos+= movimientos.get(i).getMonto();
                    }
                    else{
                        //si es ingreso
                        totalIngresos += movimientos.get(i).getMonto();
                    }
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ingresos.setText(String.valueOf(totalIngresos));
                        gastos.setText(String.valueOf(totalGastos));
                        if(totalGastos==0 && totalIngresos==0){
                            progressBar.setProgress(50);
                        }
                        else{
                            float total = Float.valueOf(String.valueOf((totalIngresos*100)/(totalIngresos+totalGastos)));
                            System.out.println(total);
                            progressBar.setProgress(total);
                        }

                    }
                });
            }
        }).start();


    }


    private void obtenerMes(){
        //Setea el mes actual

        int mes = new Date().getMonth();

        switch(mes){
            case 0:
                mesActual.setText("ENERO");
                break;
            case 1:
                mesActual.setText("FEBRERO");
                break;
            case 2:
                mesActual.setText("MARZO");
                break;
            case 3:
                mesActual.setText("ABRIL");
                break;
            case 4:
                mesActual.setText("MAYO");
                break;
            case 5:
                mesActual.setText("JUNIO");
                break;
            case 6:
                mesActual.setText("JULIO");
                break;
            case 7:
                mesActual.setText("AGOSTO");
                break;
            case 8:
                mesActual.setText("SEPTIEMBRE");
                break;
            case 9:
                mesActual.setText("OCTUBRE");
                break;
            case 10:
                mesActual.setText("NOVIEMBRE");
                break;
            case 11:
                mesActual.setText("DICIEMBRE");
                break;

        }
    }
}
