package com.findemes.util;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import com.findemes.activities.EditarCategoriasActivity;
import com.findemes.model.Categoria;

import com.findemes.R;
import com.findemes.room.MyDatabase;

public class EditarCategoriaRecyclerAdapter extends RecyclerView.Adapter<EditarCategoriaRecyclerAdapter.CategoriaHolder> {
    private List<Categoria> dataset;
    View view;
    private int id;
    private MyDatabase database;

    //Constructor
    public EditarCategoriaRecyclerAdapter(List<Categoria> cats) {
        dataset = cats;
    }

    @NonNull
    @Override
    public CategoriaHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = (View) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item_categoria_editar, viewGroup, false);
        CategoriaHolder holder = new CategoriaHolder(view);
        database = MyDatabase.getInstance(view.getContext());
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoriaHolder categoriaHolder, int i) {
        String hexColor = String.format("#%06X", (0xFFFFFF & dataset.get(i).getColor()));
        categoriaHolder.tituloCategoria.setText(dataset.get(i).getNombre());
        categoriaHolder.tituloCategoria.setTextColor(Color.parseColor(hexColor));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }



    public class CategoriaHolder extends RecyclerView.ViewHolder {
        TextView tituloCategoria;
        ImageView borrarCategoria;


        public CategoriaHolder(View v){
            super(v);
            tituloCategoria = v.findViewById(R.id.tituloCategoria);
            borrarCategoria = v.findViewById(R.id.borrarCategoria);

            borrarCategoria.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final int pos = getAdapterPosition();
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("¿Desea eliminar la categoría?")
                            .setTitle("Eliminar Categoría")
                            .setPositiveButton("Si",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dlgInt, int i) {
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    database.getCategoriaDAO().delete(dataset.get(pos));
                                                    EditarCategoriasActivity.activity.update();
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
