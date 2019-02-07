package com.findemes.activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
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

public class EditarMovimientoActivity extends AppCompatActivity {

    private MyDatabase db;
    private Movimiento movimiento;
    private EditText m_edtTitulo;
    private EditText m_edtMonto;
    private EditText m_edtDescripcion;
    private Spinner m_spinnerCategoria;
    private Spinner m_spinnerFrecuencia;
    private Switch m_switchMovimientoFijo;
    private EditText m_edtFechaSingle;
    private EditText m_edtFechaInicio;
    private EditText m_edtFechaFin;
    private CheckBox m_chkRecordatorio;
    private Button m_btnGuardar;
    private List<Categoria> categoriasRoom = new ArrayList<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private Calendar calendarSingle= Calendar.getInstance();
    private Calendar calendarInicio= Calendar.getInstance();
    private Calendar calendarFin= Calendar.getInstance();
    private boolean recordatorio;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_movimiento);

        db = MyDatabase.getInstance(EditarMovimientoActivity.this);
        System.out.println("Into editar");

        //Inicializacion de variables de vista
        m_edtTitulo=findViewById(R.id.m_edtTitulo);
        m_edtMonto=findViewById(R.id.m_edtMonto);
        m_edtDescripcion=findViewById(R.id.m_edtDescripcion);
        m_spinnerCategoria=findViewById(R.id.m_spinnerCategoria);
        m_spinnerFrecuencia=findViewById(R.id.m_spinnerFrecuencia);
        m_switchMovimientoFijo=findViewById(R.id.m_switchMovimientoFijo);
        m_edtFechaSingle=findViewById(R.id.m_edtFechaSingle);
        m_edtFechaInicio=findViewById(R.id.m_edtFechaInicio);
        m_edtFechaFin=findViewById(R.id.m_edtFechaFin);
        m_chkRecordatorio=findViewById(R.id.m_chkRecordatorio);
        m_btnGuardar=findViewById(R.id.m_btnGuardar);

        calendarSingle.setTime(new Date());
        //


        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Integer id = getIntent().getIntExtra("Id",-1);

        if(id==-1){
            finish();
        }
        System.out.println("aaaa"+id);

        if(getIntent().getStringExtra("Access") != null){
            if(getIntent().getStringExtra("Access").equals("Notificacion")){
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(id);
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Movimiento> resultado = db.getMovimientoDAO().get(id);
                if(resultado.isEmpty()){
                    System.out.println("Empty result");
                    finish();
                }else{
                    System.out.println("Not empty result");
                    movimiento=resultado.get(0);
                    System.out.println(resultado.get(0));
                    System.out.println(resultado.size());
                }

                System.out.println(movimiento);
                System.out.println(movimiento.getFechaInicio());
                System.out.println(movimiento.getFechaFinalizacion());
                System.out.println(movimiento.getFrecuenciaEnum());
                System.out.println(movimiento.getListaFechas());

                categoriasRoom.addAll(db.getCategoriaDAO().getAll(movimiento.isGasto()));

                System.out.println(categoriasRoom);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        m_edtTitulo.setText(movimiento.getTitulo());
                        m_edtMonto.setText(movimiento.getMonto().toString());
                        m_edtDescripcion.setText(movimiento.getDescripcion());


                        if (movimiento.isGasto()) {
                            getSupportActionBar().setTitle(R.string.modify_gasto);
                            m_switchMovimientoFijo.setText(R.string.costoFijo);
    
                        } else {
                            getSupportActionBar().setTitle(R.string.modify_ingreso);
                            m_switchMovimientoFijo.setText(R.string.ingresoFijo);

                        }

                        //Configuracion de adapters para spinners
                        List<Categoria> categorias = new ArrayList<>();
                        Categoria def = new Categoria();
                        def.setNombre(getResources().getString(R.string.defaultCategoryName));
                        def.setGasto(movimiento.isGasto());
                        categorias.add(def);
                        categorias.addAll(categoriasRoom);
                        ArrayAdapter<Categoria> adapterCategoria = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item, categorias);
                        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        m_spinnerCategoria.setAdapter(adapterCategoria);


                        ArrayAdapter<FrecuenciaEnum> adapterFrecuencia = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,FrecuenciaEnum.values());
                        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        m_spinnerFrecuencia.setAdapter(adapterFrecuencia);
                        //

                        if(!(movimiento.getCategoria()==null)){
                            for(int i=1;i<categorias.size();i++){
                                if(movimiento.getCategoria().getId()==categorias.get(i).getId()){
                                    m_spinnerCategoria.setSelection(i);
                                }
                            }
                        }

                        m_switchMovimientoFijo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    m_spinnerFrecuencia.setVisibility(View.VISIBLE);
                                    m_edtFechaFin.setVisibility(View.VISIBLE);
                                    m_chkRecordatorio.setVisibility(View.VISIBLE);
                                    m_edtFechaInicio.setVisibility(View.VISIBLE);
                                    m_edtFechaSingle.setVisibility(View.GONE);
                                } else {
                                    m_spinnerFrecuencia.setVisibility(View.GONE);
                                    m_edtFechaFin.setVisibility(View.GONE);
                                    m_chkRecordatorio.setVisibility(View.GONE);
                                    m_edtFechaInicio.setVisibility(View.GONE);
                                    m_edtFechaSingle.setVisibility(View.VISIBLE);
                                }

                            }
                        });

                        if(movimiento.getFrecuenciaEnum() == null){

                            //Movimiento puntual

                            m_edtFechaSingle.setVisibility(View.VISIBLE);
                            m_edtFechaInicio.setVisibility(View.GONE);
                            m_edtFechaFin.setVisibility(View.GONE);
                            m_spinnerFrecuencia.setVisibility(View.GONE);
                            m_chkRecordatorio.setVisibility(View.GONE);

                            m_switchMovimientoFijo.setChecked(false);
                            m_edtFechaSingle.setText(sdf.format(movimiento.getFechaInicio()));
                            calendarSingle.setTime(movimiento.getFechaInicio());

                        }else{

                            //Movimiento fijo

                            m_edtFechaSingle.setVisibility(View.GONE);
                            m_edtFechaInicio.setVisibility(View.VISIBLE);
                            m_edtFechaFin.setVisibility(View.VISIBLE);
                            m_spinnerFrecuencia.setVisibility(View.VISIBLE);
                            m_chkRecordatorio.setVisibility(View.VISIBLE);

                            m_switchMovimientoFijo.setChecked(true);

                            m_edtFechaInicio.setText(sdf.format(movimiento.getFechaInicio()));
                            calendarInicio.setTime(movimiento.getFechaInicio());

                            m_edtFechaFin.setText(sdf.format(movimiento.getFechaFinalizacion()));
                            calendarFin.setTime(movimiento.getFechaFinalizacion());

                            m_spinnerFrecuencia.setSelection(movimiento.getFrecuenciaEnum().ordinal());

                            recordatorio=movimiento.isRecordatorio();
                            if(recordatorio){
                                m_chkRecordatorio.setChecked(true);
                            } else m_chkRecordatorio.setChecked(false);

                        }

                        //Listeners de datepicker


                        final DatePickerDialog.OnDateSetListener dateFin = new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                calendarFin.set(Calendar.YEAR, year);
                                calendarFin.set(Calendar.MONTH, monthOfYear);
                                calendarFin.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                m_edtFechaFin.setText(sdf.format(calendarFin.getTime()));
                            }

                        };
                        m_edtFechaFin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new DatePickerDialog(EditarMovimientoActivity.this,
                                        dateFin,
                                        calendarFin.get(Calendar.YEAR),
                                        calendarFin.get(Calendar.MONTH),
                                        calendarFin.get(Calendar.DAY_OF_MONTH)).show();
                            }
                        });

                        final DatePickerDialog.OnDateSetListener dateInicio = new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                calendarInicio.set(Calendar.YEAR, year);
                                calendarInicio.set(Calendar.MONTH, monthOfYear);
                                calendarInicio.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                m_edtFechaInicio.setText(sdf.format(calendarInicio.getTime()));
                            }

                        };
                        m_edtFechaInicio.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new DatePickerDialog(EditarMovimientoActivity.this,
                                        dateInicio,
                                        calendarInicio.get(Calendar.YEAR),
                                        calendarInicio.get(Calendar.MONTH),
                                        calendarInicio.get(Calendar.DAY_OF_MONTH)).show();
                            }
                        });

                        final DatePickerDialog.OnDateSetListener dateSingle = new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                calendarSingle.set(Calendar.YEAR, year);
                                calendarSingle.set(Calendar.MONTH, monthOfYear);
                                calendarSingle.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                m_edtFechaSingle.setText(sdf.format(calendarSingle.getTime()));
                            }

                        };
                        m_edtFechaSingle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new DatePickerDialog(EditarMovimientoActivity.this,
                                        dateSingle,
                                        calendarSingle.get(Calendar.YEAR),
                                        calendarSingle.get(Calendar.MONTH),
                                        calendarSingle.get(Calendar.DAY_OF_MONTH)).show();
                            }
                        });
                        //


                    }
                });
            }
        }).start();

        m_btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Validaciones sintacticas
                if(m_edtTitulo.getText().toString().isEmpty()){
                    Toast.makeText(EditarMovimientoActivity.this, R.string.emptyTitle,Toast.LENGTH_SHORT).show();
                    return;
                } else if (m_edtMonto.getText().toString().isEmpty()){
                    Toast.makeText(EditarMovimientoActivity.this, R.string.invalidAmount,Toast.LENGTH_SHORT).show();
                    return;
                }

                //Actualizacion de los campos del movimiento
                movimiento.setTitulo(m_edtTitulo.getText().toString());
                movimiento.setMonto(Double.parseDouble(m_edtMonto.getText().toString()));

                if(m_spinnerCategoria.getSelectedItemPosition()==0){
                    movimiento.setCategoria(null);
                }else{
                    movimiento.setCategoria((Categoria)m_spinnerCategoria.getSelectedItem());
                }

                if(m_switchMovimientoFijo.isChecked()){
                    movimiento.setFrecuenciaEnum((FrecuenciaEnum)m_spinnerFrecuencia.getSelectedItem());
                    movimiento.setFechaFinalizacion(calendarFin.getTime());
                    movimiento.setFechaInicio(calendarInicio.getTime());
                }else{
                    movimiento.setFrecuenciaEnum(null);
                    movimiento.setFechaFinalizacion(calendarSingle.getTime());
                    movimiento.setFechaInicio(calendarSingle.getTime());
                }

                if(m_switchMovimientoFijo.isChecked()){
                    if(movimiento.getListaFechas().size()<=1 || movimiento.getFechaFinalizacion().before(movimiento.getFechaInicio())){
                        Toast.makeText(EditarMovimientoActivity.this, R.string.invalidDates,Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                //Guardado en ROOM
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        db.getMovimientoDAO().update(movimiento);

                        if(m_switchMovimientoFijo.isChecked()) {

                            if (m_chkRecordatorio.isChecked()) {
                                //Programado de alarma (Si ya existia una alarma para ese Id de movimiento se crea/sobreescribe a la alarma anterior)
                                Date recordatorio= AlertReceiver.proximaOcurrencia(movimiento.getFechaInicio(),(FrecuenciaEnum)m_spinnerFrecuencia.getSelectedItem());

                                if(recordatorio.before(movimiento.getFechaFinalizacion())){

                                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                    Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);

                                    intent.putExtra("Titulo",movimiento.getTitulo());
                                    intent.putExtra("Monto",movimiento.getMonto());
                                    intent.putExtra("Descripcion",movimiento.getDescripcion());
                                    intent.putExtra("Gasto",movimiento.isGasto());
                                    intent.putExtra("Id",id);
                                    intent.putExtra("FechaInicio",movimiento.getFechaInicio());
                                    intent.putExtra("FechaFinalizacion",movimiento.getFechaFinalizacion());
                                    intent.putExtra("Frecuencia",movimiento.getFrecuenciaEnum().ordinal());

                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),id, intent, 0);

                                    alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                                            /*recordatorio.getTime()*/
                                            //TEST
                                            new Date().getTime()+30000
                                            , pendingIntent);
                                }


                            } else if (recordatorio && !m_chkRecordatorio.isChecked()) {
                                //Cancelacion de la alarma (En caso de que ya hubiese una alarma programada y el usuario no quiera ser recordado)
                                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),id, intent, 0);

                                alarmManager.cancel(pendingIntent);

                            } else {
                                //No habia una alarma y no quiere una alarma

                            }
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (movimiento.isGasto()) Toast.makeText(EditarMovimientoActivity.this, R.string.successGasto,Toast.LENGTH_SHORT ).show();
                                        else Toast.makeText(EditarMovimientoActivity.this, R.string.successIngreso,Toast.LENGTH_SHORT ).show();
                                        finish();
                                    }
                                });
                            }
                        });

                    }
                }).start();

            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { //NO anda
        if(keyCode==KeyEvent.KEYCODE_BACK && getIntent().getStringExtra("Access")!=null){
            if(getIntent().getStringExtra("Access").equals("Notificacion")) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }
        return true;
    }
}
