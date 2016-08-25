package ec.bigdata.facturaelectronicamovil.pantalla;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.IOException;
import java.util.List;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.adaptador.ArrayAdapterSecuencial;
import ec.bigdata.facturaelectronicamovil.dialogs.DialogProgreso;
import ec.bigdata.facturaelectronicamovil.modelo.Secuencial;
import ec.bigdata.facturaelectronicamovil.personalizacion.MensajePersonalizado;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestSecuencial;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidad.Codigos;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Establecimiento extends AppCompatActivity implements Validator.ValidationListener {

    private Spinner spinnerEstablecimientos;

    @NotEmpty(message = "El código de establecimiento es requerido.")
    private EditText editTextCodigoEstablecimiento;

    @NotEmpty(message = "El punto de emisión es requerido.")
    private EditText editTextPuntoEmision;

    @NotEmpty(message = "La dirección del establecimiento es requerida.")
    private EditText editTextDireccionEstablecimiento;

    private Button buttonNuevo;

    private Button buttonActualizar;

    private Button buttonContinuar;

    private ProgressDialog progressDialog;

    private String codigoEstablecimientoEditado;

    private String puntoEmisionEditado;

    private String direccionEstablecimientoEditado;

    private Secuencial establecimientoSecuencialSeleccionado;

    private ClienteRestSecuencial.ServicioSecuencial servicioSecuencial;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private ArrayAdapterSecuencial adapterSecuencial;

    private int posicionSeleccionada;

    private List<Secuencial> secuenciales;

    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_establecimiento);
        validator = new Validator(this);
        validator.setValidationListener(this);

        spinnerEstablecimientos = (Spinner) findViewById(R.id.spinner_establecimientos);
        editTextCodigoEstablecimiento = (EditText) findViewById(R.id.edit_text_codigo_establecimiento);
        editTextPuntoEmision = (EditText) findViewById(R.id.edit_text_punto_emision);
        editTextDireccionEstablecimiento = (EditText) findViewById(R.id.edit_text_direccion_establecimiento);
        buttonNuevo = (Button) findViewById(R.id.button_nuevo_establecimiento);
        buttonActualizar = (Button) findViewById(R.id.button_actualizar_establecimiento);
        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();
        servicioSecuencial = ClienteRestSecuencial.getServicioSecuencial();
        cargarEstablecimientos();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_compuesta);
        buttonContinuar = (Button) toolbar.findViewById(R.id.button_continuar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar);
        tituloToolbar.setText(getResources().getString(R.string.titulo_establecimientos));

        //Valida la información que edite el usuario de su información tributaria y regresa al menu de componentes de la factura
        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InicioFacturacionElectronica.class);
                if (establecimientoSecuencialSeleccionado != null) {
                    intent.putExtra(String.valueOf(Codigos.CODIGO_ESTABLECIMIENTO_ACTUAL), establecimientoSecuencialSeleccionado);
                    setResult(RESULT_OK, intent);
                } else {
                    setResult(RESULT_CANCELED, intent);
                }
                finish();
            }
        });
        buttonNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NuevoEstablecimiento.class);
                startActivityForResult(intent, Codigos.CODIGO_REQUEST_NUEVO_ESTABLECIMIENTO);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

        buttonActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
    }

    private void actualizarInformacionEstablecimiento() {
        progressDialog = DialogProgreso.mostrarDialogProgreso(Establecimiento.this);
        Call<ResponseBody> responseBodyCall = null;
        codigoEstablecimientoEditado = editTextCodigoEstablecimiento.getText().toString();
        puntoEmisionEditado = editTextPuntoEmision.getText().toString();
        direccionEstablecimientoEditado = editTextDireccionEstablecimiento.getText().toString();
        if (!codigoEstablecimientoEditado.equals(establecimientoSecuencialSeleccionado.getCodigoEstablecimientoSecuencial())
                || !puntoEmisionEditado.equals(establecimientoSecuencialSeleccionado.getPuntoEmisionSecuencial())
                || !direccionEstablecimientoEditado.equals(establecimientoSecuencialSeleccionado.getDireccionSecuencial())) {
            responseBodyCall = servicioSecuencial.actualizarEstablecimiento(claseGlobalUsuario.getIdEmpresa(), establecimientoSecuencialSeleccionado.getIdSecuencial(), codigoEstablecimientoEditado, puntoEmisionEditado, direccionEstablecimientoEditado);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        String contenido = null;
                        try {
                            contenido = new String(response.body().bytes());
                            JsonObject jsonObject = new JsonParser().parse(contenido).getAsJsonObject();
                            if (jsonObject.get("estado").getAsBoolean() == true) {
                                progressDialog.dismiss();
                                establecimientoSecuencialSeleccionado.setDireccionSecuencial(direccionEstablecimientoEditado);
                                establecimientoSecuencialSeleccionado.setCodigoEstablecimientoSecuencial(codigoEstablecimientoEditado);
                                establecimientoSecuencialSeleccionado.setPuntoEmisionSecuencial(puntoEmisionEditado);
                                secuenciales.set(posicionSeleccionada, establecimientoSecuencialSeleccionado);
                                adapterSecuencial.notifyDataSetChanged();
                                MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_INFORMACION, "Establecimiento actualizado.");
                            } else {
                                MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ADVERTENCIA, jsonObject.get("mensajeError").getAsString());
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
        } else {
            MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_INFORMACION, "No se han hecho cambios.");
        }
    }

    private void cargarEstablecimientos() {
        progressDialog = DialogProgreso.mostrarDialogProgreso(Establecimiento.this);
        Call<List<Secuencial>> callSecuencial = servicioSecuencial.obtenerEstablecimientosPorEmpresa(claseGlobalUsuario.getIdEmpresa());
        callSecuencial.enqueue(new Callback<List<Secuencial>>() {
            @Override
            public void onResponse(Call<List<Secuencial>> call, Response<List<Secuencial>> response) {
                if (response.isSuccessful()) {
                    secuenciales = response.body();
                    if (secuenciales != null && !secuenciales.isEmpty()) {

                        //Se asigna el origen de datos
                        adapterSecuencial = new ArrayAdapterSecuencial(getApplicationContext(), secuenciales);

                        // adapterSecuencial.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Se setea el adaptador
                        spinnerEstablecimientos.setAdapter(adapterSecuencial);
                        spinnerEstablecimientos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                posicionSeleccionada = position;
                                establecimientoSecuencialSeleccionado = (Secuencial) parentView.getItemAtPosition(position);
                                editTextCodigoEstablecimiento.setText(establecimientoSecuencialSeleccionado.getCodigoEstablecimientoSecuencial());
                                editTextPuntoEmision.setText(establecimientoSecuencialSeleccionado.getPuntoEmisionSecuencial());
                                editTextDireccionEstablecimiento.setText(establecimientoSecuencialSeleccionado.getDireccionSecuencial());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parentView) {
                                //Nada
                            }

                        });
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Secuencial>> call, Throwable t) {
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
        Intent intent = new Intent(getApplicationContext(), InicioFacturacionElectronica.class);
        if (establecimientoSecuencialSeleccionado != null) {
            intent.putExtra(String.valueOf(Codigos.CODIGO_ESTABLECIMIENTO_ACTUAL), establecimientoSecuencialSeleccionado);
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED, intent);
        }
        this.finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    @Override
    public void onValidationSucceeded() {
        actualizarInformacionEstablecimiento();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Codigos.CODIGO_REQUEST_NUEVO_ESTABLECIMIENTO) {
            if (resultCode == RESULT_OK) {
                cargarEstablecimientos();
            }
        }
    }
}
