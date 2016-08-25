package ec.bigdata.facturaelectronicamovil.pantalla;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.io.IOException;
import java.util.List;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.dialogs.DialogProgreso;
import ec.bigdata.facturaelectronicamovil.personalizacion.MensajePersonalizado;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestEmpresa;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilEmpresa extends AppCompatActivity {

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

    @NotEmpty(message = "La contraseña actual es requerida.", sequence = 1)
    private EditText editTextContraseniaActual;

    @NotEmpty(message = "La contraseña nueva es requerida.", sequence = 1)
    @Password(scheme = Password.Scheme.ALPHA_NUMERIC, min = 8, message = "La contraseña debe tener al menos 8 caracteres entre dígitos,símbolos y letras.", sequence = 2)
    @ConfirmPassword(message = "Las contraseñas no coinciden.", sequence = 3)
    private EditText editTextContraseniaNueva;

    private Button buttonActualizarPerfilEmpresa;

    private Button buttonActualizarContrasenia;

    private String razonSocialActualizado;

    private String nombreComercialActualizado;

    private String correoPrincipalActualizado;

    private String telefonoPrincipalActualizado;

    private String direccionMatrizActualizado;

    private String numeroResolucion;

    private String llevaContabilidad;

    private String contraseniaActual;

    private String contraseniaNueva;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private ClienteRestEmpresa.ServicioEmpresa clienteRestEmpresa;

    private ProgressDialog progressDialog;

    private ValidacionInformacionEmpresa validacionInformacionEmpresa;

    private ValidacionCambioContrasenia validacionCambioContrasenia;

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
        buttonActualizarContrasenia = (Button) findViewById(R.id.button_actualizar_contrasenia);
        editTextContraseniaActual = (EditText) findViewById(R.id.edit_text_contrasenia_actual_empresa);
        editTextContraseniaNueva = (EditText) findViewById(R.id.edit_text_contrasenia_nueva_empresa);

        validacionInformacionEmpresa = new ValidacionInformacionEmpresa(editTextRazonSocial, editTextNombreComercial, editTextCorreoPrincipalEmpresa, editTextTelefonoPrincipalEmpresa, editTextDireccionEmpresa, editTextNumeroResolucion, new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {


                nombreComercialActualizado = editTextNombreComercial.getText().toString();

                razonSocialActualizado = editTextRazonSocial.getText().toString();

                telefonoPrincipalActualizado = editTextTelefonoPrincipalEmpresa.getText().toString();

                direccionMatrizActualizado = editTextDireccionEmpresa.getText().toString();

                numeroResolucion = editTextNumeroResolucion.getText().toString();

                correoPrincipalActualizado = editTextCorreoPrincipalEmpresa.getText().toString();
                progressDialog = DialogProgreso.mostrarDialogProgreso(PerfilEmpresa.this);
                Call<ResponseBody> responseBodyCall = clienteRestEmpresa.actualizarEmpresa(
                        claseGlobalUsuario.getIdEmpresa(), nombreComercialActualizado, razonSocialActualizado, direccionMatrizActualizado
                        , correoPrincipalActualizado, telefonoPrincipalActualizado, numeroResolucion);
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            progressDialog.dismiss();
                            String contenido = null;
                            try {
                                contenido = new String(response.body().bytes());
                                JsonObject jsonObject = new JsonParser().parse(contenido).getAsJsonObject();
                                if (jsonObject.get("estado").getAsBoolean() == true) {
                                    claseGlobalUsuario.setNombreComercial(nombreComercialActualizado);
                                    claseGlobalUsuario.setRazonSocial(razonSocialActualizado);
                                    claseGlobalUsuario.setDireccionMatriz(direccionMatrizActualizado);
                                    claseGlobalUsuario.setTelefonoPrincipal(telefonoPrincipalActualizado);
                                    claseGlobalUsuario.setCorreoPrincipal(correoPrincipalActualizado);
                                    claseGlobalUsuario.setNumeroResolucion(numeroResolucion);

                                    MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_INFORMACION, "Información de empresa actualizada.");
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
                    }
                });
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
        });

        validacionCambioContrasenia = new ValidacionCambioContrasenia(editTextContraseniaActual, editTextContraseniaNueva, new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                contraseniaActual = editTextContraseniaActual.getText().toString();
                contraseniaNueva = editTextContraseniaNueva.getText().toString();

                progressDialog = DialogProgreso.mostrarDialogProgreso(PerfilEmpresa.this);
                Call<ResponseBody> responseBodyCall = clienteRestEmpresa.actualizarContrasenia(claseGlobalUsuario.getIdEmpresa(), contraseniaActual, contraseniaNueva);
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            progressDialog.dismiss();
                            String contenido = null;
                            try {
                                contenido = new String(response.body().bytes());
                                JsonObject jsonObject = new JsonParser().parse(contenido).getAsJsonObject();
                                if (jsonObject.get("estado").getAsBoolean() == true) {
                                    //TODO como actualizo la nueva contrasenia.
                                    MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_INFORMACION, "Contraseña actualizada.");
                                } else {
                                    MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_INFORMACION, jsonObject.get("mensajeError").getAsString());
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
                    }
                });

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
        });
        buttonActualizarPerfilEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validacionInformacionEmpresa.validarCampos();
            }
        });
        buttonActualizarContrasenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validacionCambioContrasenia.validarCampos();
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
        progressDialog = DialogProgreso.mostrarDialogProgreso(PerfilEmpresa.this);
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
                progressDialog = DialogProgreso.mostrarDialogProgreso(PerfilEmpresa.this);
                Call<ResponseBody> responseBodyCall = clienteRestEmpresa.actualizarObligadoLlevarContabilidad(claseGlobalUsuario.getIdEmpresa(), isChecked);
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            progressDialog.dismiss();
                            String contenido = null;
                            try {
                                contenido = new String(response.body().bytes());
                                JsonObject jsonObject = new JsonParser().parse(contenido).getAsJsonObject();
                                if (jsonObject.get("estado").getAsBoolean() == true) {
                                    claseGlobalUsuario.setObligadoLlevarContabilidad(isChecked);
                                    MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_INFORMACION, "Obligado a llevar contabilidad actualizado.");
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
                    }
                });
            }
        });
        progressDialog.dismiss();
    }

    //Validador de información de la empresa
    class ValidacionInformacionEmpresa {
        @NotEmpty(message = "La razón social es requerida.")
        private EditText editTextRazonSocial;

        @NotEmpty(message = "El nombre comercial es requerido.")
        private EditText editTextNombreComercial;

        @NotEmpty
        @Email(message = "El correo electrónico no es válido.")
        private EditText editTextCorreoPrincipalEmpresa;

        private EditText editTextTelefonoPrincipalEmpresa;

        private EditText editTextDireccionEmpresa;

        private EditText editTextNumeroResolucion;

        private Validator validator;

        public ValidacionInformacionEmpresa(EditText editTextRazonSocial, EditText editTextNombreComercial, EditText editTextCorreoPrincipalEmpresa, EditText editTextTelefonoPrincipalEmpresa, EditText editTextDireccionEmpresa, EditText editTextNumeroResolucion, Validator.ValidationListener validationListener) {
            this.editTextRazonSocial = editTextRazonSocial;
            this.editTextNombreComercial = editTextNombreComercial;
            this.editTextCorreoPrincipalEmpresa = editTextCorreoPrincipalEmpresa;
            this.editTextTelefonoPrincipalEmpresa = editTextTelefonoPrincipalEmpresa;

            this.editTextDireccionEmpresa = editTextDireccionEmpresa;
            this.editTextNumeroResolucion = editTextNumeroResolucion;
            this.validator = new Validator(this);
            this.validator.setValidationListener(validationListener);
        }

        public void validarCampos() {
            validator.validate();
        }
    }

    class ValidacionCambioContrasenia {
        @NotEmpty(message = "La contraseña actual es requerida.", sequence = 1)
        private EditText editTextContraseniaActual;

        @NotEmpty(message = "La contraseña nueva es requerida.", sequence = 1)
        @Password(scheme = Password.Scheme.ALPHA_NUMERIC, min = 8, message = "La contraseña debe tener al menos 8 caracteres entre dígitos,símbolos y letras.", sequence = 2)
        @ConfirmPassword(message = "Las contraseñas no coinciden.", sequence = 3)
        private EditText editTextContraseniaNueva;

        private Validator validator;

        public ValidacionCambioContrasenia(EditText editTextContraseniaActual, EditText editTextContraseniaNueva, Validator.ValidationListener validationListener) {
            this.editTextContraseniaActual = editTextContraseniaActual;
            this.editTextContraseniaNueva = editTextContraseniaNueva;
            this.validator = new Validator(this);
            this.validator.setValidationListener(validationListener);
        }

        public void validarCampos() {

            validator.validate();
        }

    }


}
