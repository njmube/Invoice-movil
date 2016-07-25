package ec.bigdata.facturaelectronicamovil.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by DavidLeonardo on 24/6/2016.
 */
public class DialogConfirmacion extends DialogFragment {

    private String tituloDialog;
    private String mensajeDialog;

    public DialogConfirmacion() {

    }

    // Interfaz de comunicación
    ConfirmacionDialogListener confirmacionDialogListener;

    public interface ConfirmacionDialogListener {
        void presionarBotonSI();

        void presionarBotonCancelar();
    }

    public static DialogConfirmacion newInstance(String titulo, String mensaje) {

        DialogConfirmacion dialogConfirmacion = new DialogConfirmacion();

        Bundle args = new Bundle();
        args.putString("titulo", titulo);
        args.putString("mensaje", mensaje);
        dialogConfirmacion.setArguments(args);
        return dialogConfirmacion;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tituloDialog = getArguments().getString("titulo");
        mensajeDialog = getArguments().getString("mensaje");
        confirmacionDialogListener = (ConfirmacionDialogListener) getActivity();
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
                .setMessage(mensajeDialog)
                .setPositiveButton("Si",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                confirmacionDialogListener.presionarBotonSI();
                            }
                        })
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                confirmacionDialogListener.presionarBotonCancelar();
                            }
                        });

        return builder.create();
    }
}