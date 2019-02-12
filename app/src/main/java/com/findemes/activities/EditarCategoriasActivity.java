package com.findemes.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.Toolbar;

import com.findemes.R;
import com.findemes.fragments.TabCategoriasGastosFragment;
import com.findemes.fragments.TabCategoriasIngresosFragment;
import com.findemes.model.Categoria;
import com.findemes.room.MyDatabase;
import com.findemes.util.RandomColorGenerator;

import java.util.ArrayList;
import java.util.List;

public class EditarCategoriasActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private MyDatabase db;
    private EditText edtNombreCategoria;
    private Button btnGuardarCategoria, btnCancelarCategoria;
    private Switch switchNuevaCategoria;

    private int color = 0;

    private TabCategoriasGastosFragment tabCategoriasGastosFragment;
    private TabCategoriasIngresosFragment tabCategoriasIngresosFragment;

    public static EditarCategoriasActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_categorias);

        activity=this;

        db = MyDatabase.getInstance(this);

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
        tabCategoriasIngresosFragment = new TabCategoriasIngresosFragment();
        tabCategoriasGastosFragment = new TabCategoriasGastosFragment();
        adapter.addFragment(tabCategoriasIngresosFragment, "INGRESOS");
        adapter.addFragment(tabCategoriasGastosFragment, "GASTOS");
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nueva_categoria_appbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_nuevaCategoria){

            final Dialog myDialog = new Dialog(EditarCategoriasActivity.this,R.style.Dialog);
            myDialog.setContentView(R.layout.custom_alert_dialog);
            myDialog.setTitle(R.string.nuevaCategoria);


            //Setup del picker del color
            LinearGradient test = new LinearGradient(0.f, 0.f, 700.f, 0.0f,
                    new int[] { 0xFF000000, 0xFF0000FF, 0xFF00FF00, 0xFF00FFFF,
                            0xFFFF0000, 0xFFFF00FF, 0xFFFFFF00, 0xFFFFFFFF},
                    null, Shader.TileMode.CLAMP);
            ShapeDrawable shape = new ShapeDrawable(new RectShape());
            shape.getPaint().setShader(test);

            final SeekBar seekBarFont = (SeekBar)myDialog.findViewById(R.id.seekbar_font);
            final ImageView seekBarColor = (ImageView)myDialog.findViewById(R.id.seekbar_color);
            seekBarFont.setProgressDrawable( (Drawable)shape );

            seekBarFont.setMax(256*7-1);
            seekBarFont.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(fromUser){
                        int r = 0;
                        int g = 0;
                        int b = 0;

                        if(progress < 256){
                            b = progress;
                        } else if(progress < 256*2) {
                            g = progress%256;
                            b = 256 - progress%256;
                        } else if(progress < 256*3) {
                            g = 255;
                            b = progress%256;
                        } else if(progress < 256*4) {
                            r = progress%256;
                            g = 256 - progress%256;
                            b = 256 - progress%256;
                        } else if(progress < 256*5) {
                            r = 255;
                            g = 0;
                            b = progress%256;
                        } else if(progress < 256*6) {
                            r = 255;
                            g = progress%256;
                            b = 256 - progress%256;
                        } else if(progress < 256*7) {
                            r = 255;
                            g = 255;
                            b = progress%256;
                        }

                        seekBarColor.setBackgroundColor(Color.argb(255,r,g,b));
                        color = Color.argb(255,r,g,b);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            btnCancelarCategoria = myDialog.findViewById(R.id.btnCancelarCategoria);
            btnGuardarCategoria = myDialog.findViewById(R.id.btnGuardarCategoria);
            switchNuevaCategoria = myDialog.findViewById(R.id.switchNuevaCategoria);
            edtNombreCategoria = myDialog.findViewById(R.id.edtCategoriaNombre);

            if(tabLayout.getSelectedTabPosition()==0) {
                switchNuevaCategoria.setChecked(false);
            }
            else {
                switchNuevaCategoria.setChecked(true);
            }

            btnGuardarCategoria.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(edtNombreCategoria.getText().toString().isEmpty() || color == 0){
                        Toast.makeText(getApplicationContext(),R.string.emptyNameColor,Toast.LENGTH_SHORT).show();
                        return;
                    } else {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                List<Categoria> categorias = db.getCategoriaDAO().getAll(switchNuevaCategoria.isChecked());

                                boolean can=true;

                                if(categorias.isEmpty() || categorias.get(0)==null){
                                    can=true;
                                } else {

                                    for(Categoria categoria : categorias){
                                        if(categoria.getNombre().toLowerCase().replaceAll("\\s+","").equals(edtNombreCategoria.getText().toString().toLowerCase().replaceAll("\\s+",""))){
                                            can=false;
                                        }
                                    }

                                }

                                if(can){
                                    Categoria categoria = new Categoria();
                                    String nombre = edtNombreCategoria.getText().toString().substring(0, 1).toUpperCase() + edtNombreCategoria.getText().toString().substring(1);
                                    categoria.setGasto(switchNuevaCategoria.isChecked());
                                    categoria.setNombre(nombre);
                                    categoria.setColor(color);
                                    color = 0;

                                    db.getCategoriaDAO().insert(categoria);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(EditarCategoriasActivity.this, R.string.categoriaAgregada, Toast.LENGTH_SHORT).show();

                                            update();
                                            myDialog.dismiss();
                                            return;
                                        }
                                    });
                                    return;

                                }
                                else{

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(EditarCategoriasActivity.this, R.string.categoriaExistente, Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    });
                                    return;
                                }
                                
                            }
                        }).start();

                    }
                }
            });

            btnCancelarCategoria.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.dismiss();
                }
            });

            myDialog.show();

        }
        else onBackPressed();

        return true;
    }

    public void update(){

        tabCategoriasGastosFragment.update();
        tabCategoriasIngresosFragment.update();

    }


}
