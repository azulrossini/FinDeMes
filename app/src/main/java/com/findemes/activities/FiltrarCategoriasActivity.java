package com.findemes.activities;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

import com.findemes.model.Categoria;
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

        RandomColorGenerator generator = new RandomColorGenerator();

        // Grafico a modo de ejemplo
        List pieData = new ArrayList<>();
        //pieData.add(new SliceValue(15, generator.generar()).setLabel("A"));
        //pieData.add(new SliceValue(25, generator.generar()).setLabel("B"));
        //pieData.add(new SliceValue(10, generator.generar()).setLabel("C"));
        //pieData.add(new SliceValue(60, generator.generar()).setLabel("D"));

        //PieChartData pieChartData = new PieChartData(pieData);
        //pieChartView.setPieChartData(pieChartData);

        recyclerView = findViewById(R.id.lista_categorias);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(FiltrarCategoriasActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        final List<Categoria> categorias = new ArrayList<>();
        int[] colores = {};

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("adapter");
                adapter = new CategoriaRecyclerAdapter(database.getCategoriaDAO().getAll());
                recyclerView.setAdapter(adapter);
                categorias.addAll(database.getCategoriaDAO().getAll());
            }
        }).start();

        ;

        for(int i = 0; i< categorias.size() ; i++){
            colores[i] = generator.generar();
            pieData.add(new SliceValue(15, colores[i]));
        }

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartView.setPieChartData(pieChartData);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
