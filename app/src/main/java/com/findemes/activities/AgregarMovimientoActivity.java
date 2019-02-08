package com.findemes.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AgregarMovimientoActivity extends AppCompatActivity {

    private boolean isGasto = true;
    private Calendar calendarFin = Calendar.getInstance();
    private Calendar calendarInicio = Calendar.getInstance();
    private Calendar calendarSingle = Calendar.getInstance();
    private boolean dateFinSelected =false;
    private boolean dateInicioSelected =false;
    private Button btnGuardar;
    private EditText edtTitulo;
    private EditText edtMonto;
    private EditText edtDescripcion;
    private Spinner spinnerFrecuencia;
    private Spinner spinnerCategoria;
    private Switch switchMovimientoFijo;
    private EditText edtFechaFin;
    private EditText edtFechaInicio;
    private EditText edtFechaSingle;
    private CheckBox chkRecordatorio;
    private MyDatabase db = MyDatabase.getInstance(this);
    private CircularImageView circularImageView;
    private AgregarMovimientoActivity activity;
    private boolean tookPicture=false;
    private Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_movimiento);
        if (getIntent().getIntExtra("tipo", 1) == 0) isGasto = true;
        else isGasto = false;

        activity=this;


        //Inicializacion de variables de vista
        spinnerFrecuencia = findViewById(R.id.spinnerFrecuencia);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        btnGuardar= findViewById(R.id.btnGuardar);
        edtTitulo= findViewById(R.id.edtTitulo);
        edtMonto= findViewById(R.id.edtMonto);
        edtDescripcion=findViewById(R.id.edtDescripcion);
        switchMovimientoFijo = findViewById(R.id.switchMovimientoFijo);
        edtFechaFin = findViewById(R.id.edtFechaFin);
        edtFechaInicio = findViewById(R.id.edtFechaInicio);
        edtFechaSingle = findViewById(R.id.edtFechaSingle);
        chkRecordatorio = findViewById(R.id.chkRecordatorio);
        circularImageView = findViewById(R.id.imageView);

        //

        //Lectura de ROOM
        final List<Categoria> categoriasRoom = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                categoriasRoom.addAll(db.getCategoriaDAO().getAll(isGasto));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //Configuracion de adapters para spinners
                        List<Categoria> categorias = new ArrayList<>();
                        Categoria def = new Categoria();
                        def.setNombre(getResources().getString(R.string.defaultCategoryName));
                        def.setGasto(isGasto);
                        categorias.add(def);
                        categorias.addAll(categoriasRoom);
                        ArrayAdapter<Categoria> adapterCategoria = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item, categorias);
                        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCategoria.setAdapter(adapterCategoria);


                        ArrayAdapter<FrecuenciaEnum> adapterFrecuencia = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,FrecuenciaEnum.values());
                        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerFrecuencia.setAdapter(adapterFrecuencia);
                        //

                        getSupportActionBar().setElevation(0);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowHomeEnabled(true);


                        spinnerFrecuencia.setVisibility(View.GONE);
                        edtFechaFin.setVisibility(View.GONE);
                        chkRecordatorio.setVisibility(View.GONE);
                        edtFechaInicio.setVisibility(View.GONE);

                        if (isGasto) {
                            getSupportActionBar().setTitle(R.string.add_gasto);
                            switchMovimientoFijo.setText(R.string.costoFijo);

                        } else {
                            getSupportActionBar().setTitle(R.string.add_ingreso);
                            switchMovimientoFijo.setText(R.string.ingresoFijo);

                        }

                    }
                });


            }
        }).start();
        //

        calendarSingle.setTime(new Date());



        //Listeners de datepicker


        final DatePickerDialog.OnDateSetListener dateFin = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendarFin.set(Calendar.YEAR, year);
                calendarFin.set(Calendar.MONTH, monthOfYear);
                calendarFin.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                edtFechaFin.setText(sdf.format(calendarFin.getTime()));
                dateFinSelected =true;
            }

        };
        edtFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AgregarMovimientoActivity.this,
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
                edtFechaInicio.setText(sdf.format(calendarInicio.getTime()));
                dateInicioSelected =true;
            }

        };
        edtFechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AgregarMovimientoActivity.this,
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
                edtFechaSingle.setText(sdf.format(calendarSingle.getTime()));
            }

        };
        edtFechaSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AgregarMovimientoActivity.this,
                        dateSingle,
                        calendarSingle.get(Calendar.YEAR),
                        calendarSingle.get(Calendar.MONTH),
                        calendarSingle.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        //

        //Visualizacion de elementos tras activacion del Switch
        switchMovimientoFijo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spinnerFrecuencia.setVisibility(View.VISIBLE);
                    edtFechaFin.setVisibility(View.VISIBLE);
                    chkRecordatorio.setVisibility(View.VISIBLE);
                    edtFechaInicio.setVisibility(View.VISIBLE);
                    edtFechaSingle.setVisibility(View.GONE);
                } else {
                    spinnerFrecuencia.setVisibility(View.GONE);
                    edtFechaFin.setVisibility(View.GONE);
                    chkRecordatorio.setVisibility(View.GONE);
                    edtFechaInicio.setVisibility(View.GONE);
                    edtFechaSingle.setVisibility(View.VISIBLE);
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
                } else if (switchMovimientoFijo.isChecked() && !dateFinSelected){
                    Toast.makeText(AgregarMovimientoActivity.this, R.string.missingEndDate,Toast.LENGTH_SHORT).show();
                    return;
                } else if (switchMovimientoFijo.isChecked() && !dateInicioSelected){
                    Toast.makeText(AgregarMovimientoActivity.this, R.string.missingStartDate,Toast.LENGTH_SHORT).show();
                    return;
                }
                //

                //Guardado en ROOM
                final Movimiento movimiento = new Movimiento();
                movimiento.setMonto(Double.parseDouble(edtMonto.getText().toString()));
                movimiento.setTitulo(edtTitulo.getText().toString());
                movimiento.setDescripcion(edtDescripcion.getText().toString());
                movimiento.setGasto(isGasto);

                if(spinnerCategoria.getSelectedItemPosition()==0){
                    movimiento.setCategoria(null);
                }else{
                    movimiento.setCategoria((Categoria)spinnerCategoria.getSelectedItem());
                }

                if(switchMovimientoFijo.isChecked()){
                    movimiento.setFrecuenciaEnum((FrecuenciaEnum)spinnerFrecuencia.getSelectedItem());
                    movimiento.setFechaFinalizacion(calendarFin.getTime());
                    movimiento.setFechaInicio(calendarInicio.getTime());
                }else{
                    movimiento.setFrecuenciaEnum(null);
                    movimiento.setFechaFinalizacion(calendarSingle.getTime());
                    movimiento.setFechaInicio(calendarSingle.getTime());
                }

                if(switchMovimientoFijo.isChecked()){
                    if(movimiento.getListaFechas().size()<=1 || movimiento.getFechaFinalizacion().before(movimiento.getFechaInicio())){
                        Toast.makeText(AgregarMovimientoActivity.this, R.string.invalidDates,Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if(chkRecordatorio.isChecked()) movimiento.setRecordatorio(true);
                else movimiento.setRecordatorio(false);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        db.getMovimientoDAO().insert(movimiento);

                        //Programado de la alarma (si corresponde)
                        if(chkRecordatorio.isChecked() && switchMovimientoFijo.isChecked()){

                            //REDO en lo posible
                            Movimiento movimientoDb = db.getMovimientoDAO().getLast().get(0);

                            Date recordatorio= AlertReceiver.proximaOcurrencia(movimiento.getFechaInicio(),(FrecuenciaEnum)spinnerFrecuencia.getSelectedItem());

                            if(recordatorio.before(movimiento.getFechaFinalizacion())){

                                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);

                                intent.putExtra("Titulo",movimiento.getTitulo());
                                intent.putExtra("Monto",movimiento.getMonto());
                                intent.putExtra("Descripcion",movimiento.getDescripcion());
                                intent.putExtra("Gasto",movimiento.isGasto());
                                intent.putExtra("Id",movimientoDb.getId());
                                intent.putExtra("FechaInicio",movimiento.getFechaInicio());
                                intent.putExtra("FechaFinalizacion",movimiento.getFechaFinalizacion());
                                intent.putExtra("Frecuencia",movimiento.getFrecuenciaEnum().ordinal());

                                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),movimientoDb.getId(), intent, 0);

                                alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                                        recordatorio.getTime()
                                        //TEST
                                        /*new Date().getTime()+10000*/
                                        , pendingIntent);
                            }
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isGasto) Toast.makeText(AgregarMovimientoActivity.this, R.string.successGasto,Toast.LENGTH_SHORT ).show();
                                else Toast.makeText(AgregarMovimientoActivity.this, R.string.successIngreso,Toast.LENGTH_SHORT ).show();
                                finish();
                            }
                        });

                    }
                }).start();


            }
        });

        //ImageView

        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
                } else {

                    Intent intentFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intentFoto, 2);

                }
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==2 && resultCode==RESULT_OK){
            photo=(Bitmap)data.getExtras().get("data");
            circularImageView.setImageBitmap(photo);
            tookPicture=true;
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                circularImageView.performClick();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
