package com.findemes.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.findemes.R;

public class CustomAlertDialog extends Dialog  {

    public Activity activity;
    public Dialog dialog;
    public Button btnGuardarCategoria, btnCancelarCategoria;
    private EditText edtNombreCategoria;
    public String nombre;
    public Switch switchNuevaCategoria;
    public boolean gasto;

    public CustomAlertDialog(Context context) {
        super(context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.custom_alert_dialog);
        this.setTitle(getContext().getResources().getString(R.string.nuevaCategoria));
        btnGuardarCategoria=findViewById(R.id.btnGuardarCategoria);
        btnCancelarCategoria=findViewById(R.id.btnCancelarCategoria);
        switchNuevaCategoria=findViewById(R.id.switchNuevaCategoria);
        edtNombreCategoria = findViewById(R.id.edtCategoriaNombre);

        btnGuardarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nombre.isEmpty()){
                    Toast.makeText(getContext(),R.string.emptyName,Toast.LENGTH_SHORT);
                    return;
                } else {
                    nombre=edtNombreCategoria.getText().toString();
                    gasto=switchNuevaCategoria.isChecked();
                    activity.finish();
                    return;
                }
            }
        });

        btnCancelarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


}
