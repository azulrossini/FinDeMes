package com.findemes.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.findemes.R;
import com.findemes.model.Categoria;
import com.findemes.model.FrecuenciaEnum;
import com.findemes.model.Movimiento;
import com.findemes.room.MyDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditarMovimientoActivity extends AppCompatActivity {

    private MyDatabase db = MyDatabase.getInstance(EditarMovimientoActivity.this);
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_movimiento);

        System.out.println("Into editar");

        //Inicializacion de variables de vista
        m_edtTitulo=findViewById(R.id.m_edtTitulo);
        m_edtMonto=findViewById(R.id.m_edtMonto);
        m_edtDescripcion=findViewById(R.id.m_edtDescripcion);
        m_spinnerCategoria=findViewById(R.id.m_spinnerCategoria);
        m_spinnerFrecuencia=findViewById(R.id.m_spinnerFrecuencia);
        m_switchMovimientoFijo=findViewById(R.id.m_switchMovimientoFijo);
        m_edtFechaSingle=findViewById(R.id.m_edtFechaSingle);
        m_edtFechaInicio=findViewById(R.id.edtFechaInicio);
        m_edtFechaFin=findViewById(R.id.m_edtFechaFin);
        m_chkRecordatorio=findViewById(R.id.m_chkRecordatorio);
        m_btnGuardar=findViewById(R.id.m_btnGuardar);

        //


        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Integer id = getIntent().getIntExtra("Id",-1);

        if(id==-1){
            finish();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Movimiento> resultado = db.getMovimientoDAO().get(id);
                if(resultado.isEmpty()){
                    finish();
                }else{
                    movimiento=resultado.get(0);
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
                            getSupportActionBar().setTitle(R.string.add_gasto);
                            m_switchMovimientoFijo.setText(R.string.costoFijo);

                        } else {
                            getSupportActionBar().setTitle(R.string.add_ingreso);
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


                        if(movimiento.getFrecuenciaEnum() == null){

                            //Movimiento puntual
                            m_switchMovimientoFijo.setChecked(false);
                            m_edtFechaSingle.setText(sdf.format(movimiento.getFechaInicio()));
                            calendarSingle.setTime(movimiento.getFechaInicio());

                        }else{

                            //Movimiento fijo
                            m_switchMovimientoFijo.setChecked(true);
                            m_edtFechaInicio.setText(sdf.format(movimiento.getFechaInicio())); //VER QUE ONDA
                            calendarInicio.setTime(movimiento.getFechaInicio());

                            m_edtFechaFin.setText(sdf.format(movimiento.getFechaFinalizacion()));
                            calendarFin.setTime(movimiento.getFechaFinalizacion());

                        }

                    }
                });
            }
        }).start();

    }
}
