package com.findemes.activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.findemes.R;
import com.findemes.model.Categoria;
import com.findemes.model.FrecuenciaEnum;
import com.findemes.model.Movimiento;
import com.findemes.room.MyDatabase;
import com.findemes.util.AlertReceiver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AgregarMovimientoActivity extends AppCompatActivity {

    private boolean isGasto = true;
    private Calendar calendar = Calendar.getInstance();
    private boolean dateSelected=false;
    private Button btnGuardar;
    private EditText edtTitulo;
    private EditText edtMonto;
    private EditText edtDescripcion;
    private Spinner spinnerFrecuencia;
    private Spinner spinnerCategoria;
    private Switch switchMovimientoFijo;
    private EditText edtFecha;
    private CheckBox chkRecordatorio;
    private MyDatabase db = MyDatabase.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_movimiento);
        if (getIntent().getIntExtra("tipo", 1) == 0) isGasto = true;
        else isGasto = false;

        //Lectura de ROOM
        final List<Categoria> categoriasRoom = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                categoriasRoom.addAll(db.getCategoriaDAO().getAll(isGasto));
            }
        }).start();
        //

        //Inicializacion de variables de vista
        spinnerFrecuencia = findViewById(R.id.spinnerFrecuencia);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        btnGuardar= findViewById(R.id.btnGuardar);
        edtTitulo= findViewById(R.id.edtTitulo);
        edtMonto= findViewById(R.id.edtMonto);
        edtDescripcion=findViewById(R.id.edtDescripcion);
        switchMovimientoFijo = findViewById(R.id.switchMovimientoFijo);
        edtFecha = findViewById(R.id.edtFecha);
        chkRecordatorio = findViewById(R.id.chkRecordatorio);

        List<Categoria> categorias = new ArrayList<>();
        Categoria def = new Categoria();
        def.setNombre(getResources().getString(R.string.defaultCategoryName));
        def.setGasto(isGasto);
        categorias.add(def);
        categorias.addAll(categoriasRoom);
        ArrayAdapter<Categoria> adapterCategoria = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, categorias);
        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapterCategoria);


        ArrayAdapter<FrecuenciaEnum> adapterFrecuencia = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,FrecuenciaEnum.values());
        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrecuencia.setAdapter(adapterFrecuencia);
        //

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        spinnerFrecuencia.setVisibility(View.GONE);
        edtFecha.setVisibility(View.GONE);
        chkRecordatorio.setVisibility(View.GONE);

        if (isGasto) {
            getSupportActionBar().setTitle(R.string.add_gasto);
            switchMovimientoFijo.setText(R.string.costoFijo);

        } else {
            getSupportActionBar().setTitle(R.string.add_ingreso);
            switchMovimientoFijo.setText(R.string.ingresoFijo);

        }

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                edtFecha.setText(sdf.format(calendar.getTime()));
                dateSelected=true;
            }

        };

        edtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(AgregarMovimientoActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dpd.getDatePicker().setMinDate(new Date().getTime());
                dpd.show();
            }
        });

        //Visualizacion de elementos tras activacion del Switch
        switchMovimientoFijo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    spinnerFrecuencia.setVisibility(View.VISIBLE);
                    edtFecha.setVisibility(View.VISIBLE);
                    chkRecordatorio.setVisibility(View.VISIBLE);
                } else {
                    spinnerFrecuencia.setVisibility(View.GONE);
                    edtFecha.setVisibility(View.GONE);
                    chkRecordatorio.setVisibility(View.GONE);
                }

            }
        });

        //Codigo del boton guardar
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Validaciones sintacticas
                if(edtTitulo.getText().toString().isEmpty()){
                    Toast.makeText(AgregarMovimientoActivity.this, R.string.emptyTitle,Toast.LENGTH_SHORT).show();
                    return;
                } else if (edtMonto.getText().toString().isEmpty()){
                    Toast.makeText(AgregarMovimientoActivity.this, R.string.invalidAmount,Toast.LENGTH_SHORT).show();
                    return;
                } else if (switchMovimientoFijo.isChecked() && !dateSelected){
                    Toast.makeText(AgregarMovimientoActivity.this, R.string.missingEndDate,Toast.LENGTH_SHORT).show();
                    return;
                }
                //

                //Guardado en ROOM
                final Movimiento movimiento = new Movimiento();
                movimiento.setMonto(Double.parseDouble(edtMonto.getText().toString()));
                movimiento.setTitulo(edtTitulo.getText().toString());
                movimiento.setDescripcion(edtDescripcion.getText().toString());
                movimiento.setGasto(isGasto);
                movimiento.setFechaInicio(new Date());

                if(spinnerCategoria.getSelectedItemPosition()==0){
                    movimiento.setCategoria(null);
                }else{
                    movimiento.setCategoria((Categoria)spinnerCategoria.getSelectedItem());
                }

                if(switchMovimientoFijo.isChecked()){
                    movimiento.setFrecuenciaEnum((FrecuenciaEnum)spinnerFrecuencia.getSelectedItem());
                    movimiento.setFechaFinalizacion(calendar.getTime());
                }else{
                    movimiento.setFrecuenciaEnum(null);
                    movimiento.setFechaFinalizacion(movimiento.getFechaInicio());
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        db.getMovimientoDAO().insert(movimiento);

                        //Programado de la alarma (si corresponde)
                        if(chkRecordatorio.isChecked() && switchMovimientoFijo.isChecked()){
                            Date recordatorio= AlertReceiver.proximaOcurrencia(movimiento.getFechaInicio(),(FrecuenciaEnum)spinnerFrecuencia.getSelectedItem());

                            if(recordatorio.before(movimiento.getFechaFinalizacion())){

                                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);

                                //VER QUE INFO AGREGARLE AL INTENT PARA LA NOTIFICACION (tiene que ser suficiente para que programe el proximo alarm)
                                intent.putExtra("Titulo",movimiento.getTitulo());
                                intent.putExtra("Monto",movimiento.getMonto());
                                intent.putExtra("Descripcion",movimiento.getDescripcion());
                                intent.putExtra("Gasto",movimiento.isGasto());
                                //Fecha inicio?
                                //Fecha fin?
                                //Id? hay que buscarlo en la BD
                                //Frecuencia

                                //VER EL REQUEST CODE (tiene que usarse para identificar el broadcast y cancelarlo si el usuario quiere)
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),500, intent, 0);

                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, recordatorio.getTime(), pendingIntent);
                            }
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isGasto) Toast.makeText(AgregarMovimientoActivity.this, R.string.successGasto,Toast.LENGTH_SHORT ).show();
                                else Toast.makeText(AgregarMovimientoActivity.this, R.string.successIngreso,Toast.LENGTH_SHORT ).show();
                            }
                        });

                    }
                }).start();


            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
