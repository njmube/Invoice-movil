package ec.bigdata.facturaelectronicamovil.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.modelo.ComprobanteElectronico;
import ec.bigdata.facturaelectronicamovil.utilidad.Codigos;

/**
 * Created by DavidLeonardo on 22/8/2016.
 */
public class DialogDescargaArchivo extends DialogFragment {

    private ImageButton imageButtonDescargaPDFRIDE;

    private ImageButton imageButtonDescargaXML;

    private DialogDescargaArchivoComunicacion dialogDescargaArchivoComunicacion;

    private ComprobanteElectronico comprobanteElectronico;

    public interface DialogDescargaArchivoComunicacion {
        void iniciarDescargaArchivo(ComprobanteElectronico comprobanteElectronico, Integer tipoArchivo);
    }

    public DialogDescargaArchivo() {

    }

    public static DialogDescargaArchivo newInstance(ComprobanteElectronico comprobanteElectronico) {

        DialogDescargaArchivo dialogDescargaArchivo = new DialogDescargaArchivo();

        Bundle args = new Bundle();
        args.putSerializable("comprobante", comprobanteElectronico);

        dialogDescargaArchivo.setArguments(args);
        return dialogDescargaArchivo;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogDescargaArchivoComunicacion = (DialogDescargaArchivoComunicacion) getActivity();
        comprobanteElectronico = (ComprobanteElectronico) getArguments().getSerializable("comprobante");

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return crearDialogDescargaArchivo();
    }

    public AlertDialog crearDialogDescargaArchivo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_descarga_archivo, null);
        builder.setView(v);
        imageButtonDescargaPDFRIDE = (ImageButton) v.findViewById(R.id.image_button_descarga_pdfride);
        imageButtonDescargaXML = (ImageButton) v.findViewById(R.id.image_button_descarga_xml);

        imageButtonDescargaPDFRIDE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDescargaArchivoComunicacion.iniciarDescargaArchivo(comprobanteElectronico, Codigos.CODIGO_TIPO_ARCHIVO_FIRMADO_COMPROBANTE_ELECTRONICO);
            }
        });

        imageButtonDescargaXML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDescargaArchivoComunicacion.iniciarDescargaArchivo(comprobanteElectronico, Codigos.CODIGO_TIPO_ARCHIVO_RESPUESTA);
            }
        });
        return builder.create();

    }

}

