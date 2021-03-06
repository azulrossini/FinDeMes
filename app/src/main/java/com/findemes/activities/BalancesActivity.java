package com.findemes.activities;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
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
import java.util.concurrent.RecursiveAction;

public class BalancesActivity extends AppCompatActivity {

    private MyDatabase database;

    private TextView tvingresos;
    private TextView tvgastos;
    public static Spinner periodo;
    private RoundCornerProgressBar progressBar;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private double totalGastos = 0.0;
    private double totalIngresos = 0.0;

    public static Date fecha;

    //Segun el periodo que seleccione se setea con los movimientos correspondientes
    private List<Movimiento> movimientosDelPeriodo;

    public static BalancesActivity activity=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balances);
        database = MyDatabase.getInstance(this);

        activity=this;

        movimientosDelPeriodo = new ArrayList<>();

        fecha = new Date();

        getSupportActionBar().setTitle("Balances");
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvingresos = findViewById(R.id.monto_total_ingresos);
        tvgastos = findViewById(R.id.monto_total_gastos);
        periodo = findViewById(R.id.spinnerPeriodo);
        progressBar = findViewById(R.id.progress_bar);

        periodo.setSelection(0);

        periodo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                determinarMovimientosDelPeriodo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void totalMovimientos() {

        tvingresos.setText("$ " + String.valueOf(totalIngresos));
        tvgastos.setText("$ " + String.valueOf(totalGastos));

        if (totalGastos == 0 && totalIngresos == 0) {
            progressBar.setProgress(50);
        }
        else {
            float total = Float.valueOf(String.valueOf((totalIngresos * 100) / (totalIngresos + totalGastos)));
            progressBar.setProgress(total);
        }
    }

    private void determinarMovimientosDelPeriodo() {
        final List<Movimiento> movs = new ArrayList<>();
        totalGastos = 0.0;
        totalIngresos = 0.0;
        fecha = new Date();
        fecha = getInicioPeriodo(periodo.getSelectedItemPosition());

        new Thread(new Runnable() {
            @Override
            public void run() {

                movs.addAll(database.getMovimientoDAO().getAll());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        int periodoSeleccionado = periodo.getSelectedItemPosition();

                        movimientosDelPeriodo=new ArrayList<>();

                        if(periodoSeleccionado==0){
                            movimientosDelPeriodo = movs;
                            for(Movimiento mov:movimientosDelPeriodo){
                                if(mov.isGasto()){
                                    totalGastos+=(mov.getMonto()*mov.getListaFechas().size());
                                } else totalIngresos+=(mov.getMonto()*mov.getListaFechas().size());
                            }
                        }
                        else{
                            Date fechaInicioPeriodo = getInicioPeriodo(periodoSeleccionado);
                            fecha = fechaInicioPeriodo;
                            //Segun el periodo, seleccionar los movimientos correspondientes
                            for (int i = 0; i < movs.size(); i++) {
                                List<Date> listaFechas = movs.get(i).getListaFechas();
                                for (int x = 0; x < listaFechas.size(); x++) {
                                    if ((listaFechas.get(x).after(fechaInicioPeriodo) && listaFechas.get(x).before(new Date()))) {
                                        if(!movimientosDelPeriodo.contains(movs.get(i))){
                                            movimientosDelPeriodo.add(movs.get(i));
                                        }
                                        if(movs.get(i).isGasto()){
                                            totalGastos+=movs.get(i).getMonto();
                                        } else{
                                            totalIngresos+=movs.get(i).getMonto();
                                        }

                                    }
                                }
                            }
                        }

                        recyclerView = findViewById(R.id.lista_balance);
                        recyclerView.setHasFixedSize(true);

                        layoutManager = new LinearLayoutManager(BalancesActivity.this);
                        recyclerView.setLayoutManager(layoutManager);
                        adapter = new BalanceRecyclerAdapter(movimientosDelPeriodo);
                        recyclerView.setAdapter(adapter);

                        totalMovimientos();


                    }
                });

            }
        }).start();
    }

    private Date getInicioPeriodo(int seleccion) {
        Date fecha_inicio = new Date();
        switch (seleccion) {
            case 0:
                //Todos
                fecha_inicio.setTime(1);
                break;
            case 1:
                //Ultima semana
                fecha_inicio.setDate(fecha_inicio.getDate() - 6);
                break;
            case 2:
                //Ultimo mes
                fecha_inicio.setDate(fecha_inicio.getDate()-30);
                break;
            case 3:
                //Ultimo 3 meses
                fecha_inicio.setDate(fecha_inicio.getDate() - 92);
                break;
            case 4:
                //Ultimos 6 meses
                fecha_inicio.setDate(fecha_inicio.getDate() - 185);
                break;
            case 5:
                //Ultimo año
                fecha_inicio.setDate(fecha_inicio.getDate() - 364);
                break;
        }


        fecha_inicio.setHours(0);
        fecha_inicio.setMinutes(0);
        fecha_inicio.setSeconds(0);


        return fecha_inicio;
    }


    @Override
    protected void onResume() {
        determinarMovimientosDelPeriodo();
        super.onResume();
    }

    public void forceUpdate(){
        determinarMovimientosDelPeriodo();
    }

    public static Date getFecha() {
        return fecha;
    }

    public static Spinner getPeriodo() {
        return periodo;
    }
}
