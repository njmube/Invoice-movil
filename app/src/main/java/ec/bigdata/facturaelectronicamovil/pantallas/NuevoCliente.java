package ec.bigdata.facturaelectronicamovil.pantallas;

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
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestCliente;
import ec.bigdata.facturaelectronicamovil.utilidades.ClaseGlobalUsuario;
import ec.bigdata.utilidades.Validaciones;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NuevoCliente extends AppCompatActivity {

    private static final String TAG = NuevoCliente.class.getSimpleName();

    private EditText editTextIdentificacionCliente;

    private EditText editTextRazonSocialNombresApellidosCliente;

    private EditText editTextDireccionCliente;

    private EditText editTextCorreoCliente;

    private EditText editTextTelefonoCliente;

    private Toolbar toolbar;

    private Button buttonNuevoCliente;

    private Button buttonContinuar;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private ClienteRestCliente.ServicioCliente servicioCliente;

    private NuevoClienteAsyncTask nuevoClienteAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_cliente);

        editTextIdentificacionCliente = (EditText) findViewById(R.id.edit_text_identificacion_nuevo_cliente);
        editTextRazonSocialNombresApellidosCliente = (EditText) findViewById(R.id.edit_text_nombres_razon_social_nuevo_cliente);
        editTextDireccionCliente = (EditText) findViewById(R.id.edit_text_direccion_nuevo_cliente);
        editTextCorreoCliente = (EditText) findViewById(R.id.edit_text_correo_nuevo_cliente);
        editTextTelefonoCliente = (EditText) findViewById(R.id.edit_text_telefono_nuevo_cliente);
        buttonNuevoCliente = (Button) findViewById(R.id.button_nuevo_cliente);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                String errores = "";
                nuevoClienteAsyncTask = new NuevoClienteAsyncTask();
                Boolean validadaIdentificacion = Boolean.FALSE;
                Boolean validadoCorreo = Boolean.FALSE;
                String identificacion = editTextIdentificacionCliente.getText().toString();
                if (ec.bigdata.facturaelectronicamovil.utilidades.Utilidades.validarIdentificacion(identificacion)) {
                    try {
                        validadaIdentificacion = nuevoClienteAsyncTask.execute(new String[]{identificacion, "1"}).get();
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
                    String nombres = "";
                    String direccion = "";
                    String telefono = "";
                    String correo = "";

                    if (!editTextRazonSocialNombresApellidosCliente.getText().toString().trim().equals("")) {
                        nombres = editTextRazonSocialNombresApellidosCliente.getText().toString();
                    } else {
                        errores = errores.concat("La razón social/nombres y apellidos es obligatorio.");
                    }
                    if (!editTextDireccionCliente.getText().toString().trim().equals("")) {
                        direccion = editTextDireccionCliente.getText().toString();
                    }
                    if (!editTextTelefonoCliente.getText().toString().trim().equals("")) {
                        telefono = editTextTelefonoCliente.getText().toString();
                    }
                    correo = editTextCorreoCliente.getText().toString();
                    if (Validaciones.isEmail(correo)) {
                        try {
                            validadoCorreo = nuevoClienteAsyncTask.execute(new String[]{correo, "2"}).get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    } else {
                        errores = errores.concat("El correo electrónico no es válido.");
                    }
                    if (errores.equals("") && validadoCorreo) {

                        Call<ResponseBody> call = servicioCliente.guardarCliente(identificacion, claseGlobalUsuario.getIdEmpresa(), nombres, direccion, telefono, correo);
                        call.enqueue(new Callback<ResponseBody>() {
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
                                            Toast.makeText(NuevoCliente.this, "Cliente guardado.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(NuevoCliente.this, errores, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(NuevoCliente.this, errores, Toast.LENGTH_SHORT).show();
                }
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
            Call<ec.bigdata.facturaelectronicamovil.modelo.Cliente> call_cliente = null;
            if (metodo.equals("1")) {
                call_cliente = servicioCliente.obtenerClientePorIdentificacionYEmpresaAsociado(claseGlobalUsuario.getIdEmpresa(), valor);
            } else {
                call_cliente = servicioCliente.obtenerClientePorCorreoPrincipalYPorEmpresaAsociado(claseGlobalUsuario.getIdEmpresa(), valor);
            }

            try {
                Response<ec.bigdata.facturaelectronicamovil.modelo.Cliente> clienteResponse = call_cliente.execute();
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
                    Toast.makeText(NuevoCliente.this, errores, Toast.LENGTH_SHORT).show();
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
