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
public class DialogConfirmacionUnBoton extends DialogFragment {

    private String tituloDialog;
    private String mensajeDialog;

    // Interfaz de comunicación
    private DialogConfirmacionUnBotonComunicacion dialogConfirmacionUnBotonComunicacion;

    public DialogConfirmacionUnBoton() {

    }

    public interface DialogConfirmacionUnBotonComunicacion {
        void presionarBotonContinuar();
    }

    public static DialogConfirmacionUnBoton newInstance(String titulo, String mensaje) {

        DialogConfirmacionUnBoton dialogConfirmacion = new DialogConfirmacionUnBoton();

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
        dialogConfirmacionUnBotonComunicacion = (DialogConfirmacionUnBotonComunicacion) getActivity();
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
                .setMessage(Html.fromHtml(mensajeDialog))
                .setPositiveButton("Continuar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialogConfirmacionUnBotonComunicacion.presionarBotonContinuar();
                            }
                        }).setCancelable(false);


        return builder.create();
    }
}