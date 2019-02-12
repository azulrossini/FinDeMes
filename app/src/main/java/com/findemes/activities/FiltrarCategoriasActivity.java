package com.findemes.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

import com.findemes.model.Categoria;
import com.findemes.model.Movimiento;
import com.findemes.room.MyDatabase;
import com.findemes.util.EditarCategoriaRecyclerAdapter;
import com.findemes.util.FiltrarCategoriaRecyclerAdapter;
import com.findemes.util.RandomColorGenerator;

import com.findemes.R;

public class FiltrarCategoriasActivity extends AppCompatActivity {

    public FiltrarCategoriasActivity() { }

    PieChartView pieChartView;
    private MyDatabase database;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtrar_categorias);

        database = MyDatabase.getInstance(FiltrarCategoriasActivity.this);

        getSupportActionBar().setTitle("Filtrar por Categoría");
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pieChartView = findViewById(R.id.chart);


        // Grafico a modo de ejemplo
        final List pieData = new ArrayList<>();

        recyclerView = findViewById(R.id.lista_categorias);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(FiltrarCategoriasActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        final List<Categoria> categorias = new ArrayList<>();
        final List<Movimiento> movimientos = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                categorias.addAll(database.getCategoriaDAO().getAll());
                movimientos.addAll(database.getMovimientoDAO().getAll());

                final List<Categoria> categorias_aux = new ArrayList<>();

                for(Categoria cat : categorias){
                    if(cat != null){
                        float total = (float) getTotalCategoria(movimientos,cat.getId());
                        if(total > 0) categorias_aux.add(cat);
                    }
                }

                RandomColorGenerator generador = new RandomColorGenerator();
                float totalGastosNula = (float) getTotalCategoriaNula(movimientos,true);
                float totalIngresosNula = (float) getTotalCategoriaNula(movimientos,false);

                Categoria catNulaGastos = new Categoria();
                Categoria catNulaIngresos = new Categoria();

                catNulaGastos.setNombre("Por Defecto");
                catNulaGastos.setColor(generador.generar());
                catNulaGastos.setId(-1);
                catNulaGastos.setGasto(true);

                catNulaIngresos.setNombre("Por Defecto");
                catNulaIngresos.setColor(generador.generar());
                catNulaIngresos.setId(-2);
                catNulaIngresos.setGasto(false);

                if(totalGastosNula > 0) categorias_aux.add(catNulaGastos);
                if(totalIngresosNula > 0) categorias_aux.add(catNulaIngresos);

                adapter = new FiltrarCategoriaRecyclerAdapter(categorias_aux);
                recyclerView.setAdapter(adapter);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(Categoria cat : categorias_aux){
                            if(cat.getId() > -1){
                                float total = (float) getTotalCategoria(movimientos,cat.getId());
                                pieData.add(new SliceValue(total, cat.getColor()).setLabel("$ " + total));
                            } else {
                                if(cat.isGasto()){
                                    float total = (float) getTotalCategoriaNula(movimientos,true);
                                    pieData.add(new SliceValue(total, cat.getColor()).setLabel("$ " + total));
                                } else {
                                    float total = (float) getTotalCategoriaNula(movimientos,false);
                                    pieData.add(new SliceValue(total, cat.getColor()).setLabel("$ " + total));
                                }

                            }

                        }

                        PieChartData pieChartData = new PieChartData(pieData);
                        //pieChartData.setHasLabels(true).setValueLabelTextSize(12);
                        pieChartView.setPieChartData(pieChartData);

                    }
                });
            }
        }).start();

    }

    private double getTotalCategoriaNula(List<Movimiento> movimientos, boolean gasto) {
        double total = 0.0;
        for(Movimiento mov : movimientos){
            if(mov.getCategoria() == null) {
                if(gasto) {
                    if(mov.isGasto()) total += mov.getMonto()*mov.getListaFechas().size();
                } else {
                    if(!mov.isGasto()) total += mov.getMonto()*mov.getListaFechas().size();
                }
            }
        }
        return total;
    }

    private double getTotalCategoria(List<Movimiento> movimientos, int idCategoria){
        double total = 0.0;
        for(Movimiento mov : movimientos){
            if(!(mov.getCategoria() == null)) {
                if (mov.getCategoria().getId() == idCategoria) {
                    total += mov.getMonto()*mov.getListaFechas().size();
                }
            }
        }
        return total;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
