package ec.bigdata.facturaelectronicamovil.pantallas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.utilidades.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidades.Codigos;

public class InformacionFactura extends AppCompatActivity {

    private Toolbar toolbar;

    ClaseGlobalUsuario claseGlobalUsuario;

    private Button buttonCliente;

    private Button buttonProducto;

    private Button buttonContinuar;

    private TextView textViewClienteSeleccionado;

    private TextView textViewProductoSeleccionado;

    private ec.bigdata.facturaelectronicamovil.modelo.Cliente cliente;

    private ec.bigdata.facturaelectronicamovil.modelo.Producto producto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_factura);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get a support ActionBar corresponding to this toolbar_compuesta

        //Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar);
        tituloToolbar.setText(getResources().getString(R.string.titulo_informacion_factura));

        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();

        buttonCliente = (Button) findViewById(R.id.button_cliente);
        buttonCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Cliente.class);
                startActivityForResult(intent, Codigos.CODIGO_VALIDACION_CLIENTE);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

        buttonProducto = (Button) findViewById(R.id.button_producto);
        buttonProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Producto.class);
                startActivityForResult(intent, Codigos.CODIGO_VALIDACION_PRODUCTO);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

        buttonContinuar = (Button) toolbar.findViewById(R.id.button_continuar);
        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InicioFacturacionElectronica.class);
                // intent.putExtra(String.valueOf(Codigos.CODIGO_VALIDACION_CLIENTE), cliente);
                //intent.putExtra(String.valueOf(Codigos.CODIGO_VALIDACION_PRODUCTO), producto);
                if (cliente != null && producto != null) {
                    claseGlobalUsuario.setClienteAFacturar(cliente);
                    claseGlobalUsuario.setProductoAFacturar(producto);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(InformacionFactura.this, "Debe seleccionar un cliente y un producto.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        textViewClienteSeleccionado = (TextView) findViewById(R.id.text_view_cliente_seleccionado);
        textViewProductoSeleccionado = (TextView) findViewById(R.id.text_view_producto_seleccionado);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Validación de información factura
        if (requestCode == Codigos.CODIGO_VALIDACION_CLIENTE) {
            if (resultCode == RESULT_OK) {
                cliente = (ec.bigdata.facturaelectronicamovil.modelo.Cliente) data.getExtras().getSerializable(String.valueOf(Codigos.CODIGO_CLIENTE_SELECCIONADO));
                if (cliente != null) {
                    textViewClienteSeleccionado.setText(cliente.getIdentificacionCliente() + cliente.getNombreCliente());
                }
            } else if (resultCode == RESULT_CANCELED) {
                textViewClienteSeleccionado.setText(getResources().getString(R.string.etiqueta_valor_nodeseleccionado));
            }
        }
        if (requestCode == Codigos.CODIGO_VALIDACION_PRODUCTO) {
            if (resultCode == RESULT_OK) {
                producto = (ec.bigdata.facturaelectronicamovil.modelo.Producto) data.getExtras().getSerializable(String.valueOf(Codigos.CODIGO_PRODUCTO_SELECCIONADO));
                if (producto != null) {
                    textViewProductoSeleccionado.setText(producto.getCodigoPrincipalProducto() + producto.getDescripcionProducto());
                }
            } else if (resultCode == RESULT_CANCELED) {
                textViewProductoSeleccionado.setText(getResources().getString(R.string.etiqueta_valor_nodeseleccionado));
            }
        }
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
