package ec.bigdata.facturaelectronicamovil.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;

import ec.bigdata.facturaelectronicamovil.R;

/**
 * Created by DavidLeonardo on 22/8/2016.
 */
public class DialogListaCheckBox extends DialogFragment {

    private String[] arrayStringsItems;

    private ArrayList<String> listaSeleccionados;

    // Interfaz de comunicación
    private DialogListaCheckBoxComunicacion dialogListaCheckBoxComunicacion;

    public DialogListaCheckBox() {
    }

    public interface DialogListaCheckBoxComunicacion {
        void presionarBotonSI(ArrayList<String> listaSeleccionados);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createMultipleListDialog();
    }

    public static DialogListaCheckBox newInstance(String[] arrayStringsItems) {

        DialogListaCheckBox dialogListaCheckBox = new DialogListaCheckBox();
        Bundle args = new Bundle();

        args.putStringArray("arrayStringsItems", arrayStringsItems);
        dialogListaCheckBox.setArguments(args);
        return dialogListaCheckBox;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arrayStringsItems = getArguments().getStringArray("arrayStringsItems");

        dialogListaCheckBoxComunicacion = (DialogListaCheckBoxComunicacion) getActivity();
    }

    public AlertDialog createMultipleListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final ArrayList listaSeleccionados = new ArrayList<>();
        builder.setTitle(getActivity().getResources().getString(R.string.titulo_dialog_seleccion_multiple))
                .setMultiChoiceItems(arrayStringsItems, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            //Se almacena el índice seleccionado.
                            listaSeleccionados.add(arrayStringsItems[which]);

                        } else if (listaSeleccionados.contains(which)) {
                            // Remover índice sin selección.
                            listaSeleccionados.remove(arrayStringsItems[which]);

                        }
                    }
                }).setPositiveButton(getActivity().getResources().getString(R.string.etiqueta_boton_enviar_dialog_seleccion_multipe), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!listaSeleccionados.isEmpty()) {
                    dialogListaCheckBoxComunicacion.presionarBotonSI(listaSeleccionados);
                }
            }
        });
        return builder.create();
    }
}
