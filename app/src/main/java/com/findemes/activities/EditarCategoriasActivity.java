package com.findemes.activities;

import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;

import com.findemes.R;
import com.findemes.fragments.TabBalanceFragment;
import com.findemes.fragments.TabCategoriasGastosFragment;
import com.findemes.fragments.TabCategoriasIngresosFragment;
import com.findemes.fragments.TabGastosFragment;
import com.findemes.fragments.TabIngresosFragment;

import java.util.ArrayList;
import java.util.List;

public class EditarCategoriasActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_categorias);

        getSupportActionBar().setTitle("Editar Categorías");
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewPagerEditarCategorias);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabsEditarCategorias);
        tabLayout.setupWithViewPager(viewPager);

    }

    //Setup de las pestañas y manejo de los fragmentos de la actividad principal
    private void setupViewPager(ViewPager viewPager) {
        EditarCategoriasActivity.ViewPagerAdapter adapter = new EditarCategoriasActivity.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabCategoriasIngresosFragment(), "INGRESOS");
        adapter.addFragment(new TabCategoriasGastosFragment(), "GASTOS");
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
