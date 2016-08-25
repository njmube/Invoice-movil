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
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.IOException;
import java.util.List;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.dialogs.DialogProgreso;
import ec.bigdata.facturaelectronicamovil.personalizacion.MensajePersonalizado;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestProducto;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DavidLeonardo on 19/8/2016.
 */
public class NuevoProducto extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty(message = "El código principal del producto es requerido.")
    private EditText editTextCodigoPrincipalProducto;

    private EditText editTextCodigoAuxiliarProducto;

    @NotEmpty(message = "La descripción del producto es requerida.")
    private EditText editTextDescripcionProducto;

    @NotEmpty(message = "El precio unitario del producto es requerido.")
    private EditText editTextPrecioUnitarioProducto;

    private Button buttonGuardarProducto;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private ClienteRestProducto.ServicioProducto servicioProducto;

    private Validator validator;

    private ProgressDialog progressDialog;

    private String codigoPrincipalProducto;
    private String codigoAuxiliarProducto;
    private String descripcionProducto;
    private String precioUnitarioProducto;
    private boolean productoNuevoRegistrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_producto);

        editTextCodigoPrincipalProducto = (EditText) findViewById(R.id.edit_text_codigo_principal_producto);
        editTextCodigoAuxiliarProducto = (EditText) findViewById(R.id.edit_text_codigo_auxiliar_producto);
        editTextDescripcionProducto = (EditText) findViewById(R.id.edit_text_descripcion_producto);
        editTextPrecioUnitarioProducto = (EditText) findViewById(R.id.edit_text_precio_unitario_producto);
        buttonGuardarProducto = (Button) findViewById(R.id.button_nuevo_producto
        );
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_simple);
        setSupportActionBar(toolbar);

        //Get a support ActionBar corresponding to this toolbar_compuesta

        //Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar_simple);
        tituloToolbar.setText(getResources().getString(R.string.titulo_nuevo_producto));
        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();
        servicioProducto = ClienteRestProducto.getServicioProducto();
        validator = new Validator(this);
        validator.setValidationListener(this);

        buttonGuardarProducto.setOnClickListener(new View.OnClickListener() {
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
        if (productoNuevoRegistrado) {
            setResult(RESULT_OK);
        }
        this.finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    @Override
    public void onValidationSucceeded() {
        progressDialog = DialogProgreso.mostrarDialogProgreso(NuevoProducto.this);
        codigoPrincipalProducto = "";
        codigoAuxiliarProducto = "";
        descripcionProducto = "";
        precioUnitarioProducto = "";
        codigoPrincipalProducto = editTextCodigoPrincipalProducto.getText().toString();
        codigoAuxiliarProducto = editTextCodigoAuxiliarProducto.getText().toString();
        descripcionProducto = editTextDescripcionProducto.getText().toString();
        precioUnitarioProducto = editTextPrecioUnitarioProducto.getText().toString();
        Call<ResponseBody> responseBodyCall = servicioProducto.guardarProducto(claseGlobalUsuario.getIdEmpresa(), codigoPrincipalProducto, codigoAuxiliarProducto, descripcionProducto, precioUnitarioProducto);
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
                            progressDialog.cancel();
                            MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_INFORMACION, "Producto guardado.");
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
                progressDialog.cancel();
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
