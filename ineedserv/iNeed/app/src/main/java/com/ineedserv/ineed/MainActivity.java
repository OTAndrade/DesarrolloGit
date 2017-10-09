package com.ineedserv.ineed;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.maps.MapFragment;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import com.ineedserv.ineed.fragments.LocalizacionOfertantes;
import com.ineedserv.ineed.fragments.LocalizacionSolicitantes;
import com.ineedserv.ineed.fragments.Pagina1;
import com.ineedserv.ineed.fragments.Pagina2;
import com.ineedserv.ineed.fragments.Pagina3;

//import android.support.v4.app.FragmentActivity;

public class MainActivity extends AppCompatActivity {

    public static final int DRAWER_ITEM_MAPA_OFERTANTE = 0;
    public static final int DRAWER_ITEM_MAPA_SOLICITANTE = 1;
    public static final int DRAWER_ITEM_AYUDA = 2;
    public static final int DRAWER_ITEM_CONFIGURACION = 3;
    public static final int DRAWER_ITEM_LEGAL = 4;

    private Drawer drawer;

    public int DRAWER_SELECCIONADO=0;

    private static final String LOG = "MainActivity";
    private Context context;
    private Toolbar toolbar;

    String nombre="";
    private String email="";
    String zipCode;
    String nroCelular;
    public String tipoUsuario;
    String nom;
    String correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context=this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        context=this;

        Intent intent = getIntent();
        zipCode = getIntent().getStringExtra("zipCode");
        nroCelular = getIntent().getStringExtra("nroCelular");
        tipoUsuario = getIntent().getStringExtra("tipoUsuario");
        nombre = getIntent().getStringExtra("nombre")+" "+nroCelular;
        correo = getIntent().getStringExtra("correo");

        setZipCode(zipCode);
        setNroCelular(nroCelular);

        //nombre="ineed";
        email="contacto@ineedserv.com ";

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName(nombre)
                                .withEmail(email)
                                .withIcon(new IconicsDrawable(this)
                                        .icon(FontAwesome.Icon.faw_user)
                                        .color(getResources().getColor(R.color.blanco))
                                        .backgroundColor(getResources().getColor(R.color.colorAccent))
                                        .paddingDp(2)
                                        .sizeDp(20))
                )
                .withHeaderBackground(R.color.colorAccent)
                .build();

        if (tipoUsuario.equals("1")) { // si es solicitantemuestra los ofertantes
            drawer = new DrawerBuilder(this)
                    .withToolbar(toolbar)
                    .withAccountHeader(headerResult)
                    .withActionBarDrawerToggle(true)
                    .addDrawerItems(
                            new DividerDrawerItem(),
                            new PrimaryDrawerItem().
                                    withIdentifier(DRAWER_ITEM_MAPA_OFERTANTE).// MAPA DE OFERTANTES PARA LOS SOLICITANTES
                                    withName("Servicios ofrecidos solicitados").
                                    withSelectedIconColor(getResources().getColor(R.color.colorAccent)).
                                    withSelectedTextColor(getResources().getColor(R.color.colorAccent)),//.withIcon(FontAwesome.Icon.faw_home),
                            new PrimaryDrawerItem().
                                    withIdentifier(DRAWER_ITEM_CONFIGURACION).
                                    withName("Acerca de").
                                    withSelectedIconColor(getResources().getColor(R.color.colorAccent)).
                                    withSelectedTextColor(getResources().getColor(R.color.colorAccent))//.withIcon(FontAwesome.Icon.faw_file_text)
                    )
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem drawerItem) {
                            selectItem(drawerItem.getIdentifier());
                            return false;
                        }
                    })
                    .withSelectedItem(DRAWER_ITEM_MAPA_OFERTANTE)
                    .withSavedInstance(savedInstanceState)
                    .build();
            selectItem(DRAWER_ITEM_MAPA_OFERTANTE);
        } else if (tipoUsuario.equals("2")) {  // si es ofertante; muestra las solicitudes
            drawer = new DrawerBuilder(this)
                    .withToolbar(toolbar)
                    .withAccountHeader(headerResult)
                    .withActionBarDrawerToggle(true)
                    .addDrawerItems(
                            new DividerDrawerItem(),
                            new PrimaryDrawerItem().
                                    withIdentifier(DRAWER_ITEM_MAPA_SOLICITANTE). // MAPA DE SOLICITUDES PARA LOS OFERTANTES
                                    withName("Solicitudes de servicio recibidas").
                                    withSelectedIconColor(getResources().getColor(R.color.colorAccent)).
                                    withSelectedTextColor(getResources().getColor(R.color.colorAccent)),//.withIcon(FontAwesome.Icon.faw_home),
                            new PrimaryDrawerItem().
                                    withIdentifier(DRAWER_ITEM_MAPA_OFERTANTE).
                                    withName("Servicios ofrecidos solicitados").
                                    withSelectedIconColor(getResources().getColor(R.color.colorAccent)).
                                    withSelectedTextColor(getResources().getColor(R.color.colorAccent)),//.withIcon(FontAwesome.Icon.faw_plus_square),
                            new PrimaryDrawerItem().
                                    withIdentifier(DRAWER_ITEM_CONFIGURACION).
                                    withName("Acerca de").
                                    withSelectedIconColor(getResources().getColor(R.color.colorAccent)).
                                    withSelectedTextColor(getResources().getColor(R.color.colorAccent))//.withIcon(FontAwesome.Icon.faw_file_text)
                    )
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem drawerItem) {
                            selectItem(drawerItem.getIdentifier());
                            return false;
                        }
                    })
                    .withSelectedItem(DRAWER_ITEM_MAPA_SOLICITANTE)
                    .withSavedInstance(savedInstanceState)
                    .build();
            selectItem(DRAWER_ITEM_MAPA_SOLICITANTE);
        }
        toolbar.setTitle("iNeed");


    }

    /* para que se salga de la aplicacion estando en otra actividad*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            System.exit(0);
            //finish();
        }
    }


    public void selectItem(int idMenu)
    {
        String titulo = "";
        Fragment f=new Pagina1();
        MapFragment mf=new MapFragment();
       // SupportMapFragment mf = SupportMapFragment.newInstance();

        Bundle args=new Bundle();

        switch (idMenu)
        {
            case DRAWER_ITEM_MAPA_OFERTANTE:
                titulo="iNeed";
                f=new LocalizacionOfertantes();
                break;
            case DRAWER_ITEM_MAPA_SOLICITANTE:
                titulo="iNeed";
                f=new LocalizacionSolicitantes();
                break;
            case DRAWER_ITEM_AYUDA:
                titulo="Ayuda";
                f=new Pagina1();
                break;
            case DRAWER_ITEM_CONFIGURACION:
                titulo="Acerca de";
                f=new Pagina2();
                break;
            case DRAWER_ITEM_LEGAL:
                titulo="Legal";
                f=new Pagina3();
                break;
        }
        toolbar.setTitle(titulo);
        args.putString("param1", titulo);
        f.setArguments(args);

        FragmentManager fm = getSupportFragmentManager();
        Fragment oldFragment = getSupportFragmentManager().findFragmentById(R.id.contenedor);
        if (oldFragment != null)
        {
                fm.beginTransaction()
                        .remove(oldFragment)
                        .addToBackStack("tag")
                        .replace(R.id.contenedor, f)
                        .commit();
        }
        else
            fm.beginTransaction()
                    .addToBackStack("tag")
                    .replace(R.id.contenedor, f)
                    .commit();

        DRAWER_SELECCIONADO=idMenu;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (drawer != null) {
            outState = drawer.saveInstanceState(outState);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(DRAWER_SELECCIONADO== DRAWER_ITEM_CONFIGURACION)
        {
            selectItem(DRAWER_ITEM_CONFIGURACION);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            if (drawer.isDrawerOpen())
                drawer.closeDrawer();
            else
                drawer.openDrawer();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DRAWER_SELECCIONADO=0;
/*        if (drawer.getCurrentSelection() == 0 || drawer.getCurrentSelection()== 1){
            //handle the back press :D close the drawer first and if the drawer is closed close the activity
            drawer.closeDrawer();
            finish();
            System.exit(0);
        }else{
            selectItem(DRAWER_ITEM_MAPA_SOLICITANTE);
*//*            drawer.closeDrawer();
            //moveTaskToBack(true);
            //super.onBackPressed();
            finish();
            System.exit(0);*//*
        }*/
        if (drawer != null && drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else
        {
            //drawer.closeDrawer();
            super.onBackPressed();
            moveTaskToBack(true);
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getNroCelular() {
        return nroCelular;
    }

    public void setNroCelular(String nroCelular) {
        this.nroCelular = nroCelular;
    }
}
