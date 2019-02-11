package com.findemes.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.findemes.R;
import com.findemes.fragments.TabIngresosFragment;
import com.findemes.fragments.TabBalanceFragment;
import com.findemes.fragments.TabGastosFragment;
import com.findemes.model.Categoria;
import com.findemes.model.FrecuenciaEnum;
import com.findemes.model.Movimiento;
import com.findemes.room.MyDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private TabIngresosFragment ingresosFragment;
    private TabGastosFragment gastosFragment;
    private TabBalanceFragment balanceFragment;

    public static MainActivity self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Variables
        self=this;

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton fabGastos= findViewById(R.id.fab_add_gasto);
        FloatingActionButton fabIngresos = findViewById(R.id.fab_add_ingreso);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
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

        fabIngresos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AgregarMovimientoActivity.class);
                intent.putExtra("tipo",1);
                startActivity(intent);
                fab.performClick();
            }
        });

        fabGastos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AgregarMovimientoActivity.class);
                intent.putExtra("tipo",0);
                startActivity(intent);
                fab.performClick();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(1).select();
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

            Intent intent = new Intent(MainActivity.this, BalancesActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_editar_categorias) {

            Intent intent = new Intent(MainActivity.this, EditarCategoriasActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_filtrar_categorias) {

            Intent intent = new Intent(MainActivity.this, FiltrarCategoriasActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //Setup de las pestañas y manejo de los fragmentos de la actividad principal
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        ingresosFragment=new TabIngresosFragment();
        balanceFragment = new TabBalanceFragment();
        gastosFragment = new TabGastosFragment();

        adapter.addFragment(ingresosFragment, "INGRESOS");
        adapter.addFragment(balanceFragment, "BALANCE");
        adapter.addFragment(gastosFragment, "GASTOS");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    protected void onResume() {
        //Se debe notificar a los 3 fragments de recargarse

        if(balanceFragment!= null && ingresosFragment != null && gastosFragment != null){
            balanceFragment.update(getApplicationContext());
            ingresosFragment.update(getApplicationContext());
            gastosFragment.update(getApplicationContext());
        }



        super.onResume();
    }

    public void forceUpdate(){
        //Se debe notificar a los 3 fragments de recargarse

        System.out.println("forceUpdate1");
        System.out.println(self);

        if(balanceFragment!= null && ingresosFragment != null && gastosFragment != null){

            System.out.println("forceUpdate2");
            balanceFragment.update(getApplicationContext());
            ingresosFragment.update(getApplicationContext());
            gastosFragment.update(getApplicationContext());
        }
    }
}
