package ec.bigdata.facturaelectronicamovil.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import ec.bigdata.facturaelectronicamovil.R;

/**
 * Created by DavidLeonardo on 9/8/2016.
 */
public class DialogSpinner extends DialogFragment {

    private String titulo;
    private String[] items;
    private DialogSpinnerComunicacion dialogSpinnerComunicacion;

    public DialogSpinner() {
    }

    public interface DialogSpinnerComunicacion {

        void enviarItemSeleccionado(String itemSeleccionado);
    }

    public static DialogSpinner newInstance(String titulo, String[] items) {

        DialogSpinner dialogSpinner = new DialogSpinner();
        Bundle args = new Bundle();
        args.putString("titulo", titulo);
        args.putSerializable("items", items);
        dialogSpinner.setArguments(args);
        return dialogSpinner;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titulo = getArguments().getString("titulo");
        items = (String[]) getArguments().getSerializable("items");
        dialogSpinnerComunicacion = (DialogSpinnerComunicacion) getActivity();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(titulo).setIcon(R.drawable.ic_call_black_48dp).setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogSpinnerComunicacion.enviarItemSeleccionado(items[which]);
            }
        });

        return builder.create();
    }
}
