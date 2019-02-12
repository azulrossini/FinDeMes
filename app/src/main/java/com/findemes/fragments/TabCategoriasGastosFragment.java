package com.findemes.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.findemes.R;
import com.findemes.room.MyDatabase;
import com.findemes.util.EditarCategoriaRecyclerAdapter;

public class TabCategoriasGastosFragment extends Fragment{

    public TabCategoriasGastosFragment() { }
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private MyDatabase database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = MyDatabase.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_categorias_gastos, container, false);

        recyclerView = v.findViewById(R.id.lista_categoria_gastos);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("adapter");
                adapter = new EditarCategoriaRecyclerAdapter(database.getCategoriaDAO().getAll(true));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        }).start();

        return v;
    }

    public void update(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("adapter");
                adapter = new EditarCategoriaRecyclerAdapter(database.getCategoriaDAO().getAll(true));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        }).start();

    }

}
