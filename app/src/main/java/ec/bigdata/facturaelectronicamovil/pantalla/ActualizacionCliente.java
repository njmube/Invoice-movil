package ec.bigdata.facturaelectronicamovil.pantalla;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestCliente;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidad.Codigos;
import ec.bigdata.facturaelectronicamovil.utilidad.Personalizacion;
import ec.bigdata.utilidades.Validaciones;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActualizacionCliente extends AppCompatActivity {

    private EditText editTextIdentificacionCliente;

    private EditText editTextRazonSocialNombresApellidosCliente;

    private EditText editTextDireccionCliente;

    private EditText editTextCorreoCliente;

    private EditText editTextTelefonoCliente;

    private Toolbar toolbar;

    private Button buttonActualizarCliente;

    private Button buttonContinuar;

    private boolean validadoIdentificacionCliente;

    private boolean validadoCorreoCliente;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private ClienteRestCliente.ServicioCliente servicioCliente;

    private ec.bigdata.facturaelectronicamovil.modelo.Cliente clienteSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizacion_cliente);

        editTextIdentificacionCliente = (EditText) findViewById(R.id.edit_text_identificacion_edicion_cliente);
        editTextRazonSocialNombresApellidosCliente = (EditText) findViewById(R.id.edit_text_nombres_razon_social_edicion_cliente);
        editTextDireccionCliente = (EditText) findViewById(R.id.edit_text_direccion_edicion_cliente);
        editTextCorreoCliente = (EditText) findViewById(R.id.edit_text_correo_edicion_cliente);
        editTextTelefonoCliente = (EditText) findViewById(R.id.edit_text_telefono_edicion_cliente);
        buttonActualizarCliente = (Button) findViewById(R.id.button_actualizar_cliente);
        toolbar = (Toolbar) findViewById(R.id.toolbar_compuesta);
        setSupportActionBar(toolbar);

        //Get a support ActionBar corresponding to this toolbar_compuesta

        //Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar);
        tituloToolbar.setText(getResources().getString(R.string.titulo_actualizar_cliente));
        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();
        servicioCliente = ClienteRestCliente.getServicioCliente();
        //Se recupera el cliente enviado

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            clienteSeleccionado = (ec.bigdata.facturaelectronicamovil.modelo.Cliente) bundle.getSerializable(String.valueOf(Codigos.CODIGO_CLIENTE_SELECCIONADO));
            editTextIdentificacionCliente.setText(clienteSeleccionado.getIdentificacionCliente());
            editTextRazonSocialNombresApellidosCliente.setText(clienteSeleccionado.getNombreCliente());
            editTextDireccionCliente.setText(clienteSeleccionado.getDireccionCliente());
            editTextCorreoCliente.setText(clienteSeleccionado.getCorreoElectronicoCliente());
            editTextTelefonoCliente.setText(clienteSeleccionado.getTelefonoCliente());
        }
        buttonActualizarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarCliente();
            }
        });
        buttonContinuar = (Button) toolbar.findViewById(R.id.button_continuar);
        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Cliente.class);
                intent.putExtra(String.valueOf(Codigos.CODIGO_CLIENTE_SELECCIONADO_ACTUALIZADO), clienteSeleccionado);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private boolean validarIdentificacion() {
        validadoIdentificacionCliente = false;
        if (ec.bigdata.facturaelectronicamovil.utilidad.Utilidades.validarIdentificacion(editTextIdentificacionCliente.getText().toString())) {
            if (!clienteSeleccionado.getIdentificacionCliente().equals(editTextIdentificacionCliente.getText().toString())) {
                Call<ec.bigdata.facturaelectronicamovil.modelo.Cliente> call = servicioCliente.obtenerClientePorIdentificacionYEmpresaAsociado(claseGlobalUsuario.getIdEmpresa(), editTextIdentificacionCliente.getText().toString());
                call.enqueue(new Callback<ec.bigdata.facturaelectronicamovil.modelo.Cliente>() {
                    @Override
                    public void onResponse(Call<ec.bigdata.facturaelectronicamovil.modelo.Cliente> call, Response<ec.bigdata.facturaelectronicamovil.modelo.Cliente> response) {
                        if (response.isSuccessful()) {
                            ec.bigdata.facturaelectronicamovil.modelo.Cliente cliente = response.body();
                            if (cliente != null) {

                                Personalizacion.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), Personalizacion.TOAST_ADVERTENCIA, "La identificación está siendo usada por otro cliente");


                            } else {
                                validadoIdentificacionCliente = true;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ec.bigdata.facturaelectronicamovil.modelo.Cliente> call, Throwable t) {

                    }
                });
            } else {
                validadoIdentificacionCliente = true;
            }
        } else {
            validadoIdentificacionCliente = true;
        }

        return validadoIdentificacionCliente;
    }

    private boolean validarCorreoPrincipal() {
        validadoCorreoCliente = false;
        if (Validaciones.isEmail(editTextCorreoCliente.getText().toString())) {
            if (!clienteSeleccionado.getCorreoElectronicoCliente().equals(editTextCorreoCliente.getText().toString())) {
                Call<ec.bigdata.facturaelectronicamovil.modelo.Cliente> call = servicioCliente.obtenerClientePorCorreoPrincipalYPorEmpresaAsociado(claseGlobalUsuario.getIdEmpresa(), editTextCorreoCliente.getText().toString());
                call.enqueue(new Callback<ec.bigdata.facturaelectronicamovil.modelo.Cliente>() {
                    @Override
                    public void onResponse(Call<ec.bigdata.facturaelectronicamovil.modelo.Cliente> call, Response<ec.bigdata.facturaelectronicamovil.modelo.Cliente> response) {
                        if (response.isSuccessful()) {
                            ec.bigdata.facturaelectronicamovil.modelo.Cliente cliente = response.body();
                            if (cliente != null) {

                                Personalizacion.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), Personalizacion.TOAST_ADVERTENCIA, "El correo electrónico está siendo usada por otro cliente");

                            } else {
                                validadoCorreoCliente = true;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ec.bigdata.facturaelectronicamovil.modelo.Cliente> call, Throwable t) {

                    }
                });
            } else {
                validadoCorreoCliente = true;
            }
        }

        return validadoCorreoCliente;
    }

    private void actualizarCliente() {
        String identificacion_cliente = "";
        String nombres = "";
        String direccion = "";
        String correo = "";
        String telefono = "";
        if (validarIdentificacion() && validarCorreoPrincipal()) {

            identificacion_cliente = editTextIdentificacionCliente.getText().toString();
            correo = editTextCorreoCliente.getText().toString();

            if (!editTextRazonSocialNombresApellidosCliente.getText().toString().trim().equals("")) {
                nombres = editTextRazonSocialNombresApellidosCliente.getText().toString();
            } else {
                nombres = clienteSeleccionado.getNombreCliente();
            }
            if (!editTextDireccionCliente.getText().toString().trim().equals("")) {
                direccion = editTextDireccionCliente.getText().toString();
            } else {
                direccion = clienteSeleccionado.getDireccionCliente() != null && !clienteSeleccionado.getDireccionCliente().trim().equals("") ? clienteSeleccionado.getDireccionCliente() : "";
            }

            if (!editTextTelefonoCliente.getText().toString().trim().equals("")) {
                telefono = editTextTelefonoCliente.getText().toString();
            } else {
                telefono = clienteSeleccionado.getTelefonoCliente() != null && !clienteSeleccionado.getTelefonoCliente().trim().equals("") ? clienteSeleccionado.getTelefonoCliente() : "";
            }
            Call<ResponseBody> responseBodyCall =
                    servicioCliente.actualizarCliente(clienteSeleccionado.getIdentificacionCliente(),
                            identificacion_cliente, claseGlobalUsuario.getIdEmpresa(), nombres, direccion, telefono, correo);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JsonParser parser = new JsonParser();
                        JsonObject o = null;
                        String s = null;
                        try {
                            s = new String(response.body().bytes());
                            o = parser.parse(s).getAsJsonObject();
                            if (o.get("status").getAsBoolean() == true) {
                                //TODO Refrescar objeto cliente actualizado.
                                Personalizacion.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), Personalizacion.TOAST_INFORMACION, "Cliente actualizado.");

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    Personalizacion.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), Personalizacion.TOAST_ERROR, "Error al actualizar el cliente.");


                }
            });
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
