package ec.bigdata.facturaelectronicamovil.pantalla;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.adaptador.ArrayAdapterProducto;
import ec.bigdata.facturaelectronicamovil.personalizacion.MensajePersonalizado;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestProducto;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidad.Codigos;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Producto extends AppCompatActivity {

    private Toolbar toolbar;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private ClienteRestProducto.ServicioProducto servicioProducto;

    private ArrayAdapterProducto arrayAdapterProducto;

    private AutoCompleteTextView autoCompleteTextViewProductos;

    private ec.bigdata.facturaelectronicamovil.modelo.Producto productoSeleccionado;

    private Button buttonNuevoProducto;

    private Button buttonActualizarProducto;

    private Button buttonContinuar;

    private TextView textViewProductoSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        toolbar = (Toolbar) findViewById(R.id.toolbar_compuesta);
        setSupportActionBar(toolbar);
        //Get a support ActionBar corresponding to this toolbar_compuesta

        //Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar);
        tituloToolbar.setText(getResources().getString(R.string.titulo_producto));
        autoCompleteTextViewProductos = (AutoCompleteTextView) findViewById(R.id.auto_complete_text_view_busqueda_producto);
        textViewProductoSeleccionado = (TextView) findViewById(R.id.text_view_producto_seleccionado);
        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();
        servicioProducto = ClienteRestProducto.getServicioProducto();
        Call<List<ec.bigdata.facturaelectronicamovil.modelo.Producto>> call_producto = servicioProducto.obtenerProductosPorEmpresaAsociado(claseGlobalUsuario.getIdEmpresa());
        call_producto.enqueue(new Callback<List<ec.bigdata.facturaelectronicamovil.modelo.Producto>>() {
            @Override
            public void onResponse(Call<List<ec.bigdata.facturaelectronicamovil.modelo.Producto>> call, Response<List<ec.bigdata.facturaelectronicamovil.modelo.Producto>> response) {
                if (response.isSuccessful()) {
                    List<ec.bigdata.facturaelectronicamovil.modelo.Producto> productos = response.body();
                    if (productos != null && !productos.isEmpty()) {
                        //Se asigna el origen de datos
                        arrayAdapterProducto = new ArrayAdapterProducto(getApplicationContext(), R.layout.activity_cliente, R.id.tv_cliente_filtrado, productos);
                        //Se setea el adaptador
                        autoCompleteTextViewProductos.setAdapter(arrayAdapterProducto);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ec.bigdata.facturaelectronicamovil.modelo.Producto>> call, Throwable t) {

            }
        });
        autoCompleteTextViewProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                productoSeleccionado = (ec.bigdata.facturaelectronicamovil.modelo.Producto) parent.getItemAtPosition(position);
                if (productoSeleccionado != null) {
                    textViewProductoSeleccionado.setText(productoSeleccionado.getCodigoPrincipalProducto() + "-" + productoSeleccionado.getDescripcionProducto());
                }
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
            }
        });
        buttonNuevoProducto = (Button) findViewById(R.id.button_nuevo_producto);
        buttonNuevoProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNuevoProducto = new Intent(getApplicationContext(), NuevoProducto.class);
                startActivity(intentNuevoProducto);
                finish();
            }
        });
        buttonActualizarProducto = (Button) findViewById(R.id.button_editar_producto);
        buttonActualizarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productoSeleccionado != null) {
                    Intent intent = new Intent(getApplicationContext(), ActualizacionProducto.class);
                    intent.putExtra(String.valueOf(Codigos.CODIGO_PRODUCTO_SELECCIONADO), productoSeleccionado);
                    startActivity(intent);
                    finish();
                } else {

                    MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, "Debe seleccionar un producto.");
                }
            }
        });

        buttonContinuar = (Button) toolbar.findViewById(R.id.button_continuar);
        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InicioFacturacionElectronica.class);
                intent.putExtra(String.valueOf(Codigos.CODIGO_PRODUCTO_SELECCIONADO), productoSeleccionado);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }
}
