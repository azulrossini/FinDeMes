package com.findemes.util;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.findemes.R;
import com.findemes.activities.EditarMovimientoActivity;
import com.findemes.model.Movimiento;

import java.util.ArrayList;
import java.util.List;

public class BalanceRecyclerAdapter extends RecyclerView.Adapter<BalanceRecyclerAdapter.BalanceHolder> {
    private List<Movimiento> dataset;


    //Constructor
    public BalanceRecyclerAdapter(List<Movimiento> movs) {
        dataset = movs;
    }

    @NonNull
    @Override
    public BalanceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = (View) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fila_movimiento, viewGroup, false);

        BalanceHolder holder = new BalanceHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final BalanceHolder balanceHolder, int i) {
        balanceHolder.monto.setText(dataset.get(i).getMonto().toString());
        balanceHolder.tituloMovimiento.setText(dataset.get(i).getTitulo().toUpperCase());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
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
        }
    }
}
