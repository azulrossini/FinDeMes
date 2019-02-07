package com.findemes.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.findemes.R;
import com.findemes.model.Categoria;
import com.findemes.util.CategoriaAdapter;

import java.util.ArrayList;

public class TabCategoriasGastosFragment extends Fragment{

    ArrayList<Categoria> categorias;
    ListView listView;
    private static CategoriaAdapter adapter;

    public TabCategoriasGastosFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        categorias = new ArrayList<>();

        categorias.add(new Categoria("Categoria 1"));
        categorias.add(new Categoria("Categoria 2"));
        categorias.add(new Categoria("Categoria 3"));
        categorias.add(new Categoria("Categoria 4"));
        categorias.add(new Categoria("Categoria 5"));
        categorias.add(new Categoria("Categoria 6"));
        categorias.add(new Categoria("Categoria 7"));
        categorias.add(new Categoria("Categoria 8"));
        categorias.add(new Categoria("Categoria 9"));
        categorias.add(new Categoria("Categoria 10"));
        categorias.add(new Categoria("Categoria 11"));
        categorias.add(new Categoria("Categoria 12"));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab_categorias_gastos, container, false);
        ListView listaCategorias = v.findViewById(R.id.listCategoriasGastos);
        adapter = new CategoriaAdapter(categorias,getActivity().getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return v;


    }


}
