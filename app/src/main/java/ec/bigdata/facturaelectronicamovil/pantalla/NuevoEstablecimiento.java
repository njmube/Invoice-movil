package ec.bigdata.facturaelectronicamovil.pantalla;

import android.app.ProgressDialog;
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
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.IOException;
import java.util.List;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.dialogs.DialogProgreso;
import ec.bigdata.facturaelectronicamovil.personalizacion.MensajePersonalizado;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestSecuencial;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NuevoEstablecimiento extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty(message = "El código de establecimiento es requerido.")
    @Min(value = 3, message = "El código de establecimiento debe tener 3 dígitos.")
    private EditText editTextCodigoEstablecimiento;

    @Min(value = 3, message = "El punto de emisión debe tener 3 dígitos.")
    @NotEmpty(message = "El punto de emisión es requerido.")
    private EditText editTextPuntoEmision;

    @NotEmpty(message = "La dirección del establecimiento es requerida.")
    private EditText editTextDireccionEstablecimiento;

    private Button buttonAgregar;

    private ProgressDialog progressDialog;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private ClienteRestSecuencial.ServicioSecuencial servicioSecuencial;

    private Validator validator;

    private String codigoEstablecimiento;

    private String puntoEmisionEstablecimiento;

    private String direccionEstablecimiento;

    private boolean establecimiengoGuardado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_establecimiento);
        validator = new Validator(this);
        validator.setValidationListener(this);

        editTextCodigoEstablecimiento = (EditText) findViewById(R.id.edit_text_codigo_nuevo_establecimiento);
        editTextPuntoEmision = (EditText) findViewById(R.id.edit_text_nuevo_punto_emision);
        editTextDireccionEstablecimiento = (EditText) findViewById(R.id.edit_text_direccion_nuevo_establecimiento);
        buttonAgregar = (Button) findViewById(R.id.button_agregar_nuevo_establecimiento);
        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();
        servicioSecuencial = ClienteRestSecuencial.getServicioSecuencial();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_simple);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar_simple);
        tituloToolbar.setText(getResources().getString(R.string.titulo_nuevo_establecimiento));
        buttonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
    }

    private void guardarEstablecimiento() {
        progressDialog = DialogProgreso.mostrarDialogProgreso(NuevoEstablecimiento.this);
        Call<ResponseBody> responseBodyCall = null;
        codigoEstablecimiento = editTextCodigoEstablecimiento.getText().toString();
        puntoEmisionEstablecimiento = editTextPuntoEmision.getText().toString();
        direccionEstablecimiento = editTextDireccionEstablecimiento.getText().toString();
        responseBodyCall = servicioSecuencial.guardarEstablecimiento(claseGlobalUsuario.getIdEmpresa(), codigoEstablecimiento, puntoEmisionEstablecimiento, direccionEstablecimiento);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String contenido = null;
                    try {
                        contenido = new String(response.body().bytes());
                        JsonObject jsonObject = new JsonParser().parse(contenido).getAsJsonObject();
                        if (jsonObject.get("estado").getAsBoolean() == true) {

                            establecimiengoGuardado = true;
                            MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_INFORMACION, "Establecimiento guardado.");
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ADVERTENCIA, jsonObject.get("mensajeError").getAsString());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
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
        if (establecimiengoGuardado) {
            setResult(RESULT_OK);
        }
        this.finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    @Override
    public void onValidationSucceeded() {
        guardarEstablecimiento();
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
