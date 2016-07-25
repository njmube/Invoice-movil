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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import ec.bigdata.comprobanteelectronico.esquema.comprobantebase.ImpuestoComprobanteElectronico;
import ec.bigdata.comprobanteelectronico.esquema.factura.InformacionFactura;
import ec.bigdata.comprobanteelectronico.esquema.implementacion.ImplementacionFactura;
import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.utilidad.Calculos;
import ec.bigdata.facturaelectronicamovil.utilidad.Codigos;
import ec.bigdata.facturaelectronicamovil.utilidad.Personalizacion;

public class ResumenFacturaElectronica extends AppCompatActivity {

    private TextView textViewSubTotal12;

    private TextView textViewSubTotal14;

    private TextView textViewSubTotal0;

    private TextView textViewSubTotalNoObjetoIVA;

    private TextView textViewSubTotalExentoIVA;

    private TextView textViewSubTotalSinImpuestos;

    private TextView textViewTotalDescuento;

    private TextView textViewICE;

    private TextView textViewIVA12;

    private TextView textViewIVA14;

    private TextView textViewIRBPNR;

    private Switch switchPropina;

    private TextView textViewValorPropina;

    private TextView textViewValorTotal;

    private ImageView imageViewPDFRIDE;

    private ImplementacionFactura implementacionFactura;

    private InformacionFactura informacionFactura;

    private String nombreDocumentoPDF;

    private byte[] bytesPDFRIDE;

    private boolean rideVerificado;

    private Button buttonContinuarFirmaAutorizacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_factura_electronica);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_simple);
        setSupportActionBar(toolbar);
        //Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar);

        tituloToolbar.setText(getResources().getString(R.string.titulo_resumen_factura));


        textViewSubTotal12 = (TextView) findViewById(R.id.text_view_valor_subtotal_12);

        textViewSubTotal14 = (TextView) findViewById(R.id.text_view_valor_subtotal_14);

        textViewSubTotal0 = (TextView) findViewById(R.id.text_view_valor_subtotal_0);

        textViewSubTotalNoObjetoIVA = (TextView) findViewById(R.id.text_view_valor_subtototal_no_objeto_iva);

        textViewSubTotalExentoIVA = (TextView) findViewById(R.id.text_view_valor_subtototal_exento_iva);

        textViewSubTotalSinImpuestos = (TextView) findViewById(R.id.text_view_valor_subtototal_sin_impuestos);

        textViewTotalDescuento = (TextView) findViewById(R.id.text_view_valor_total_descuento);

        textViewICE = (TextView) findViewById(R.id.text_view_valor_ice);

        textViewIVA12 = (TextView) findViewById(R.id.text_view_valor_iva_12);

        textViewIVA14 = (TextView) findViewById(R.id.text_view_valor_iva_14);

        textViewIRBPNR = (TextView) findViewById(R.id.text_view_valor_irbpnr);

        switchPropina = (Switch) findViewById(R.id.switch_propina);

        switchPropina.setTextOn("SI");

        switchPropina.setTextOff("NO");

        textViewValorPropina = (TextView) findViewById(R.id.text_view_valor_propina);

        textViewValorTotal = (TextView) findViewById(R.id.text_view_valor_importe_total);

        imageViewPDFRIDE = (ImageView) findViewById(R.id.image_button_pdf_ride);

        rideVerificado = false;

        buttonContinuarFirmaAutorizacion = (Button) findViewById(R.id.button_continuar_firma_autorizacion_sri);
        buttonContinuarFirmaAutorizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rideVerificado) {

                    Intent intent = new Intent(getApplicationContext(), FirmaAutorizacionSRI.class);
                    intent.putExtra(String.valueOf(Codigos.CODIGO_FACTURA_ELECTRONICA), implementacionFactura);
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                } else {
                    Personalizacion.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), Personalizacion.TOAST_ADVERTENCIA, "Por favor verificar el PDF-RIDE.");

                }
            }
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            implementacionFactura = (ImplementacionFactura) bundle.getSerializable(String.valueOf(Codigos.CODIGO_FACTURA_ELECTRONICA));
            if (implementacionFactura != null) {
                informacionFactura = implementacionFactura.getInformacionFactura();

                textViewSubTotalSinImpuestos.setText(informacionFactura.getTotalSinImpuestos());

                textViewTotalDescuento.setText(informacionFactura.getTotalDescuento());

                textViewValorTotal.setText(informacionFactura.getImporteTotal());

                List<ImpuestoComprobanteElectronico> impuestoComprobanteElectronicoList = informacionFactura.getTotalConImpuesto();
                ImpuestoComprobanteElectronico impuestoComprobante = null;
                for (int i = 0; i < impuestoComprobanteElectronicoList.size(); i++) {
                    impuestoComprobante = impuestoComprobanteElectronicoList.get(i);
                    if (impuestoComprobante.getCodigo().equals("2") && impuestoComprobante.getCodigoPorcentaje().equals("0")) {
                        textViewSubTotal0.setText(impuestoComprobante.getBaseImponible());
                    } else if (impuestoComprobante.getCodigo().equals("2") && impuestoComprobante.getCodigoPorcentaje().equals("2")) {
                        textViewSubTotal12.setText(impuestoComprobante.getBaseImponible());
                        textViewIVA12.setText(impuestoComprobante.getValor());
                    } else if (impuestoComprobante.getCodigo().equals("2") && impuestoComprobante.getCodigoPorcentaje().equals("3")) {
                        textViewSubTotal14.setText(impuestoComprobante.getBaseImponible());
                        textViewIVA14.setText(impuestoComprobante.getValor());
                    } else if (impuestoComprobante.getCodigo().equals("2") && impuestoComprobante.getCodigoPorcentaje().equals("6")) {
                        textViewSubTotalNoObjetoIVA.setText(impuestoComprobante.getBaseImponible());

                    } else if (impuestoComprobante.getCodigo().equals("2") && impuestoComprobante.getCodigoPorcentaje().equals("7")) {
                        textViewSubTotalExentoIVA.setText(impuestoComprobante.getBaseImponible());

                    } else if (impuestoComprobante.getCodigo().equals("3")) {
                        textViewICE.setText(impuestoComprobante.getBaseImponible());
                    }

                }
            }
        }
        switchPropina.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BigDecimal valorPropina = BigDecimal.ZERO;
                BigDecimal importeTotal = BigDecimal.ZERO;
                importeTotal = new BigDecimal(informacionFactura.getImporteTotal());
                valorPropina = Calculos.calcularPropina(informacionFactura.getTotalSinImpuestos());
                if (isChecked) {

                    importeTotal = importeTotal.add(valorPropina);
                    informacionFactura.setPropina(valorPropina.toString());
                    informacionFactura.setImporteTotal(importeTotal.toString());
                    implementacionFactura.setInformacionFactura(informacionFactura);
                    textViewValorPropina.setText(valorPropina.toString());
                    textViewValorTotal.setText(importeTotal.toString());

                } else {
                    importeTotal = importeTotal.subtract(valorPropina);
                    informacionFactura.setPropina("0");
                    informacionFactura.setImporteTotal(importeTotal.toString());
                    implementacionFactura.setInformacionFactura(informacionFactura);
                    textViewValorPropina.setText(getResources().getString(R.string.valor_cero));
                    textViewValorTotal.setText(importeTotal.toString());
                }
            }
        });


        imageViewPDFRIDE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarRIDE();
                rideVerificado = true;
                File filePDF = new File(getFilesDir() + "/" + nombreDocumentoPDF);
                String tipoDocumento = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(filePDF.getAbsolutePath()));
                Intent intentTargetPDF = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("content://ec.bigdata.facturaelectronicamovil.contentprovider/" + nombreDocumentoPDF);
                intentTargetPDF.setDataAndType(uri, tipoDocumento);
                intentTargetPDF.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                Intent intentChooser = Intent.createChooser(intentTargetPDF, getResources().getString(R.string.etiqueta_ver_pdf_ride));
                startActivity(intentChooser);

            }
        });
    }

    private void generarRIDE() {
        if (bytesPDFRIDE == null) {
            nombreDocumentoPDF = implementacionFactura.getInformacionTributariaComprobanteElectronico().getClaveAcceso().concat(".pdf");
            ec.bigdata.ride.bordes.PrincipalRIDE pdfRIDE = new ec.bigdata.ride.bordes.PrincipalRIDE();
            FileOutputStream outputStream = null;
            try {
                bytesPDFRIDE = pdfRIDE.generarRIDEPDFComprobante(implementacionFactura, null, null, null);
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
                    Personalizacion.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), Personalizacion.TOAST_INFORMACION, "El PDF se copió en la siguiente ruta: /Descargas/RIDES_FACTURA_ELECTRONICA/" + nombreDocumentoPDF);

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
