package com.findemes.activities;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;

import com.findemes.R;

public class EditarCategoriasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_categorias);

        Toolbar toolbar = (Toolbar) findViewById(R.id.editar_categorias_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setElevation(0);

        // TODO SEGUIR ACÁ
    }

}
