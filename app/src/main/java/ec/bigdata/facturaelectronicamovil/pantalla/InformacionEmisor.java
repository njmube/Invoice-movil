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
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

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

public class InformacionEmisor extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty(message = "La razón social es requerida.")
    private EditText editTextRazonSocial;

    @NotEmpty(message = "El nombre comercial es requerido.")
    private EditText editTextNombreComercial;

    private Switch switchObligadoLlevarContabilidad;

    @NotEmpty(message = "La dirección matríz es requerida.")
    private EditText editTextDireccionEmpresa;

    private EditText editTextNumeroResolucion;

    private TextView textViewObligadoLlevarContabilidad;

    private Button botonContinuar;

    private ProgressDialog progressDialog;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private String llevaContabilidad;

    private ClienteRestEmpresa.ServicioEmpresa clienteRestEmpresa;

    private boolean validadoInformacionTributaria;

    private Validator validator;

    private String razonSocialActualizado;

    private String nombreComercialActualizado;

    private String direccionMatrizActualizada;

    private String numeroResolucion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_emisor);

        validadoInformacionTributaria = false;

        validator = new Validator(this);
        validator.setValidationListener(this);

        editTextRazonSocial = (EditText) findViewById(R.id.edit_text_razon_social_emisor);
        editTextNombreComercial = (EditText) findViewById(R.id.edit_text_nombre_comercial_emisor);
        switchObligadoLlevarContabilidad = (Switch) findViewById(R.id.switch_obligado_llevar_contabilidad_emisor);
        editTextDireccionEmpresa = (EditText) findViewById(R.id.edit_text_direccion_empresa_emisor);
        editTextNumeroResolucion = (EditText) findViewById(R.id.edit_text_numero_resolucion_emisor);
        textViewObligadoLlevarContabilidad = (TextView) findViewById(R.id.text_view_obligado_llevar_contabilidad_emisor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_compuesta);
        setSupportActionBar(toolbar);

        clienteRestEmpresa = ClienteRestEmpresa.getServicioEmpresa();

        botonContinuar = (Button) toolbar.findViewById(R.id.button_continuar);

        //Valida la información que edite el usuario de su información tributaria y regresa al menú de componentes de la factura electrónica.
        botonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar);
        tituloToolbar.setText(getResources().getString(R.string.titulo_informacion_emisor));
        progressDialog = DialogProgreso.mostrarDialogProgreso(InformacionEmisor.this);
        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();
        editTextRazonSocial.setText(claseGlobalUsuario.getRazonSocial());
        editTextNombreComercial.setText(claseGlobalUsuario.getNombreComercial());
        editTextDireccionEmpresa.setText(claseGlobalUsuario.getDireccionMatriz());
        editTextNumeroResolucion.setText(claseGlobalUsuario.getNumeroResolucion());
        llevaContabilidad = claseGlobalUsuario.isObligadoLlevarContabilidad() == true ? "SI" : "NO";
        textViewObligadoLlevarContabilidad.setText(llevaContabilidad);
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
                progressDialog = DialogProgreso.mostrarDialogProgreso(InformacionEmisor.this);
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

    private void validarInformacionEmisor() {
        progressDialog = DialogProgreso.mostrarDialogProgreso(InformacionEmisor.this);
        nombreComercialActualizado = editTextNombreComercial.getText().toString();
        razonSocialActualizado = editTextRazonSocial.getText().toString();
        direccionMatrizActualizada = editTextDireccionEmpresa.getText().toString();

        if (!editTextNumeroResolucion.getText().toString().trim().equals("")) {
            numeroResolucion = editTextNumeroResolucion.getText().toString();
        } else {
            numeroResolucion = claseGlobalUsuario.getNumeroResolucion();
        }
        Call<ResponseBody> responseBodyCall = clienteRestEmpresa.actualizarEmpresa(
                claseGlobalUsuario.getIdEmpresa(), nombreComercialActualizado, razonSocialActualizado, null, null, direccionMatrizActualizada
                , numeroResolucion);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    String contenido = null;
                    try {
                        contenido = new String(response.body().bytes());
                        JsonObject jsonObject = new JsonParser().parse(contenido).getAsJsonObject();
                        if (jsonObject.get("estado").getAsBoolean() == true) {
                            claseGlobalUsuario.setNombreComercial(nombreComercialActualizado);
                            claseGlobalUsuario.setRazonSocial(razonSocialActualizado);
                            claseGlobalUsuario.setDireccionMatriz(direccionMatrizActualizada);
                            claseGlobalUsuario.setNumeroResolucion(numeroResolucion);
                            progressDialog.dismiss();
                            Intent intent = new Intent(getApplicationContext(), getCallingActivity().getClass());
                            setResult(RESULT_OK, intent);
                            finish();

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
        validarInformacionEmisor();
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
}
