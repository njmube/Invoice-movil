package ec.bigdata.facturaelectronicamovil.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.login.Inicio;
import ec.bigdata.facturaelectronicamovil.pantallas.OpcionesFacturacion;
import ec.bigdata.facturaelectronicamovil.pantallas.PerfilEmpresa;
import ec.bigdata.facturaelectronicamovil.pantallas.PerfilUsuario;
import ec.bigdata.facturaelectronicamovil.utilidades.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidades.Utilidades;


public class NavigationDrawer extends AppCompatActivity {
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBar actionBar;
    TextView textView;
    ClaseGlobalUsuario claseGlobalUsuario;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();

        String nombreUsuarioSesion = "";
        if (claseGlobalUsuario.getTipoUsuario().equals("1")) {
            nombreUsuarioSesion = claseGlobalUsuario.getNombreComercial();
        } else {
            nombreUsuarioSesion = claseGlobalUsuario.getNombreUsuario();
        }
        Toast.makeText(NavigationDrawer.this, "Bienvenido: " + nombreUsuarioSesion, Toast.LENGTH_SHORT).show();
        navigationView = (NavigationView) findViewById(R.id.navigation_view_menu);

        View header = navigationView.getHeaderView(0);

        textView = (TextView) header.findViewById(R.id.text_view_usuario_actual);

        textView.setText(claseGlobalUsuario.getNombreUsuario());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);


        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }
        //Carga del menú dinámico para el navigation drawer
        /**
         * Accessing the NavigationView menu.
         * The getMenu method returns menu resource used by navigationView.
         * Later,we will add items to this menu.
         */

       /*final Menu drawerMenu = navigationView.getMenu();

        ClienteRestRecursos.ServicioRecursos servicioRecursos = ClienteRestRecursos.getServicioRecursos();
        Call<List<Recurso>> call_usuario_acceso = servicioRecursos.obtenerPantallasPorPerfil(claseGlobalUsuario.getIdPerfil());
        call_usuario_acceso.enqueue(new Callback<List<Recurso>>() {
            @Override
            public void onResponse(Call<List<Recurso>> call, Response<List<Recurso>> response) {
                if (response.isSuccessful()) {
                    final List<Recurso> pantallas_por_perfil = response.body();
                    if (pantallas_por_perfil != null && !pantallas_por_perfil.isEmpty()) {
                        *//**
         * runOnUiThread is used to update UI i.e Add Items to NavigationView Menu from this non-UI thread(AysncTask).
         *//*
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                *//**
         * params[0] refers to the first parameter passed to this method i.e a menu.
         * Add items to this menu by iterating the arrayList.
         *//*
                                //Menu drawerMenu = params[0];
                                for (int temp = 0; temp < pantallas_por_perfil.size(); temp++) {
                                    drawerMenu.add(0, 1, Menu.NONE, R.string.app_name).setIcon(R.drawable.ic_home_black_24dp);
                                    drawerMenu.add(pantallas_por_perfil.get(temp).getNombreRecurso());
                                }
                            }
                        });
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Recurso>> call, Throwable t) {

            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // LLena el menú con las opciones del menu del navigation drawer
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.item_perfil:
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = null;
                if (claseGlobalUsuario.getTipoUsuario().equals(Utilidades.USUARIO_PERSONA)) {
                    intent = new Intent(this.getApplicationContext(), PerfilUsuario.class);

                } else {
                    intent = new Intent(this.getApplicationContext(), PerfilEmpresa.class);
                }
                startActivity(intent);
                return true;
            case R.id.item_otra_opcion_salir:
                Intent intent_salir = new Intent(this, Inicio.class);
                startActivity(intent_salir);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        textView = (TextView) findViewById(R.id.text_view_titulo);
                        switch (menuItem.getItemId()) {
                            case R.id.item_inicio:
                                menuItem.setChecked(true);
                                textView.setText(menuItem.getTitle());
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_repositorio:
                                menuItem.setChecked(true);
                                textView.setText(menuItem.getTitle());
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_facturacion_electronica:
                                menuItem.setChecked(true);
                                textView.setText(menuItem.getTitle());
                                drawerLayout.closeDrawer(GravityCompat.START);
                                Intent intent = new Intent(getApplicationContext(), OpcionesFacturacion.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                                return true;
                            case R.id.item_opcion1:
                                menuItem.setChecked(true);
                                textView.setText(menuItem.getTitle());
                                drawerLayout.closeDrawer(GravityCompat.START);
                                Intent intent1 = new Intent(getApplicationContext(), PerfilUsuario.class);
                                startActivity(intent1);
                                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                                return true;
                            case R.id.item_opcion2:
                                menuItem.setChecked(true);
                                textView.setText(menuItem.getTitle());
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_salir:

                                finish();
                                return true;

                        }
                        return true;
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
