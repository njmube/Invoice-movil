package ec.bigdata.facturaelectronicamovil.dialogs;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

/**
 * Created by DavidLeonardo on 11/7/2016.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class DialogFecha extends DialogFragment {

    public DialogFecha() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Obtener fecha actual
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Retornar en nueva instancia del dialogo selector de fecha
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                (DatePickerDialog.OnDateSetListener) getActivity(),
                year,
                month,
                day);

        return datePickerDialog;
    }
}
