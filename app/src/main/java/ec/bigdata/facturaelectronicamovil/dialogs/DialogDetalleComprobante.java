package ec.bigdata.facturaelectronicamovil.dialogs;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.modelo.ComprobanteElectronico;
import ec.bigdata.facturaelectronicamovil.utilidad.Utilidades;

/**
 * Created by DavidLeonardo on 23/8/2016.
 */
public class DialogDetalleComprobante extends DialogFragment {

    private TextView textViewSecuencial;
    private TextView textViewFechaEmision;
    private TextView textViewTipo;
    private TextView textViewRUCReceptor;
    private TextView textViewRazonSocialReceptor;
    private TextView textViewClaveAcceso;
    private TextView textViewNumeroAutorizacion;
    private TextView textViewFechaAutorizacion;
    private TextView textViewObservacion;
    private Button buttonContinuar;

    private ComprobanteElectronico comprobanteElectronico;

    private DialogDetalleComprobanteComunicacion dialogDetalleComprobanteComunicacion;

    public interface DialogDetalleComprobanteComunicacion {
        void cambiarOrientacionPantalla();
    }

    public static DialogDetalleComprobante newInstance(ComprobanteElectronico comprobanteElectronico) {

        DialogDetalleComprobante dialogDetalleComprobante = new DialogDetalleComprobante();

        Bundle args = new Bundle();
        args.putSerializable("comprobante", comprobanteElectronico);
        dialogDetalleComprobante.setArguments(args);
        return dialogDetalleComprobante;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        comprobanteElectronico = (ComprobanteElectronico) getArguments().getSerializable("comprobante");


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return crearDialogDetallesComprobante();
    }

    public AlertDialog crearDialogDetallesComprobante() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_detalles_comprobante, null);

        builder.setView(v);

        textViewSecuencial = (TextView) v.findViewById(R.id.text_view_secuencial_detalle_comprobante);
        textViewFechaEmision = (TextView) v.findViewById(R.id.text_view_fecha_emision_detalle_comprobante);
        textViewTipo = (TextView) v.findViewById(R.id.text_view_tipo_comprobante_detalle_comprobante);
        textViewRUCReceptor = (TextView) v.findViewById(R.id.text_view_ruc_receptor_detalle_comprobante);
        textViewRazonSocialReceptor = (TextView) v.findViewById(R.id.text_view_razon_social_receptor_detalle_comprobante);
        textViewClaveAcceso = (TextView) v.findViewById(R.id.text_view_clave_acceso_detalle_comprobante);
        textViewNumeroAutorizacion = (TextView) v.findViewById(R.id.text_view_numero_autorizacion_detalle_comprobante);
        textViewFechaAutorizacion = (TextView) v.findViewById(R.id.text_view_fecha_autorizacion_detalle_comprobante);
        textViewObservacion = (TextView) v.findViewById(R.id.text_view_observacion_detalle_comprobante);

        textViewSecuencial.setText(comprobanteElectronico.getCodigoEstablecimientoComprobanteElectronico().concat("-").concat(comprobanteElectronico.getPuntoEmisionComprobanteElectronico().concat("-").concat(comprobanteElectronico.getSecuencialComprobanteElectronico())));
        textViewFechaEmision.setText(comprobanteElectronico.getFechaEmisionComprobanteElectronico());
        textViewTipo.setText(Utilidades.obtenerTipoDocumento(comprobanteElectronico.getTipoComprobanteElectronico()));
        textViewRUCReceptor.setText(comprobanteElectronico.getRucReceptor());
        textViewRazonSocialReceptor.setText(comprobanteElectronico.getRazonSocialReceptor());
        textViewClaveAcceso.setText(comprobanteElectronico.getClaveAccesoComprobanteElectronico());
        textViewNumeroAutorizacion.setText(comprobanteElectronico.getNumeroAutorizacionComprobanteElectronico());
        textViewFechaAutorizacion.setText(comprobanteElectronico.getFechaAutorizacionComprobanteElectronico());
        textViewObservacion.setText(comprobanteElectronico.getMensajeComprobanteElectronico());

        buttonContinuar = (Button) v.findViewById(R.id.button_continuar);
        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (getActivity().getResources().getConfiguration().orientation) {
                    case Configuration.ORIENTATION_LANDSCAPE:
                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        break;
                }
                dismiss();
            }
        });

        return builder.create();
    }

}
