package ec.bigdata.facturaelectronicamovil.utilidad;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import ec.bigdata.facturaelectronicamovil.login.Inicio;

/**
 * Created by DavidLeonardo on 21/7/2016.
 */
public class PreferenciasUsuario {

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    // Context
    private Context context;

    //Nombre de la preferencia compartida
    private static final String SHARED_PREFERENCES_NAME = "PreferenciasApp";

    //Llaves
    private static final String LLAVE_EXISTE_SESION_USUARIO = "SesionUsuarioActiva";

    public static final String LLAVE_NOMBRE_USUARIO = "nombreUsuario";

    public static final String LLAVE_CONTRASENIA = "claveUsuario";

    public static final String LLAVE_CODIGO_REGISTRO_GCM = "codigoRegistroGoogleCloudMessaging";

    public PreferenciasUsuario(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        this.editor = this.sharedPreferences.edit();
    }

    //Crea la sesión del usuario
    public boolean createSesionUsuario(String nombreUsuario, String claveUsuario) {
        //Se pone el estado de la sesión a true
        editor.putBoolean(LLAVE_EXISTE_SESION_USUARIO, true);

        //Almacena el nombre y la clave del usuario
        editor.putString(LLAVE_NOMBRE_USUARIO, nombreUsuario);
        editor.putString(LLAVE_CONTRASENIA, claveUsuario);

        //Guarda los cambios
        return editor.commit();
    }

    /**
     * Verifica el estaod de la sesión del usuario si es falso le direcciona a la página de inicio caso contrario no hace nada
     *
     * @return
     */
    public boolean verificarInicioSesion() {
        // Check login status
        if (!this.estaActivaSesionUsuario()) {

            //Direcciona al inicio
            Intent intent = new Intent(context, Inicio.class);

            //Cierra todas las actividades
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Agrega una nueva bandera para empezar una nueva actividad
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);

            return true;
        }
        return false;
    }

    public String obtenerCodigoRegistroGoogleCloudMessaging() {
        return sharedPreferences.getString(LLAVE_CODIGO_REGISTRO_GCM, "");
    }

    public boolean guardarCodigoRegistroGoogleCloudMessaging(String codigoRegistro) {
        editor.putString(LLAVE_CODIGO_REGISTRO_GCM, codigoRegistro);
        return editor.commit();
    }

    /**
     * Verifica si existe sesion activa
     */
    public boolean estaActivaSesionUsuario() {
        return sharedPreferences.getBoolean(LLAVE_EXISTE_SESION_USUARIO, false);
    }

    /**
     * Obtiene los detalles del usuario
     */
    public HashMap<String, String> obtenerDetallesSesionUsuario() {


        HashMap<String, String> stringHashMapUsuario = new HashMap<String, String>();


        stringHashMapUsuario.put(LLAVE_NOMBRE_USUARIO, sharedPreferences.getString(LLAVE_NOMBRE_USUARIO, null));


        stringHashMapUsuario.put(LLAVE_CONTRASENIA, sharedPreferences.getString(LLAVE_CONTRASENIA, null));


        return stringHashMapUsuario;
    }

    public void cerrarSesion() {
        //TODO Revisar borrado toda la información guardada en las preferencias del usuario

        //editor.clear();
        //editor.commit();

        //Redirecciona al inicio
        Intent i = new Intent(context, Inicio.class);

        //Cierra todas las actividades
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Agrega una nueva bandera para empezar una nueva activida
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Empieza la nueva actividad
        context.startActivity(i);

    }

}
