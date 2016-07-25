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

import java.io.IOException;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.dialogs.DialogProgreso;
import ec.bigdata.facturaelectronicamovil.interfaz.ServicioEmpresa;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestEmpresa;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidad.Personalizacion;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InformacionEmisor extends AppCompatActivity {

    private Toolbar toolbar;

    private EditText editTextRazonSocial;

    private EditText editTextNombreComercial;

    private Switch switchObligadoLlevarContabilidad;

    private EditText editTextDireccionEmpresa;

    private EditText editTextNumeroResolucion;

    private TextView textViewObligadoLlevarContabilidad;

    private Button botonContinuar;

    private String razonSocialActualizado;

    private String nombreComercialActualizado;

    private String direccionMatrizActualizada;

    private String numeroResolucion;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private String llevaContabilidad;

    private ServicioEmpresa clienteRestEmpresa;

    private boolean validadoInformacionTributaria;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        validadoInformacionTributaria = false;
        setContentView(R.layout.activity_informacion_emisor);
        editTextRazonSocial = (EditText) findViewById(R.id.edit_text_razon_social_emisor);
        editTextNombreComercial = (EditText) findViewById(R.id.edit_text_nombre_comercial_emisor);
        switchObligadoLlevarContabilidad = (Switch) findViewById(R.id.switch_obligado_llevar_contabilidad_emisor);
        editTextDireccionEmpresa = (EditText) findViewById(R.id.edit_text_direccion_empresa_emisor);
        editTextNumeroResolucion = (EditText) findViewById(R.id.edit_text_numero_resolucion_emisor);
        textViewObligadoLlevarContabilidad = (TextView) findViewById(R.id.text_view_obligado_llevar_contabilidad_emisor);
        toolbar = (Toolbar) findViewById(R.id.toolbar_compuesta);
        setSupportActionBar(toolbar);

        clienteRestEmpresa = ClienteRestEmpresa.getServicioEmpresa();

        botonContinuar = (Button) toolbar.findViewById(R.id.button_continuar);

        //Valida la información que edite el usuario de su información tributaria y regresa al menu de componentes de la factura
        botonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarBotonContinuar();
            }
        });

        //Get a support ActionBar corresponding to this toolbar_compuesta

        //Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar);
        tituloToolbar.setText(getResources().getString(R.string.titulo_informacion_tributaria));
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

                Call<ResponseBody> call_response_body = clienteRestEmpresa.actualizarObligadoLlevarContabilidad(claseGlobalUsuario.getIdEmpresa(), isChecked);
                call_response_body.enqueue(new Callback<ResponseBody>() {
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
                                    claseGlobalUsuario.setObligadoLlevarContabilidad(isChecked);

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
        });
        progressDialog.dismiss();
    }

    private void validarBotonContinuar() {

        String errores_edicion_info_tributaria = "";
        if (editTextNombreComercial.getText() != null && !editTextNombreComercial.getText().toString().trim().equals("")) {
            nombreComercialActualizado = editTextNombreComercial.getText().toString();
        } else {
            nombreComercialActualizado = claseGlobalUsuario.getNombreComercial();
        }
        if (editTextRazonSocial.getText() != null && !editTextRazonSocial.getText().toString().trim().equals("")) {
            razonSocialActualizado = editTextRazonSocial.getText().toString();
        } else {
            razonSocialActualizado = claseGlobalUsuario.getRazonSocial();
        }

        if (editTextDireccionEmpresa.getText() != null && !editTextDireccionEmpresa.getText().toString().trim().equals("")) {
            direccionMatrizActualizada = editTextDireccionEmpresa.getText().toString();
        } else {
            direccionMatrizActualizada = claseGlobalUsuario.getDireccionMatriz();
        }

        if (editTextNumeroResolucion.getText() != null && !editTextNumeroResolucion.getText().toString().trim().equals("")) {
            numeroResolucion = editTextNumeroResolucion.getText().toString();
        } else {
            numeroResolucion = claseGlobalUsuario.getNumeroResolucion();
        }
        if (errores_edicion_info_tributaria.equals("")) {

            Call<ResponseBody> call_response_body = clienteRestEmpresa.actualizarEmpresa(
                    claseGlobalUsuario.getIdEmpresa(), nombreComercialActualizado, razonSocialActualizado, direccionMatrizActualizada
                    , numeroResolucion);
            call_response_body.enqueue(new Callback<ResponseBody>() {
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
                                claseGlobalUsuario.setNombreComercial(nombreComercialActualizado);
                                claseGlobalUsuario.setRazonSocial(razonSocialActualizado);
                                claseGlobalUsuario.setDireccionMatriz(direccionMatrizActualizada);
                                claseGlobalUsuario.setNumeroResolucion(numeroResolucion);

                                Intent intent = new Intent(getApplicationContext(), InicioFacturacionElectronica.class);
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

        } else {
            Personalizacion.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), Personalizacion.TOAST_ERROR, errores_edicion_info_tributaria);

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
