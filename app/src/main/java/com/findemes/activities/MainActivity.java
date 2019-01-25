package com.findemes.activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.findemes.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //VARIABLES

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton fabGastos= findViewById(R.id.fab_add_gasto);
        FloatingActionButton fabIngresos = findViewById(R.id.fab_add_ingreso);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView labelfabVolver = findViewById(R.id.labelFabVolver);
        TextView labelfabIngresos = findViewById(R.id.labelFabIngreso);
        TextView labelfabGastos = findViewById(R.id.labelFabGasto);
        setSupportActionBar(toolbar);

        getSupportActionBar().setElevation(0);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                FloatingActionButton fabGastos= findViewById(R.id.fab_add_gasto);
                FloatingActionButton fabIngresos = findViewById(R.id.fab_add_ingreso);
                TextView labelfabVolver = findViewById(R.id.labelFabVolver);
                TextView labelfabIngresos = findViewById(R.id.labelFabIngreso);
                TextView labelfabGastos = findViewById(R.id.labelFabGasto);

                if(fabGastos.isOrWillBeShown() && fabIngresos.isOrWillBeShown()){
                    labelfabVolver.setVisibility(View.GONE);
                    labelfabIngresos.setVisibility(View.GONE);
                    labelfabGastos.setVisibility(View.GONE);
                    fabGastos.hide();
                    fabIngresos.hide();
                    fab.setImageResource(R.drawable.ic_add_white);

                } else if (fabGastos.isOrWillBeHidden() && fabIngresos.isOrWillBeHidden()){
                    labelfabVolver.setVisibility(View.VISIBLE);
                    labelfabIngresos.setVisibility(View.VISIBLE);
                    labelfabGastos.setVisibility(View.VISIBLE);
                    fabGastos.show();
                    fabIngresos.show();
                    fab.setImageResource(R.drawable.ic_arrow_back_white);

                }


            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add_gasto) {

            Intent intent = new Intent(MainActivity.this, AgregarMovimientoActivity.class);
            intent.putExtra("tipo",0);
            startActivity(intent);

        } else if (id == R.id.nav_add_ingreso) {

            Intent intent = new Intent(MainActivity.this, AgregarMovimientoActivity.class);
            intent.putExtra("tipo",1);
            startActivity(intent);

        } else if (id == R.id.nav_balance) {

        } else if (id == R.id.nav_editar_categorias) {

        } else if (id == R.id.nav_filtrar_categorias) {

        } else if (id == R.id.nav_movimientos) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
