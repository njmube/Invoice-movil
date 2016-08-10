package ec.bigdata.facturaelectronicamovil.pantalla;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.personalizacion.MensajePersonalizado;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestCliente;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidad.Codigos;
import ec.bigdata.utilidades.Validaciones;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActualizacionCliente extends AppCompatActivity {

    private EditText editTextIdentificacionCliente;

    private EditText editTextNombreslCliente;

    private EditText editTextApellidosCliente;

    private EditText editTextRazonSocialCliente;

    private EditText editTextDireccionCliente;

    private EditText editTextCorreoCliente;

    private EditText editTextTelefonoCliente;

    private Switch switchEstadoCliente;

    private Toolbar toolbar;

    private Button buttonActualizarCliente;

    private Button buttonCorreosAdicionales;

    private Button buttonContinuar;

    private boolean validadoIdentificacionCliente;

    private boolean validadoCorreoCliente;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private ClienteRestCliente.ServicioCliente servicioCliente;

    private ec.bigdata.facturaelectronicamovil.modelo.Cliente clienteSeleccionado;

    private boolean clienteActualizado;

    private String identificacionCliente;
    private String nombre;
    private String apellido;
    private String razonSocial;
    private String direccion;
    private String correo;
    private String telefono;
    private boolean estadoCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizacion_cliente);

        editTextIdentificacionCliente = (EditText) findViewById(R.id.edit_text_identificacion_edicion_cliente);
        editTextNombreslCliente = (EditText) findViewById(R.id.edit_text_nombres_edicion_cliente);
        editTextApellidosCliente = (EditText) findViewById(R.id.edit_text_apellidos_edicion_cliente);
        editTextRazonSocialCliente = (EditText) findViewById(R.id.edit_text_razon_social_edicion_cliente);
        editTextDireccionCliente = (EditText) findViewById(R.id.edit_text_direccion_edicion_cliente);
        editTextCorreoCliente = (EditText) findViewById(R.id.edit_text_correo_edicion_cliente);
        editTextTelefonoCliente = (EditText) findViewById(R.id.edit_text_telefono_edicion_cliente);
        switchEstadoCliente = (Switch) findViewById(R.id.switch_estado_cliente);
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

            switchEstadoCliente.setChecked(clienteSeleccionado.isEstadoCliente());
            if (clienteSeleccionado.isEstadoCliente()) {
                switchEstadoCliente.setTextOn("ACTIVO");
            } else {
                switchEstadoCliente.setTextOff("ACTIVO");
            }
            estadoCliente = clienteSeleccionado.isEstadoCliente();
            if (clienteSeleccionado.getTipoCliente()) {//Cliente true, Proveedor false
                editTextNombreslCliente.setText(clienteSeleccionado.getNombreCliente());
                editTextApellidosCliente.setText(clienteSeleccionado.getApellidoCliente());
                editTextNombreslCliente.setVisibility(View.VISIBLE);
                editTextApellidosCliente.setVisibility(View.VISIBLE);
            } else {
                editTextRazonSocialCliente.setText(clienteSeleccionado.getRazonSocialCliente());
                editTextRazonSocialCliente.setVisibility(View.VISIBLE);
            }

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
                Intent intent = new Intent(getApplicationContext(), getCallingActivity().getClass());
                intent.putExtra(String.valueOf(Codigos.CODIGO_CLIENTE_ACTUALIZADO), clienteSeleccionado);
                if (clienteActualizado) {
                    setResult(RESULT_OK, intent);
                } else {
                    setResult(RESULT_CANCELED, intent);
                }
                finish();
            }
        });

        switchEstadoCliente.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    estadoCliente = Boolean.TRUE;
                    switchEstadoCliente.setTextOn("ACTIVO");
                } else {
                    estadoCliente = Boolean.FALSE;
                    switchEstadoCliente.setTextOn("INACTIVO");
                }
            }
        });
        buttonCorreosAdicionales = (Button) findViewById(R.id.button_correos_adicionales_cliente);
        buttonCorreosAdicionales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CorreosAdicionalesCliente.class);
                intent.putExtra(String.valueOf(Codigos.CODIGO_CLIENTE_SELECCIONADO), clienteSeleccionado);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
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
                                MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ADVERTENCIA, "La identificación está siendo usada por otro cliente.");

                            } else {
                                validadoIdentificacionCliente = true;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ec.bigdata.facturaelectronicamovil.modelo.Cliente> call, Throwable t) {
                        call.cancel();
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

                                MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ADVERTENCIA, "El correo electrónico está siendo usada por otro cliente.");

                            } else {
                                validadoCorreoCliente = true;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ec.bigdata.facturaelectronicamovil.modelo.Cliente> call, Throwable t) {
                        call.cancel();
                    }
                });
            } else {
                validadoCorreoCliente = true;
            }
        }

        return validadoCorreoCliente;
    }

    private void actualizarCliente() {
        identificacionCliente = "";
        nombre = "";
        apellido = "";
        razonSocial = "";
        direccion = "";
        correo = "";
        telefono = "";
        if (validarIdentificacion() && validarCorreoPrincipal()) {

            identificacionCliente = editTextIdentificacionCliente.getText().toString();
            correo = editTextCorreoCliente.getText().toString();

            if (clienteSeleccionado.getTipoCliente()) {
                if (!editTextNombreslCliente.getText().toString().trim().equals("")) {
                    nombre = editTextNombreslCliente.getText().toString();
                } else {
                    nombre = clienteSeleccionado.getNombreCliente();
                }

                if (!editTextApellidosCliente.getText().toString().trim().equals("")) {
                    apellido = editTextApellidosCliente.getText().toString();
                } else {
                    apellido = clienteSeleccionado.getApellidoCliente();
                }

            } else {

                if (!editTextRazonSocialCliente.getText().toString().trim().equals("")) {
                    razonSocial = editTextRazonSocialCliente.getText().toString();
                } else {
                    razonSocial = clienteSeleccionado.getRazonSocialCliente();
                }
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
                    servicioCliente.actualizarCliente(clienteSeleccionado);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        String contenido = null;
                        try {
                            contenido = new String(response.body().bytes());
                            JsonObject jsonObject = new JsonParser().parse(contenido).getAsJsonObject();
                            if (jsonObject.get("estado").getAsBoolean() == true) {
                                clienteActualizado = true;
                                clienteSeleccionado.setIdentificacionCliente(identificacionCliente);
                                clienteSeleccionado.setCorreoElectronicoCliente(correo);
                                clienteSeleccionado.setDireccionCliente(direccion);
                                clienteSeleccionado.setTelefonoCliente(telefono);
                                clienteSeleccionado.setEstadoCliente(estadoCliente);
                                if (clienteSeleccionado.getTipoCliente()) {
                                    clienteSeleccionado.setNombreCliente(nombre);
                                    clienteSeleccionado.setApellidoCliente(apellido);
                                } else {
                                    clienteSeleccionado.setRazonSocialCliente(razonSocial);
                                }

                                MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_INFORMACION, "Cliente actualizado.");

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    call.cancel();
                    MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, "Error al actualizar el cliente.");


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
