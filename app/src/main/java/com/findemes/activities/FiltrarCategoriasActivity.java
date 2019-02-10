package com.findemes.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

import com.findemes.model.Categoria;
import com.findemes.model.Movimiento;
import com.findemes.room.MyDatabase;
import com.findemes.util.CategoriaRecyclerAdapter;
import com.findemes.util.RandomColorGenerator;

import com.findemes.R;

public class FiltrarCategoriasActivity extends AppCompatActivity {

    public FiltrarCategoriasActivity() { }

    PieChartView pieChartView;
    private MyDatabase database;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private double total = 0.0;

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

        final RandomColorGenerator generator = new RandomColorGenerator();

        // Grafico a modo de ejemplo
        final List pieData = new ArrayList<>();

        recyclerView = findViewById(R.id.lista_categorias);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(FiltrarCategoriasActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        final List<Categoria> categorias = new ArrayList<>();
        final List<Integer> colores = new ArrayList<>();
        final List<Movimiento> movimientos = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                adapter = new CategoriaRecyclerAdapter(database.getCategoriaDAO().getAll());
                recyclerView.setAdapter(adapter);
                categorias.addAll(database.getCategoriaDAO().getAll());
                movimientos.addAll(database.getMovimientoDAO().getAll());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int color;
                        for(Categoria cat : categorias){
                            pieData.add(new SliceValue((float) getTotalCategoria(movimientos,cat.getId()), cat.getColor()));
                        }

                        PieChartData pieChartData = new PieChartData(pieData);
                        pieChartView.setPieChartData(pieChartData);

                    }
                });
            }
        }).start();

    }

    private double getTotalCategoria(List<Movimiento> movimientos, int idCategoria){
        double total = 0.0;
        for(Movimiento mov : movimientos){
            if(mov.getCategoria().getId() == idCategoria ){
                total += mov.getMonto();
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
