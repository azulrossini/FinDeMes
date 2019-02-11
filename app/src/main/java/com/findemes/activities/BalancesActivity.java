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

public class BalancesActivity extends AppCompatActivity {

    private MyDatabase database;

    private TextView tvingresos;
    private TextView tvgastos;
    private Spinner periodo;
    private RoundCornerProgressBar progressBar;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private double totalGastos = 0.0;
    private double totalIngresos = 0.0;

    //Segun el periodo que seleccione se setea con los movimientos correspondientes
    private List<Movimiento> movimientosDelPeriodo;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balances);

        getSupportActionBar().setTitle("Balances");
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvingresos = findViewById(R.id.monto_total_ingresos);
        tvgastos = findViewById(R.id.monto_total_gastos);
        periodo = findViewById(R.id.spinnerPeriodo);
        progressBar = findViewById(R.id.progress_bar);

        periodo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                determinarMovimientosDelPeriodo();
            }
        });


        recyclerView = findViewById(R.id.lista_gastos);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new BalanceRecyclerAdapter(new ArrayList()));

        new Thread(new Runnable() {
            @Override
            public void run() {
                adapter = new BalanceRecyclerAdapter(database.getMovimientoDAO().getAll());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(adapter);
                    }
                });

            }
        }).start();

        totalMovimientos();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
                    determinarMovimientosDelPeriodo();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvingresos.setText("$ " + String.valueOf(totalIngresos));
                        tvgastos.setText("$ " + String.valueOf(totalGastos));
                        if (totalGastos == 0 && totalIngresos == 0) {
                            progressBar.setProgress(50);
                        } else {
                            float total = Float.valueOf(String.valueOf((totalIngresos * 100) / (totalIngresos + totalGastos)));
                            progressBar.setProgress(total);
                        }

                    }
                });
            }
        }).start();

    }

    private void determinarMovimientosDelPeriodo(){
        Calendar calendar = Calendar.getInstance();
        final List<Movimiento> movs = new ArrayList<>();
        int periodoSeleccionado = periodo.getSelectedItemPosition();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //Traer todos los movimientos
                movs.addAll(database.getMovimientoDAO().getAll());
            }
        }).start();

        //Segun el periodo, seleccionar los movimientos correspondientes
        for(int i = 0; i<movs.size(); i++){

            List<Date> listaFechas = movs.get(i).getListaFechas();

            for(int x=0; x<listaFechas.size(); x++){
                switch(periodoSeleccionado){
                    case 1:
                        Date fecha = new Date();
                        fecha.setDate(fecha.getDate() -7);

                        if((listaFechas.get(x).after(fecha) && listaFechas.get(x).before(new Date())) && !movimientosDelPeriodo.contains(movs.get(i))){
                            movimientosDelPeriodo.add(movs.get(i));
                        }

                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                }
            }

        }



    }
}
