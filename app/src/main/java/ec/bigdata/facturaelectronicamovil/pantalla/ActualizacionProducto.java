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
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestProducto;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidad.Codigos;
import ec.bigdata.facturaelectronicamovil.utilidad.Personalizacion;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActualizacionProducto extends AppCompatActivity implements Validator.ValidationListener {

    private static final String TAG = NuevoProducto.class.getSimpleName();

    private TextView textViewCodigoPrincipalProducto;

    private EditText editTextCodigoAuxiliarProducto;

    @NotEmpty(message = "La descripción del producto es requerida.")
    private EditText editTextDescripcionProducto;

    private EditText editTextPrecioUnitarioProducto;

    private Button buttonActualizarProducto;

    private Button buttonContinuar;

    private Toolbar toolbar;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private ClienteRestProducto.ServicioProducto servicioProducto;

    private ec.bigdata.facturaelectronicamovil.modelo.Producto productoSeleccionado;

    private Validator validator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizacion_producto);
        textViewCodigoPrincipalProducto = (TextView) findViewById(R.id.text_view_codigo_principal_producto);
        editTextCodigoAuxiliarProducto = (EditText) findViewById(R.id.edit_text_codigo_auxiliar_edicion_producto);
        editTextDescripcionProducto = (EditText) findViewById(R.id.edit_text_descripcion_edicion_producto);
        editTextPrecioUnitarioProducto = (EditText) findViewById(R.id.edit_text_precio_unitario_edicion_producto);
        toolbar = (Toolbar) findViewById(R.id.toolbar_compuesta);
        setSupportActionBar(toolbar);

        //Get a support ActionBar corresponding to this toolbar_compuesta

        //Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar);
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
        buttonContinuar = (Button) toolbar.findViewById(R.id.button_continuar);
        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Cliente.class);
                intent.putExtra(String.valueOf(Codigos.CODIGO_PRODUCTO_SELECCIONADO_ACTUALIZADO), productoSeleccionado);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void actualizarProducto() {
        Call<ResponseBody> call = null;
        String codigoAuxiliar = "";
        String precioUnitario = "";
        if (editTextCodigoAuxiliarProducto.getText().toString().trim().equals("")) {
            codigoAuxiliar = productoSeleccionado.getCodigoAuxiliarProducto() != null ? productoSeleccionado.getCodigoAuxiliarProducto() : "";
        } else {
            codigoAuxiliar = editTextCodigoAuxiliarProducto.getText().toString();
        }

        if (editTextPrecioUnitarioProducto.getText().toString().trim().equals("")) {
            precioUnitario = productoSeleccionado.getPrecioUnitarioProducto() != null ? productoSeleccionado.getPrecioUnitarioProducto() : "";
        } else {
            precioUnitario = editTextPrecioUnitarioProducto.getText().toString();
        }

        call = servicioProducto.actualizarProducto(claseGlobalUsuario.getIdEmpresa(), productoSeleccionado.getIdProducto(), codigoAuxiliar, editTextDescripcionProducto.getText().toString(), precioUnitario);
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
                            //TODO Refrescar objeto producto actualizado.

                            Personalizacion.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), Personalizacion.TOAST_INFORMACION, "Producto actualizado.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Personalizacion.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), Personalizacion.TOAST_ERROR, "Error al actualizar el producto.");

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

                Personalizacion.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), Personalizacion.TOAST_ERROR, message);
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
        this.finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

}
