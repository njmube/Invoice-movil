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
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.IOException;
import java.util.List;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.anotacion.Identificacion;
import ec.bigdata.facturaelectronicamovil.dialogs.DialogProgreso;
import ec.bigdata.facturaelectronicamovil.personalizacion.MensajePersonalizado;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestCliente;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DavidLeonardo on 19/8/2016.
 */
public class NuevoCliente extends AppCompatActivity implements Validator.ValidationListener {

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

    private ClaseGlobalUsuario claseGlobalUsuario;

    private ClienteRestCliente.ServicioCliente servicioCliente;

    private Validator validator;

    private ProgressDialog progressDialog;

    private String identificacionCliente;
    private String razonSocial;
    private String direccion;
    private String correo;
    private String telefono;

    private boolean clienteNuevoRegistrado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_cliente);

        validator = new Validator(this);
        validator.setValidationListener(this);
        Validator.registerAnnotation(Identificacion.class);

        editTextIdentificacionCliente = (EditText) findViewById(R.id.edit_text_identificacion_nuevo_cliente);
        editTextRazonSocialCliente = (EditText) findViewById(R.id.edit_text_nombres_razon_social_nuevo_cliente);
        editTextDireccionCliente = (EditText) findViewById(R.id.edit_text_direccion_nuevo_cliente);
        editTextCorreoCliente = (EditText) findViewById(R.id.edit_text_correo_nuevo_cliente);
        editTextTelefonoCliente = (EditText) findViewById(R.id.edit_text_telefono_nuevo_cliente);
        buttonNuevoCliente = (Button) findViewById(R.id.button_nuevo_cliente);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_simple);
        setSupportActionBar(toolbar);


        //Get a support ActionBar corresponding to this toolbar_simple

        //Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar_simple);
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
        if (clienteNuevoRegistrado) {
            setResult(RESULT_OK);
        }
        this.finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    @Override
    public void onValidationSucceeded() {
        progressDialog = DialogProgreso.mostrarDialogProgreso(NuevoCliente.this);
        identificacionCliente = editTextIdentificacionCliente.getText().toString();
        razonSocial = editTextRazonSocialCliente.getText().toString();
        direccion = editTextDireccionCliente.getText().toString();
        telefono = editTextTelefonoCliente.getText().toString();
        correo = editTextCorreoCliente.getText().toString();
        Call<ResponseBody> responseBodyCall = servicioCliente.guardarCliente(identificacionCliente, claseGlobalUsuario.getIdEmpresa(), razonSocial, direccion, telefono, correo);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    JsonParser parser = new JsonParser();
                    JsonObject o = null;
                    try {
                        String s = new String(response.body().bytes());
                        o = parser.parse(s).getAsJsonObject();

                        if (o.get("estado").getAsBoolean() == true) {
                            progressDialog.dismiss();
                            MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_INFORMACION, "Cliente guardado.");
                        } else {
                            MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, o.get("mensajeError").getAsString());
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
