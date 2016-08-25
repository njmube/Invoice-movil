package ec.bigdata.facturaelectronicamovil.pantalla;

import android.app.ProgressDialog;
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
import ec.bigdata.facturaelectronicamovil.adaptador.ArrayAdapterCliente;
import ec.bigdata.facturaelectronicamovil.dialogs.DialogProgreso;
import ec.bigdata.facturaelectronicamovil.personalizacion.MensajePersonalizado;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestCliente;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidad.Codigos;
import ec.bigdata.facturaelectronicamovil.utilidad.Utilidades;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cliente extends AppCompatActivity {

    private Button buttonNuevoCliente;

    private Button buttonActualizarCliente;

    private Button buttonContinuar;

    private TextView textViewClienteSeleccionado;

    private ProgressDialog progressDialog;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private ClienteRestCliente.ServicioCliente servicioCliente;

    private ArrayAdapterCliente arrayAdapterCliente;

    private AutoCompleteTextView autoCompleteTextViewClientes;

    private ec.bigdata.facturaelectronicamovil.modelo.Cliente clienteSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_compuesta);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar);
        tituloToolbar.setText(getResources().getString(R.string.titulo_cliente));
        autoCompleteTextViewClientes = (AutoCompleteTextView) findViewById(R.id.auto_complete_text_view_cliente);
        textViewClienteSeleccionado = (TextView) findViewById(R.id.text_view_cliente_seleccionado);
        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();
        servicioCliente = ClienteRestCliente.getServicioCliente();
        progressDialog = DialogProgreso.mostrarDialogProgreso(Cliente.this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(String.valueOf(Codigos.CODIGO_CLIENTE_SELECCIONADO))) {
                clienteSeleccionado = (ec.bigdata.facturaelectronicamovil.modelo.Cliente) bundle.getSerializable(String.valueOf(Codigos.CODIGO_CLIENTE_SELECCIONADO));
                textViewClienteSeleccionado.setText(clienteSeleccionado.getIdentificacionCliente() + " " + Utilidades.obtenerNombreCliente(clienteSeleccionado));
            }
        }

        Call<List<ec.bigdata.facturaelectronicamovil.modelo.Cliente>> listCall = servicioCliente.obtenerClientesPorEmpresaAsociado(claseGlobalUsuario.getIdEmpresa(), 0, 100);
        listCall.enqueue(new Callback<List<ec.bigdata.facturaelectronicamovil.modelo.Cliente>>() {
            @Override
            public void onResponse(Call<List<ec.bigdata.facturaelectronicamovil.modelo.Cliente>> call, Response<List<ec.bigdata.facturaelectronicamovil.modelo.Cliente>> response) {
                if (response.isSuccessful()) {
                    List<ec.bigdata.facturaelectronicamovil.modelo.Cliente> clientes = response.body();
                    if (clientes != null && !clientes.isEmpty()) {
                        //Se asigna el origen de datos
                        arrayAdapterCliente = new ArrayAdapterCliente(getApplicationContext(), R.layout.activity_cliente, R.id.text_view_filtrado, clientes);
                        //Se setea el adaptador
                        autoCompleteTextViewClientes.setAdapter(arrayAdapterCliente);
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<ec.bigdata.facturaelectronicamovil.modelo.Cliente>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

        autoCompleteTextViewClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clienteSeleccionado = (ec.bigdata.facturaelectronicamovil.modelo.Cliente) parent.getItemAtPosition(position);
                if (clienteSeleccionado != null) {
                    textViewClienteSeleccionado.setText(clienteSeleccionado.getIdentificacionCliente() + " " + Utilidades.obtenerNombreCliente(clienteSeleccionado));
                }
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
            }
        });

        buttonActualizarCliente = (Button) findViewById(R.id.button_editar_cliente);
        buttonActualizarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clienteSeleccionado != null) {
                    Intent intent = new Intent(getApplicationContext(), ActualizacionCliente.class);
                    intent.putExtra(String.valueOf(Codigos.CODIGO_CLIENTE_SELECCIONADO), clienteSeleccionado);
                    startActivityForResult(intent, Codigos.CODIGO_CLIENTE_ACTUALIZADO);
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                } else {
                    MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ADVERTENCIA, "Debe seleccionar un cliente.");
                }
            }
        });

        buttonNuevoCliente = (Button) findViewById(R.id.button_nuevo_cliente);
        buttonNuevoCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NuevoCliente.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

        buttonContinuar = (Button) toolbar.findViewById(R.id.button_continuar);
        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InicioFacturacionElectronica.class);
                if (clienteSeleccionado != null) {
                    intent.putExtra(String.valueOf(Codigos.CODIGO_CLIENTE_SELECCIONADO), clienteSeleccionado);
                    setResult(RESULT_OK, intent);
                } else {
                    setResult(RESULT_CANCELED, intent);
                }
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
        Intent intent = new Intent(getApplicationContext(), InicioFacturacionElectronica.class);
        if (clienteSeleccionado != null) {
            intent.putExtra(String.valueOf(Codigos.CODIGO_CLIENTE_SELECCIONADO), clienteSeleccionado);
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED, intent);
        }
        this.finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Codigos.CODIGO_CLIENTE_ACTUALIZADO) {
            if (resultCode == RESULT_OK) {
                if (data.hasExtra(String.valueOf(Codigos.CODIGO_CLIENTE_SELECCIONADO))) {

                    clienteSeleccionado = (ec.bigdata.facturaelectronicamovil.modelo.Cliente) data.getExtras().getSerializable(String.valueOf(Codigos.CODIGO_CLIENTE_SELECCIONADO));
                    if (clienteSeleccionado != null) {
                        textViewClienteSeleccionado.setText(clienteSeleccionado.getIdentificacionCliente() + " " + Utilidades.obtenerNombreCliente(clienteSeleccionado));
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                textViewClienteSeleccionado.setText(getResources().getString(R.string.etiqueta_valor_noseleccionado));

            }
        }

    }
}
