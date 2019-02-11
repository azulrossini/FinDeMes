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
import com.findemes.model.Movimiento;
import com.findemes.room.MyDatabase;

import java.util.List;

public class FiltrarCategoriaRecyclerAdapter extends RecyclerView.Adapter<FiltrarCategoriaRecyclerAdapter.CategoriaHolder> {
    private List<Categoria> dataset;
    View view;
    private List<Movimiento> movimientos;

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
    public void onBindViewHolder(@NonNull final CategoriaHolder categoriaHolder, final int i) {

        if(movimientos == null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    movimientos = MyDatabase.getInstance(view.getContext()).getMovimientoDAO().getAll();

                    String hexColor = String.format("#%06X", (0xFFFFFF & dataset.get(i).getColor()));

                    Categoria categoria = dataset.get(i);
                    Double monto=0.0;

                    if(categoria.getId()==-2){
                        monto=getTotalCategoriaNula(movimientos,false);
                    } else if (categoria.getId()==-1){
                        monto=getTotalCategoriaNula(movimientos,true);
                    } else {
                        monto=getTotalCategoria(movimientos, categoria.getId());
                    }

                    categoriaHolder.tituloCategoria.setText(dataset.get(i).getNombre()+" - ($"+monto+")");
                    categoriaHolder.tituloCategoria.setTextColor(Color.parseColor(hexColor));

                    if(dataset.get(i).isGasto()){
                        categoriaHolder.tipoCategoria.setText("GASTOS");
                        categoriaHolder.tipoCategoria.setTextColor(Color.parseColor("#e07575"));
                    } else {
                        categoriaHolder.tipoCategoria.setText("INGRESOS");
                        categoriaHolder.tipoCategoria.setTextColor(Color.parseColor("#5ecf8a"));
                    }
                }
            }).start();
        }
         else {
            String hexColor = String.format("#%06X", (0xFFFFFF & dataset.get(i).getColor()));

            Categoria categoria = dataset.get(i);
            Double monto = 0.0;

            if (categoria.getId() == -2) {
                monto = this.getTotalCategoriaNula(movimientos, false);
            } else if (categoria.getId() == -1) {
                monto = this.getTotalCategoriaNula(movimientos, true);
            } else {
                monto = this.getTotalCategoria(movimientos, categoria.getId());
            }

            categoriaHolder.tituloCategoria.setText(dataset.get(i).getNombre() + " - ($" + monto + ")");
            categoriaHolder.tituloCategoria.setTextColor(Color.parseColor(hexColor));

            if (dataset.get(i).isGasto()) {
                categoriaHolder.tipoCategoria.setText("GASTOS");
                categoriaHolder.tipoCategoria.setTextColor(Color.parseColor("#e07575"));
            } else {
                categoriaHolder.tipoCategoria.setText("INGRESOS");
                categoriaHolder.tipoCategoria.setTextColor(Color.parseColor("#5ecf8a"));
            }
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


    private double getTotalCategoriaNula(List<Movimiento> movimientos, boolean gasto) {
        double total = 0.0;
        for(Movimiento mov : movimientos){
            if(mov.getCategoria() == null) {
                if(gasto) {
                    if(mov.isGasto()) total += mov.getMonto()*mov.getListaFechas().size();
                } else {
                    if(!mov.isGasto()) total += mov.getMonto()*mov.getListaFechas().size();
                }
            }
        }
        return total;
    }

    private double getTotalCategoria(List<Movimiento> movimientos, int idCategoria){
        double total = 0.0;
        for(Movimiento mov : movimientos){
            if(!(mov.getCategoria() == null)) {
                if (mov.getCategoria().getId() == idCategoria) {
                    total += mov.getMonto()*mov.getListaFechas().size();
                }
            }
        }
        return total;
    }

}
