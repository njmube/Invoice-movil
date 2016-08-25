package ec.bigdata.facturaelectronicamovil.pantalla;

import android.content.Intent;
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
import ec.bigdata.facturaelectronicamovil.personalizacion.MensajePersonalizado;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestProducto;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidad.Codigos;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActualizacionProducto extends AppCompatActivity implements Validator.ValidationListener {

    private static final String TAG = NuevoProductoCorregir.class.getSimpleName();

    private TextView textViewCodigoPrincipalProducto;

    private EditText editTextCodigoAuxiliarProducto;

    @NotEmpty(message = "La descripción del producto es requerida.")
    private EditText editTextDescripcionProducto;

    @NotEmpty(message = "El precio unitario del producto es requerida.")
    private EditText editTextPrecioUnitarioProducto;

    private Button buttonActualizarProducto;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private ClienteRestProducto.ServicioProducto servicioProducto;

    private ec.bigdata.facturaelectronicamovil.modelo.Producto productoSeleccionado;

    private Validator validator;

    private String codigoAuxiliarProducto;

    private String descripcionProducto;

    private String precioUnitarioProducto;

    private boolean productoActualizado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizacion_producto);
        textViewCodigoPrincipalProducto = (TextView) findViewById(R.id.text_view_codigo_principal_producto);
        editTextCodigoAuxiliarProducto = (EditText) findViewById(R.id.edit_text_codigo_auxiliar_edicion_producto);
        editTextDescripcionProducto = (EditText) findViewById(R.id.edit_text_descripcion_edicion_producto);
        editTextPrecioUnitarioProducto = (EditText) findViewById(R.id.edit_text_precio_unitario_edicion_producto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_simple);
        setSupportActionBar(toolbar);

        //Get a support ActionBar corresponding to this toolbar_simple

        //Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar_simple);
        tituloToolbar.setText(getResources().getString(R.string.titulo_actualizar_producto));

        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();
        servicioProducto = ClienteRestProducto.getServicioProducto();
        validator = new Validator(this);
        validator.setValidationListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            productoSeleccionado = (ec.bigdata.facturaelectronicamovil.modelo.Producto) bundle.getSerializable(String.valueOf(Codigos.CODIGO_PRODUCTO_SELECCIONADO));
            if (productoSeleccionado != null) {
                textViewCodigoPrincipalProducto.setText(productoSeleccionado.getCodigoPrincipalProducto());
                if (productoSeleccionado.getCodigoAuxiliarProducto() != null) {
                    editTextCodigoAuxiliarProducto.setText(productoSeleccionado.getCodigoAuxiliarProducto());
                }
                editTextDescripcionProducto.setText(productoSeleccionado.getDescripcionProducto());
                editTextPrecioUnitarioProducto.setText(productoSeleccionado.getPrecioUnitarioProducto());

            }
        }
        buttonActualizarProducto = (Button) findViewById(R.id.button_actualizacion_producto);
        buttonActualizarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });

    }

    private void actualizarProducto() {
        Call<ResponseBody> call = null;
        codigoAuxiliarProducto = "";
        descripcionProducto = "";
        precioUnitarioProducto = "";

        codigoAuxiliarProducto = editTextCodigoAuxiliarProducto.getText().toString();
        descripcionProducto = editTextDescripcionProducto.getText().toString();
        precioUnitarioProducto = editTextPrecioUnitarioProducto.getText().toString();

        call = servicioProducto.actualizarProducto(claseGlobalUsuario.getIdEmpresa(), productoSeleccionado.getIdProducto(), codigoAuxiliarProducto, descripcionProducto, precioUnitarioProducto);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String contenido = null;
                    try {
                        contenido = new String(response.body().bytes());
                        JsonObject jsonObject = new JsonParser().parse(contenido).getAsJsonObject();
                        if (jsonObject.get("estado").getAsBoolean() == true) {
                            productoActualizado = true;
                            //TODO Refrescar objeto producto actualizado.
                            productoSeleccionado.setPrecioUnitarioProducto(precioUnitarioProducto);
                            productoSeleccionado.setCodigoAuxiliarProducto(codigoAuxiliarProducto);
                            productoSeleccionado.setDescripcionProducto(descripcionProducto);
                            MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_INFORMACION, "Producto actualizado.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, "Error al actualizar el producto.");

            }
        });

    }

    @Override
    public void onValidationSucceeded() {
        actualizarProducto();
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
        intent.putExtra(String.valueOf(Codigos.CODIGO_PRODUCTO_ACTUALIZADO), productoSeleccionado);
        if (productoActualizado) {
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED, intent);
        }
        this.finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

}
