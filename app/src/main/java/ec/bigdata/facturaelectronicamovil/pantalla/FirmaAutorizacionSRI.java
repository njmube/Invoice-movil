package ec.bigdata.facturaelectronicamovil.pantalla;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ec.bigdata.comprobanteelectronico.construccionxml.GeneradorComprobanteElectronicoXML;
import ec.bigdata.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico;
import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.dialogs.DialogConfirmacion;
import ec.bigdata.facturaelectronicamovil.dialogs.DialogConfirmacionUnBoton;
import ec.bigdata.facturaelectronicamovil.dialogs.DialogProgreso;
import ec.bigdata.facturaelectronicamovil.modelo.RespuestaSRIMovil;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestComprobanteElectronico;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidad.Codigos;
import ec.bigdata.facturaelectronicamovil.utilidad.Utilidades;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirmaAutorizacionSRI extends AppCompatActivity implements
        DialogConfirmacion.DialogConfirmacionComunicacion, DialogConfirmacionUnBoton.DialogConfirmacionUnBotonComunicacion {

    private ComprobanteElectronico comprobanteElectronico;

    private TextView textViewMensajeProcesoComprobantesElectronicos;

    private Button buttonConfirmarFirmarAutorizarSRI;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private ClienteRestComprobanteElectronico.ServicioComprobanteElectronico servicioComprobanteElectronico;

    private DialogFragment dialogFragmentConfirmacionFirmaAutorizacionSRI;

    private DialogFragment dialogFragmenResultadoFirmaAutorizacionSRI;

    private ProgressDialog progressDialog;

    private String nombreDocumentoXML;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firma_autorizacion_sri);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_simple);
        setSupportActionBar(toolbar);
        //Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar_simple);

        tituloToolbar.setText(getResources().getString(R.string.titulo_firma_autorizacion_sri));

        String htmlAsString = getString(R.string.mensaje_html_proceso_firma_autorizacion_sri);
        Spanned htmlAsSpanned = Html.fromHtml(htmlAsString);

        textViewMensajeProcesoComprobantesElectronicos = (TextView) findViewById(R.id.text_view_mensaje_proceso_comprobantes_electronicos);

        textViewMensajeProcesoComprobantesElectronicos.setText(htmlAsSpanned);

        buttonConfirmarFirmarAutorizarSRI = (Button) findViewById(R.id.button_confirmar_firma_autorizacion_sri);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey(String.valueOf(Codigos.CODIGO_FACTURA_ELECTRONICA))) {
                comprobanteElectronico = (ComprobanteElectronico) bundle.getSerializable(String.valueOf(Codigos.CODIGO_FACTURA_ELECTRONICA));
            }
        }
        buttonConfirmarFirmarAutorizarSRI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                dialogFragmentConfirmacionFirmaAutorizacionSRI = DialogConfirmacion.newInstance(
                        getResources().getString(R.string.titulo_confirmacion),
                        getResources().getString(R.string.mensaje_continuar_firma_autorizacion_sri), Boolean.FALSE);
                dialogFragmentConfirmacionFirmaAutorizacionSRI.show(fragmentManager, "DialogConfirmacion");
            }
        });

        servicioComprobanteElectronico = ClienteRestComprobanteElectronico.getServicioComprobanteElectronico();
        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();
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
    public void presionarBotonSI() {
        dialogFragmentConfirmacionFirmaAutorizacionSRI.dismiss();
        progressDialog = DialogProgreso.mostrarDialogProcesComprobantesElectronicos(FirmaAutorizacionSRI.this);
        GeneradorComprobanteElectronicoXML generadorComprobanteElectronicoXML = new GeneradorComprobanteElectronicoXML();
        String stringComprobante = generadorComprobanteElectronicoXML.generarStringComprobante(comprobanteElectronico, "1.1.0");
        nombreDocumentoXML = comprobanteElectronico.getInformacionTributariaComprobanteElectronico().getClaveAcceso().concat(".xml");

        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput(nombreDocumentoXML, Context.MODE_PRIVATE);
            outputStream.write(stringComprobante.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        File fileXML = new File(getFilesDir() + "/" + nombreDocumentoXML);

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), fileXML);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("archivo", fileXML.getName(), requestFile);

        Call<ResponseBody> responseBodyCall = servicioComprobanteElectronico.archivo(body, claseGlobalUsuario.getIdEmpresa());
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();


                    JsonParser parser = new JsonParser();
                    JsonObject o = null;
                    String s = null;
                    RespuestaSRIMovil respuestaSRIMovil = null;
                    try {
                        s = new String(response.body().bytes());
                        Gson gson = new GsonBuilder().serializeNulls().create();
                        respuestaSRIMovil = gson.fromJson(s, RespuestaSRIMovil.class);

                        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                        dialogFragmenResultadoFirmaAutorizacionSRI = DialogConfirmacionUnBoton.newInstance(
                                getResources().getString(R.string.titulo_mensaje_resultado_firma_autorizacion_sri),
                                Utilidades.formatearRespuestaSRIMovil(respuestaSRIMovil));
                        dialogFragmenResultadoFirmaAutorizacionSRI.show(fragmentManager, "DialogConfirmacionUnBoton");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
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
    public void presionarBotonCancelar() {

    }

    @Override
    public void presionarBotonContinuar() {
        //Se borra el XML y PDF
        File fileXML = new File(getFilesDir() + "/" + nombreDocumentoXML);
        if (fileXML.exists()) {
            fileXML.delete();
        }

        File filePDF = new File(getFilesDir() + "/" + nombreDocumentoXML.replace(".xml", ".pdf"));
        if (filePDF.exists()) {
            filePDF.delete();
        }
    }
}
