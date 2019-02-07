package com.findemes.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.findemes.model.Categoria;
import com.findemes.R;

import java.util.ArrayList;

public class CategoriaAdapter extends ArrayAdapter<Categoria> implements View.OnClickListener{

    private ArrayList<Categoria> categorias;
    Context mContext;

    @Override
    public void onClick(View v) {

    }

    // Datos de la vista
    private static class ViewHolder {
        TextView txtNombreCategoria;
        ImageView borrar;
    }

    public CategoriaAdapter(ArrayList<Categoria> data, Context context) {
        super(context, R.layout.row_item_categoria, data);
        this.categorias = data;
        this.mContext=context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Categoria categoria = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_categoria, parent, false);
            viewHolder.txtNombreCategoria = (TextView) convertView.findViewById(R.id.tituloCategoria);
            viewHolder.borrar = (ImageView) convertView.findViewById(R.id.borrarCategoria);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtNombreCategoria.setText(categoria.getNombre());
        viewHolder.borrar.setOnClickListener(this);
        viewHolder.borrar.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}
