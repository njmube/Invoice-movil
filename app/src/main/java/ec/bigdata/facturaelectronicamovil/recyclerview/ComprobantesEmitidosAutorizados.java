package ec.bigdata.facturaelectronicamovil.recyclerview;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.dialogs.DialogDescargaArchivo;
import ec.bigdata.facturaelectronicamovil.dialogs.DialogDetalleComprobante;
import ec.bigdata.facturaelectronicamovil.dialogs.DialogListaCheckBox;
import ec.bigdata.facturaelectronicamovil.dialogs.DialogProgreso;
import ec.bigdata.facturaelectronicamovil.intentservice.ServicioDescarga;
import ec.bigdata.facturaelectronicamovil.modelo.ComprobanteElectronico;
import ec.bigdata.facturaelectronicamovil.modelo.CorreoCliente;
import ec.bigdata.facturaelectronicamovil.parcelable.Descarga;
import ec.bigdata.facturaelectronicamovil.personalizacion.MensajePersonalizado;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestArchivo;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestCorreoAdicionalCliente;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestNotificacion;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidad.Codigos;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComprobantesEmitidosAutorizados extends AppCompatActivity implements DialogDescargaArchivo.DialogDescargaArchivoComunicacion, DialogListaCheckBox.DialogListaCheckBoxComunicacion {

    public TextView textViewProgresoDescarga;

    public ProgressBar progressBarProgresoDescarga;

    private FloatingActionButton floatingActionButton;

    private ComprobantesEmitidosAutorizadosFragmento comprobantesEmitidosAutorizadosFragmento;

    private FragmentTransaction fragmentTransaction;

    private FragmentManager fragmentManager;

    private ProgressDialog progressDialog;

    private ClaseGlobalUsuario claseGlobalUsuario;

    public static final String MENSAJE_PROGRESO = "MENSAJE_PROGRESO";

    private DialogDescargaArchivo dialogDescargaArchivo;

    private DialogListaCheckBox dialogListaCheckBox;

    private DialogDetalleComprobante dialogDetalleComprobante;

    private ClienteRestCorreoAdicionalCliente.ServicioCorreoAdicional servicioCorreoAdicional;

    private List<String> correosAdicionales;

    private ClienteRestNotificacion.ServicioNotificacionComprobante servicioNotificacionComprobante;

    private ClienteRestArchivo.ServicioArchivo servicioArchivo;

    private ComprobanteElectronico comprobanteElectronico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprobantes_emitidos_autorizados);
        registrarReceiver();
        textViewProgresoDescarga = (TextView) findViewById(R.id.text_view_progreso_descarga_archivo);
        progressBarProgresoDescarga = (ProgressBar) findViewById(R.id.progress_bar_descarga_archivo);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floating_action_button_ir_inicio);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_simple);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar_simple);

        tituloToolbar.setText(getResources().getString(R.string.titulo_repositorio_comprobantes_emitidos_autorizados));

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        comprobantesEmitidosAutorizadosFragmento = new ComprobantesEmitidosAutorizadosFragmento();
        fragmentTransaction.replace(R.id.frame_layout_comprobantes_emitidos_autorizados, comprobantesEmitidosAutorizadosFragmento, "ComprobantesEmitidosAutorizados");
        fragmentTransaction.commit();

        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();

        servicioCorreoAdicional = ClienteRestCorreoAdicionalCliente.getServicioCliente();
        servicioNotificacionComprobante = ClienteRestNotificacion.getServicioNotificacionComprobante();
        servicioArchivo = ClienteRestArchivo.getServicioArchivo();

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
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(comprobantesEmitidosAutorizadosFragmento);
        fragmentTransaction.commit();
        this.finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    public Fragment obtenerFragment() {
        return comprobantesEmitidosAutorizadosFragmento;
    }

    @Override
    public void iniciarDescargaArchivo(ComprobanteElectronico comprobanteElectronico, Integer tipoArchivo) {
        Intent intent = new Intent(this, ServicioDescarga.class);
        intent.putExtra(String.valueOf(Codigos.CODIGO_COMPROBANTE_ELECTRONICO_DESCARGA), comprobanteElectronico);
        intent.putExtra(String.valueOf(Codigos.CODIGO_TIPO_ARCHIVO), tipoArchivo);
        dialogDescargaArchivo.dismiss();
        startService(intent);
    }

    public void mostrarDialogDescargaArchivo(ComprobanteElectronico comprobanteElectronico) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        dialogDescargaArchivo = DialogDescargaArchivo.newInstance(comprobanteElectronico);
        dialogDescargaArchivo.show(fragmentManager, "DialogDescargaArchivo");
    }

    public void mostrarDialogListaCheckBoxReenvioComprobante(ComprobanteElectronico comprobanteElectronico) {
        this.comprobanteElectronico = comprobanteElectronico;
        correosAdicionales = new ArrayList<>();
        progressDialog = DialogProgreso.mostrarDialogProgreso(ComprobantesEmitidosAutorizados.this);
        Call<List<CorreoCliente>> listCall = servicioCorreoAdicional.obtenerCorreosElectronicosPorIdentificacionCliente(claseGlobalUsuario.getIdEmpresa(), comprobanteElectronico.getRucReceptor());
        listCall.enqueue(new Callback<List<CorreoCliente>>() {
            @Override
            public void onResponse(Call<List<CorreoCliente>> call, Response<List<CorreoCliente>> response) {
                if (response.isSuccessful()) {


                    String stringArrayCorreos[] = null;

                    List<CorreoCliente> correoClienteList = response.body();
                    if (correoClienteList != null && !correoClienteList.isEmpty()) {
                        stringArrayCorreos = new String[correoClienteList.size()];
                        for (int i = 0; i < correoClienteList.size(); i++) {
                            stringArrayCorreos[i] = correoClienteList.get(i).getCorreoCliente();
                        }
                    }

                    if (stringArrayCorreos != null && stringArrayCorreos.length > 0) {
                        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                        dialogListaCheckBox = DialogListaCheckBox.newInstance(stringArrayCorreos);
                        dialogListaCheckBox.show(fragmentManager, "DialogReenvioComprobante");
                    } else {

                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<CorreoCliente>> call, Throwable t) {
                call.cancel();
                progressDialog.dismiss();
            }

        });
    }

    public void mostrarDialogDetallesComprobante(ComprobanteElectronico comprobanteElectronico) {
        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
        }
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        dialogDetalleComprobante = DialogDetalleComprobante.newInstance(comprobanteElectronico);
        dialogDetalleComprobante.show(fragmentManager, "DialogDetalleComprobante");
    }

    public void compartirComprobante(ComprobanteElectronico comprobanteElectronico) {
        //Se comprueba si existen los comprobantes en el directorio para no volverlos a descargar.
        final String nombreDocumento = "ZIP_XML_PDF_" + comprobanteElectronico.getClaveAccesoComprobanteElectronico().concat(".zip");

        //Consulta los archivos XML y PDF desde el servidor.
        Call<ResponseBody> responseBodyCall = servicioArchivo.obtenerZipRIDERespuestaSRI(comprobanteElectronico.getIdComprobanteElectronico());
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //Se escribe el archivo.
                    try {
                        FileOutputStream outputStream = null;
                        outputStream = openFileOutput(nombreDocumento, Context.MODE_PRIVATE);
                        outputStream.write(response.body().bytes());
                        outputStream.close();

                        Uri uri = Uri.parse("content://ec.bigdata.facturaelectronicamovil.contentprovider/" + nombreDocumento);

                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        shareIntent.setType("*/*");
                        startActivityForResult(Intent.createChooser(shareIntent, "Compartir comprobantes PDF y XML comprimidos"), Codigos.CODIGO_COMPARTIR_ZIP_COMPROBANTE_ELECTRONICO);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
            }
        });
    }


    private void registrarReceiver() {
        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MENSAJE_PROGRESO);
        bManager.registerReceiver(broadcastReceiver, intentFilter);
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(MENSAJE_PROGRESO)) {

                Descarga descarga = intent.getParcelableExtra("descarga");
                progressBarProgresoDescarga.setVisibility(View.VISIBLE);
                textViewProgresoDescarga.setVisibility(View.VISIBLE);
                progressBarProgresoDescarga.setProgress(descarga.getProgress());

                if (descarga.getProgress() == 100) {
                    progressBarProgresoDescarga.setVisibility(View.INVISIBLE);
                    textViewProgresoDescarga.setVisibility(View.INVISIBLE);
                    MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_INFORMACION, "Archivo descargado.");

                } else {
                    textViewProgresoDescarga.setText(String.format("Descargado (%d/%d) MB", descarga.getCurrentFileSize(), descarga.getTotalFileSize()));

                }
            }
        }
    };

    @Override
    public void presionarBotonSI(ArrayList<String> listaSeleccionados) {
        Call<ResponseBody> responseBodyCall = servicioNotificacionComprobante.reenviarComprobanteEmitidoAutorizado(this.comprobanteElectronico.getIdComprobanteElectronico(), listaSeleccionados);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String contenido = null;
                    try {
                        contenido = new String(response.body().bytes());
                        JsonObject jsonObject = new JsonParser().parse(contenido).getAsJsonObject();
                        if (jsonObject.get("estado").getAsBoolean() == true) {

                            MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_INFORMACION, "Comprobante reenviado.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
            }
        });

    }
}

