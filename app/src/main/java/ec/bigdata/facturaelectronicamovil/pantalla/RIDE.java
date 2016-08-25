package ec.bigdata.facturaelectronicamovil.pantalla;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import ec.bigdata.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico;
import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.personalizacion.MensajePersonalizado;
import ec.bigdata.facturaelectronicamovil.utilidad.Codigos;

/**
 * Created by DavidLeonardo on 21/8/2016.
 */
public class RIDE extends AppCompatActivity {

    private ImageView imageViewPDFRIDE;

    private Button buttonFirmarAutorizarSRI;

    private ec.bigdata.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico comprobanteElectronico;

    private String nombreDocumentoPDF;

    private byte[] bytesPDFRIDE;

    private boolean rideVerificado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_comprobante_electroncio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_simple);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar_simple);

        tituloToolbar.setText(getResources().getString(R.string.titulo_pdf_ride_previsualizacion));

        buttonFirmarAutorizarSRI = (Button) findViewById(R.id.button_continuar_firmar_autorizar_sri);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            comprobanteElectronico = (ComprobanteElectronico) bundle.getSerializable(String.valueOf(Codigos.CODIGO_EXTRA_COMPROBANTE_ELECTRONICO));
        }
        imageViewPDFRIDE = (ImageView) findViewById(R.id.image_button_pdf_ride);
        imageViewPDFRIDE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarRIDE();
                rideVerificado = true;
                File filePDF = new File(getFilesDir() + "/" + nombreDocumentoPDF);
                Uri uri = Uri.parse("content://ec.bigdata.facturaelectronicamovil.contentprovider/" + nombreDocumentoPDF);
                String tipoDocumento = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(filePDF.getAbsolutePath()));
                Intent intentTargetPDF = new Intent(Intent.ACTION_VIEW);
                intentTargetPDF.setDataAndType(uri, tipoDocumento);
                intentTargetPDF.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                //Verifica que el intent pueda resolver a una actividad.
                if (intentTargetPDF.resolveActivity(getPackageManager()) != null) {
                    Intent intentChooser = Intent.createChooser(intentTargetPDF, getResources().getString(R.string.etiqueta_ver_pdf_ride));
                    startActivity(intentChooser);
                } else {
                    MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ADVERTENCIA, "No se ha encontrado una aplicación que pueda abrir el documento.");
                }
            }
        });
        buttonFirmarAutorizarSRI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FirmaAutorizacionSRI.class);
                intent.putExtra(String.valueOf(Codigos.CODIGO_EXTRA_COMPROBANTE_ELECTRONICO), comprobanteElectronico);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });


    }

    private void generarRIDE() {
        if (bytesPDFRIDE == null) {
            nombreDocumentoPDF = comprobanteElectronico.getInformacionTributariaComprobanteElectronico().getClaveAcceso().concat(".pdf");
            ec.bigdata.ride.bordes.PrincipalRIDE pdfRIDE = new ec.bigdata.ride.bordes.PrincipalRIDE();
            FileOutputStream outputStream = null;
            try {
                bytesPDFRIDE = pdfRIDE.generarRIDEPDFComprobante(comprobanteElectronico, null, null, null);
                try {
                    outputStream = openFileOutput(nombreDocumentoPDF, Context.MODE_PRIVATE);
                    outputStream.write(bytesPDFRIDE);
                    outputStream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_opciones_pdf_ride, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.item_compartir:
                generarRIDE();
                rideVerificado = true;
                File filePDF = new File(getFilesDir() + "/" + nombreDocumentoPDF);
                Uri uri = Uri.parse("content://ec.bigdata.facturaelectronicamovil.contentprovider/" + nombreDocumentoPDF);
                String tipoDocumento = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(filePDF.getAbsolutePath()));
                Intent compartir = new Intent(Intent.ACTION_SEND);
                compartir.putExtra(Intent.EXTRA_TEXT, nombreDocumentoPDF);
                compartir.putExtra(Intent.EXTRA_STREAM, uri);
                compartir.setType(tipoDocumento);
                startActivity(Intent.createChooser(compartir, "Compartir PDF RIDE"));
                break;
            case R.id.item_copiar:
                generarRIDE();
                rideVerificado = true;
                File file = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS), "RIDES_FACTURA_ELECTRONICA");
                if (!file.exists()) {
                    file.mkdir();
                }
                File fileOrigen = new File(getFilesDir() + "/" + nombreDocumentoPDF);
                File fileDestino = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "RIDES_FACTURA_ELECTRONICA" + File.separator + nombreDocumentoPDF);
                try {
                    ec.bigdata.facturaelectronicamovil.utilidad.Utilidades.exportFile(fileOrigen, fileDestino);
                    MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_INFORMACION, "El PDF se copió en la siguiente ruta: " + fileDestino.getAbsolutePath());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }


}
