package ec.bigdata.facturaelectronicamovil.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.modelo.Producto;
import ec.bigdata.facturaelectronicamovil.pantalla.ActualizacionProducto;
import ec.bigdata.facturaelectronicamovil.pantalla.NuevoProducto;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidad.Codigos;
import ec.bigdata.facturaelectronicamovil.utilidad.Utilidades;

public class AdministracionProductosActividad extends AppCompatActivity {

    private AdministracionProductosFragmento administracionProductosFragmento;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private FloatingActionButton floatingActionButton;
    private ClaseGlobalUsuario claseGlobalUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_adm_productos);
        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floating_action_button_ir_inicio);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_simple);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar_simple);

        tituloToolbar.setText(getResources().getString(R.string.titulo_administracion_productos));

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        administracionProductosFragmento = new AdministracionProductosFragmento();
        fragmentTransaction.replace(R.id.frame_layout_adm_productos, administracionProductosFragmento, "AdmProductosFragment");
        fragmentTransaction.commit();
    }

    public void editarProducto(Producto productoSeleccionado) {
        Intent intent = new Intent(this, ActualizacionProducto.class);
        intent.putExtra(String.valueOf(Codigos.CODIGO_PRODUCTO_SELECCIONADO), productoSeleccionado);
        startActivityForResult(intent, Codigos.CODIGO_ADM_PRODUCTOS_SELECCIONADO);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    public void compartirProductos(List<Producto> productos) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Utilidades.formaterListaProductosACompartir(productos));
        startActivity(Intent.createChooser(sharingIntent, "Compartir información de productos"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nuevo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.item_nuevo:
                Intent intent = new Intent(this, NuevoProducto.class);
                startActivityForResult(intent, Codigos.CODIGO_PRODUCTO_NUEVO);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(administracionProductosFragmento);
        fragmentTransaction.commit();
        this.finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Codigos.CODIGO_ADM_PRODUCTOS_SELECCIONADO) {
            if (resultCode == RESULT_OK) {
                Producto producto = (Producto) data.getSerializableExtra(String.valueOf(Codigos.CODIGO_PRODUCTO_ACTUALIZADO));
                if (producto != null) {
                    administracionProductosFragmento.actualizarProductoRecyclerView(producto);
                }
            }
        } else if (requestCode == Codigos.CODIGO_PRODUCTO_NUEVO) {
            if (resultCode == RESULT_OK) {
                //TODO: Definir que se hace en el recyclerview cuando se registra un nuevo producto.
            }
        }
    }

    public Fragment obtenerFragment() {
        return administracionProductosFragmento;
    }
}
