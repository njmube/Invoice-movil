package ec.bigdata.facturaelectronicamovil.utilidades;

import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ec.bigdata.utilidades.Validaciones;

/**
 * Created by DavidLeonardo on 27/1/2016.
 */
public class Utilidades {

    //public static String IP_DOMINIO_WEB_SERVICE = "http://pre.ebox.ec/ws/";
    //public static String IP_DOMINIO_WEB_SERVICE = "http://192.168.2.5:8080/aw_web_service_rest_jersey/";
    public static String IP_DOMINIO_WEB_SERVICE = "http://192.168.100.20:8080/aw_web_service_rest_jersey/";

    private static final String TAG = Utilidades.class.getSimpleName();

    public static final String USUARIO_EMPRESA = "1";
    public static final String USUARIO_PERSONA = "2";

    public static String obtenerIpLocal() {
        String ip = "";
        try {
            InetAddress ownIP = InetAddress.getLocalHost();
            ip = ownIP.getHostAddress();
        } catch (UnknownHostException e) {
            Log.e(TAG, "No se pudo formatear la fecha." + e);
        }
        return ip;
    }

    public static Date obtenerFechaDeCadena(String _cadena) {
        Date fecha = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            fecha = sdf.parse(_cadena);
        } catch (ParseException e) {
            Log.e(TAG, "No se pudo formatear la fecha." + e);
        }
        return fecha;
    }

    public static Date obtenerFechaFormatoMySQL(String _cadenaFecha) {
        try {
            Date fecha = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setLenient(false);
            fecha = sdf.parse(_cadenaFecha);
            return fecha;
        } catch (Exception ex) {
            Log.e(TAG, "No se pudo formatear la fecha." + ex);
            return null;
        }
    }

    public static String obtenerURLWebService() {
        return IP_DOMINIO_WEB_SERVICE + "facturamovil/";
    }

    public static boolean validarIdentificacion(String _identificacion) {
        boolean validado = false;
        if (_identificacion != null && !_identificacion.trim().equals("")) {

            if (Validaciones.isNum(_identificacion)) {

                if (_identificacion.length() == 10) {
                    if (Validaciones.validarCedula(_identificacion)) {
                        validado = true;
                    }
                } else if (_identificacion.length() == 13) {
                    if (Validaciones.validarRucPersonaNatural(_identificacion) || Validaciones.validarRucSociedadPrivada(_identificacion)
                            || Validaciones.validarRucSociedadPublica(_identificacion)) {
                        validado = true;
                    }
                }
            } else {
                validado = true;
            }
        }
        return validado;
    }


    public static void main(String args[]) {


    }

}
