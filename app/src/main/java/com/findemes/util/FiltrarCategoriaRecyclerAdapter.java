package com.findemes.util;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.findemes.R;
import com.findemes.model.Categoria;

import java.util.List;

public class FiltrarCategoriaRecyclerAdapter extends RecyclerView.Adapter<FiltrarCategoriaRecyclerAdapter.CategoriaHolder> {
    private List<Categoria> dataset;
    View view;

    //Constructor
    public FiltrarCategoriaRecyclerAdapter(List<Categoria> cats) {
        dataset = cats;
    }

    @NonNull
    @Override
    public CategoriaHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = (View) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item_categoria_filtrar, viewGroup, false);
        CategoriaHolder holder = new CategoriaHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoriaHolder categoriaHolder, int i) {
        String hexColor = String.format("#%06X", (0xFFFFFF & dataset.get(i).getColor()));
        categoriaHolder.tituloCategoria.setText(dataset.get(i).getNombre());
        categoriaHolder.tituloCategoria.setTextColor(Color.parseColor(hexColor));

        if(dataset.get(i).isGasto()){
            categoriaHolder.tipoCategoria.setText("GASTOS");
            categoriaHolder.tipoCategoria.setTextColor(Color.parseColor("#e07575"));
        } else {
            categoriaHolder.tipoCategoria.setText("INGRESOS");
            categoriaHolder.tipoCategoria.setTextColor(Color.parseColor("#5ecf8a"));
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }



    public class CategoriaHolder extends RecyclerView.ViewHolder {
        TextView tituloCategoria;
        TextView tipoCategoria;


        public CategoriaHolder(View v){
            super(v);
            tituloCategoria = v.findViewById(R.id.tituloCategoria);
            tipoCategoria = v.findViewById(R.id.tipoCategoria);

        }

    }
}