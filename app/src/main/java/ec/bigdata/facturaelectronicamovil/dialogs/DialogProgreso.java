package ec.bigdata.facturaelectronicamovil.dialogs;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by DavidLeonardo on 30/6/2016.
 */
public class DialogProgreso {

    public static ProgressDialog mostrarDialogProgreso(Context context) {
        return ProgressDialog.show(context,
                "Conectando",
                "Espere por favor...");
    }

    public static ProgressDialog mostrarDialogProcesComprobantesElectronicos(Context context) {
        return ProgressDialog.show(context,
                "Procesando comprobantes electrónico",
                "Espere por favor...");
    }
}
