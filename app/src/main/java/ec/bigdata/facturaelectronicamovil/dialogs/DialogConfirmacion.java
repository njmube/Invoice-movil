package ec.bigdata.facturaelectronicamovil.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;

/**
 * Created by DavidLeonardo on 24/6/2016.
 */
public class DialogConfirmacion extends DialogFragment {

    private String tituloDialog;
    private String mensajeDialog;
    private boolean esContenidoHtml;

    // Interfaz de comunicación
    private DialogConfirmacionComunicacion dialogConfirmacionComunicacion;

    public DialogConfirmacion() {

    }

    public interface DialogConfirmacionComunicacion {
        void presionarBotonSI();

        void presionarBotonCancelar();
    }

    public static DialogConfirmacion newInstance(String titulo, String mensaje, boolean esContenidoHtml) {

        DialogConfirmacion dialogConfirmacion = new DialogConfirmacion();

        Bundle args = new Bundle();
        args.putString("titulo", titulo);
        args.putString("mensaje", mensaje);
        args.putBoolean("esHtml", esContenidoHtml);
        dialogConfirmacion.setArguments(args);
        return dialogConfirmacion;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tituloDialog = getArguments().getString("titulo");
        mensajeDialog = getArguments().getString("mensaje");
        esContenidoHtml = getArguments().getBoolean("esHtml");
        dialogConfirmacionComunicacion = (DialogConfirmacionComunicacion) getActivity();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return crearDialogConfirmacion();
    }

    /**
     * Crea un diálogo de alerta de confirmación
     *
     * @return Nuevo diálogo de tipo AlertDialog
     */
    public AlertDialog crearDialogConfirmacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(tituloDialog)
                .setMessage(esContenidoHtml == true ? Html.fromHtml(mensajeDialog) : mensajeDialog)
                .setPositiveButton("Sí",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialogConfirmacionComunicacion.presionarBotonSI();
                            }
                        })
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialogConfirmacionComunicacion.presionarBotonCancelar();
                            }
                        });

        return builder.create();
    }
}