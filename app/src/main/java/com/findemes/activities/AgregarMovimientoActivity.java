package com.findemes.activities;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.findemes.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AgregarMovimientoActivity extends AppCompatActivity {

    private boolean isCosto = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_movimiento);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent().getIntExtra("tipo", 1) == 0) isCosto = true;
        else isCosto = false;

        Switch switchMovimientoFijo = findViewById(R.id.switchMovimientoFijo);
        final Spinner spinnerFrecuencia = (Spinner) findViewById(R.id.spinnerFrecuencia);
        final Calendar myCalendar = Calendar.getInstance();
        final EditText edtFecha = findViewById(R.id.edtFecha);

        spinnerFrecuencia.setVisibility(View.GONE);
        edtFecha.setVisibility(View.GONE);

        if (isCosto) {
            getSupportActionBar().setTitle(R.string.add_gasto);
            switchMovimientoFijo.setText(R.string.costoFijo);

        } else {
            getSupportActionBar().setTitle(R.string.add_ingreso);
            switchMovimientoFijo.setText(R.string.ingresoFijo);

        }

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                edtFecha.setText(sdf.format(myCalendar.getTime()));
            }

        };

        edtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AgregarMovimientoActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Visualizacion de elementos tras activacion del Switch
        switchMovimientoFijo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spinnerFrecuencia.setVisibility(View.VISIBLE);
                    edtFecha.setVisibility(View.VISIBLE);
                } else {
                    spinnerFrecuencia.setVisibility(View.GONE);
                    edtFecha.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
