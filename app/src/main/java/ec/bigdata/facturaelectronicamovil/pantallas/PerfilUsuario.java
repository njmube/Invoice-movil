package ec.bigdata.facturaelectronicamovil.pantallas;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import ec.bigdata.facturaelectronicamovil.interfaces.ServicioUsuarioAcceso;
import ec.bigdata.facturaelectronicamovil.modelo.UsuarioAcceso;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestUsuarioAcceso;
import ec.bigdata.facturaelectronicamovil.utilidades.ClaseGlobalUsuario;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilUsuario extends AppCompatActivity implements Validator.ValidationListener {

    private static final String TAG = PerfilUsuario.class.getSimpleName();

    @NotEmpty(message = "El nombre es requerido.")
    private EditText editTextNombres;

    @NotEmpty(message = "El apellido es requerido.")
    private EditText editTextApellidos;

    @NotEmpty
    @Email(message = "El correo electrónico no es válido.")
    private EditText editTextCorreoPrincipal;

    @Email(message = "El correo electrónico no es válido.")
    private EditText editTextCorreoAdicional;

    private EditText editTextTelefonoPrincipal;

    private EditText editTextTelefonoAdicional;

    private Button buttonActualizarPerfil;

    ClaseGlobalUsuario claseGlobalUsuario;

    private Validator validator;
    private ServicioUsuarioAcceso clienteRestUsuarioAcceso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        editTextNombres = (EditText) findViewById(R.id.edit_text_nombres);
        editTextApellidos = (EditText) findViewById(R.id.edit_text_apellidos);
        editTextCorreoPrincipal = (EditText) findViewById(R.id.edit_text_correo_principal);
        editTextCorreoAdicional = (EditText) findViewById(R.id.edit_text_correo_adicional);
        editTextTelefonoPrincipal = (EditText) findViewById(R.id.edit_text_telefono_principal);
        editTextTelefonoAdicional = (EditText) findViewById(R.id.edit_text_telefono_adicional);
        buttonActualizarPerfil = (Button) findViewById(R.id.button_actualizar_perfil_usuario);
        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();
        editTextNombres.setText(claseGlobalUsuario.getNombres());
        editTextApellidos.setText(claseGlobalUsuario.getApellidos());
        editTextCorreoPrincipal.setText(claseGlobalUsuario.getCorreoPrincipal());
        editTextCorreoAdicional.setText(claseGlobalUsuario.getCorreoAdicional());
        editTextTelefonoPrincipal.setText(claseGlobalUsuario.getTelefonoPrincipal());
        editTextTelefonoAdicional.setText(claseGlobalUsuario.getTelefonoAdicional());


        clienteRestUsuarioAcceso = ClienteRestUsuarioAcceso.getServicioUsuarioAcceso();

        validator = new Validator(this);
        validator.setValidationListener(this);

        buttonActualizarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validator.validate();
            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        ActualizacionPerfilUsuarioAsyncTask actualizacionPerfilUsuarioAsyncTask = new ActualizacionPerfilUsuarioAsyncTask();
        boolean validadoCorreoPrincipal = Boolean.FALSE;
        boolean validadoCorreoSecundario = Boolean.FALSE;
        if (editTextCorreoPrincipal.getText().toString().equals(claseGlobalUsuario.getCorreoPrincipal())) {
            validadoCorreoPrincipal = Boolean.TRUE;
        } else {
            try {
                validadoCorreoPrincipal = actualizacionPerfilUsuarioAsyncTask.execute(editTextCorreoPrincipal.getText().toString()).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        if (!editTextCorreoAdicional.getText().toString().trim().equals("")) {
            if (editTextCorreoAdicional.getText().toString().equals(claseGlobalUsuario.getCorreoAdicional())) {
                validadoCorreoSecundario = Boolean.TRUE;
            } else {
                try {
                    validadoCorreoSecundario = actualizacionPerfilUsuarioAsyncTask.execute(editTextCorreoAdicional.getText().toString()).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        } else {
            validadoCorreoSecundario = Boolean.TRUE;
        }

        if (validadoCorreoPrincipal && validadoCorreoSecundario) {
            Call<ResponseBody> call = null;
            call = clienteRestUsuarioAcceso.actualizarUsuario(claseGlobalUsuario.getIdUsuario(), editTextNombres.getText().toString()
                    , editTextApellidos.getText().toString(), editTextCorreoPrincipal.getText().toString(), editTextCorreoAdicional.getText().toString(), editTextTelefonoPrincipal.getText().toString(), editTextTelefonoAdicional.getText().toString());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JsonParser parser = new JsonParser();
                        JsonObject o = null;
                        try {
                            String s = new String(response.body().bytes());
                            o = parser.parse(s).getAsJsonObject();

                            if (o.get("status").getAsBoolean() == true) {
                                claseGlobalUsuario.setNombres(editTextNombres.getText().toString());
                                claseGlobalUsuario.setApellidos(editTextApellidos.getText().toString());
                                claseGlobalUsuario.setCorreoPrincipal(editTextCorreoPrincipal.getText().toString());
                                claseGlobalUsuario.setCorreoAdicional(editTextCorreoAdicional.getText().toString());
                                claseGlobalUsuario.setTelefonoPrincipal(editTextTelefonoPrincipal.getText().toString());
                                claseGlobalUsuario.setTelefonoAdicional(editTextTelefonoAdicional.getText().toString());
                                Toast.makeText(PerfilUsuario.this, "Información de usuario actualizada.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(PerfilUsuario.this, "Error al actualizar el perfil.", Toast.LENGTH_SHORT).show();
                }
            });
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
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class ActualizacionPerfilUsuarioAsyncTask extends AsyncTask<String, Integer, Boolean> {

        private String errores;

        public ActualizacionPerfilUsuarioAsyncTask() {
            this.errores = "";
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean validado = false;
            String correo = params[0];
            Call<UsuarioAcceso> call_usuario = clienteRestUsuarioAcceso.obtenerUsuarioPorCorreo(correo);
            try {
                Response<UsuarioAcceso> usuarioAccesoResponse = call_usuario.execute();
                if (usuarioAccesoResponse.isSuccessful()) {

                    UsuarioAcceso cUsuarioAcceso = usuarioAccesoResponse.body();
                    if (cUsuarioAcceso != null && cUsuarioAcceso.getIdUsuario() != null) {
                        errores = errores.concat("El correo electrónico está siendo usado por otro usuario.");
                    } else {
                        validado = true;
                    }
                } else {
                    ResponseBody responseBody = usuarioAccesoResponse.errorBody();
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
                    Toast.makeText(PerfilUsuario.this, errores, Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}
