package com.findemes.activities;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;

import com.findemes.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AgregarMovimientoActivity extends AppCompatActivity {

    private boolean isCosto=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_movimiento);

        if(getIntent().getIntExtra("tipo",1)==0) isCosto=true;
        else isCosto=false;

        Switch switchMovimientoFijo = findViewById(R.id.switchMovimientoFijo);
        final Calendar myCalendar = Calendar.getInstance();
        final EditText edittext=findViewById(R.id.edtFecha);

        if(isCosto){
            getSupportActionBar().setTitle(R.string.add_gasto);
            switchMovimientoFijo.setText(R.string.costoFijo);

        }else{
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
                edittext.setText(sdf.format(myCalendar.getTime()));
            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AgregarMovimientoActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
}
