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
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.modelo.Producto;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestProducto;
import ec.bigdata.facturaelectronicamovil.utilidades.ClaseGlobalUsuario;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NuevoProducto extends AppCompatActivity implements Validator.ValidationListener {

    private static final String TAG = NuevoProducto.class.getSimpleName();

    private Toolbar toolbar;

    @NotEmpty(message = "El código principal del producto es requerido.")
    private EditText editTextCodigoPrincipalProducto;

    private EditText editTextCodigoAuxiliarProducto;

    @NotEmpty(message = "La descripción del producto es requerida.")
    private EditText editTextDescripcionProducto;

    private EditText editTextPrecioUnitarioProducto;

    private Button buttonGuardarProducto;

    private Button buttonContinuar;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private ClienteRestProducto.ServicioProducto servicioProducto;

    private Validator validator;

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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get a support ActionBar corresponding to this toolbar_compuesta

        //Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar);
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

        buttonContinuar = (Button) toolbar.findViewById(R.id.button_continuar);
        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Cliente.class);
                startActivity(intent);
                finish();
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
        ProductoAsyncTask productoAsyncTask = new ProductoAsyncTask();
        boolean validadoExistenciaProducto = Boolean.FALSE;
        try {
            validadoExistenciaProducto = productoAsyncTask.execute(editTextCodigoPrincipalProducto.getText().toString()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (validadoExistenciaProducto) {
            Call<ResponseBody> responseBodyCall = servicioProducto.guardarProducto(claseGlobalUsuario.getIdEmpresa(), editTextCodigoPrincipalProducto.getText().toString()
                    , editTextCodigoAuxiliarProducto.getText().toString(), editTextDescripcionProducto.getText().toString(), editTextPrecioUnitarioProducto.getText().toString());
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JsonParser parser = new JsonParser();
                        JsonObject o = null;
                        try {
                            String s = new String(response.body().bytes());
                            o = parser.parse(s).getAsJsonObject();

                            if (o.get("status").getAsBoolean() == true) {

                                Toast.makeText(NuevoProducto.this, "Producto guardado.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(NuevoProducto.this, "Error al guardar el producto.", Toast.LENGTH_SHORT).show();
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

    private class ProductoAsyncTask extends AsyncTask<String, Integer, Boolean> {


        private String errores;

        public ProductoAsyncTask() {
            this.errores = "";
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean validado = false;
            String codigoPrincipalProducto = params[0];
            Call<ec.bigdata.facturaelectronicamovil.modelo.Producto> callProducto = servicioProducto.obtenerProductoPorCodigoPrincipalPorEmpresaAsociado(codigoPrincipalProducto, claseGlobalUsuario.getIdEmpresa());
            try {
                Response<Producto> productoResponse = callProducto.execute();
                if (productoResponse.isSuccessful()) {

                    Producto producto = productoResponse.body();
                    if (producto != null && producto.getIdProducto() != null) {
                        errores = errores.concat("El producto ya se encuentra registrado.");
                    } else {
                        validado = true;
                    }
                } else {
                    ResponseBody responseBody = productoResponse.errorBody();
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
                    Toast.makeText(NuevoProducto.this, errores, Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}
