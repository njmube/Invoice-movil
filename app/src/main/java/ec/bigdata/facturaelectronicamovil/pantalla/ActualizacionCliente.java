package ec.bigdata.facturaelectronicamovil.pantalla;

import android.app.ProgressDialog;
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
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.IOException;
import java.util.List;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.anotacion.Identificacion;
import ec.bigdata.facturaelectronicamovil.dialogs.DialogProgreso;
import ec.bigdata.facturaelectronicamovil.modelo.Cliente;
import ec.bigdata.facturaelectronicamovil.personalizacion.MensajePersonalizado;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestCliente;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidad.Codigos;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActualizacionCliente extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty(message = "La identificación no puede estar vacía.")
    @Identificacion
    private EditText editTextIdentificacionCliente;

    @NotEmpty(message = "La razón social/nombre no puede estar vacía.")
    private EditText editTextRazonSocialCliente;

    @NotEmpty(message = "La dirección no puede estar vacía.")
    private EditText editTextDireccionCliente;

    @NotEmpty(message = "El correo electrónico no puede estar vacío.")
    @Email(message = "El correo electrónico no es válido")
    private EditText editTextCorreoCliente;

    @NotEmpty(message = "El teléfono no puede estar vacío")
    private EditText editTextTelefonoCliente;

    private Switch switchEstadoCliente;

    private Button buttonActualizarCliente;

    private Button buttonCorreosAdicionales;

    private boolean validadoIdentificacionCliente;

    private boolean validadoCorreoCliente;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private ClienteRestCliente.ServicioCliente servicioCliente;

    private ec.bigdata.facturaelectronicamovil.modelo.Cliente clienteSeleccionado;

    private ec.bigdata.facturaelectronicamovil.modelo.Cliente clienteActualizado;

    private boolean actualizacionExitosaCliente;

    private Validator validator;

    private String identificacionCliente;

    private ProgressDialog progressDialog;

    private String razonSocial;

    private String direccion;

    private String correo;

    private String telefono;

    private boolean estadoCliente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizacion_cliente);

        razonSocial = "";
        validator = new Validator(this);
        validator.setValidationListener(this);
        Validator.registerAnnotation(Identificacion.class);

        editTextIdentificacionCliente = (EditText) findViewById(R.id.edit_text_identificacion_edicion_cliente);
        editTextRazonSocialCliente = (EditText) findViewById(R.id.edit_text_razon_social_edicion_cliente);
        editTextDireccionCliente = (EditText) findViewById(R.id.edit_text_direccion_edicion_cliente);
        editTextCorreoCliente = (EditText) findViewById(R.id.edit_text_correo_edicion_cliente);
        editTextTelefonoCliente = (EditText) findViewById(R.id.edit_text_telefono_edicion_cliente);
        switchEstadoCliente = (Switch) findViewById(R.id.switch_estado_cliente);
        buttonActualizarCliente = (Button) findViewById(R.id.button_actualizar_cliente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_simple);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar_simple);
        tituloToolbar.setText(getResources().getString(R.string.titulo_actualizar_cliente));
        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();
        servicioCliente = ClienteRestCliente.getServicioCliente();
        //Se recupera el cliente enviado
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            clienteSeleccionado = (ec.bigdata.facturaelectronicamovil.modelo.Cliente) bundle.getSerializable(String.valueOf(Codigos.CODIGO_CLIENTE_SELECCIONADO));
            editTextIdentificacionCliente.setText(clienteSeleccionado.getIdentificacionCliente());

            switchEstadoCliente.setChecked(clienteSeleccionado.isEstadoCliente());

            switchEstadoCliente.setTextOn("ACTIVO");

            switchEstadoCliente.setTextOff("INACTIVO");

            estadoCliente = clienteSeleccionado.isEstadoCliente();
            editTextRazonSocialCliente.setText(clienteSeleccionado.getRazonSocialCliente());
            editTextRazonSocialCliente.setVisibility(View.VISIBLE);


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

        switchEstadoCliente.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    estadoCliente = Boolean.TRUE;

                } else {
                    estadoCliente = Boolean.FALSE;

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
        identificacionCliente = editTextIdentificacionCliente.getText().toString();
        if (!clienteSeleccionado.getIdentificacionCliente().equals(identificacionCliente)) {
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


        return validadoIdentificacionCliente;
    }

    private boolean validarCorreoPrincipal() {
        validadoCorreoCliente = false;
        correo = editTextCorreoCliente.getText().toString();
        if (!clienteSeleccionado.getCorreoElectronicoCliente().equals(correo)) {
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


        return validadoCorreoCliente;
    }

    private void actualizarCliente() {
        clienteActualizado = new Cliente();
        if (validarIdentificacion() && validarCorreoPrincipal()) {
            progressDialog = DialogProgreso.mostrarDialogProgreso(ActualizacionCliente.this);
            razonSocial = editTextRazonSocialCliente.getText().toString();

            direccion = editTextDireccionCliente.getText().toString();

            telefono = editTextTelefonoCliente.getText().toString();

            //Se ponen los valores del cliente en un nuevo objeto a actualizar.
            clienteActualizado.setIdCliente(clienteSeleccionado.getIdCliente());
            clienteActualizado.setIdentificacionCliente(identificacionCliente);
            clienteActualizado.setCorreoElectronicoCliente(correo);
            clienteActualizado.setDireccionCliente(direccion);
            clienteActualizado.setTelefonoCliente(telefono);
            clienteActualizado.setEstadoCliente(estadoCliente);

            clienteActualizado.setRazonSocialCliente(razonSocial);


            Call<ResponseBody> responseBodyCall =
                    servicioCliente.actualizarCliente(clienteActualizado);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        String contenido = null;
                        try {
                            contenido = new String(response.body().bytes());
                            JsonObject jsonObject = new JsonParser().parse(contenido).getAsJsonObject();
                            if (jsonObject.get("estado").getAsBoolean() == true) {
                                actualizacionExitosaCliente = true;
                                clienteSeleccionado = clienteActualizado;
                                MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_INFORMACION, "Cliente actualizado.");
                                progressDialog.dismiss();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    call.cancel();
                    progressDialog.dismiss();
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
        Intent intent = new Intent(getApplicationContext(), getCallingActivity().getClass());
        intent.putExtra(String.valueOf(Codigos.CODIGO_CLIENTE_SELECCIONADO), clienteSeleccionado);
        setResult(RESULT_OK, intent);
        this.finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    @Override
    public void onValidationSucceeded() {

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getApplicationContext());

            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, message);
            }
        }
    }
}
