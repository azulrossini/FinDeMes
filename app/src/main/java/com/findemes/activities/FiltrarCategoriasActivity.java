package com.findemes.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;
import com.findemes.util.RandomColourGenerator;

import com.findemes.R;

public class FiltrarCategoriasActivity extends AppCompatActivity {

    PieChartView pieChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtrar_categorias);

        getSupportActionBar().setTitle("Filtrar por Categoría");
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pieChartView = findViewById(R.id.chart);

        RandomColourGenerator generator = new RandomColourGenerator();

        //TODO DEFINIR BIEN EL TEMA COLORES
        List pieData = new ArrayList<>();
        pieData.add(new SliceValue(15, generator.generar()).setLabel("A"));
        pieData.add(new SliceValue(25, generator.generar()).setLabel("B"));
        pieData.add(new SliceValue(10, generator.generar()).setLabel("C"));
        pieData.add(new SliceValue(60, generator.generar()).setLabel("D"));

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartView.setPieChartData(pieChartData);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
