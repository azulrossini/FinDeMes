package com.findemes.util;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.findemes.R;
import com.findemes.activities.BalancesActivity;
import com.findemes.activities.EditarMovimientoActivity;
import com.findemes.activities.MainActivity;
import com.findemes.model.Movimiento;
import com.findemes.room.MyDatabase;


import java.util.Date;
import java.util.List;

public class BalanceRecyclerAdapter extends RecyclerView.Adapter<BalanceRecyclerAdapter.BalanceHolder> {
    private List<Movimiento> dataset;
    View view;
    private int id;
    private int repeticionesMovMes;
    private int repeticionesMovBalance;
    private MyDatabase database;

    //Constructor
    public BalanceRecyclerAdapter(List<Movimiento> movs) {
        dataset = movs;
    }

    @NonNull
    @Override
    public BalanceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = (View) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item_movimiento, viewGroup, false);
        BalanceHolder holder = new BalanceHolder(view);
        database = MyDatabase.getInstance(view.getContext());
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final BalanceHolder balanceHolder, int i) {
        if(dataset.get(i).isGasto()){
            balanceHolder.monto.setTextColor(Color.parseColor("#e07575"));
        } else {
            balanceHolder.monto.setTextColor(Color.parseColor("#5ecf8a"));
        }

        if(view.getContext().getClass() == BalancesActivity.class){
            getRepeticionesMovBalance(i);
            balanceHolder.monto.setText("$ " + dataset.get(i).getMonto().toString());
            balanceHolder.tituloMovimiento.setText(dataset.get(i).getTitulo().toUpperCase() + " x" + repeticionesMovBalance);
        }
        else{
            getRepeticionesMov(i);
            balanceHolder.monto.setText("$ " + dataset.get(i).getMonto().toString());
            balanceHolder.tituloMovimiento.setText(dataset.get(i).getTitulo().toUpperCase() + " x" + repeticionesMovMes);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull BalanceHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void getRepeticionesMov(int i){
        repeticionesMovMes = 0;
        for(int x=0; x<dataset.get(i).getListaFechas().size(); x++){
            if(dataset.get(i).getListaFechas().get(x).getMonth() == new Date().getMonth()){
                repeticionesMovMes++;
            }
        }

    }

    public void getRepeticionesMovBalance(int i){
        repeticionesMovBalance = 0;

        //Obtiene las repeticiones del movimiento de acuerdo a el periodo
        //Si el periodo es 'All', muestra todas las repeticiones

        if(BalancesActivity.activity.getPeriodo().getSelectedItemPosition() == 0){
            System.out.println("todo");
            for(int x=0; x<dataset.get(i).getListaFechas().size(); x++){
                    repeticionesMovBalance++;
            }
        }


        //Cuando no es all
        Date fecha_hoy = new Date();
        fecha_hoy.setHours(23);
        fecha_hoy.setMinutes(59);
        fecha_hoy.setSeconds(59);

        for(int x=0; x<dataset.get(i).getListaFechas().size(); x++){
            if(dataset.get(i).getListaFechas().get(x).after(BalancesActivity.activity.getFecha()) && dataset.get(i).getListaFechas().get(x).before(fecha_hoy)){
                repeticionesMovBalance++;
            }
        }
    }

    public class BalanceHolder extends RecyclerView.ViewHolder {
        TextView tituloMovimiento;
        TextView monto;
        ImageView borrar;
        ImageView editar;


        public BalanceHolder(View v){
            super(v);
            tituloMovimiento = v.findViewById(R.id.tituloMov);
            monto = v.findViewById(R.id.montoMov);
            borrar = v.findViewById(R.id.borrar);
            editar = v.findViewById(R.id.editar);

            editar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(view.getContext(), EditarMovimientoActivity.class);
                    System.out.println("pos adapter mod"+getAdapterPosition());
                    id = dataset.get(getAdapterPosition()).getId();
                    i.putExtra("Id", id);
                    view.getContext().startActivity(i);
                    notifyDataSetChanged();
                }
            });

            borrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final int pos = getAdapterPosition();
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("¿Desea eliminar el movimiento?")
                            .setTitle("Eliminar Movimiento")
                            .setPositiveButton("Si",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dlgInt, int i) {
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    database.getMovimientoDAO().delete(dataset.get(pos));
                                                    MainActivity.self.forceUpdate();
                                                    if(BalancesActivity.activity!=null) BalancesActivity.activity.forceUpdate();
                                                }
                                            }).start();


                                        }
                                    })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }


    }
}
