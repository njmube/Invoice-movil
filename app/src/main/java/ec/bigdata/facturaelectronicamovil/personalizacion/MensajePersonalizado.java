package ec.bigdata.facturaelectronicamovil.personalizacion;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ec.bigdata.facturaelectronicamovil.R;

/**
 * Created by DavidLeonardo on 21/6/2016.
 */
public class MensajePersonalizado {

    public static final int TOAST_INFORMACION = 1;
    public static final int TOAST_ADVERTENCIA = 2;
    public static final int TOAST_ERROR = 3;

    public static void mostrarToastPersonalizado(Context context, LayoutInflater layoutInflater, int tipoToast, String mensaje) {

        View customToastroot = null;
        TextView textViewMensaje = null;
        if (tipoToast == TOAST_INFORMACION) {
            customToastroot = layoutInflater.inflate(R.layout.custom_toast_info, null);
            textViewMensaje = (TextView) customToastroot.findViewById(R.id.text_view_mensaje_toast_info);
        } else if (tipoToast == TOAST_ADVERTENCIA) {
            customToastroot = layoutInflater.inflate(R.layout.custom_toast_warning, null);
            textViewMensaje = (TextView) customToastroot.findViewById(R.id.text_view_mensaje_toast_advertencia);
        } else {
            customToastroot = layoutInflater.inflate(R.layout.custom_toast_error, null);
            textViewMensaje = (TextView) customToastroot.findViewById(R.id.text_view_mensaje_toast_error);
        }
        textViewMensaje.setText(mensaje);
        Toast customtoast = new Toast(context);
        customtoast.setView(customToastroot);
        customtoast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);
        customtoast.setDuration(Toast.LENGTH_LONG);
        customtoast.show();
    }

}
