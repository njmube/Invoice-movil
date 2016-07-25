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

import java.io.IOException;
import java.util.List;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.adaptador.ArrayAdapterSecuencial;
import ec.bigdata.facturaelectronicamovil.dialogs.DialogProgreso;
import ec.bigdata.facturaelectronicamovil.modelo.Secuencial;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestSecuencial;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidad.Codigos;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Establecimiento extends AppCompatActivity {

    private Spinner spinnerEstablecimientos;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private ArrayAdapterSecuencial adapterSecuencial;

    private Toolbar toolbar;

    private Button botonContinuar;

    private EditText editTextCodigoEstablecimiento;

    private EditText editTextPuntoEmision;

    private EditText editTextDireccionEstablecimiento;

    private String codigoEstablecimientoEditado;

    private String puntoEmisionEditado;

    private String direccionEstablecimientoEditado;

    private Secuencial establecimientoSecuencialSeleccionado;

    private ClienteRestSecuencial.ServicioSecuencial servicioSecuencial;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_establecimiento);

        spinnerEstablecimientos = (Spinner) findViewById(R.id.spinner_establecimientos);
        editTextCodigoEstablecimiento = (EditText) findViewById(R.id.edit_text_codigo_establecimiento);
        editTextPuntoEmision = (EditText) findViewById(R.id.edit_text_punto_emision);
        editTextDireccionEstablecimiento = (EditText) findViewById(R.id.edit_text_direccion_establecimiento);

        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();
        servicioSecuencial = ClienteRestSecuencial.getServicioSecuencial();
        progressDialog = DialogProgreso.mostrarDialogProgreso(Establecimiento.this);
        Call<List<Secuencial>> callSecuencial = servicioSecuencial.obtenerEstablecimientosPorEmpresa(claseGlobalUsuario.getIdEmpresa());
        callSecuencial.enqueue(new Callback<List<Secuencial>>() {
            @Override
            public void onResponse(Call<List<Secuencial>> call, Response<List<Secuencial>> response) {
                if (response.isSuccessful()) {
                    List<Secuencial> secuenciales = response.body();
                    if (secuenciales != null && !secuenciales.isEmpty()) {

                        //Se asigna el origen de datos
                        adapterSecuencial = new ArrayAdapterSecuencial(getApplicationContext(), secuenciales);

                        //Se asigna el layout a inflar para cada elemento
                        //al momento de desplegar la lista
                        // adapterSecuencial.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Se setea el adaptador
                        spinnerEstablecimientos.setAdapter(adapterSecuencial);
                        spinnerEstablecimientos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
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
                progressDialog.dismiss();
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar_compuesta);
        botonContinuar = (Button) toolbar.findViewById(R.id.button_continuar);

        setSupportActionBar(toolbar);
        //Get a support ActionBar corresponding to this toolbar_compuesta

        //Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar);
        tituloToolbar.setText(getResources().getString(R.string.titulo_establecimientos));

        //Valida la información que edite el usuario de su información tributaria y regresa al menu de componentes de la factura
        botonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarInformacionEstablecimiento();
            }
        });
    }

    private void actualizarInformacionEstablecimiento() {
        Call<ResponseBody> responseBodyCall = null;

        if (editTextCodigoEstablecimiento.getText() != null && !editTextCodigoEstablecimiento.getText().toString().trim().equals("")) {
            codigoEstablecimientoEditado = editTextCodigoEstablecimiento.getText().toString();
        } else {
            codigoEstablecimientoEditado = establecimientoSecuencialSeleccionado.getCodigoEstablecimientoSecuencial();
        }
        if (editTextPuntoEmision.getText() != null && !editTextPuntoEmision.getText().toString().trim().equals("")) {
            puntoEmisionEditado = editTextPuntoEmision.getText().toString();
        } else {
            puntoEmisionEditado = establecimientoSecuencialSeleccionado.getPuntoEmisionSecuencial();
        }
        if (editTextDireccionEstablecimiento.getText() != null && !editTextDireccionEstablecimiento.getText().toString().trim().equals("")) {
            direccionEstablecimientoEditado = editTextDireccionEstablecimiento.getText().toString();
        } else {
            direccionEstablecimientoEditado = establecimientoSecuencialSeleccionado.getDireccionSecuencial();
        }
        responseBodyCall = servicioSecuencial.actualizarEstablecimiento(claseGlobalUsuario.getIdEmpresa(), establecimientoSecuencialSeleccionado.getIdSecuencial(), codigoEstablecimientoEditado, puntoEmisionEditado, direccionEstablecimientoEditado);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
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
                            Intent intent = new Intent(getApplicationContext(), InicioFacturacionElectronica.class);
                            intent.putExtra(String.valueOf(Codigos.CODIGO_ESTABLECIMIENTO_ACTUAL), establecimientoSecuencialSeleccionado);
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
        intent.putExtra(String.valueOf(Codigos.CODIGO_ESTABLECIMIENTO_ACTUAL), establecimientoSecuencialSeleccionado);
        if (establecimientoSecuencialSeleccionado != null) {
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED, intent);
        }
        this.finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

}
