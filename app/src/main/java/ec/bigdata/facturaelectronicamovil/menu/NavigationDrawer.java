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

import java.util.ArrayList;
import java.util.List;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.modelo.Recurso;
import ec.bigdata.facturaelectronicamovil.pantalla.OpcionesFacturacion;
import ec.bigdata.facturaelectronicamovil.pantalla.OpcionesRepositorio;
import ec.bigdata.facturaelectronicamovil.pantalla.PerfilEmpresa;
import ec.bigdata.facturaelectronicamovil.pantalla.PerfilUsuario;
import ec.bigdata.facturaelectronicamovil.pantalla.Test;
import ec.bigdata.facturaelectronicamovil.recyclerview.AdministracionClientesActividad;
import ec.bigdata.facturaelectronicamovil.recyclerview.AdministracionProductosActividad;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestPantallas;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidad.PreferenciasUsuario;
import ec.bigdata.facturaelectronicamovil.utilidad.Utilidades;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NavigationDrawer extends AppCompatActivity {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private TextView textViewUsuarioActual;
    private TextView textViewTitulo;
    private ClaseGlobalUsuario claseGlobalUsuario;
    private Menu menu;
    private List<MenuItem> menuItems;
    private PreferenciasUsuario preferenciasUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();
        preferenciasUsuario = new PreferenciasUsuario(getApplicationContext());

        menuItems = new ArrayList<>();

        View view = findViewById(android.R.id.content);
        navigationView = (NavigationView) findViewById(R.id.navigation_view_menu);

        View header = navigationView.getHeaderView(0);

        textViewUsuarioActual = (TextView) header.findViewById(R.id.text_view_usuario_actual);

        textViewUsuarioActual.setText(claseGlobalUsuario.getNombreUsuario());

        toolbar = (Toolbar) findViewById(R.id.toolbar_navigation_drawer);
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
        menu = navigationView.getMenu();

        ClienteRestPantallas.ServicioPantallas servicioRecursos = ClienteRestPantallas.getServicioPantallas();
        final Call<List<Recurso>> listCall = servicioRecursos.obtenerPantallasPorPerfil(claseGlobalUsuario.getIdPerfil());
        listCall.enqueue(new Callback<List<Recurso>>() {
            @Override
            public void onResponse(Call<List<Recurso>> call, Response<List<Recurso>> response) {
                if (response.isSuccessful()) {
                    final List<Recurso> pantallasPorPerfil = response.body();
                    if (pantallasPorPerfil != null && !pantallasPorPerfil.isEmpty()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int temp = 0; temp < pantallasPorPerfil.size(); temp++) {
                                    menu.add(0, pantallasPorPerfil.get(temp).getIdRecurso(), Menu.NONE, pantallasPorPerfil.get(temp).getNombreRecurso()).setIcon(getResources().getIdentifier(pantallasPorPerfil.get(temp).getIcono(), "drawable", getPackageName()));
                                    menuItems.add(menu.getItem(temp));
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Recurso>> call, Throwable t) {

                call.cancel();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // LLena el menú con las opciones del menu del navigation drawer
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.item_perfil:
                drawerLayout.closeDrawer(GravityCompat.START);
                if (claseGlobalUsuario.getTipoUsuario().equals(Utilidades.USUARIO_RECEPTOR)) {
                    intent = new Intent(this.getApplicationContext(), PerfilUsuario.class);
                } else {
                    intent = new Intent(this.getApplicationContext(), PerfilEmpresa.class);
                }
                startActivity(intent);
                return true;
            case R.id.item_otra_opcion_salir:
                preferenciasUsuario.cerrarSesion();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        Intent intent = null;
                        textViewTitulo = (TextView) findViewById(R.id.text_view_titulo);
                        //int position = menuItems.indexOf(menuItem) + 1;
                        String titulo = menuItem.getTitle().toString();
                        switch (titulo) {
                            case "Inicio":
                                menuItem.setChecked(true);
                                // textViewTitulo.setText(menuItem.getTitle());
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case "Facturación Electrónica":
                                menuItem.setChecked(true);
                                //textViewTitulo.setText(menuItem.getTitle());
                                drawerLayout.closeDrawer(GravityCompat.START);
                                intent = new Intent(getApplicationContext(), OpcionesFacturacion.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                                return true;
                            case "Repositorio":
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                intent = new Intent(getApplicationContext(), OpcionesRepositorio.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                                return true;
                            case "Test":
                                menuItem.setChecked(true);
                                //textViewTitulo.setText(menuItem.getTitle());
                                drawerLayout.closeDrawer(GravityCompat.START);
                                intent = new Intent(getApplicationContext(), Test.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                                return true;
                            case "Clientes":
                                menuItem.setChecked(true);
                                // textViewTitulo.setText(menuItem.getTitle());
                                drawerLayout.closeDrawer(GravityCompat.START);
                                intent = new Intent(getApplicationContext(), AdministracionClientesActividad.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                                return true;
                            case "Productos":
                                menuItem.setChecked(true);
                                //textViewTitulo.setText(menuItem.getTitle());
                                intent = new Intent(getApplicationContext(), AdministracionProductosActividad.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case "Salir":
                                preferenciasUsuario.cerrarSesion();
                                return true;

                        }
                        return true;
                    }
                });
    }

}
