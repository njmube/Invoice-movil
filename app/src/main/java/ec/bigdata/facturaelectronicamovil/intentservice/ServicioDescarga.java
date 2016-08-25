package ec.bigdata.facturaelectronicamovil.intentservice;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.webkit.MimeTypeMap;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.modelo.ComprobanteElectronico;
import ec.bigdata.facturaelectronicamovil.parcelable.Descarga;
import ec.bigdata.facturaelectronicamovil.recyclerview.ComprobantesEmitidosAutorizados;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestArchivo;
import ec.bigdata.facturaelectronicamovil.utilidad.Codigos;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DavidLeonardo on 22/8/2016.
 */
public class ServicioDescarga extends IntentService {

    private ClienteRestArchivo.ServicioArchivo servicioArchivo;
    private NotificationCompat.Builder nBuilder;
    private NotificationManager notificationManager;
    private int totalTamanioArchivos;
    private ComprobanteElectronico comprobanteElectronico;
    private Integer tipoArchivo;
    private File outputFile;

    public ServicioDescarga() {
        super("ServicioDescarga");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        servicioArchivo = ClienteRestArchivo.getServicioArchivo();

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            comprobanteElectronico = (ComprobanteElectronico) bundle.getSerializable(String.valueOf(Codigos.CODIGO_COMPROBANTE_ELECTRONICO_DESCARGA));
            tipoArchivo = bundle.getInt(String.valueOf(Codigos.CODIGO_TIPO_ARCHIVO));
        }
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        nBuilder
                = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_file_download_black_48dp)
                .setContentTitle("Descarga")
                .setContentText("Descargando comprobante electrónico...")
                .setAutoCancel(true);
        notificationManager.notify(0, nBuilder.build());

        iniciarDescarga();

    }

    public void iniciarDescarga() {
        Call<ResponseBody> responseBodyCall = servicioArchivo.obtenerArchivoPorBytesPorTipoArchivo(comprobanteElectronico.getIdComprobanteElectronico(), tipoArchivo);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        procesarArchivo(response.body());

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


    private void procesarArchivo(ResponseBody body) throws IOException {

        String extensionArchivo = tipoArchivo.equals(1) ? ".xml" : ".pdf";
        int count;
        byte data[] = new byte[1024 * 4];
        long tamanioArchivo = body.contentLength();
        InputStream bis = new BufferedInputStream(body.byteStream());
        outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), comprobanteElectronico.getClaveAccesoComprobanteElectronico() + extensionArchivo);
        OutputStream output = new FileOutputStream(outputFile);
        long total = 0;
        long startTime = System.currentTimeMillis();
        int timeCount = 1;
        while ((count = bis.read(data)) != -1) {
            total += count;
            totalTamanioArchivos = (int) (tamanioArchivo / (Math.pow(1024, 2)));
            double current = Math.round(total / (Math.pow(1024, 2)));

            int progress = (int) ((total * 100) / tamanioArchivo);

            long currentTime = System.currentTimeMillis() - startTime;

            Descarga descarga = new Descarga();
            descarga.setTotalFileSize(totalTamanioArchivos);

            if (currentTime > 1000 * timeCount) {

                descarga.setCurrentFileSize((int) current);
                descarga.setProgress(progress);
                sendNotification(descarga);
                timeCount++;
            }
            output.write(data, 0, count);
        }
        onDownloadComplete();
        output.flush();
        output.close();
        bis.close();
    }


    private void sendNotification(Descarga descarga) {

        sendIntent(descarga);
        nBuilder.setProgress(100, descarga.getProgress(), false);
        nBuilder.setContentText(String.format("Descargado (%d/%d) MB", descarga.getCurrentFileSize(), descarga.getTotalFileSize()));
        notificationManager.notify(0, nBuilder.build());
    }


    private void sendIntent(Descarga descarga) {

        Intent intent = new Intent(ComprobantesEmitidosAutorizados.MENSAJE_PROGRESO);
        intent.putExtra("descarga", descarga);
        LocalBroadcastManager.getInstance(ServicioDescarga.this).sendBroadcast(intent);
    }

    private void onDownloadComplete() {

        Descarga descarga = new Descarga();
        descarga.setProgress(100);
        sendIntent(descarga);
        notificationManager.cancel(0);
        nBuilder.setProgress(0, 0, false);
        nBuilder.setContentText("Archivo descargado");
        StringBuilder stringBuilderContenido = new StringBuilder();
        String tipoDocumento = tipoArchivo.equals(2) ? "PDF" : "XML";
        stringBuilderContenido.append("Archivo " + tipoDocumento);
        stringBuilderContenido.append("Nombre " + comprobanteElectronico.getClaveAccesoComprobanteElectronico());
        stringBuilderContenido.append("Pulse aquí para abrir el archivo");
        nBuilder.setSubText(stringBuilderContenido.toString());
        //nBuilder.setSubText();
        String mimeTypeFromExtension = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(outputFile.getAbsolutePath()));

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(outputFile), mimeTypeFromExtension);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        nBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;
        nBuilder.setContentIntent(contentIntent);
        notificationManager.notify(0, nBuilder.build());


    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }
}
