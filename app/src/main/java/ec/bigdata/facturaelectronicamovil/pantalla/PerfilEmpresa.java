package ec.bigdata.facturaelectronicamovil.pantalla;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import java.util.concurrent.ExecutionException;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.interfaz.ServicioEmpresa;
import ec.bigdata.facturaelectronicamovil.modelo.ClienteEmpresa;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestEmpresa;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidad.Personalizacion;
import ec.bigdata.utilidades.Validaciones;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilEmpresa extends AppCompatActivity implements Validator.ValidationListener {

    private static final String TAG = PerfilEmpresa.class.getSimpleName();

    @NotEmpty(message = "La razón social es requerida.")
    private EditText editTextRazonSocial;

    @NotEmpty(message = "El nombre comercial es requerido.")
    private EditText editTextNombreComercial;

    @NotEmpty
    @Email(message = "El correo electrónico no es válido.")
    private EditText editTextCorreoPrincipalEmpresa;

    private EditText editTextTelefonoPrincipalEmpresa;

    private Switch switchObligadoLlevarContabilidad;

    private EditText editTextDireccionEmpresa;

    private EditText editTextNumeroResolucion;

    private TextView textViewObligadoLlevarContabilidad;

    ClaseGlobalUsuario claseGlobalUsuario;

    private Button buttonActualizarPerfilEmpresa;

    private String razonSocialActualizado;

    private String nombreComercialActualizado;

    private String correoPrincipalActualizado;

    private String telefonoPrincipalActualizado;

    private String direccionMatrizActualizado;

    private String numeroResolucion;

    private String llevaContabilidad;

    private Validator validator;

    private ServicioEmpresa clienteRestEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_empresa);

        editTextRazonSocial = (EditText) findViewById(R.id.edit_text_razon_social);
        editTextNombreComercial = (EditText) findViewById(R.id.edit_text_nombre_comercial);
        editTextCorreoPrincipalEmpresa = (EditText) findViewById(R.id.edit_text_correo_principal_empresa);
        editTextTelefonoPrincipalEmpresa = (EditText) findViewById(R.id.edit_text_telefono_principal_empresa);
        switchObligadoLlevarContabilidad = (Switch) findViewById(R.id.switch_obligado_llevar_contabilidad);
        editTextNumeroResolucion = (EditText) findViewById(R.id.edit_text_numero_resolucion);
        editTextDireccionEmpresa = (EditText) findViewById(R.id.edit_text_direccion_empresa);
        textViewObligadoLlevarContabilidad = (TextView) findViewById(R.id.text_view_obligado_llevar_contabilidad);
        buttonActualizarPerfilEmpresa = (Button) findViewById(R.id.button_actualizar_informacion_empresa);


        validator = new Validator(this);
        validator.setValidationListener(this);

        buttonActualizarPerfilEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarPerfilEmpresa();
            }
        });

        clienteRestEmpresa = ClienteRestEmpresa.getServicioEmpresa();

        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();

        editTextRazonSocial.setText(claseGlobalUsuario.getRazonSocial());
        editTextNombreComercial.setText(claseGlobalUsuario.getNombreComercial());
        editTextCorreoPrincipalEmpresa.setText(claseGlobalUsuario.getCorreoPrincipal());
        editTextTelefonoPrincipalEmpresa.setText(claseGlobalUsuario.getTelefonoPrincipal());
        editTextDireccionEmpresa.setText(claseGlobalUsuario.getDireccionMatriz());
        editTextNumeroResolucion.setText(claseGlobalUsuario.getNumeroResolucion());

        llevaContabilidad = claseGlobalUsuario.isObligadoLlevarContabilidad() == true ? "SI" : "NO";
        textViewObligadoLlevarContabilidad.setText(llevaContabilidad);
        switchObligadoLlevarContabilidad.setChecked(claseGlobalUsuario.isObligadoLlevarContabilidad());
        switchObligadoLlevarContabilidad.setTextOn("SI");
        switchObligadoLlevarContabilidad.setTextOff("NO");
        switchObligadoLlevarContabilidad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         final boolean isChecked) {
                switchObligadoLlevarContabilidad.setChecked(isChecked);
                if (isChecked) {
                    textViewObligadoLlevarContabilidad.setText("SI");
                } else {
                    textViewObligadoLlevarContabilidad.setText("NO");
                }

                Call<ResponseBody> call_response_body = clienteRestEmpresa.actualizarObligadoLlevarContabilidad(claseGlobalUsuario.getIdEmpresa(), isChecked);
                call_response_body.enqueue(new Callback<ResponseBody>() {
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
                                    claseGlobalUsuario.setObligadoLlevarContabilidad(isChecked);

                                    Personalizacion.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), Personalizacion.TOAST_INFORMACION, "Obligado a llevar contabilidad actualizado.");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        actualizarPerfilEmpresa();
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
                Personalizacion.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), Personalizacion.TOAST_ERROR, message);
            }
        }
    }

    private class ActualizacionPerfilEmpresaAsyncTask extends AsyncTask<String, Integer, Boolean> {


        private String errores;

        public ActualizacionPerfilEmpresaAsyncTask() {
            this.errores = "";
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean validado = false;
            String correo = params[0];
            Call<ClienteEmpresa> call_empresa = clienteRestEmpresa.obtenerClienteEmpresaPorCorreo(correo);
            try {
                Response<ClienteEmpresa> clienteEmpresaResponse = call_empresa.execute();
                if (clienteEmpresaResponse.isSuccessful()) {

                    ClienteEmpresa clienteEmpresa = clienteEmpresaResponse.body();
                    if (clienteEmpresa != null && clienteEmpresa.getIdClienteEmpresa() != null) {
                        errores = errores.concat("El correo electrónico está siendo usado por otra empresa.");
                    } else {
                        validado = true;
                    }
                } else {
                    ResponseBody responseBody = clienteEmpresaResponse.errorBody();
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
                    Personalizacion.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), Personalizacion.TOAST_ERROR, errores);
                }
            }
        }

    }

    private void actualizarPerfilEmpresa() {
        ActualizacionPerfilEmpresaAsyncTask actualizacionPerfilEmpresaAsyncTask = new ActualizacionPerfilEmpresaAsyncTask();

        if (!editTextNombreComercial.getText().toString().trim().equals("")) {
            nombreComercialActualizado = editTextNombreComercial.getText().toString();
        } else {
            nombreComercialActualizado = claseGlobalUsuario.getNombreComercial();
        }
        if (!editTextRazonSocial.getText().toString().trim().equals("")) {
            razonSocialActualizado = editTextRazonSocial.getText().toString();
        } else {
            razonSocialActualizado = claseGlobalUsuario.getRazonSocial();
        }

        if (!editTextTelefonoPrincipalEmpresa.getText().toString().trim().equals("")) {
            telefonoPrincipalActualizado = editTextTelefonoPrincipalEmpresa.getText().toString();
        } else {
            telefonoPrincipalActualizado = claseGlobalUsuario.getTelefonoPrincipal();
        }
        if (!editTextDireccionEmpresa.getText().toString().trim().equals("")) {
            direccionMatrizActualizado = editTextDireccionEmpresa.getText().toString();
        } else {
            direccionMatrizActualizado = claseGlobalUsuario.getTelefonoPrincipal();
        }

        if (!editTextNumeroResolucion.getText().toString().trim().equals("")) {
            numeroResolucion = editTextNumeroResolucion.getText().toString();
        } else {
            numeroResolucion = claseGlobalUsuario.getNumeroResolucion();
        }
        correoPrincipalActualizado = editTextCorreoPrincipalEmpresa.getText().toString();
        if (!correoPrincipalActualizado.trim().equals("")) {
            if (Validaciones.isEmail(correoPrincipalActualizado)) {
                Boolean validadoCorreo = Boolean.FALSE;
                if (editTextCorreoPrincipalEmpresa.getText().toString().equals(claseGlobalUsuario.getCorreoPrincipal())) {
                    validadoCorreo = Boolean.TRUE;
                } else {
                    try {
                        validadoCorreo = actualizacionPerfilEmpresaAsyncTask.execute(correoPrincipalActualizado).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                if (validadoCorreo) {

                    Call<ResponseBody> call_response_body = clienteRestEmpresa.actualizarEmpresa(
                            claseGlobalUsuario.getIdEmpresa(), nombreComercialActualizado, razonSocialActualizado, direccionMatrizActualizado
                            , correoPrincipalActualizado, telefonoPrincipalActualizado, numeroResolucion);
                    call_response_body.enqueue(new Callback<ResponseBody>() {
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
                                        claseGlobalUsuario.setNombreComercial(nombreComercialActualizado);
                                        claseGlobalUsuario.setRazonSocial(razonSocialActualizado);
                                        claseGlobalUsuario.setDireccionMatriz(direccionMatrizActualizado);
                                        claseGlobalUsuario.setTelefonoPrincipal(telefonoPrincipalActualizado);
                                        claseGlobalUsuario.setCorreoPrincipal(correoPrincipalActualizado);
                                        claseGlobalUsuario.setNumeroResolucion(numeroResolucion);

                                        Personalizacion.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), Personalizacion.TOAST_INFORMACION, "Información de empresa actualizada.");
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
            } else {
                Personalizacion.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), Personalizacion.TOAST_ERROR, "El correo principal no es válido.");
            }
        } else {
            Personalizacion.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), Personalizacion.TOAST_ERROR, "El correo principal no puede estar vacío.");
        }

    }
}
