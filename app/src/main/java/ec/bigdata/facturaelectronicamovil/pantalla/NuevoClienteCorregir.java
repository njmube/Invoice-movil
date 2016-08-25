package ec.bigdata.facturaelectronicamovil.pantalla;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.anotacion.Identificacion;
import ec.bigdata.facturaelectronicamovil.personalizacion.MensajePersonalizado;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestCliente;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NuevoClienteCorregir extends AppCompatActivity implements Validator.ValidationListener {

    private static final String TAG = NuevoClienteCorregir.class.getSimpleName();
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

    private Button buttonNuevoCliente;

    private Button buttonContinuar;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private ClienteRestCliente.ServicioCliente servicioCliente;

    private NuevoClienteAsyncTask nuevoClienteAsyncTask;

    private Validator validator;

    private String identificacionCliente;
    private String razonSocial;
    private String direccion;
    private String correo;
    private String telefono;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_cliente);

        validator = new Validator(this);
        validator.setValidationListener(this);

        editTextIdentificacionCliente = (EditText) findViewById(R.id.edit_text_identificacion_nuevo_cliente);
        editTextRazonSocialCliente = (EditText) findViewById(R.id.edit_text_nombres_razon_social_nuevo_cliente);
        editTextDireccionCliente = (EditText) findViewById(R.id.edit_text_direccion_nuevo_cliente);
        editTextCorreoCliente = (EditText) findViewById(R.id.edit_text_correo_nuevo_cliente);
        editTextTelefonoCliente = (EditText) findViewById(R.id.edit_text_telefono_nuevo_cliente);
        buttonNuevoCliente = (Button) findViewById(R.id.button_nuevo_cliente);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_compuesta);
        setSupportActionBar(toolbar);

        buttonContinuar = (Button) toolbar.findViewById(R.id.button_continuar);
        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Cliente.class);
                startActivity(intent);
                finish();
            }
        });

        //Get a support ActionBar corresponding to this toolbar_compuesta

        //Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar);
        tituloToolbar.setText(getResources().getString(R.string.titulo_nuevo_cliente));
        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();
        servicioCliente = ClienteRestCliente.getServicioCliente();

        buttonNuevoCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validator.validate();

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

    @Override
    public void onValidationSucceeded() {
        String errores = "";
        nuevoClienteAsyncTask = new NuevoClienteAsyncTask();
        Boolean validadaIdentificacion = Boolean.FALSE;
        Boolean validadoCorreo = Boolean.FALSE;
        identificacionCliente = editTextIdentificacionCliente.getText().toString();
        razonSocial = editTextRazonSocialCliente.getText().toString();
        direccion = editTextDireccionCliente.getText().toString();
        telefono = editTextTelefonoCliente.getText().toString();
        correo = editTextCorreoCliente.getText().toString();
        if (ec.bigdata.facturaelectronicamovil.utilidad.Utilidades.validarIdentificacion(identificacionCliente)) {
            try {
                validadaIdentificacion = nuevoClienteAsyncTask.execute(new String[]{identificacionCliente, "1"}).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else {

            errores = errores.concat("La identificación no es válida.");
        }
        if (validadaIdentificacion) {
            nuevoClienteAsyncTask = new NuevoClienteAsyncTask();
            try {
                validadoCorreo = nuevoClienteAsyncTask.execute(new String[]{correo, "2"}).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if (errores.equals("") && validadoCorreo) {

                Call<ResponseBody> call = servicioCliente.guardarCliente(identificacionCliente, claseGlobalUsuario.getIdEmpresa(), razonSocial, direccion, telefono, correo);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if (response.isSuccessful()) {
                            JsonParser parser = new JsonParser();
                            JsonObject o = null;
                            try {
                                String s = new String(response.body().bytes());
                                o = parser.parse(s).getAsJsonObject();

                                if (o.get("estado").getAsBoolean() == true) {

                                    MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_INFORMACION, "Cliente guardado.");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        } else {
                            try {
                                Log.e(TAG, response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            } else {
                MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, errores);
            }

        } else {

            MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, errores);
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, message);
            }
        }
    }

    private class NuevoClienteAsyncTask extends AsyncTask<String, Integer, Boolean> {


        private String errores;

        public NuevoClienteAsyncTask() {
            this.errores = "";
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean validado = false;
            String valor = params[0];
            String metodo = params[1];
            Call<ec.bigdata.facturaelectronicamovil.modelo.Cliente> callCliente = null;
            if (metodo.equals("1")) {
                callCliente = servicioCliente.obtenerClientePorIdentificacionYEmpresaAsociado(claseGlobalUsuario.getIdEmpresa(), valor);
            } else {
                callCliente = servicioCliente.obtenerClientePorCorreoPrincipalYPorEmpresaAsociado(claseGlobalUsuario.getIdEmpresa(), valor);
            }

            try {
                Response<ec.bigdata.facturaelectronicamovil.modelo.Cliente> clienteResponse = callCliente.execute();
                if (clienteResponse.isSuccessful()) {

                    ec.bigdata.facturaelectronicamovil.modelo.Cliente cliente = clienteResponse.body();
                    if (cliente != null) {
                        if (metodo.equals("1")) {
                            errores = errores.concat("La identificación está siendo usada por otro cliente.");
                        } else {
                            errores = errores.concat("El correo electrónico está siendo usado por otro cliente.");
                        }
                    } else {
                        validado = true;
                    }
                } else {
                    ResponseBody responseBody = clienteResponse.errorBody();
                    Log.e(TAG, responseBody.string());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return validado;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result.equals(Boolean.FALSE)) {
                if (!errores.equals("")) {

                    MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, errores);
                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            nuevoClienteAsyncTask = null;

        }

    }
}
